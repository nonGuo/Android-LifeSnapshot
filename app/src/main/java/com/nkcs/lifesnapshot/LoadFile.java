package com.nkcs.lifesnapshot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nkcs.lifesnapshot.SQLite.Note;
import com.nkcs.lifesnapshot.SQLite.NoteCollection;
import com.nkcs.lifesnapshot.SQLite.NoteHelper;

import java.util.LinkedList;
import java.util.List;

public class LoadFile {
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    public LoadFile(Context _context){
        context = _context;
        sqLiteDatabase = new NoteHelper(context).getReadableDatabase();
    }

    public List<Note> getNote(){
        List<Note> note_list = new LinkedList<Note>();
        cursor = sqLiteDatabase.query(NoteCollection.Table.NAME, null,
                null,null,null, null,
                NoteCollection.Table.Cols.MOD_TIME + " desc");
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String dir = cursor.getString(2);
            String type = cursor.getString(3);
            int time = cursor.getInt(4);
            String tag = cursor.getString(5);
            note_list.add(new Note(id, dir, title, time, type, tag));
            cursor.moveToNext();
        }
        return note_list;
    }

    public List<Note> getNoteFromType(String type){
        List<Note> note_list = new LinkedList<Note>();
        cursor = sqLiteDatabase.query(NoteCollection.Table.NAME, null,
                "type=?",new String[]{type},null, null,
                NoteCollection.Table.Cols.MOD_TIME + " desc");
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++){
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String dir = cursor.getString(2);
            int time = cursor.getInt(4);
            String tag = cursor.getString(5);
            note_list.add(new Note(id, dir, title, time, type, tag));
            cursor.moveToNext();
            Log.d("tag", tag);
        }
        return note_list;
    }

    
}
