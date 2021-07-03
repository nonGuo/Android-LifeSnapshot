package com.nkcs.lifesnapshot.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteHelper extends SQLiteOpenHelper {

    public NoteHelper(Context context){
        super(context, "LifeSnapshot", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table IF NOT EXISTS note_info(_id integer primary key autoincrement," +
                NoteCollection.Table.Cols.TITLE + " text," + NoteCollection.Table.Cols.FILE_DIR + " text," +
                NoteCollection.Table.Cols.FILE_TYPE + " text," +  NoteCollection.Table.Cols.MOD_TIME +" integer," +
                NoteCollection.Table.Cols.TAG + " text)";
//        String sql2 = "create table IF NOT EXISTS tag(note_id integer primary key, tag_name text)";
        db.execSQL(sql1);
//        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
