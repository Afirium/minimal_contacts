package com.gpashev.minimal_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {
    protected String ID;
    protected EditText euName, euTel;
    protected Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        euName = findViewById(R.id.euName);
        euTel = findViewById(R.id.euTel);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            ID = b.getString("Id");
            euName.setText(b.getString("Name"));
            euTel.setText(b.getString("Phone"));
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {
                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/min_contact.db",
                            null

                    );
                    String name = euName.getText().toString();
                    String tel = euTel.getText().toString();

                    // Сheck fields for emptiness
                    if (name.isEmpty() || tel.isEmpty()) {
                        throw new java.lang.Exception("Fill Name and Phone fields");
                    }

                    String s = "UPDATE Contact SET Name=?, Phone=? WHERE ";
                    s += "Id=? ";


                    db.execSQL(s, new Object[]{name, tel, ID});

                    euName.getText().clear();
                    euTel.getText().clear();

                    Toast.makeText(getApplicationContext(),
                            "Record UPDATED",
                            Toast.LENGTH_LONG
                    ).show();

                    finishActivity(200);

                    Intent i = new Intent(UpdateActivity.this, ContactListActivity.class);
                    startActivity(i);
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
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = null;
                try {

                    db = SQLiteDatabase.openOrCreateDatabase(
                            getFilesDir().getPath() + "/min_contact.db",
                            null

                    );

                    // Remove contact
                    String s = "DELETE FROM Contact WHERE ";
                    s += "Id=? ";


                    db.execSQL(s, new Object[]{ID});
                    Toast.makeText(getApplicationContext(),
                            "Contact deleted",
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

                finishActivity(200);

                Intent i = new Intent(UpdateActivity.this, ContactListActivity.class);
                startActivity(i);
            }
        });


    }
}
