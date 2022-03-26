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


public class cricket extends AppCompatActivity
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
        c=sqLiteDatabase.rawQuery("SELECT * FROM cricket WHERE id="+newId,null);
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
                    Cursor c = sqLiteDatabase.rawQuery("SELECT cricket FROM scores WHERE username ='"+register.user+"'",null);
                    c.moveToFirst();
                    if(score>c.getInt(0))
                    {
                        sqLiteDatabase.execSQL("UPDATE scores SET cricket = "+score+" WHERE username ='"+register.user+"'");
                    }
                }

            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket);
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

        sqLiteDatabase = this.openOrCreateDatabase("SPORTS", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cricket (id INT(3) PRIMARY KEY,question VARCHAR(20),op1 VARCHAR(20),op2 VARCHAR(20),op3 VARCHAR(20),op4 VARCHAR(20), ans VARCHAR(20)) ");
        try {
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (1,'Who is the highest scoring IPL Batsmen?','Suresh Raina','Glenn Maxwell','AB De Villiers','Virat Kohli','Virat Kohli')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (2,'Which is the oldest cricket club in the world?','London Cricket Club','Marylebone Cricket Club','Melbourne Cricket Club','None of the Above','Marylebone Cricket Club')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (3,'First player to score six 6s in an over in an ODI match','Yuvraj Singh','Adam Gilchrist','Brian Lara','Herschelle Gibbs','Herschelle Gibbs')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (4,'Who won the Orange cap in the 2019 IPL for being the highest run scorer in the tournament?','Virat Kohli','David Warner','Rohit Sharma','Sachin Tendulkar','David Warner')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (5,'Who set a new record for most number of sixes by an individual in an ODI innings','Eoin Morgan','Faf Du Plessis','Shaun Tait','KL Rahul','Eoin Morgan')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (6,'Who become the first cricketer to win three top ICC honours in the same year?','Shahid Afridi','Virat Kohli','Mathew Hayden','Mahela Jayawardene','Virat kohli')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (7,'Who amongst the following is the Indian player to make fastest century in the T20?','Virat Kohli','Rohit Sharma','Rishabh Pant','KL Rahul','Rishabh Pant')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (8,'Chennai Super Kings lifted the Trophy of IPL 2018 for third time by defeating?','Royal Challengers Bangalore','Mumbai Indians','Sunrisers Hyderabad','Delhi Daredevils','Sunrisers Hyderabad')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (9,'Which famous Indian player has been inducted into the ICC Hall of Fame in 2018 ?','Sourav Ganguly','Sachin Tendulkar','Rahul Dravid','Yuvraj Singh','Rahul Dravid')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (10,'First non test playing nation that beat India in International Cricket?','Sri Lanka','Bangladesh','Netherlands','South Africa','Sri Lanka')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (11,'First sportsperson nominated for Rajya Sabha membership?','Suresh Raina','Rahul Dravid','Sachin Tendulkar','Virat Kohli','Sachin Tendulkar')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (12,'Which Indian batsman was the first to hit six consecutive sixes in first-class cricket?','Yuvraj Singh','Ravi Shastri','Sunil Gavaskar','Kapil Dev','Ravi Shastri')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (13,'The book At the Close of Play is an autobiography of ?','Ricky Ponting','AB De Villiers','Sachin Tendulkar','Brian Lara','Ricky Ponting')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (14,'First player to take 10 wickets in a single game','Muttaiah Muralidharan','Jim Laker','Glenn McGrath','Shane Warne','Jim Laker')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (15,'Which of the following is the first woman cricketer who scored 6000 runs in ODIs?','Harmanpreet Kaur','Mithali Raj','Smriti Mandhana','Poonam Yadav','Mithali Raj')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (16,'Who among the following has scored first double century in World Cup Cricket?','Brian Lara','Chris Gayle','Virender Sehwag','Adam Gilchrist','Chris Gayle')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (17,'How many times have Australia won the ICC World Cup?','8','4','3','5','4')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (18,'Which country won the first Cricket World Cup in 1975??','Brazil','England','South Africa','West Indies','West Indies')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (19,'Who among the following is the highest wicket taker in all formats of cricket?','M Muralitharan','SK Warne','Anil Kumble','G McGrath','M Muralitharan')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (20,'Who among the following is the highest wicket taker of Ashes Series?','Mike Grating','Ian Border','Graham Gooch','Shane Warne','Shane Warne')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (21,'Which of the following cricketer entitled Dazzle as his autobiography?','Adam Gilchrist','Darren Gough','Allen Border','Steve Vaugh','Darren Gough')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (22,'India played its first one day in ?','1970','1974','1978','1980','1974')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (23,'Sachin Tendulkar played his One-day debut against?','England','Sri Lanka','Pakistan','West Indies','Pakistan')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (24,'How many overs are in ODI cricket?','20','100','50','80','50')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (25,' ________was India’s first One-day captain.','Sunil Gavaskar','Kapil Dev','Ajit Wadekar','Ravi Shastri','Ajit Wadekar')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (26,'In which year was the opening edition of the ICC Women’s T20 World Cup played?','2007','2009','2008','2010','2009')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (27,'Who won the opening edition of the ICC Women’s T20 World Cup?','Australia','India','England','West Indies','England')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (28,'Who is the youngest player in the ICC Cricket World Cup 2019?','Kagiso Rabada','Johnny Bairstow','Yuzvendra Chahal','Mujeeb ur Rahman','Mujeeb ur Rahman')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (29,'Who was the captain of the Indian cricket team in the ICC World Cup 1983?','Ravi Shastri','Anil Kumble','Sunil Gavaskar','Kapil Dev','Kapil Dev')");
            sqLiteDatabase.execSQL("INSERT INTO cricket (id,question,op1,op2,op3,op4,ans) VALUES (30,'Which of the following country has hosted the ICC World Cup most times?','India','New Zealand','England','West Indies','England')");
        }
        catch (Exception e)
        {

        }
        c = sqLiteDatabase.rawQuery("SELECT * FROM cricket WHERE id=7",null);
        int nameIndex = c.getColumnIndex("ans");

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