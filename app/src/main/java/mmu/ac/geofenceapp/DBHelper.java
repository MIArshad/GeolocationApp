package mmu.ac.geofenceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.Authenticator;

public class DBHelper extends SQLiteOpenHelper {

    public static final String database_name = "geofence";

    public static final int database_version = 1;

    public static final String table_users = "users";

    public static final String Key_ID = "ID";

    public static final String Key_username = "username";

    public static final String Key_email = "email";

    public static final String Key_password = "password";

    public static final String SQL_users_table = " CREATE TABLE " + table_users
            + " ( "
            + Key_ID + " INTEGER PRIMARY KEY, "
            + Key_username + " TEXT, "
            + Key_email + " TEXT, "
            + Key_password + " TEXT "
            +" ) ";

    public DBHelper(Context context)
    {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_users_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + table_users);
    }

    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Key_username, user.username);
        values.put(Key_email, user.email);
        values.put(Key_password, user.password);

        long insrt = db.insert(table_users, null, values);
    }

    public User Authenticate(User user)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_users,
                new String[]{Key_ID, Key_username, Key_email, Key_password},
                Key_email + "=?",
                new String[]{user.email},
                null, null, null);
        if(cursor != null && cursor.moveToFirst() && cursor.getCount()>0)
        {
            //if the cursor has a value, then it has matched a value in the user database
            User user1 = new User (cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //check if both passwords match each other
            if (user.password.equalsIgnoreCase(user1.password))
            {
                return user1;
            }

        }

        //if the password doesn't match, or there isn't a user with that email, then return null
        return null;
    }

    public boolean doesEmailExist(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(table_users,
                new String[]{Key_ID, Key_username, Key_email, Key_password},
                Key_email + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount()>0)
        {
            //if the cursor has a value, then there is a user in the database with this email, so return true
            return true;
        }

        //if the email doesn't exists, return false
        return false;

    }
}
