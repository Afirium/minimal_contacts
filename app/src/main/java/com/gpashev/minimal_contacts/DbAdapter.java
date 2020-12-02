package com.gpashev.minimal_contacts;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class DbAdapter extends Application {
    private Context app_ctx;
    private String path = getFilesDir().getPath() + "/min_contact.db";
    private ListView simpleList;
    private EditText editName;
    private EditText editTel;

    public DbAdapter(ListView simpList) {
        simpleList = simpList;
    }

    public void setEditName(EditText editName) {
        this.editName = editName;
    }

    public void setEditTel(EditText editTel) {
        this.editTel = editTel;
    }

    public void initDB() throws SQLException {
        SQLiteDatabase db = null;
        db = SQLiteDatabase.openOrCreateDatabase(
                path,
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
                path + "/min_contact.db",
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
                app_ctx,
                R.layout.activity_listview,
                R.id.textView,
                listResults
        );

        simpleList.setAdapter(arrayAdapter);

        db.close();
    }

    public void CreateContact() {
        SQLiteDatabase db=null;

        try {

        } catch (Exception e) {

        }

        try{
            db=SQLiteDatabase.openOrCreateDatabase(
                    path+"/min_contact.db",
                    null

            );

            String Name = editName.getText().toString();
            String Phone = editTel.getText().toString();

            if (Name.isEmpty() || Phone.isEmpty()) {
                throw new java.lang.Exception("Fill Name and Phone fields");
            }

            String s="INSERT INTO Contact(Name, Phone) ";
            s+=" VALUES(?, ?);";
            db.execSQL(s, new Object[]{Name, Phone});

            editName.getText().clear();
            editTel.getText().clear();

            Toast.makeText(app_ctx,
                    "Contact Added",
                    Toast.LENGTH_LONG
            ).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        }finally {
            if(db!=null){
                db.close();
            }
        }
        try {
            selectDB();
        }catch (Exception e){

        }
    }
}
