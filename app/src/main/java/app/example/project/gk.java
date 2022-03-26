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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class gk extends AppCompatActivity
{
    MediaPlayer Correct,Wrong;
    TextView ques,fscore;
    SQLiteDatabase sqLiteDatabase;
    TextView timer;
    String name1;
    ProgressBar progressBar;
    TextView end;
    Button btn0,btn1,btn2,btn3,play,goBack;
    int score=0;
    int noq=0;
    GridLayout layout;
    String question;
    int op1,op2,op3,op4,ans,q1;
    String opt1,opt2,opt3,opt4,answ,qs1;
    int location,newId;
    TextView scores;
    Cursor c;
    ArrayList<Integer> answers = new ArrayList<Integer>();

    public String tReturn(String id) {
        if (id.equals("0")) {
            return btn0.getText().toString();
        } else if (id.equals("1")) {
            return btn1.getText().toString();
        } else if (id.equals("2")) {
            return btn2.getText().toString();
        } else {
            return btn3.getText().toString();
        }
    }
    public void choose(View view)
    {
        String id = view.getTag().toString();
        String optAns = tReturn(id);
        if(optAns.equals(answ))
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
            noq++;
            Wrong.start();
        }
        scores.setText(Integer.toString(score) + "/" + Integer.toString(noq));
        newQues();

    }
    public void newQues()
    {
        Random rand = new Random();
        newId = rand.nextInt(35);
        c=sqLiteDatabase.rawQuery("SELECT * FROM gk WHERE id="+newId,null);
        c.moveToFirst();
        q1=c.getColumnIndex("question");
        op1=c.getColumnIndex("op1");
        op2=c.getColumnIndex("op2");
        op3=c.getColumnIndex("op3");
        op4=c.getColumnIndex("op4");
        ans=c.getColumnIndex("ans");

        try {
            qs1=c.getString(q1);
            opt1=c.getString(op1);
            opt2=c.getString(op2);
            opt3=c.getString(op3);
            opt4=c.getString(op4);
            answ=c.getString(ans);
        }
        catch (Exception e)
        {

        }


        ques.setText(qs1);
        btn0.setText(opt1);
        btn1.setText(opt2);
        btn2.setText(opt3);
        btn3.setText(opt4);

    }
    public void reset()
    {
        timer.setText("30s");
        score = 0;
        noq = 0;
        scores.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
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

    public void setTimer()
    {
        new CountDownTimer(30100, 1000) {
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
                    Cursor c = sqLiteDatabase.rawQuery("SELECT gk FROM scores WHERE username ='"+register.user+"'",null);
                    c.moveToFirst();
                    if(score>c.getInt(0))
                    {
                        sqLiteDatabase.execSQL("UPDATE scores SET gk = "+score+" WHERE username ='"+register.user+"'");
                    }
                }

            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football);
        location=1;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        name1=getIntent().getStringExtra("name");
        ques = (TextView) findViewById(R.id.ques);
        layout = (GridLayout) findViewById(R.id.gridLayout);
        scores = (TextView) findViewById(R.id.scoreView);
        btn0 = (Button) findViewById(R.id.button0);
        btn1 = (Button) findViewById(R.id.button1);
        btn2 = (Button) findViewById(R.id.button2);
        end = (TextView) findViewById(R.id.end);
        btn3 = (Button) findViewById(R.id.button3);
        fscore = (TextView) findViewById(R.id.fscore);
        play = (Button) findViewById(R.id.play);
        goBack = (Button) findViewById(R.id.back);
        timer = (TextView) findViewById(R.id.timer);
        Correct = MediaPlayer.create(this,R.raw.correctsound);
        Wrong = MediaPlayer.create(this,R.raw.wrongsound);

        sqLiteDatabase = this.openOrCreateDatabase("GK", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS gk (id INT(3) PRIMARY KEY,question VARCHAR(20),op1 VARCHAR(20),op2 VARCHAR(20),op3 VARCHAR(20),op4 VARCHAR(20), ans VARCHAR(20)) ");
        try {
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (1,'Who was the first person to step on the Moon?','Neil Armstrong','Rakesh Sharma','Sunita Williams','Kalpana Chawla','Neil Armstrong')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (2,'Grand Central Terminal, Park Avenue, New York is the worlds','Largest Railway Station','Highest Railway Station','Longest Railway Station','None of the Above','Largest Railway Station')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (3,'What is the top color in a rainbow?','Yellow','Indigo','Red','Violet','Red')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (4,'How many weeks are there in one year?','50','49','52','51','52')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (5,'Which is the longest river on the earth?','Periyar','Nile','Amazon','Brahmaputra','Nile')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (6,'When is the International Equal Pay Day observed?','28 June','14 December','18 September','25 March','18 September')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (7,'In what US State is the city Nashville?','Arizona','Tennessee','Houston','Los Angeles','Tennessee')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (8,'Which country in the world is believed to have the most miles of motorway?','Brazil','India','China','France','China')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (9,'What is rapper P Diddy’s real name?','Sean Paul','Sean Combs','Sean kingston','Sean Jackson','Sean Combs')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (10,'Which European city hosted the 1936 Summer Olympics?','India','London','Berlin','Greece','Berlin')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (11,'In what year did The Beatles split up?','1970','1969','1965','1963','1970')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (12,'How many permanent teeth does a dog have?','38','32','42','50','42')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (13,'In what decade was pop icon Madonna born?','1910s','1980s','1960s','1950s','1950s')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (14,'How many fingers does a human have in one hand?','4','3','2','5','5')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (15,'Which country is Pretoria situated in?','South Africa','Qatar','Italy','Brazil','South Africa')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (16,'Gouda is a popular cheese originating from which country?','France','Switzerland','Netherlands','Berlin','Netherlands')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (17,'What is the capital city of Australia?','Sydney','Queens','Canberra','Melbourne','Canberra')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (18,'What language is spoken in Brazil?','Brazilian','English','Spanish','Portugese','Portugese')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (19,'Where is Melbourne situated in?','Uruguay','Argentina','Brazil','Chile','Australia')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (20,'Where is Rio Di Janeiro situated in?','Uruguay','Brazil','Argentina','Chile','Brazil')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (21,'What is the tallest mountain in the world?','Mt. Kilimanjaro','Mt. Everest','Mt. Mauna Kea','Mt. K2','Mt. Everest')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (22,'First sponsor of English Premier League','Pepsi','Barclays','Carling','Brother','Carling')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (23,'What language is spoken in Norway?','Norwegian','Dutch','German','English','Norwegian')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (24,'How many keys are there on a piano?','66','100','88','80','88')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (25,'What is the best-selling novel of all time?','Don Quixote','The Three Musketeers','The Shining','Hardy Boys','Don Quixote')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (26,'How many planets are in our solar system?','7','9','8','10','8')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (27,'Which planet has the most moons?','Saturn','Jupiter','Uranus','Neptune','Saturn')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (28,'How many hearts does an octopus have?','1','2','3','5','3')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (29,'How many elements are in the periodic table?','115','116','117','118','118')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (30,'Where is the smallest bone in the human body located?','Nose','Ear','Finger','Leg','Ear')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (31,'Elon Musk is the CEO of which global brand.','Google','Tesla','Amazon','VIT','Tesla')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (32,'In which year was the Microsoft XP operating system released?','2000','1999','2001','2003','2001')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (33,'In which year was the Nintendo 64 released in Europe?','1990','1983','1997','2000','1997')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (34,'Who is the former drummer of Nirvana who fronts the Foo Fighters?','Jurgen Klopp','Dave Grohl','Keith Urban','Narasimha Reddy','Dave Grohl')");
            sqLiteDatabase.execSQL("INSERT INTO gk (id,question,op1,op2,op3,op4,ans) VALUES (35,'In which year did Taylor Swift release her debut single, Love Story?','2008','1965','2010','2007','2020')");
        }
        catch (Exception e)
        {

        }


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