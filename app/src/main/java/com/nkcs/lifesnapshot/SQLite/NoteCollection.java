package com.nkcs.lifesnapshot.SQLite;


public class NoteCollection {
    public static final class Table {
        public static final String NAME = "note_info";
        public static final class Cols {
            public static final String ID = "_id";
            public static final String FILE_DIR = "file_dir";
            public static final String TITLE = "title";
            public static final String MOD_TIME = "modify_time";
            public static final String FILE_TYPE = "type";
            public static final String TAG = "tag";
        }
    }
}