package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.StrictMath.abs;

public class brain extends AppCompatActivity {

    MediaPlayer Correct,Wrong;
    TextView ques,fscore;
    TextView timer;
    TextView end;
    Intent intent;
    String name1;
    ProgressBar progressBar;
    Button btn0,btn1,btn2,btn3,play,goBack;
    int location;
    int score=0;
    int noq=0;
    TextView scores;
    GridLayout layout;

    ArrayList<Integer> answers = new ArrayList<Integer>();

    public void choose(View view)
    {
        if(Integer.toString(location).equals(view.getTag().toString()))
        {
            ques = (TextView) findViewById(R.id.ques);
            ques.setText("Correct");
            scores.setBackgroundColor(Color.GREEN);
            score+=1;
            noq++;
            Correct.start();

        }
        else {
        ques = (TextView) findViewById(R.id.ques);
        ques.setText("Wrong");
        scores.setBackgroundColor(Color.RED);
        Log.i("Hello","Wrong");
        noq++;
        Wrong.start();
    }
        scores.setText(Integer.toString(score) + "/" + Integer.toString(noq));
        newQues();
    }

    public void newQues() {
        Random rand = new Random();
        int a;
        int b;
        int op = rand.nextInt(3);
        location = rand.nextInt(4);
        String operator;
        int ans;
        if (op == 0) {
            a = rand.nextInt(500);
            b = rand.nextInt(500);
            operator = "+";
            ans = a + b;
        } else if(op == 1){
            a = rand.nextInt(50);
            b = rand.nextInt(50);
            operator = "*";
            ans = a * b;
        }
        else
        {
            a = rand.nextInt(500);
            b = rand.nextInt(500);
            operator = "-";
            while(b>a)
            {
                a = rand.nextInt(500);
                b = rand.nextInt(500);
            }
            ans = a - b;
        }

        ques = (TextView) findViewById(R.id.ques);
        ques.setText(Integer.toString(a) + " " + operator + " " + Integer.toString(b));
        answers.clear();
        for (int i = 0; i < 4; i++) {
            if (i == location) {
                answers.add(ans);
            } else {
                if (op == 0) {
                    int wans = rand.nextInt(500) + rand.nextInt(500);
                    while (wans == ans) {
                        wans = rand.nextInt(500) + rand.nextInt(500);
                    }
                    answers.add(wans);
                } else if(op == 1) {
                    int wans = rand.nextInt(50) * rand.nextInt(50);
                    while (wans == ans) {
                        wans = rand.nextInt(50) * rand.nextInt(50);
                    }
                    answers.add(wans);
                }
                else {
                    int wans = abs(rand.nextInt(500) - rand.nextInt(500));
                    while (wans == ans) {
                        wans = abs(rand.nextInt(500) - rand.nextInt(500));
                    }
                    answers.add(wans);
                }
            }

        }
        btn0.setText(Integer.toString(answers.get(0)));
        btn1.setText(Integer.toString(answers.get(1)));
        btn2.setText(Integer.toString(answers.get(2)));
        btn3.setText(Integer.toString(answers.get(3)));


    }

    public void setTimer()
    {
        new CountDownTimer(60100, 1000) {
            @Override
            public void onTick(long l) {
                timer.setText(String.valueOf((l/1000) + "s"));
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
                ques.setVisibility(View.GONE);
                timer.setVisibility(View.INVISIBLE);
                if(StartUp.saves == true) {
                    SQLiteDatabase sqLiteDatabase = getApplicationContext().openOrCreateDatabase("userinfo", MODE_PRIVATE, null);
                    Cursor c = sqLiteDatabase.rawQuery("SELECT maths FROM scores WHERE username ='"+register.user+"'",null);
                    c.moveToFirst();
                    if(score>c.getInt(0))
                    {
                        sqLiteDatabase.execSQL("UPDATE scores SET maths = "+score+" WHERE username ='"+register.user+"'");
                    }
                }

            }
        }.start();
    }
public void reset()
{
    timer.setText("30s");
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
    ques.setVisibility(View.VISIBLE);
    timer.setVisibility(View.VISIBLE);

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        name1=getIntent().getStringExtra("name");
        Correct = MediaPlayer.create(this,R.raw.correctsound);
        Wrong = MediaPlayer.create(this,R.raw.wrongsound);
        end = (TextView) findViewById(R.id.end);
        layout = (GridLayout) findViewById(R.id.gridLayout);
        scores = (TextView) findViewById(R.id.scoreView);
        fscore = (TextView) findViewById(R.id.fscore);
        btn0 = (Button) findViewById(R.id.button1);
        btn1 = (Button) findViewById(R.id.button2);
        btn2 = (Button) findViewById(R.id.sbutton);
        btn3 = (Button) findViewById(R.id.button4);
        play = (Button) findViewById(R.id.play);
        goBack = (Button) findViewById(R.id.back);
        timer = (TextView) findViewById(R.id.timer);
        goBack.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(getApplicationContext(),SelectCategory.class);
                                          intent.putExtra("name",name1);
                                          startActivity(intent);
                                      }
                                  });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reset();
                        newQues();
                        setTimer();
                    }
                });
        newQues();
        setTimer();
    }

    }
