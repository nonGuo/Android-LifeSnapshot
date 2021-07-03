package com.nkcs.lifesnapshot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.nkcs.lifesnapshot.SQLite.Note;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

public class RichEditorActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 1;

    private RichEditor mEditor;
    private Toolbar toolBar;
    private PopupWindow popupWindow;

    private SaveFile saveFile;
    private String type;
    private String tag;
    private boolean isSaved = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //remove Actionbar
        setContentView(R.layout.editor);

        toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        toolBar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(!isSaved){
                    if(mEditor.getHtml() != null){
                        saveFile.saveContent(mEditor.getHtml());
                        isSaved = true;
                    }
                }
                finish();
            }
        });

        mEditor = (RichEditor) findViewById(R.id.editor);
//        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                isSaved = false;
            }
        });
        initTools();

        Intent intent = getIntent();
        String content = intent.getStringExtra(MainActivity.HTML);
        type = intent.getStringExtra("type");
        if(content != null){
            mEditor.setHtml(content);
            Note note = Note.fromString(intent.getStringExtra("note"));
            saveFile = new SaveFile(this, type, note.id, note.dir);
            tag = note.tag;
        }else{
            saveFile = new SaveFile(this, type);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List res = Matisse.obtainResult(data);
            for(int i = 0; i < res.size(); i++){
                mEditor.insertImage(Matisse.obtainResult(data).get(i).toString(), "\" style=\"max-width:80%");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if(mEditor.getHtml() != null){
                    Log.d("save", mEditor.getHtml());
                    saveFile.saveContent(mEditor.getHtml());
                    isSaved = true;
                    mEditor.clearFocusEditor();
                }
                break;
            case R.id.delete:
                saveFile.deleteFile();
                finish();
                break;
            case R.id.tag:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        new LovelyTextInputDialog(this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorAccent)
                .setTitle(R.string.tag_input_title)
                .setMessage(R.string.tag_input_message)
                .setIcon(R.drawable.ic_assignment_white_36dp)
                .setInputFilter(R.string.tag_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("^[\u4e00-\u9fa5A-Za-z]+$");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        if(saveFile.isNew && mEditor.getHtml() != null){
                            saveFile.saveContent(mEditor.getHtml());
                            tag = text;
                        }else if(!saveFile.isNew){
                            saveFile.saveTag(text);
                            tag = text;
                        }
                    }
                })
                .setHint(tag)
                .show();
        Log.d("tag", tag);
    }

    private void initTools(){
        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });


        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.WHITE : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });


        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(RichEditorActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(9)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .showPreview(false) // Default is `true`
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
    }
}
