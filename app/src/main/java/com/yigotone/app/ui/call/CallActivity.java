package com.yigotone.app.ui.call;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.yigotone.app.R;
import com.yigotone.app.api.UrlUtil;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.user.Constant;
import com.yigotone.app.user.UserManager;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;
import com.yigotone.app.util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.iv_mini) ImageView ivMini;
    private AudioManager mAudioManager;
    private boolean mCallMode = false;
    private int mode;
    private MediaPlayer mMediaPlayer;
    private int mCallId; // 主叫
    private int callId;  // 被叫
    private String phoneNum;
    private String comeFrom;
    private boolean louderFlag = false;
    private static int CALL_TYPE = -1;
    private final int CALL_OUT = 0;
    private final int CALL_IN = 1;
    private String thisPhoneNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    public CallPresenter initPresenter() {
        return new CallPresenter(this);
    }

    @Override
    public void initView() {
        thisPhoneNum = UserManager.getInstance().userData.getMobile();
        EbCallDelegate.setCallback(this);
        EbLoginDelegate.setLoginCallback(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNum = bundle.getString("phonenum");
            comeFrom = bundle.getString("comefrom");
            callId = bundle.getInt(MebConstants.CALL_ID);
        }
        tvName.setText(phoneNum);
        if (comeFrom.equals("dial")) { // call out
            CALL_TYPE = CALL_OUT;
            setCallMode(); // 设置通话音频模式

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
            tvName.setText(EbCallDelegate.getPeerNum(callId));
            tvStatus.setText("来电");
            ivHangUp.setVisibility(View.GONE);
            tvHangUp.setVisibility(View.VISIBLE);
            tvAnswer.setVisibility(View.VISIBLE);
            EbCallDelegate.alert(callId);
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
        map.put("callId", mCallId);
        map.put("mobile", thisPhoneNum);
        map.put("targetMobile", phoneNum);
        map.put("targetName", Utils.getContactName(phoneNum));
        presenter.postParams(UrlUtil.RECORD_CALL_OUT, map, "recordCallOut");
    }

    private void refreshCallStatus(int status) { // 更新记录通话状态
        Map<String, Object> map = new HashMap<>();
        map.put("uid", UserManager.getInstance().userData.getUid());
        map.put("token", UserManager.getInstance().userData.getToken());
        map.put("callId", mCallId);
        map.put("status", status);
        map.put("targetStatus", status);
        presenter.postParams(UrlUtil.RECORD_CALL_OUT, map, "refreshCallStatus");
    }

    @OnClick({R.id.iv_keyboard, R.id.iv_speakerphone, R.id.iv_hang_up, R.id.tv_hang_up, R.id.tv_answer, R.id.iv_mini})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_keyboard:
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
        // 被叫 振铃
    }

    @Override
    public void ebCallDelegateTalking(int iCallId) {
       // 通话已接通广播事件
    }

    @Override
    public void ebCallDelegateTermed(int i, int i1, String s) {
        // 对方挂断
        stopAlarm();
        tvStatus.setText("通话结束");
        refreshCallStatus(2);
        U.showToast("对方已挂断");
        clearCallMode();
        finish();
    }

    @Override
    public void ebCallDelegateDidTerm(int i, int i1, String s) {
        stopAlarm();
        tvStatus.setText("通话结束");
        // refreshCallStatus( );
        U.showToast("已挂断");
        clearCallMode();
        finish();
    }

    @Override
    public void ebCallDelegateLogouted() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearCallMode();
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
}
