package com.nkcs.lifesnapshot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nkcs.lifesnapshot.SQLite.Note;

import java.util.LinkedList;
import java.util.List;

public class ShowTextFragment extends Fragment {
    private Activity activity;
    private List<Note> noteList;
    private RecyclerView recyclerView;
    private TextAdapter textAdapter;
    private String type;

    public static ShowTextFragment createFragment(String _type){
        ShowTextFragment fragment = new ShowTextFragment();
        Bundle args = new Bundle();
        args.putString("type", _type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = this.getActivity();
        return inflater.inflate(R.layout.show_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String _type = args.getString("type");
        type = _type;
        recyclerView = view.findViewById(R.id.textList);
        LoadFile loadFile = new LoadFile(activity);
        noteList = loadFile.getNoteFromType(type);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        textAdapter = new TextAdapter(activity, noteList);
        recyclerView.setAdapter(textAdapter);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RichEditorActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    public void update(){
        LoadFile loadFile = new LoadFile(activity);
        noteList = loadFile.getNoteFromType(type);
        textAdapter = new TextAdapter(activity, noteList);
        recyclerView.setAdapter(textAdapter);
    }

    public void searchTag(String tag){
        if(tag == ""){
            textAdapter = new TextAdapter(activity, noteList);
        }else{
            List<Note> list = new LinkedList<Note>();
            for(int i = 0; i < noteList.size(); i++){
                if(noteList.get(i).tag.equals(tag)){
                    list.add(noteList.get(i));
                }
            }
            textAdapter = new TextAdapter(activity, list);
        }
        recyclerView.setAdapter(textAdapter);
    }
}
