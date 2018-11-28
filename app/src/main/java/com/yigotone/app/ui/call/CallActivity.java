package com.yigotone.app.ui.call;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.ebupt.ebjar.EbCallDelegate;
import com.ebupt.ebjar.EbLoginDelegate;
import com.ebupt.ebjar.MebConstants;
import com.ebupt.ebjar.MebMdm;
import com.justalk.cloud.zmf.ZmfAudio;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.user.Constant;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;
import com.yigotone.app.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/6  23:24.
 */
public class CallActivity extends BaseActivity<CallContract.Presenter> implements CallContract.View, EbCallDelegate.Callback, EbLoginDelegate.LoginCallback {
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.iv_keyboard) ImageView ivKeyboard;
    @BindView(R.id.iv_speakerphone) ImageView ivSpeakerphone;
    @BindView(R.id.iv_hang_up) ImageView ivHangUp;
    @BindView(R.id.tv_hang_up) TextView tvHangUp;
    @BindView(R.id.tv_answer) TextView tvAnswer;
    @BindView(R.id.tv_input) TextView tvInput;
    @BindView(R.id.iv_mini) ImageView ivMini;
    @BindView(R.id.cl_keyboard) ConstraintLayout clKeyboard;

    private AudioManager mAudioManager;
    private boolean mCallMode = false;
    private int mode;
    private MediaPlayer mMediaPlayer;
    private int mCallId = 0; // 主叫
    private int callId = 0;  // 被叫
    private String phoneNum;
    private String comeFrom;
    private boolean louderFlag = false;
    private static int CALL_TYPE = -1;
    private final int CALL_OUT = 0;
    private final int CALL_IN = 1;
    private String thisPhoneNum;
    private long baseTimer;
    private int callTime; // 通话时长
    private StringBuffer inputStr = new StringBuffer();
    @SuppressLint("HandlerLeak") Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (null != tvStatus) {
                tvStatus.setText((String) msg.obj);
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    public CallPresenter initPresenter() {
        return new CallPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        new RxPermissions(this).request(Manifest.permission.RECORD_AUDIO).subscribe(granted -> {
            if (!granted) {
                U.showToast("权限申请失败");
                finish();
            }
        });

        thisPhoneNum = UserManager.getInstance().userData.getMobile();
        EbCallDelegate.setCallback(this);
        EbLoginDelegate.setLoginCallback(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNum = bundle.getString("phonenum");
            comeFrom = bundle.getString("comefrom");
            callId = bundle.getInt(MebConstants.CALL_ID);
        }

        if (comeFrom.equals("dial")) { // call out
            CALL_TYPE = CALL_OUT;
            setCallMode(); // 设置通话音频模式
            tvName.setText(Utils.getContactName(phoneNum));
            EbLoginDelegate.SetJustAddress(Constant.JUSTALK_KEY, Constant.JUSTALK_IP);
            DataUtils.saveAccount(thisPhoneNum, this);

            EbAuthDelegate.AuthloginByTrust(thisPhoneNum, new OnAuthLoginListener() {
                @Override
                public void ebAuthOk(String authcode, String deadline) {
                    Logger.d("authcode " + authcode + deadline);
                    if (AuthUtils.isDeadlineAvailable(deadline)) {
                        //  EbLoginDelegate.login(thisPhoneNum, "ebupt");
                        mCallId = EbCallDelegate.call(phoneNum);
                        Logger.d("callId: " + mCallId);
                        recordCallOut();// 记录呼出状态
                    }
                }

                @Override
                public void ebAuthFailed(int code, String reason) {
                    Logger.d("ebAuthFailed: " + code + reason);
                }
            });
            if (AuthUtils.isDeadlineAvailable(DataUtils.readDeadline(this, DataUtils.readAccount(this)))) {
                // U.showToast("正在处于鉴权有效期");
                //  setCallMode(); // 设置通话音频模式

            } else {
                U.showToast("您的鉴权有效期已过期，请重新鉴权");
                finish();
            }
        } else if (comeFrom.equals(MebConstants.FRO_PEER)) {
            CALL_TYPE = CALL_IN;
            phoneNum = EbCallDelegate.getPeerNum(callId);
            tvName.setText(Utils.getContactName(phoneNum));
            tvStatus.setText("来电");
            ivHangUp.setVisibility(View.GONE);
            tvHangUp.setVisibility(View.VISIBLE);
            tvAnswer.setVisibility(View.VISIBLE);
            EbCallDelegate.alert(callId);
            recordCallIn();
            startAlarm();
        }
    }

    private void setCallMode() {
        Log.d("", "====setCallMode_method====");
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (mCallMode) return;
        mCallMode = true;

        // 通过 MtcMdm 获取最优语音模式设置
        mode = MebMdm.Meb_MdmGetAndroidAudioMode();
        if (mode != mAudioManager.getMode()) {
            mAudioManager.setMode(mode);
        }

        // 获取音频焦点，获取音频焦点之后，如果其他 APP 正在使用音频设备，将抑制其他 APP 的音量，使通话更清晰
        // 否则其他 APP 和当前 APP 的声音将被混合播放，造成通话声音不清晰
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_VOICE_CALL, AudioManager.AUDIOFOCUS_GAIN);

        // 启动音频设备
        audioStart();
    }

    @SuppressLint("StaticFieldLeak")
    private void audioStart() {
        Log.d("", "====audioStart_method====");

        // 音频设备打开接口为同步接口，可能会对界面造成少许的延迟，为了增加用户体验所以在放到另外一个线程中执行
        // 如果在直接主线程中执行也是可以的
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                synchronized (CallActivity.this) {
                    // 打开音频采集设备，使用 MtcMdm 接口可以获取设备最优的配置选项
                    ZmfAudio.inputStart(MebMdm.Meb_MdmGetAndroidAudioInputDevice(), 0, 0,
                            MebMdm.Meb_MdmGetOsAec() ? ZmfAudio.AEC_ON : ZmfAudio.AEC_OFF,
                            MebMdm.Meb_MdmGetOsAgc() ? ZmfAudio.AGC_ON : ZmfAudio.AGC_OFF);

                    // 打开视频播放设备，使用 MtcMdm 接口可以获取设备最优的配置选项
                    ZmfAudio.outputStart(MebMdm.Meb_MdmGetAndroidAudioOutputDevice(), 0, 0);
                }
                return null;
            }
        }.execute();
    }

    private void clearCallMode() {
        mCallMode = false;
        if (mAudioManager == null) {
            return;
        }
        synchronized (this) {
            // 停止音频采集
            ZmfAudio.inputStopAll();
            // 停止音频播放
            ZmfAudio.outputStopAll();
        }

        // 释放音频焦点，如果有其他 APP 同时使用音频设备，将恢复其他 APP 的音量
        mAudioManager.abandonAudioFocus(null);

        // 恢复系统音频模式
        if (AudioManager.MODE_NORMAL != mAudioManager.getMode()) {
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
        }
    }

    private void startAlarm() {
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(true);
            try {
                mMediaPlayer.prepare();
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();
        }
    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }

    private void stopAlarm() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onError(Throwable throwable) {
        dismissLoadingDialog();
        Logger.e(throwable.toString());
    }

    private void recordCallOut() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("callId", mCallId == 0 ? callId : mCallId);
        map.put("mobile", thisPhoneNum);
        map.put("targetMobile", phoneNum);
        map.put("targetName", Utils.getContactName(phoneNum));
        presenter.postParams(UrlUtil.RECORD_CALL_OUT, map, "recordCallOut");
    }

    private void recordCallIn() {
        Map<String, Object> map = new HashMap<>();
        map.put("callId", mCallId == 0 ? callId : mCallId);
        map.put("mobile", phoneNum);
        map.put("targetMobile", thisPhoneNum);
        map.put("targetName", Utils.getContactName(phoneNum));
        presenter.postParams(UrlUtil.RECORD_CALL_IN, map, "recordCall");
    }

    private void refreshCallStatus(int status) { // 更新记录通话状态
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("callId", mCallId == 0 ? callId : mCallId);
        map.put("status", status);
        map.put("targetStatus", status);
        presenter.postParams(UrlUtil.UPDATE_CALL_STATUS, map, "refreshCallStatus");
    }

    @OnClick({R.id.iv_one, R.id.iv_two, R.id.iv_three, R.id.iv_four, R.id.iv_five, R.id.iv_six, R.id.iv_seven, R.id.iv_eight, R.id.iv_nine, R.id.iv_asterisk, R.id.iv_zero, R.id.iv_pound, R.id.iv_keyboard, R.id.iv_speakerphone, R.id.iv_hang_up, R.id.tv_hang_up, R.id.tv_answer, R.id.iv_mini, R.id.tv_hide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hide:
                showHideKeyboardLayout(false);
                break;
            case R.id.iv_keyboard:
                showHideKeyboardLayout(true);
                break;
            case R.id.iv_speakerphone:
                setupSpeaker();
                break;
            case R.id.iv_hang_up:
            case R.id.tv_hang_up:
                hangUp();
                break;
            case R.id.tv_answer:
                setCallMode();
                EbCallDelegate.answer(callId);
                break;
            case R.id.iv_mini:
                break;
            case R.id.iv_one:
                PostDTMF(1, "1");
                break;
            case R.id.iv_two:
                PostDTMF(2, "2");
                break;
            case R.id.iv_three:
                PostDTMF(3, "3");
                break;
            case R.id.iv_four:
                PostDTMF(4, "4");
                break;
            case R.id.iv_five:
                PostDTMF(5, "5");
                break;
            case R.id.iv_six:
                PostDTMF(6, "6");
                break;
            case R.id.iv_seven:
                PostDTMF(7, "7");
                break;
            case R.id.iv_eight:
                PostDTMF(8, "8");
                break;
            case R.id.iv_nine:
                PostDTMF(9, "9");
                break;
            case R.id.iv_asterisk:
                PostDTMF(10, "10");
                break;
            case R.id.iv_zero:
                PostDTMF(0, "0");
                break;
            case R.id.iv_pound:
                PostDTMF(11, "11");
                break;
        }
    }

    private void hangUp() {
        if (CALL_TYPE == CALL_OUT) {
            EbCallDelegate.droped(mCallId);
        } else if (CALL_TYPE == CALL_IN) {
            EbCallDelegate.droped(callId);
        }
    }

    private void setupSpeaker() {
        louderFlag = !louderFlag;
        ivSpeakerphone.setSelected(louderFlag);
        if (louderFlag) {
            // 设置开启扬声器
            if (mCallMode && !mAudioManager.isSpeakerphoneOn())
                mAudioManager.setSpeakerphoneOn(true);
        } else {
            // 设置关闭扬声器
            if (mCallMode && mAudioManager.isSpeakerphoneOn())
                mAudioManager.setSpeakerphoneOn(false);
        }
    }

    @Override
    public void ebCallDelegateOutgoing(int i) {
        // 呼出电话动作完成后执行此方法
        tvStatus.setText("正在呼叫…");
    }

    @Override
    public void ebCallDelegateAlerted(int iCallId, int alertType) {
        // 振铃
        Logger.d("响铃：" + alertType);
    }

    @Override
    public void ebCallDelegateTalking(int iCallId) {
        // 通话已接通
        refreshCallStatus(3);
        stopAlarm();
        startTimeClock();
    }

    private void startTimeClock() {
        baseTimer = SystemClock.elapsedRealtime();
        new Timer("计时器").scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                callTime = (int) ((SystemClock.elapsedRealtime() - baseTimer) / 1000);
                String hh = new DecimalFormat("00").format(callTime / 3600);
                String mm = new DecimalFormat("00").format(callTime % 3600 / 60);
                String ss = new DecimalFormat("00").format(callTime % 60);
                String timeFormat = hh + ":" + mm + ":" + ss;
                Message msg = new Message();
                msg.obj = timeFormat;
                timeHandler.sendMessage(msg);
            }

        }, 0, 1000L);
    }

    @Override
    public void ebCallDelegateTermed(int callId, int statusCode, String reason) {
        Logger.d("对方挂断：" + statusCode + " reason: " + reason);
        // 对方挂断
        stopAlarm();
        tvStatus.setText("通话结束");
        refreshCallStatus(2);
        U.showToast("对方已挂断");
        clearCallMode();
        finish();
    }

    @Override
    public void ebCallDelegateDidTerm(int callId, int statusCode, String reason) {
        Logger.d("我方挂断：" + statusCode + " reason: " + reason);
        stopAlarm();
        tvStatus.setText("通话结束");

        if (callTime == 0) { // 如果电话挂断时，通话时间为0，则为未接通
            refreshCallStatus(1);
        }
        U.showToast("已挂断");
        clearCallMode();
        finish();
    }

    @Override
    public void ebCallDelegateLogouted() {
        Logger.d("ebCallDelegateLogouted");
    }

    @Override
    protected void onDestroy() {
        if (callTime > 0) {
            recordCallTime();
        }
        clearCallMode();
        super.onDestroy();
    }

    private void recordCallTime() { // 记录通话时长
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("callId", mCallId == 0 ? callId : mCallId);
        map.put("talktime", callTime);
        presenter.postParams(UrlUtil.RECORD_CALL_TIME, map, "recordCallTime");
    }

    private static long currentBackPressedTime = 0;
    int BACK_PRESSED_INTERVAL = 2000;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
                currentBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次挂断电话", Toast.LENGTH_SHORT).show();
            } else {
                hangUp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);// 继续执行父类其他点击事件
    }

    @Override
    public void ebLoginResult(int i, String s) {
        if (i == 0) {
            mCallId = EbCallDelegate.call(phoneNum);
            Logger.d("callId: " + mCallId);
        }
    }

    @Override
    public void ebLogoutOk() {

    }

    @Override
    public void ebLogouted() {

    }

    @Override
    public void onResult(Object result, String message) {
        switch (message) {
            case "recordCallOut":

                break;
        }
    }

    private void showHideKeyboardLayout(boolean isShow) {
        clKeyboard.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void PostDTMF(int playId, String tag) {
        EbCallDelegate.dtmf(mCallId, playId);
        Log.d("", "playId=" + playId);
        tvInput.setText(inputStr.append(tag).toString());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        Logger.d("eventBus: " + event);
        switch (event) {
            case "activityDestroy":
                hangUp();
                break;
        }
    }
}
