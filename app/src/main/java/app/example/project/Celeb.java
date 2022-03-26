package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Celeb extends AppCompatActivity {
    MediaPlayer Correct,Wrong;
    Button btn0,btn1,btn2,btn3,play,goBack;
    ImageView imageView;
    TextView ques,fscore,scores;
    TextView timer;
    TextView end;
    ProgressBar progressBar;
    GridLayout layout;
    ArrayList<String> celebURLs = new ArrayList<String>();
    ArrayList<String> celebNames = new ArrayList<String>();
    String txt;
    String[] answers = new String[4];
    int locationOfCorrectAnswer = 0;
    int chosenCeleb = 0;
    int score=0;
    int noq=0;

    public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
    public void choose(View view)
    {
        if(view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer)))
        {
            scores.setBackgroundColor(Color.GREEN);
            score+=1;
            noq++;
            Correct.start();

        }
        else
        {
            scores.setBackgroundColor(Color.RED);
            noq++;
            Wrong.start();

        }
        scores.setText(Integer.toString(score) + "/" + Integer.toString(noq));
        newQues();
    }
    public void newQues()
    {
        try {

            Random rand = new Random();
            chosenCeleb = rand.nextInt(celebURLs.size());
            ImageDownloader imageDownloader = new ImageDownloader();
            Bitmap celebImage = imageDownloader.execute(celebURLs.get(chosenCeleb)).get();
            imageView.setImageBitmap(celebImage);
            locationOfCorrectAnswer = rand.nextInt(4);
            int incorrectLocations;

            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectAnswer) {
                    answers[i] = celebNames.get(chosenCeleb);
                } else {
                    incorrectLocations = rand.nextInt(celebURLs.size());
                    while (incorrectLocations == chosenCeleb) {
                        incorrectLocations = rand.nextInt(celebURLs.size());

                    }
                    answers[i] = celebNames.get(incorrectLocations);
                }
            }
            btn0.setText(answers[0]);
            btn1.setText(answers[1]);
            btn2.setText(answers[2]);
            btn3.setText(answers[3]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void reset()
    {
        score = 0;
        noq = 0;
        scores.setVisibility(View.VISIBLE);
        scores.setText("0/0");
        fscore.setVisibility(View.GONE);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        end.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        goBack.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        scores.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
    }
    public void setTimer()
    {
        new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long l) {

                int current = progressBar.getProgress() + 1;
                progressBar.setProgress(current);
            }

            @Override
            public void onFinish() {
                fscore.setText("SCORE : " + Integer.toString(score) + "/" + Integer.toString(noq) + "!");
                fscore.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                scores.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                end.setVisibility(View.VISIBLE);
                play.setVisibility(View.VISIBLE);
                goBack.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                if(StartUp.saves == true) {
                    SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("userinfo", MODE_PRIVATE, null);
                    Cursor c = sqLiteDatabase.rawQuery("SELECT celeb FROM scores WHERE username ='"+register.user+"'",null);
                    c.moveToFirst();
                    if(score>c.getInt(0))
                    {
                        sqLiteDatabase.execSQL("UPDATE scores SET celeb = "+score+" WHERE username ='"+register.user+"'");
                    }
                }

            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celeb);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn0 = (Button) findViewById(R.id.button0);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        end = (TextView) findViewById(R.id.end);
        layout = (GridLayout) findViewById(R.id.gridLayout);
        scores = (TextView) findViewById(R.id.scoreView);
        fscore = (TextView) findViewById(R.id.fscore);
        imageView = (ImageView) findViewById(R.id.imageView);
        play = (Button) findViewById(R.id.play);
        goBack = (Button) findViewById(R.id.back);
        Correct = MediaPlayer.create(this,R.raw.correctsound);
        Wrong = MediaPlayer.create(this,R.raw.wrongsound);
        txt="";
        try
            {
                InputStream in = getResources().openRawResource(R.raw.website);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            txt=new String(buffer);
            Pattern p = Pattern.compile("img alt=\"(.*?)\"");
            Matcher m = p.matcher(txt);
            while (m.find())
            {
                celebNames.add(m.group(1));
            }
            p = Pattern.compile("src=\"(.*?)\"");
            m = p.matcher(txt);
            while (m.find())
            {
                celebURLs.add(m.group(1));
            }
            newQues();
            }
        catch (Exception e)
        {
        e.printStackTrace();
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                newQues();
                setTimer();
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SelectCategory.class);
                startActivity(intent);
            }
        });
        newQues();
        setTimer();
    }
}