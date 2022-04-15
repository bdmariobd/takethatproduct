package es.ucm.fdi.takethatproduct.integration;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAONote {
    @Insert
    void insert(Note note);
    @Update
    public void updateWords(Note... notes);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("DELETE FROM notes where uuid = :uuid")
    void deleteOneNote(int uuid);

    @Query("SELECT * from notes")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE titulo LIKE :uuid ")
    public List<Note> findNotebyUuid(int uuid);

    @Query("SELECT * FROM notes WHERE titulo LIKE :word ")
    public List<Note> findNotebyTitle(String word);

}
