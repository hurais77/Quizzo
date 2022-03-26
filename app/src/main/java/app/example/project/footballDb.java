package app.example.project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class footballDb extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    Cursor c;
    int nameIndex;
    void  footballDb() {
        SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("SPORTS", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS football (id INT(3) PRIMARY KEY,question VARCHAR(20),op1 VARCHAR(20),op2 VARCHAR(20),op3 VARCHAR(20),op4 VARCHAR(20), ans VARCHAR(20)) ");
    }
    void createDb()
    {
        sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (1,'Who is the current manager of Chelsea FC?','Pep Guardiola','Frank Lampard','John Terry','Jurgen Klopp','Frank Lampard')");
        sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (2,'Who is the current manager of Manchested United FC?','Pep Guardiola','Alex Ferguson','Ole Gunnar Solkjaer','Jurgen Klopp','Ole Gunnar Solkjaer')");
        sqLiteDatabase.execSQL("INSERT INTO football (id,question,op1,op2,op3,op4,ans) VALUES (3,'When did Chelsea last win the Premier League?','2017','2014','2011','2019','2017')");
    }



    }

