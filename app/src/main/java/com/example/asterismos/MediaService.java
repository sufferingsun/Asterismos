package com.example.asterismos;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.content.SharedPreferences;

public class MediaService extends Service {
    public static MediaPlayer ambientMediaPlayer;
    private SharedPreferences prefs;
    public static float currentVolume = 0.5f;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate(){
        prefs = getSharedPreferences("AsterismosPrefs", MODE_PRIVATE);
        ambientMediaPlayer=MediaPlayer.create(this, R.raw.song);
        ambientMediaPlayer.setLooping(true);

        int savedVolume = prefs.getInt("music_volume", 50);
        currentVolume = savedVolume / 100f;
        ambientMediaPlayer.setVolume(currentVolume, currentVolume);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();

            if ("ACTION_STOP_MUSIC".equals(action)) {
                // Останавливаем музыку, если она играет
                if (ambientMediaPlayer != null && ambientMediaPlayer.isPlaying()) {
                    ambientMediaPlayer.pause();
                }
                return START_NOT_STICKY;
            } else if ("ACTION_START_MUSIC".equals(action)) {
                // Запускаем музыку, если она не играет
                if (ambientMediaPlayer != null && !ambientMediaPlayer.isPlaying()) {
                    ambientMediaPlayer.start();
                }
                return START_STICKY;
            }

            return START_STICKY;
        }

        if (ambientMediaPlayer != null && !ambientMediaPlayer.isPlaying()) {
            ambientMediaPlayer.start();
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        if (ambientMediaPlayer != null) {
            int volumeToSave = (int) (currentVolume * 100);
            prefs.edit().putInt("music_volume", volumeToSave).apply();

            ambientMediaPlayer.stop();
            ambientMediaPlayer.release();
        }
    }
}


