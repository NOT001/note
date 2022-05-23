package com.example.note.entity;

public class NoteImage {
    private int id, idNote;
    private byte[] image;

    public NoteImage() {
    }

    public NoteImage(int id, int idNote, byte[] image) {
        this.id = id;
        this.idNote = idNote;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
