package app.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {

    SQLiteDatabase myDatabase;
Intent intent;

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText pass = (EditText) findViewById(R.id.pword);
        TextView sign = (TextView) findViewById(R.id.signIn);

        try
        {
            myDatabase = this.openOrCreateDatabase("userinfo", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (username VARCHAR, email VARCHAR, phone INT, password VARCHAR )");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Button btn = (Button) findViewById(R.id.sign);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),register.class);
                startActivity(intent1);
            }
        });
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
                                StartUp.saves = true;
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
    }
}