package com.loonydev.notes.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.collection.ArrayMap;

import com.loonydev.notes.MainActivity;
import com.loonydev.notes.data.NoteData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public enum NoteType {
        PINNED,
        OTHER
    }

    private static String readNotes() {
        StringBuilder result = new StringBuilder();
        File notesFile = new File(MainActivity.getInstance().getFilesDir().getAbsolutePath(), "notes.json");

        if (!notesFile.exists()) {
            try {
                notesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileInputStream in = MainActivity.getInstance().openFileInput("notes.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;

            while((line = br.readLine()) != null)
                result.append(line).append("\n");

        } catch (Exception e) {
            Log.e("NOTES", e.getMessage());
        }

        return result.toString();
    }

    private static void saveNotes(String file) {
        try {
            FileOutputStream out = MainActivity.getInstance().openFileOutput("notes.json", Context.MODE_PRIVATE);

            out.write(file.getBytes());
            out.close();

            Toast.makeText(MainActivity.getInstance(), "File saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.getInstance(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static JSONArray createNoteJSONObject(List<NoteData> notes) throws JSONException {
        JSONArray notesArray = new JSONArray();

        for (int i = 0; i < notes.size(); i++) {
            JSONObject noteObject = new JSONObject();

            noteObject.put("title", notes.get(i).getTitle());
            noteObject.put("desc", notes.get(i).getDescription());

            notesArray.put(noteObject);
        }

        return notesArray;
    }

    public static List<NoteData> loadNotes(NoteType type) {
        List<NoteData> notes = new ArrayList<>();
        String result = readNotes();

        Log.e("notes", result);

        if (result.equals(""))
            return notes;

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray notesArray = jsonObject.getJSONArray(type.toString().toLowerCase());

            for (int i = 0; i < notesArray.length(); i++) {
                notes.add(new NoteData(
                        notesArray.getJSONObject(i).getString("title"),
                        notesArray.getJSONObject(i).getString("desc"),
                        type
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public static void saveNote(NoteType noteType, String title, String note) {
        String result = readNotes();

        Log.e("notes", result);

        try {
            JSONObject jsonObject;
            JSONObject noteObject = new JSONObject();

            JSONArray notesArray = null;

            if (result.equals("")) {
                jsonObject = new JSONObject();
                notesArray = new JSONArray();
            } else {
                jsonObject = new JSONObject(result);
                notesArray = jsonObject.getJSONArray(noteType.toString().toLowerCase());
            }

            noteObject.put("title", title);
            noteObject.put("desc", note);

            notesArray.put(noteObject);
            jsonObject.put(noteType.toString().toLowerCase(), notesArray);

            Log.e("notes", jsonObject.toString());

            saveNotes(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveNotes() {
        JSONObject jsonObject = new JSONObject();

        try {
            List<NoteData> pinnedNotesData = MainActivity.getInstance().pinnedNotes;
            List<NoteData> otherNotesData = MainActivity.getInstance().otherNotes;

            jsonObject.put("pinned", createNoteJSONObject(pinnedNotesData));
            jsonObject.put("other", createNoteJSONObject(otherNotesData));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        saveNotes(jsonObject.toString());
    }

}
