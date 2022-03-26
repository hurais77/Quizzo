package app.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SelectCategory extends AppCompatActivity {
    MediaPlayer buttonSound;
    SQLiteDatabase database;
    TextView personName,head;
    String name;
    ImageView info;
    Cursor c;
    ImageView footinfo,cricinfo;
    Intent intent;
    Button general,sbutton,mathButton,football,cricket;
  DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    public void brain(View view)
    {
        intent.putExtra("name",name);
        startActivity(intent);

    }
//    public void showDialog()
//    {
//        Button btn1 = (Button) findViewById(R.id.startit);
//        Button btn2 = (Button) findViewById(R.id.close);
//        final Dialog dialog = new Dialog(SelectCategory.this);
//        dialog.setContentView(R.layout.bt_rules);
//        dialog.show();
//
//    }


//    }
                      @SuppressLint("SetTextI18n")

@Override
public void onBackPressed() {
                          if(StartUp.saves==true)
                          {
                              AlertDialog.Builder builder = new AlertDialog.Builder(this);
                              builder.setMessage("Do you want to Log Out?")
                                      .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              SelectCategory.super.onBackPressed();
                                          }
                                      })
                                      .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {

                                          }
                                      });
                              AlertDialog dialog = builder.create();
                              dialog.show();
                          }
                          else
                          {
                              super.onBackPressed();
                          }
    }



                      @Override


    protected void onCreate(Bundle savedInstanceState) {
        intent = new Intent(getApplicationContext(),brain.class);
        buttonSound = MediaPlayer.create(this,R.raw.buttonsound);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);


         View v = getLayoutInflater().inflate(R.layout.header,null);
         TextView hname = (TextView) v.findViewById(R.id.header_name);

         if(StartUp.saves == true) {
             hname.setText(register.user);
         }
         else
         {
             hname.setText(StartUp.logName);
         }

                          this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                          getSupportActionBar().setDisplayShowCustomEnabled(true);
                          getSupportActionBar().setCustomView(R.layout.custom_action);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.settings)
        {
            if(StartUp.saves == true) {
                Intent intent2 = new Intent(getApplicationContext(), change_details.class);
                startActivity(intent2);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"You Aren't Logged In",Toast.LENGTH_LONG).show();
            }
            }
        else if(menuItem.getItemId() == R.id.logout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Do you want to Log Out?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SelectCategory.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return false;
    }
});

        info = (ImageView) findViewById(R.id.math_info);
        head = (TextView) findViewById(R.id.textView4);
        football = (Button) findViewById(R.id.celeb);
        cricket = (Button) findViewById(R.id.gk);
        sbutton = (Button) findViewById(R.id.sbutton);
        mathButton = (Button) findViewById(R.id.mathsButton);
        general = (Button) findViewById(R.id.button4);
        personName = (TextView) findViewById(R.id.helloName);
        footinfo = (ImageView) findViewById(R.id.football_info);
        cricinfo = (ImageView) findViewById(R.id.cricket_info);
        name = getIntent().getStringExtra("name");

        database=this.openOrCreateDatabase("userinfo",MODE_PRIVATE,null);
                          Log.i("save",String.valueOf(StartUp.saves));
                          if(StartUp.saves == false)
                          {
                              info.setVisibility(View.GONE);
                          }



        if(StartUp.saves==true) {
            personName.setText("Hello " + register.user + "!");
        }
        else
        {
            personName.setText("Hello " + StartUp.logName + "!");
        }

        personName.setGravity(Gravity.CENTER_HORIZONTAL);


        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gIntent = new Intent(getApplicationContext(),General.class);
                gIntent.putExtra("name",name);
                startActivity(gIntent);
                Log.i("save",String.valueOf(StartUp.saves));
            }
        });
        sbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sIntent = new Intent(getApplicationContext(),sports.class);
                startActivity(sIntent);

                    }
                });


                          info.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  int sc=0;
                                  AlertDialog.Builder builder = new AlertDialog.Builder(SelectCategory.this);
                                  View view = getLayoutInflater().inflate(R.layout.scorecard,null);
                                  TextView score = (TextView) view.findViewById(R.id.score);
                                  try {
                                      c = database.rawQuery("SELECT * FROM scores WHERE username = '" + register.user + "'", null);
                                      int s = c.getColumnIndex("maths");
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



            }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return toggle.onOptionsItemSelected(item);
    }

        }

