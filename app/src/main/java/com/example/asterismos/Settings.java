package com.example.asterismos;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private SeekBar volumeControl;
    private int tapCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        //Я вам запрещаю переворачивать экран
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        createMusic();

        ImageButton Secret = findViewById(R.id.developer);
        Secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapCount++; // Увеличиваем счетчик при каждом тапе

                if (tapCount == 7) {
                    // Сбрасываем счетчик, чтобы меню можно было открыть снова
                    tapCount = 0;

                    if (!UserAdministration.getInstance().isAdmin()){
                        UserAdministration.getInstance().setRole("admin");
                        Toast.makeText(Settings.this,"Эта реальность в ваших руках", Toast.LENGTH_SHORT).show();
                    } else{
                        UserAdministration.getInstance().setRole("user");
                        Toast.makeText(Settings.this,"Вы отказались от своих сил", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ImageButton Back = findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(Settings.this, MainMenu.class));
            }
        });

    }

    private void createMusic(){
        volumeControl = findViewById(R.id.volumeControl);
        volumeControl.setMax(100);

        if (MediaService.ambientMediaPlayer != null) {
            volumeControl.setProgress((int) (MediaService.currentVolume * 100));
        } else {
            volumeControl.setProgress(50);
        }

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    float volume = progress / 100f;
                    MediaService.currentVolume = volume;
                    if (MediaService.ambientMediaPlayer != null) {
                        MediaService.ambientMediaPlayer.setVolume(volume, volume);
                    }
                    // Сохранение в SharedPreferences
                    getSharedPreferences("AsterismosPrefs", MODE_PRIVATE)
                            .edit()
                            .putInt("music_volume", progress)
                            .apply();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Очередная хрень, которая обязательна
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Очередная хрень, которая обязательна
            }

        });
    }


}