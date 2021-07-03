package com.nkcs.lifesnapshot;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import com.nkcs.lifesnapshot.menu.DrawerAdapter;
import com.nkcs.lifesnapshot.menu.DrawerItem;
import com.nkcs.lifesnapshot.menu.SimpleItem;
import com.nkcs.lifesnapshot.menu.SpaceItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{
    private static final int POS_DIARY = 0;
    private static final int POS_PLAN = 1;
    private static final int POS_MESSAGES = 2;
    private static List<String> types = new ArrayList<String>(Arrays.asList("日记", "计划", "备忘"));

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    private Toolbar toolBar;
    private EditText searchText;

    public static final String HTML = "content";
    public static final String DIR = "dir";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //remove Actionbar
        setContentView(R.layout.activity_main);

        requestPermison();

        toolBar = findViewById(R.id.toolbar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingRootNav.openMenu();
            }
        });

        searchText = findViewById(R.id.search);
        searchText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    ShowTextFragment f = (ShowTextFragment)getSupportFragmentManager().getFragments().get(0);
                    if(searchText.length() == 0){
                        f.searchTag("");
                    }else{
                        f.searchTag(searchText.getText().toString());
                    }
                    searchText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
                return false;
            }
        });

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withMenuLayout(R.layout.left_drawer)
                .inject();

        findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DIARY).setChecked(true),
                createItemFor(POS_PLAN),
                createItemFor(POS_MESSAGES)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_DIARY);

    }

    @Override
    protected void onRestart(){
        super.onRestart();
        ShowTextFragment f = (ShowTextFragment)getSupportFragmentManager().getFragments().get(0);
        f.update();
    }

    @Override
    public void onItemSelected(int position) {
        slidingRootNav.closeMenu();
        Fragment selectedScreen = ShowTextFragment.createFragment(types.get(position));
        showFragment(selectedScreen);
        toolBar.setTitle(types.get(position));
    }


    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    private void requestPermison(){
        if(ContextCompat.checkSelfPermission(MainActivity.this,"Manifest.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,"Manifest.permission.WRITE_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,"Manifest.permission.CAMERA")
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,"Manifest.permission.INTERNET")
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    1);
        }
    }



    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}