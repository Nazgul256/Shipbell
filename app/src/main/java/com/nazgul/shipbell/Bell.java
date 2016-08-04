package com.nazgul.shipbell;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;

import static android.os.SystemClock.sleep;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Bell extends IntentService implements SoundPool.OnLoadCompleteListener {
    public static final String EXTRA_MOD = "mod";
    SoundPool soundPool;
    int soundIdTutu;
    String mod;
    Handler handler;

    public Bell() {
        super("Bell");
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(this);
        soundIdTutu = soundPool.load(this, R.raw.tutu, 1);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mod = intent.getStringExtra(EXTRA_MOD);
        switch (mod) {
            case "interval":
                interval(MainActivity.interval);
                break;
            case "iteration":
                iteration(MainActivity.iteration);
                break;
            case "inTime":
                inTime(MainActivity.interval);
                break;
        }
    }

    public void interval(long interval){
        while (MainActivity.isRunning){
            sleep(interval);
            soundPool.play(soundIdTutu, 1, 1, 0, 0, 1);
        }
    }

    public void inTime(final long interval) {
        MainActivity.deltaTime = MainActivity.inTime - SystemClock.currentThreadTimeMillis();
        handler = new Handler();

        Runnable count = new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(MainActivity.deltaTime, interval) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        soundPool.play(soundIdTutu, 1, 1, 0, 0, 1);
                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        };

        handler.post(count);
        while(MainActivity.isRunning){
            sleep(1000);
        }
        handler.removeCallbacks(count);

    }

    public void iteration(int interval){
       while (MainActivity.isRunning){
            for(int i=0; i<MainActivity.iteration; i++){

                sleep(interval);
                soundPool.play(soundIdTutu, 1, 1, 0, 0, 1);
            }
        }
    }


    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

    }
}
