package com.example.asterismos;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TEST", "Главное меню");
        setContentView(R.layout.main_menu);
        //Я вам запрещаю переворачивать экран
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent musicIntent = new Intent(this, MediaService.class);
        startService(musicIntent);

        onClick();
    }


    private void onClick(){
        ImageButton buttonToEncyclopedia = (ImageButton) findViewById(R.id.encyclopedia);
        ImageButton buttonToSettings = (ImageButton) findViewById(R.id.settings);
        ImageButton buttonToTest = (ImageButton) findViewById(R.id.test);

        buttonToEncyclopedia.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainMenu.this, ConstellationEncyclopedia.class));
            }
        });

        buttonToTest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainMenu.this, Tests.class));
            }
        });

        buttonToSettings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainMenu.this, Settings.class));
            }
        });
    }


}
