package app.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class change_details extends AppCompatActivity {

    EditText curpass,newpass;
    SQLiteDatabase database;
    Cursor c;
    Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);



        curpass = (EditText) findViewById(R.id.curpass);
        newpass = (EditText) findViewById(R.id.newpass);
        change = (Button) findViewById(R.id.change);

        try {
            database = this.openOrCreateDatabase("userinfo",MODE_PRIVATE,null);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cpass,npass;
                cpass=curpass.getText().toString();
                npass=newpass.getText().toString();

                if(cpass.equals(""))
                {
                    curpass.setError("Enter Current Password");
                }
                if (npass.equals(""))
                {
                    newpass.setError("Enter New Password");
                }

                c = database.rawQuery("SELECT password FROM users WHERE username = '"+register.user+"'",null);
                c.moveToFirst();
                String pword = c.getString(0);
                if(!(cpass.equals(pword)))
                {
                    Toast.makeText(getApplicationContext(),"Invalid Current Password" ,Toast.LENGTH_LONG ).show();
                }
                else
                {
                    database.execSQL("UPDATE users SET password = '"+npass+"' WHERE username = '"+register.user+"'");
                    Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_LONG ).show();
                }

            }
        });
    }
}