package com.nkcs.lifesnapshot.SQLite;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;

public class Note {
    public int id;
    public String dir;
    public String title;
    public long time;
    public String type;
    public String tag;
    private String content;

    public Note(int _id, String _dir, String _title,int _time, String _type, String _tag){
        id = _id;
        dir = _dir;
        title = _title;
        time = _time;
        type = _type;
        tag = _tag;
        content = null;
    }

    private Note(){
        content = null;
    }

    public String getContent(){
        if(content == null){
            readFile();
        }
        return content;
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer();
        Field[] fields = Note.class.getFields();
        for(Field f : fields){
            try {
                buffer.append(f.getName() + "=" + f.get(this).toString() + ",");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    public static Note fromString(String noteStr){
        String[] subStr = noteStr.split(",");
        Note note = new Note();
        for(String str: subStr){
            String[] keyValue = str.split("=");
            if(keyValue.length > 1){
                setValue(note, keyValue[0], keyValue[1]);
            }else{
                setValue(note, keyValue[0], "");
            }
        }
        return note;
    }

    private static void setValue(Note note, String key, String value){
        switch (key){
            case "id":
                note.id = Integer.parseInt(value);
                break;
            case "dir":
                note.dir = value;
                break;
            case "title":
                note.title = value;
                break;
            case "time":
                note.time = Integer.parseInt(value);
                break;
            case "type":
                note.type = value;
                break;
            case "tag":
                note.tag = value;
                break;
        }
    }

    private void readFile(){
        File file = new File(dir);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuilder result = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                result.append(temp);
            }
            reader.close();
            bufferedReader.close();
            inputStream.close();

            content = result.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
