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


public class football extends AppCompatActivity
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
        c=sqLiteDatabase.rawQuery("SELECT * FROM football WHERE id="+newId,null);
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
            public void onFinish()
            {
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
                    Cursor c = sqLiteDatabase.rawQuery("SELECT football FROM scores WHERE username ='"+register.user+"'",null);
                    c.moveToFirst();
                    if(score>c.getInt(0))
                    {
                        sqLiteDatabase.execSQL("UPDATE scores SET football = "+score+" WHERE username ='"+register.user+"'");
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

        sqLiteDatabase = this.openOrCreateDatabase("SPORTS", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS football (id INT(3) PRIMARY KEY,question VARCHAR(20),op1 VARCHAR(20),op2 VARCHAR(20),op3 VARCHAR(20),op4 VARCHAR(20), ans VARCHAR(20)) ");
        try {
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (1,'When did Manchester City last win the Premier League?','2017','2018','2011','2019','2019')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (2,'When did Chelsea last win the Premier League?','2017','2014','2011','2019','2017')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (3,'Who is the current manager of Manchested United FC?','Pep Guardiola','Alex Ferguson','Ole Gunnar Solkjaer','Jurgen Klopp','Ole Gunnar Solkjaer')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (4,'Who is the current manager of Manchested City FC?','Pep Guardiola','Alex Ferguson','Ole Gunnar Solkjaer','Luiz Enrique','Pep Guardiola')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (5,'Who are the current UEFA Champions League Winners','Bayern Munich','Barcelona','Real Madrid','Chelsea','Bayern Munich')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (6,'Who are the current Premier League Winners','Liverpool','Chelsea','Manchester City','Arsenal','Liverpool')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (7,'Who was the top scorer in the last FIFA World Cup','Luka Modric','Harry Kane','Eden Hazard','Sterling','Harry Kane')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (8,'Who were FIFA WorldCup 2018 Winners','Brazil','Argentina','Croatia','France','France')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (9,'Who were FIFA WorldCup 2014 Winners','Brazil','Belgium','Spain','France','Spain')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (10,'Highest Goalscoring Chelsea Player','Didier Drogba','Gianfranco Zola','Frank Lampard','Eden Hazard','Frank Lampard')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (11,'Youngest Player to score in a FA CUP final? ','Wayne Rooney','Robin Van Persie','Theirry Henry','Christian Pulisic','Christian Pulisic')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (12,'PL Player Of the Year - 2020? ','Kevin De Bruyne','Raheem Sterling','Jordan Henderson','Van Dijk','Kevin De Bruyne')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (13,'When Did Cristiano Ronaldo Join Real Madrid? ','2010','2008','2009','2012','2009')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (14,'How many Ballon d`ORs Does Lionel Messi have? ','4','3','2','6','6')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (15,'Where was the FIFA 2014 World held','South Africa','Qatar','Italy','Brazil','South Africa')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (16,'Which player has the most goals among the following?','Zlatan Ibrahimovic','Lionel Messi','Cristiano Ronaldo','Pele','Pele')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (17,'Which player has scored the most goals in the English Premier League?','Alan Shearer','Sergio Aguero','Cristiano Ronaldo','Frank Lampard','Alan Shearer')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (18,'The penalty area is usually how far from the touch line?','19 yards','20 yards','18 yards','17 yards','18 yards')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (19,'Which of the following country hosted the first Football World Cup?','Uruguay','Argentina','Brazil','Chile','Uruguay')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (20,'Which of the following country won Football World Cup maximum times?','Uruguay','Brazil','Argentina','Chile','Brazil')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (21,'Which player holds the record for the most goals in consecutive Premier League games?','Sergio Aguero','Jamie Vardy','Pierre-Emerick Aubameyang','Thierry Henry','Jamie Vardy')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (22,'Who was the first sponsor of the Premier League?','Pepsi','Barclays','Carling','Brother','Carling')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (23,'Which player scored the fastest hat-trick in the Premier League?','Mohammed Salah','Sadio Mane','Marcus Rashford','Raheem Sterling','Sadio Mane')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (24,'Which player, with 653 games, has made the most Premier League appearances?','Michael Owen','Ryan Giggs','Paul Scholes','Gareth Barry','Gareth Barry')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (25,'Which Ballon dOr-winning footballer had a galaxy named after them in 2015?','Lionel Messi','Luka Modric','Sergio Aguero','Cristiano Ronaldo','Cristiano Ronaldo')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (26,'Messi has won a record number of Ballon dOr awards - how many?','4','5','6','7','6')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (27,'Who is the Champions Leagues top goalscorer of all time?','Diego Maradona','Pele','Lionel Messi','Cristiano Ronaldo','Cristiano Ronaldo')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (28,'The Euro 2000 final between France and Italy was decided by Golden Goal. Which player scored the goal?','Zinedine Zidane','David Trezeguet','Paolo Maldini','Giorgio Chiellini','David Trezeguet')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (29,'With 365 goals, who holds the record for top Bundesliga goalscorer of all time?','Thomas Mueller','Gerd Muller','Robert Lewandowski','Serge Gnabry','Gerd Muller')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (30,'I was the first Liverpool player to win the Ballon dOr. I scored 40 goals for my country and have played in England and Spain. Who Am I?','Michael Owen','Steven Gerrard','Fernando Torres','Mohammed Salah','Michael Owen')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (31,'In video game FIFA 20, team Piemonte Calcio represents which real-life club?','AS Roma','Juventus FC','Inter Milan','Manchester City','Juventus FC')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (32,'Which MLS franchise team does David Beckham own?','Inter Milan','Inter Miami','L.A Galaxy','RB Leipzig','Inter Miami')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (33,'Which club is associated with Galacticos?','Inter Milan','Real Madrid','L.A Galaxy','Barcelona','Real Madrid')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (34,'I was originally a striker before becoming a defender. I played 11 seasons for the same club before managing them. Ive won two Bundesliga titles and a Champions League.Who Am I?','Jurgen Klopp','Rivaldo','Pep Guardiola','L Roman','Jurgen Klopp')");
            sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (35,'When was the first FIFA World Cup inaugurated?','1954','1965','1926','1930','1930')");
        }
        catch (Exception e)
        {

        }
        c = sqLiteDatabase.rawQuery("SELECT * FROM football WHERE id=7",null);
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