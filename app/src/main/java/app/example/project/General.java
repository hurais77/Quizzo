package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static app.example.project.StartUp.saves;

public class General extends AppCompatActivity {
TextView head;
Button celeb,gk;
Intent intent;
    Cursor c;
ImageView cinfo,ginfo;
public boolean checkConnection () {


    boolean connected = false;
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        //we are connected to a network
        connected = true;
    } else {
        connected = false;
    }
return connected;
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        intent = getIntent();
        final String name = intent.getStringExtra("name");

        final SQLiteDatabase database = this.openOrCreateDatabase("userinfo",MODE_PRIVATE,null);


        head = (TextView) findViewById(R.id.textView4);
        celeb = (Button) findViewById(R.id.celeb);
        gk = (Button) findViewById(R.id.gk);
        cinfo = (ImageView) findViewById(R.id.celeb_info);
        ginfo = (ImageView) findViewById(R.id.gk_info);


        if(StartUp.saves == false)
        {
            ginfo.setVisibility(View.GONE);
            cinfo.setVisibility(View.GONE);
        }

        head.animate().alpha(1).setDuration(1000);
        celeb.animate().alpha(1).setDuration(3000);
        gk.animate().alpha(1).setDuration(3000);
        ginfo.animate().alpha(1).setDuration(3000);
        cinfo.animate().alpha(1).setDuration(3000);


        cinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sc=0;
                AlertDialog.Builder builder = new AlertDialog.Builder(General.this);
                View view = getLayoutInflater().inflate(R.layout.scorecard,null);
                TextView score = (TextView) view.findViewById(R.id.score);
                try {
                    c = database.rawQuery("SELECT * FROM scores WHERE username = '" + register.user + "'", null);
                    int s = c.getColumnIndex("celeb");
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
        ginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sc=0;
                AlertDialog.Builder builder = new AlertDialog.Builder(General.this);
                View view = getLayoutInflater().inflate(R.layout.scorecard,null);
                TextView score = (TextView) view.findViewById(R.id.score);
                try {
                    c = database.rawQuery("SELECT * FROM scores WHERE username = '" + register.user + "'", null);
                    int s = c.getColumnIndex("gk");
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





        celeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pIntent = new Intent(getApplicationContext(),Celeb.class);
                boolean check;
                check = checkConnection();
                if(check == true)
                {
                    startActivity(pIntent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Make Sure You Have Internet Connection",Toast.LENGTH_LONG).show();
                }


            }
        });
        gk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pIntent = new Intent(getApplicationContext(),gk.class);
                startActivity(pIntent);
            }
        });


    }
}