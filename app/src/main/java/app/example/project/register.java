package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class register extends AppCompatActivity {
    static  String user;
    SQLiteDatabase myDatabase;
    Button sign;
    TextView login;
    EditText uname,email,pword,phno;
    Cursor c;
    int userIndex;
    int passIndex;

    private boolean isValidPass(String p)
    {
        if(p.matches("^[A-Za-z0-9]{5,30}$"))
        {

        }
        else if (pword.length()==0)
        {
            pword.setError("Password Cannot Be Empty");
        }
        else
        {
            pword.setError("Only Alpha Numerical Characters are Allowed");
            return false;
        }
        return true;
    }


    private boolean isValidMail(String em) {
        int n=0;
        try {
            c = myDatabase.rawQuery("SELECT * FROM users WHERE email = '"+em+"'",null);
             n = c.getCount();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(n>0)
        {
            email.setError("EMAIL ALREADY EXISTS");
            Log.i("Totals",String.valueOf(n));
            return false;
        }

        if( android.util.Patterns.EMAIL_ADDRESS.matcher(em).matches())
        {

        }
        else
        {
            email.setError("Enter Valid Email");
            return false;
        }
        return true;
    }
    private boolean isValidMobile(String phone) {
        if (android.util.Patterns.PHONE.matcher(phone).matches() && phone.length()==10)
        {

        }
        else
        {
            phno.setError("Enter Valid Phone Number");
            return false;
        }
        return true;
    }
    private boolean isValidNmae(String n)
    {
        int no=0;
        try {
            c = myDatabase.rawQuery("SELECT * FROM users WHERE email = '"+n+"'",null);
            no = c.getCount();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(no>0)
        {
            email.setError("Username ALREADY EXISTS");
            Log.i("Totals",String.valueOf(n));
            return false;
        }

        if(n.matches("^[A-Za-z]{1,30}$"))
        {

        }
        else if (n.length()==0)
        {
            uname.setError("Username Cannot Be Empty");
            return false;
        }
        else if (n.length()>30)
        {
            uname.setError("Only 30 Characters Allowed");
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sign = (Button) findViewById(R.id.sign);
        uname = (EditText) findViewById(R.id.uname);
        email = (EditText) findViewById(R.id.email);
        phno = (EditText) findViewById(R.id.mobile);
        pword = (EditText) findViewById(R.id.pword);
        login = (TextView) findViewById(R.id.log);
        try
        {
            myDatabase = this.openOrCreateDatabase("userinfo", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (username VARCHAR, email VARCHAR, phone INT, password VARCHAR )");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = uname.getText().toString();
                String pass = pword.getText().toString();
                String em = email.getText().toString();
                String phone = phno.getText().toString();

                if(isValidNmae(user)) {
                    if (isValidMail(em)){
                        if(isValidMobile(phone)){
                            if (isValidPass(pass))
                            {
                                try {
                                    myDatabase.execSQL("INSERT INTO users VALUES('"+user+"','"+em+"','"+phone+"','"+pass+"')");
                                    myDatabase.execSQL("CREATE TABLE IF NOT EXISTS scores (username VARCHAR, football INT, cricket INT, maths INT, celeb INT, gk INT)");
                                    myDatabase.execSQL("INSERT INTO scores VALUES('"+user+"',0,0,0,0,0)");
                                    Intent intent = new Intent(getApplicationContext(),SelectCategory.class);
                                    StartUp.saves=true;
                                    startActivity(intent);

                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

            }
        });
    }
    }

