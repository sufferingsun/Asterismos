package com.example.asterismos;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;


public class LifecycleForMusic extends Application implements LifecycleObserver {
    // Создано для отслеживания находится ли приложение в свёрнутом виде или нет. Для музыки
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onAppBackgrounded() {
        Intent intent = new Intent(this, MediaService.class);
        intent.setAction("ACTION_STOP_MUSIC");
        startService(intent);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onAppForegrounded() {
        Intent intent = new Intent(this, MediaService.class);
        intent.setAction("ACTION_START_MUSIC");
        startService(intent);
    }
}
