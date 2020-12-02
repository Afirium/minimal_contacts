package com.gpashev.minimal_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected EditText editName, editTel;
    protected Button btnInsert;
    protected ListView simpleList;

    public void openContactList(View view) {
        Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
        startActivity(intent);
    }

    protected void initDB() throws SQLException {
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/min_contact.db",
                null
        );

        String q = "CREATE TABLE if not exists Contact( ";
        q += "Id integer primary key AUTOINCREMENT, ";
        q += "Name text not null, ";
        q += "Phone text not null, ";
        q += "unique(Name, Phone) ); ";

        db.execSQL(q);
        db.close();
    }

    public void selectDB() throws SQLException {
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/min_contact.db",
                null
        );

        String q = "SELECT * FROM Contact ORDER BY Name;";
        Cursor c = db.rawQuery(q, null);
        StringBuilder sb = new StringBuilder();
        ArrayList<String> listResults = new ArrayList<String>();
        while (c.moveToNext()) {
            String Name = c.getString(c.getColumnIndex("Name"));
            String Phone = c.getString(c.getColumnIndex("Phone"));
            String Id = c.getString(c.getColumnIndex("Id"));

            listResults.add(Id + " — " + Name + " — " + Phone);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(),
                R.layout.activity_listview,
                R.id.textView,
                listResults
        );

        simpleList.setAdapter(arrayAdapter);

        db.close();
    }

    public void CreateContact() {
        SQLiteDatabase db = null;

        try {

            // Create contact
            db = SQLiteDatabase.openOrCreateDatabase(
                    getFilesDir().getPath() + "/min_contact.db",
                    null
            );

            String Name = editName.getText().toString();
            String Phone = editTel.getText().toString();

            if (Name.isEmpty() || Phone.isEmpty()) {
                throw new java.lang.Exception("Fill Name and Phone fields");
            }

            String s = "INSERT INTO Contact(Name, Phone) ";
            s += " VALUES(?, ?);";
            db.execSQL(s, new Object[]{Name, Phone});

            editName.getText().clear();
            editTel.getText().clear();

            Toast.makeText(getApplicationContext(),
                    "Contact Added",
                    Toast.LENGTH_LONG
            ).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {
            if (db != null) {
                db.close();
            }
        }
        try {
            selectDB();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            initDB();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        editName = findViewById(R.id.editName);
        editTel = findViewById(R.id.editTel);
        btnInsert = findViewById(R.id.btnInsert);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateContact();

                // Auto hide keyboard
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }
}
