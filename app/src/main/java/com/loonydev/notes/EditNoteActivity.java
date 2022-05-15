package com.loonydev.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.loonydev.notes.databinding.ActivityEditNoteBinding;
import com.loonydev.notes.utils.Utils;

import jp.wasabeef.richeditor.RichEditor;

public class EditNoteActivity extends AppCompatActivity {

    private RichEditor editor;
    private Action action;

    public enum Action {
        EDIT,
        CREATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityEditNoteBinding binding = ActivityEditNoteBinding.inflate(getLayoutInflater());
        Bundle extras = getIntent().getExtras();

        setContentView(binding.getRoot());

        editor = binding.editor;
        editor.setPlaceholder("Введите заметку...");
        editor.setEditorBackgroundColor(0x00000000);
        editor.setEditorFontColor(binding.title.getTextColors().getDefaultColor());

        switch (extras.getString("action")) {
            case "edit":
                action = Action.EDIT;

                binding.title.setText(extras.getString("title"));
                editor.setHtml(extras.getString("desc"));
                break;
            case "create": action = Action.CREATE; break;
        }

        binding.save.setOnClickListener(view -> {
            String title = String.valueOf(binding.title.getText());
            String desc = editor.getHtml() == null ? "" : editor.getHtml();

            if (action == Action.CREATE) {
                MainActivity.getInstance().addNote(title, desc);
                Utils.saveNote(Utils.NoteType.OTHER, title, desc);
            } else if (action == Action.EDIT) {
                Utils.NoteType type = Utils.NoteType.valueOf(extras.getString("type"));

                MainActivity.getInstance().updateNote(
                        Integer.parseInt(extras.getString("id")),
                        type, title, desc
                );
                Utils.saveNotes();
            }
        });

        binding.undo.setOnClickListener(view -> editor.undo());
        binding.redo.setOnClickListener(view -> editor.redo());

        binding.bold.setOnClickListener(view -> editor.setBold());
        binding.italic.setOnClickListener(view -> editor.setItalic());

        binding.textColor.setOnClickListener(view -> {
            // -- TODO - Open color picker and set text color
        });

        binding.bgColor.setOnClickListener(view -> {
            // -- TODO - Open color picker and set text background color
        });
    }
}