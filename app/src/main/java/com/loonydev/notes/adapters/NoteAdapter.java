package com.loonydev.notes.adapters;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.loonydev.notes.EditNoteActivity;
import com.loonydev.notes.MainActivity;
import com.loonydev.notes.R;
import com.loonydev.notes.data.NoteData;
import com.loonydev.notes.utils.Utils;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter {

    private final List<NoteData> notes;

    public NoteAdapter(List<NoteData> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NoteHolder) holder).title.setText(notes.get(position).getTitle());
        ((NoteHolder) holder).desc.setText(Html.fromHtml(notes.get(position).getDescription(), Html.FROM_HTML_MODE_LEGACY));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.getInstance(), EditNoteActivity.class);
            intent.putExtra("id", String.valueOf(position));
            intent.putExtra("action", "edit");
            intent.putExtra("type", notes.get(position).getType().toString());
            intent.putExtra("title", notes.get(position).getTitle());
            intent.putExtra("desc", notes.get(position).getDescription());

            MainActivity.getInstance().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (notes.get(position).getType() == Utils.NoteType.OTHER)
                MainActivity.getInstance().pinNote(position);
            else MainActivity.getInstance().unpinNote(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static final class NoteHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView title;
        public AppCompatTextView desc;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
    }
}
