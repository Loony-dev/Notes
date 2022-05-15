package com.loonydev.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.loonydev.notes.adapters.NoteAdapter;
import com.loonydev.notes.data.NoteData;
import com.loonydev.notes.databinding.ActivityMainBinding;
import com.loonydev.notes.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<NoteData> pinnedNotes = new ArrayList<>();
    public List<NoteData> otherNotes = new ArrayList<>();

    private NoteAdapter pinnedNotesAdapter;
    private NoteAdapter otherNotesAdapter;

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        loadPinnedNotes();
        loadOtherNotes();

        pinnedNotesAdapter = new NoteAdapter(pinnedNotes);
        otherNotesAdapter = new NoteAdapter(otherNotes);

        binding.pinnedRecycler.setAdapter(pinnedNotesAdapter);
        binding.allRecycler.setAdapter(otherNotesAdapter);

        binding.createNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditNoteActivity.class);
            intent.putExtra("action", "create");
            intent.putExtra("id", String.valueOf(otherNotes.size()));

            startActivity(intent);
        });
    }

    private void loadPinnedNotes() {
        pinnedNotes = Utils.loadNotes(Utils.NoteType.PINNED);
    }

    private void loadOtherNotes() {
        otherNotes = Utils.loadNotes(Utils.NoteType.OTHER);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addNote(String title, String desc) {
        otherNotes.add(new NoteData(title, desc, Utils.NoteType.OTHER));
        otherNotesAdapter.notifyDataSetChanged();
    }

    public void updateNote(int id, Utils.NoteType noteType, String title, String desc) {
        NoteData newNote = new NoteData(title, desc, noteType);

        switch (noteType) {
            case PINNED:
                pinnedNotes.remove(id);
                pinnedNotes.add(id, newNote);

                pinnedNotesAdapter.notifyItemChanged(id);
                break;

            case OTHER:
                otherNotes.remove(id);
                otherNotes.add(id, newNote);

                otherNotesAdapter.notifyItemChanged(id);
                break;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void pinNote(int noteId) {
        NoteData newData = otherNotes.get(noteId);
        newData.setType(Utils.NoteType.PINNED);

        pinnedNotes.add(newData);
        otherNotes.remove(noteId);

        pinnedNotesAdapter.notifyDataSetChanged();
        otherNotesAdapter.notifyDataSetChanged();

        Utils.saveNotes();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void unpinNote(int noteId) {
        NoteData newData = pinnedNotes.get(noteId);
        newData.setType(Utils.NoteType.OTHER);

        otherNotes.add(newData);
        pinnedNotes.remove(noteId);

        pinnedNotesAdapter.notifyDataSetChanged();
        otherNotesAdapter.notifyDataSetChanged();

        Utils.saveNotes();
    }

    public static MainActivity getInstance() {
        return instance;
    }
}