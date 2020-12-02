package com.gpashev.minimal_contacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.SQLException;
import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    protected ListView simpleList;

    public void selectDB() throws SQLException {
        // Create DB connection
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath() + "/min_contact.db",
                null
        );

        // SQL Query
        String q = "SELECT * FROM Contact ORDER BY Name;";

        Cursor c = db.rawQuery(q, null);
        StringBuilder sb = new StringBuilder();
        ArrayList<String> listResults = new ArrayList<String>();

        // Get all contacts from table Contact to ArrayList
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

        // Close connection
        db.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ContactListActivity.this, MainActivity.class));
        finish();
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        simpleList = findViewById(R.id.simpleListView);

        try {
            selectDB();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list_view);

        simpleList = findViewById(R.id.simpleListView);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = "";
                TextView clickedText = view.findViewById(R.id.textView);
                selected = clickedText.getText().toString();
                String[] elements = selected.split(" — ");
                String ID = elements[0];
                Toast.makeText(getApplicationContext(), ID, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ContactListActivity.this, UpdateActivity.class);

                Bundle b = new Bundle();
                b.putString(
                        "Id", ID
                );
                b.putString("Name", elements[1]);
                b.putString("Phone", elements[2]);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);
            }
        });

        try {
            selectDB();
        } catch (Exception e) {

        }
    }
}
