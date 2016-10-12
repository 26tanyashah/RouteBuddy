package com.example.aditi.routebuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME="routedatabase.db";
    public static final String CONTACTS_TABLE_NAME="register";
    public static final String CONTACTS_COLUMN_USERID="loginid";
    public static final String CONTACTS_COLUMN_NAME="name";
    public static final String CONTACTS_COLUMN_EMAIL="email";
    public static final String CONTACTS_COLUMN_PHONENO="phone";
    public static final String CONTACTS_COLUMN_PASSWORD="password";

    public DBhelper(Context context)
    {
        super(context,DATABASE_NAME, null,1);
    }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table register " + "(loginid varchar(10) primary key,name varchar(15),phone integer(20),email varchar(20),password varchar(12)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS register");
        onCreate(db);
    }
    public void login_details(String login,String pass,String email,long phone,String name){
        SQLiteDatabase db=this.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("loginid", login);
            contentValues.put("name", name);
            contentValues.put("email", email);
            contentValues.put("phone", phone);
            contentValues.put("password", pass);
            db.insert("register", null, contentValues);
        }
        catch(SQLiteException s)
        {
            Log.d("match5","match5");
        }
        //String query = "INSERT INTO register (loginid,name,email,phone,password) VALUES('"+login+" , "+name+"', '"+email+" , "+email+" , "+phone+" , "+pass+");";
        //db.execSQL(query);
        //return true;
        db.close();
    }

    public int checklogin(String login,String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("match","match1");
        int b=0;
        Cursor res = db.rawQuery("select * from register",null);
        Log.d("match","match2");
        //Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        res.moveToFirst();
        Log.d("match4","match4");
            while (res.isAfterLast() == false) {
                Log.d("match3","match3");
                String loginid = res.getString(0);
                String pass = res.getString(4);
                Log.d(loginid,pass);
                Log.d(login,password);
                if (loginid.equals(login) && pass.equals(password)) {
                    b=1;
                    Log.d("match","match");
                    res.close();
                    break;
                }
                res.moveToNext();
            }
        res.close();
        db.close();
        return(b);
    }

}
