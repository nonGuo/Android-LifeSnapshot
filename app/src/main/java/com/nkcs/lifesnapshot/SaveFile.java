package com.nkcs.lifesnapshot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nkcs.lifesnapshot.SQLite.Note;
import com.nkcs.lifesnapshot.SQLite.NoteCollection;
import com.nkcs.lifesnapshot.SQLite.NoteHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveFile {
    private String dir;
    private String type;
    private File dirFile;
    private Context context;
    private NoteHelper noteHelper;
    private SQLiteDatabase sqLiteDatabase;
    public boolean isNew;
    private int id;

    public SaveFile(Context _context, String _type){
        context = _context;
        type = _type;
        dir = context.getExternalFilesDir("data").getAbsolutePath();
        dirFile = new File(dir);
        noteHelper = new NoteHelper(_context);
        sqLiteDatabase = noteHelper.getWritableDatabase();
        isNew = true;
    }

    public SaveFile(Context _context, String _type, int _id, String _dir){
        context = _context;
        type = _type;
        dir = _dir;
        dirFile = new File(dir);
        isNew = false;
        id = _id;
        noteHelper = new NoteHelper(_context);
        sqLiteDatabase = noteHelper.getWritableDatabase();
    }

    public void saveContent(String html){
        if(isNew){
            saveHTML(html);
        }else{
            updateHTML(html);
        }
    }

    public void saveTag(String tagName){
        ContentValues values = new ContentValues();
        values.put("tag", tagName);
        sqLiteDatabase.update(NoteCollection.Table.NAME, values, "_id=?",
                new String[]{String.valueOf(id)});
//        Cursor cursor = sqLiteDatabase.rawQuery("select tag_name from tag where note_id=?",
//                new String[]{String.valueOf(id)});
//        if(cursor.getCount() > 0){
//            sqLiteDatabase.update("tag", values,"note_id=?",new String[]{String.valueOf(id)});
//        }else{
//            sqLiteDatabase.insert("tag", null, values);
//        }
    }

    public void deleteFile(){
        sqLiteDatabase.delete(NoteCollection.Table.NAME, "_id = ?", new String[]{String.valueOf(id)});
        File file = new File(dir);
        if(file.exists()){
            file.delete();
        }
    }

    private void saveHTML(String html){
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        long timestamp = System.currentTimeMillis();
        String time = sdf.format(new Date(timestamp));
        String path = dir + "/" + time + ".html";
        try{
            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = html.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();

            ContentValues values = new ContentValues();
            values.put(NoteCollection.Table.Cols.TITLE, "title");
            values.put(NoteCollection.Table.Cols.FILE_DIR, path);
            values.put(NoteCollection.Table.Cols.FILE_TYPE, type);
            values.put(NoteCollection.Table.Cols.MOD_TIME, timestamp);
            values.put(NoteCollection.Table.Cols.TAG, "");
            Cursor cursor = sqLiteDatabase.query(NoteCollection.Table.NAME, null,
                    null,null,null, null,
                    null);
            String[] name = cursor.getColumnNames();
            for(int i =0; i< name.length; i++){
                Log.d("column", i + ": " + name[i]);
            }
            sqLiteDatabase.insert(NoteCollection.Table.NAME, null, values);

            dir = path;
            Cursor cursor1 = sqLiteDatabase.rawQuery("select LAST_INSERT_ROWID()", null);
            id = cursor1.getInt(0);
            isNew = false;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateHTML(String html){
        try{
            File file = new File(dir);
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            byte[] bytes = html.getBytes();
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            ContentValues values = new ContentValues();
            values.put(NoteCollection.Table.Cols.MOD_TIME, System.currentTimeMillis());
            sqLiteDatabase.update(NoteCollection.Table.NAME, values, "_id = ?",
                    new String[]{String.valueOf(id)});
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveImage(String path){

    }
}
