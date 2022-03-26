package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class sports extends AppCompatActivity {
    TextView head;
    Button fball,cric;
    Intent intent;
    Cursor c;
    ImageView finfo,cinfo;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        intent = getIntent();
        final String name = intent.getStringExtra("name");

        final SQLiteDatabase database = this.openOrCreateDatabase("userinfo",MODE_PRIVATE,null);


        head = (TextView) findViewById(R.id.textView4);
        fball = (Button) findViewById(R.id.ft);
        cric = (Button) findViewById(R.id.ct);
        finfo = (ImageView) findViewById(R.id.football_info);
        cinfo = (ImageView) findViewById(R.id.cricket_info);

        head.animate().alpha(1).setDuration(1000);
        fball.animate().alpha(1).setDuration(3000);
        cric.animate().alpha(1).setDuration(3000);

        if(StartUp.saves == true)
        {
            cinfo.animate().alpha(1).setDuration(3000);
            finfo.animate().alpha(1).setDuration(3000);
            cinfo.setVisibility(View.VISIBLE);
            finfo.setVisibility(View.VISIBLE);

        }
        finfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sc=0;
                AlertDialog.Builder builder = new AlertDialog.Builder(sports.this);
                View view = getLayoutInflater().inflate(R.layout.scorecard,null);
                TextView score = (TextView) view.findViewById(R.id.score);
                try {
                    c = database.rawQuery("SELECT * FROM scores WHERE username = '" + register.user + "'", null);
                    int s = c.getColumnIndex("football");
                    c.moveToFirst();
                    sc = c.getInt(s);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(sc == 0)
                {
                    score.setText("You haven't attempted the quiz yet");
                }
                else
                {
                    score.setText(String.valueOf(sc));
                }

                score.setText(String.valueOf(sc));
                builder.setView(view);
                AlertDialog mdialog = builder.create();
                mdialog.show();
            }
        });
        cinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sc=0;
                AlertDialog.Builder builder = new AlertDialog.Builder(sports.this);
                View view = getLayoutInflater().inflate(R.layout.scorecard,null);
                TextView score = (TextView) view.findViewById(R.id.score);
                try {
                    c = database.rawQuery("SELECT * FROM scores WHERE username = '" + register.user + "'", null);
                    int s = c.getColumnIndex("cricket");
                    c.moveToFirst();
                    sc = c.getInt(s);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(sc == 0)
                {
                    score.setText("You haven't attempted the quiz yet");
                }
                else
                {
                    score.setText(String.valueOf(sc));
                }

                builder.setView(view);
                AlertDialog mdialog = builder.create();
                mdialog.show();
            }
        });





        fball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pIntent = new Intent(getApplicationContext(),football.class);
                    startActivity(pIntent);

            }
        });
        cric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cIntent = new Intent(getApplicationContext(),cricket.class);
                startActivity(cIntent);
            }
        });


    }
}