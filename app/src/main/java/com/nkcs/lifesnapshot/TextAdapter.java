package com.nkcs.lifesnapshot;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nkcs.lifesnapshot.SQLite.Note;
import com.nkcs.lifesnapshot.SQLite.NoteCollection;

import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {
    private List<Note> note_list;
    private Context context;
    private View inflater;

    public TextAdapter(Context _context, List<Note> list){
        note_list = list;
        context = _context;
    }

    @NonNull
    @Override
    public TextAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.item_text, parent,false);
        ViewHolder viewHolder = new ViewHolder(inflater);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TextAdapter.ViewHolder holder, int position) {
        String content = note_list.get(position).getContent();
        String dir = note_list.get(position).dir;
        int id = note_list.get(position).id;

        Spanned charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(content,Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(content);
        }
        holder.textView.setText(charSequence);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RichEditorActivity.class);
                intent.putExtra(MainActivity.HTML, content);
                intent.putExtra("note", note_list.get(position).toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return note_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.showText);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
