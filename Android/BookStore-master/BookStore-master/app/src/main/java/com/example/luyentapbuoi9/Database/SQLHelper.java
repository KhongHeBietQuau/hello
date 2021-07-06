package com.example.luyentapbuoi9.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.luyentapbuoi9.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "AccountDATA";
    static final int DB_VERSION = 2;
    static final String DB_TABLE_ACCOUNT = "Accounts";
    static final String DB_TABLE_COMMENT= "Comments";
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Context context;

    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DB_TABLE_ACCOUNT + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, password TEXT)";
        String query2= "CREATE TABLE " + DB_TABLE_COMMENT + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, time TEXT, content TEXT, imageLink TEXT)";
        db.execSQL(query);
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ACCOUNT);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_COMMENT);
            onCreate(db);
        }
    }

    public boolean insertAccount(Account account) {
        sqLiteDatabase = getWritableDatabase();
        if (checkExists(account.getUsername())) {
            return false;
        }else{
            contentValues = new ContentValues();
            contentValues.put("username", account.getUsername());
            contentValues.put("password", account.getPassword());
            sqLiteDatabase.insert(DB_TABLE_ACCOUNT, null, contentValues);
            return true;
        }
    }
    public void insertComment(Account account, Book book, String contentComment){
        sqLiteDatabase=getWritableDatabase();
        contentValues=new ContentValues();
        contentValues.put("username", account.getUsername());

        Calendar now = Calendar.getInstance();
        String strDateFormat = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf=new SimpleDateFormat(strDateFormat);
        String date=sdf.format(now.getTime());

        contentValues.put("time",date);
        contentValues.put("content", contentComment);
        contentValues.put("imageLink", book.getImageLink());
        sqLiteDatabase.insert(DB_TABLE_COMMENT, null, contentValues);
    }
    public List<Comment> GetCommentOfBook(String imageLink){
        List<Comment> list=new ArrayList<>();
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DB_TABLE_COMMENT+" WHERE imageLink=?", new String[]{imageLink +""});
        while (cursor.moveToNext()){
            String username=cursor.getString(cursor.getColumnIndex("username"));
            String time =cursor.getString(cursor.getColumnIndex("time"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            list.add(new Comment(username, time, content, null));
        }
        return list;
    }

    public void deleteAccount(int id) {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE_ACCOUNT, "id=?", new String[]{id + ""});
    }

    public void deleteAllAcount() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE_ACCOUNT, null, null);
    }

    public boolean checkExists(String username) {
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DB_TABLE_ACCOUNT + " WHERE username=?", new String[]{username + ""});
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;
    }
    public boolean checkLogin(Account account){
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DB_TABLE_ACCOUNT +" WHERE username=? and password=?", new String[]{account.getUsername()+"", account.getPassword()+""});
        if (cursor.getCount()==1){
            return true;
        }
        return false;
    }

}
