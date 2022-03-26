package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MediaPlayer buttonSound;


    public void start(View view)
    {
        buttonSound.start();
        Intent intent = new Intent(getApplicationContext(), StartUp.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        buttonSound = MediaPlayer.create(this,R.raw.buttonsound);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}