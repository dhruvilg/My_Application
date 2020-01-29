package vss.myandroid.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public DBManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public Cursor getPersonaldetails() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String columns[] = {DBHelper.DATA};
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DBHelper.TABLE_NAME, null);
        return cursor;
    }

    public void DeletePersonaltable() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.rawQuery(" delete from " + DBHelper.TABLE_NAME, null);
    }

    public int getpersonalcount() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String columns[] = {DBHelper.DATA};
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DBHelper.TABLE_NAME, null);
        if (cursor.getCount() == 0) {
            return 0;
        }
        return 1;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertpersonal(String Access) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.DATA, Access);
        long i = sqLiteDatabase.insertWithOnConflict(DBHelper.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return i;
    }

   /* public long insertNews(String Access) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.NEWS_DATA, Access);
        long i = sqLiteDatabase.insertWithOnConflict(DBHelper.TABLE_NEWS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return i;
    }

    public Cursor getNewsdetails() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String columns[] = {DBHelper.NEWS_DATA};
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DBHelper.TABLE_NEWS_NAME, null);
        return cursor;
    }

    public void DeleteNewstable() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.rawQuery(" delete from " + DBHelper.TABLE_NEWS_NAME, null);
    }

    public int getnewscount() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String columns[] = {DBHelper.NEWS_DATA};
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + DBHelper.TABLE_NEWS_NAME, null);
        if (cursor.getCount() == 0) {
            return 0;
        }
        return 1;
    }*/

}
