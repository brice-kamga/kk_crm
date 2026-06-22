package it.kk_crm.model.dao;

import it.kk_crm.model.mo.Note;

import java.util.List;

public interface NoteDAO {

    public void create(Note nota);

    public void update(Note nota);

    public void delete(Integer id);

    public List<Note> loadNoteAll();

    public List<Note> loadNoteFilter(String nome, String cognome);

    public Note getNota(String id);

    public List<Note> loadNoteNotOwner(String owner);

    public List<Note> loadNoteOwner(String owner);

    public void deleteC(String cf);
}
