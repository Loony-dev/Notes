package com.loonydev.notes.data;

import com.loonydev.notes.utils.Utils;

public class NoteData {

    private String title;
    private String description;

    private Utils.NoteType type;

    public NoteData(String title, String description, Utils.NoteType type) {
        this.title = title;
        this.description = description;

        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Utils.NoteType getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(Utils.NoteType type) {
        this.type = type;
    }
}
