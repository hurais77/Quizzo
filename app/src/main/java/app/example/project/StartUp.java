package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StartUp extends AppCompatActivity {
    static String logName;
EditText personName;
SQLiteDatabase myDatabase;
static Boolean saves;
InputMethodManager inputMethodManager;
TextView login;
Button btn;
    MediaPlayer buttonSound;
    Intent intent;

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        try
        {
            myDatabase = this.openOrCreateDatabase("userinfo", MODE_PRIVATE,null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        login = (TextView) findViewById(R.id.sign);
        personName = (EditText) findViewById(R.id.personName);
        buttonSound = MediaPlayer.create(this,R.raw.buttonsound);
        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logName = personName.getText().toString();
                if (logName.equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(),"Name Cannot Be Empty",Toast.LENGTH_LONG).show();
                }
else
                {
                    buttonSound.start();
                    intent = new Intent(getApplicationContext(), SelectCategory.class);
                    Log.i("save",String.valueOf(StartUp.saves));

                    register.user=logName;
                    startActivity(intent);
                    saves = false;



                }
            }
        });
login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartUp.this);
        View view = getLayoutInflater().inflate(R.layout.login,null);
        TextView sign = (TextView) view.findViewById(R.id.sign);
        final EditText email = (EditText) view.findViewById(R.id.email);
        final EditText pass = (EditText) view.findViewById(R.id.password);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),register.class);
                startActivity(intent);
            }
        });
        Button btn = (Button) view.findViewById(R.id.log);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty())
                {
                    if (isEmail(email) == false) {
                        email.setError("Enter valid email!");
                    }
                    else
                    {
                       String checkmail = email.getText().toString();
                        Cursor c = myDatabase.rawQuery("SELECT * FROM users WHERE email ='" + checkmail + "'",null);
                        int n = c.getCount();
                        if(n==1)
                        {
                            Cursor d = myDatabase.rawQuery("SELECT password FROM users WHERE email ='" + checkmail +"'",null);
                            d.moveToFirst();
                            String res = d.getString(0);
                            Log.i("Check",res);
                            if  (res.equals(pass.getText().toString()))
                            {
                                Cursor e = myDatabase.rawQuery("SELECT username FROM users WHERE email ='" + checkmail +"'",null);
                                e.moveToFirst();
                                String name = e.getString(0);
                                intent = new Intent(getApplicationContext(), SelectCategory.class);
                                saves = true;
                                register.user=name;
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                         email.setError("Email doesnt exist");
                        }


                    }
                }
                else if (email.getText().toString().isEmpty() && pass.getText().toString().isEmpty())
                {
                    email.setError("Enter an Email");
                    pass.setError("Enter a Password");
                }
                else if (email.getText().toString().isEmpty())
                {
                    email.setError("Enter an Email");
                }
                else
                {
                    pass.setError("Enter a Password");
                }
            }
        });
        builder.setView(view);
        AlertDialog mdialog = builder.create();
        mdialog.show();

    }
});


    }

    public void screentouch(View view) {
        InputMethodManager inputMethodManager =  (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}