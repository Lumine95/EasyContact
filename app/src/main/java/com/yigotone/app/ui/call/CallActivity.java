package com.yigotone.app.ui.call;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.utils.U;
import com.ebupt.ebauth.biz.EbAuthDelegate;
import com.ebupt.ebauth.biz.auth.OnAuthLoginListener;
import com.ebupt.ebjar.EbCallDelegate;
import com.ebupt.ebjar.EbLoginDelegate;
import com.ebupt.ebjar.MebMdm;
import com.justalk.cloud.zmf.ZmfAudio;
import com.yigotone.app.R;
import com.yigotone.app.base.BaseActivity;
import com.yigotone.app.base.BasePresenter;
import com.yigotone.app.user.Constant;
import com.yigotone.app.util.AuthUtils;
import com.yigotone.app.util.DataUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZMM on 2018/11/6  23:24.
 */
public class CallActivity extends BaseActivity {
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
    private int mCallId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void initView() {
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        int tag = getIntent().getIntExtra("tag", 0);
        tvName.setText(phoneNumber);
        EbLoginDelegate.SetJustAddress(Constant.JUSTALK_KEY, Constant.JUSTALK_IP);

        EbAuthDelegate.AuthloginByTrust("18237056520", new OnAuthLoginListener() {
            @Override
            public void ebAuthOk(String authcode, String deadline) {
                Log.i("AuthloginByTrust", "ebAuthOk " + deadline);

//    鉴权成功，判断是否在有效期内 执行SDK登录(此登录需企业输入自定义密码（需每个产品保持统一）)
                if (AuthUtils.isDeadlineAvailable(deadline)) {
                    EbLoginDelegate.login("18237056520", "ebupt");
                }
            }

            @Override
            public void ebAuthFailed(int code, String reason) {
                Log.i("AuthloginByTrust", "ebAuthFailed :" + code + reason);


            }
        });


        if (AuthUtils.isDeadlineAvailable(DataUtils.readDeadline(this, DataUtils.readAccount(this)))) {
            U.showToast("正在处于鉴权有效期");
        } else {
            U.showToast("您的鉴权有效期已过期，请重新鉴权");
//                jumpToDeployActivity();
        }
        // 设置通话音频模式
        setCallMode();
        mCallId = EbCallDelegate.call(phoneNumber);
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
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_keyboard, R.id.iv_speakerphone, R.id.iv_hang_up, R.id.tv_hang_up, R.id.tv_answer, R.id.iv_mini})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_keyboard:
                break;
            case R.id.iv_speakerphone:
                break;
            case R.id.iv_hang_up:
                break;
            case R.id.tv_hang_up:
                break;
            case R.id.tv_answer:
                break;
            case R.id.iv_mini:
                break;
        }
    }
}
