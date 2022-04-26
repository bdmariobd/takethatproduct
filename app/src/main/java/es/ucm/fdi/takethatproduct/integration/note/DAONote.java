package es.ucm.fdi.takethatproduct.integration.note;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAONote {
    @Insert
    public void insert(Note note);

    @Update
    public void updateNotes(Note... notes);

    @Query("DELETE FROM notes")
    public void deleteAll();

    @Query("DELETE FROM notes where uuid = :uuid")
    public void deleteOneNote(int uuid);

    @Query("SELECT * FROM notes ORDER BY titulo ASC, fechaModificacion DESC;")
    public LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes ORDER BY titulo DESC, fechaModificacion DESC;")
    public LiveData<List<Note>> getNotesOrderByDescTitle();

    @Query("SELECT * FROM notes ORDER BY fechaCreacion DESC;")
    public LiveData<List<Note>> getNotesOrderByDescCreationDate();

    @Query("SELECT * FROM notes ORDER BY fechaCreacion ASC;")
    public LiveData<List<Note>> getNotesOrderByAscCreationDate();

    @Query("SELECT * FROM notes ORDER BY fechaModificacion DESC;")
    public LiveData<List<Note>> getNotesOrderByDescModificationDate();

    @Query("SELECT * FROM notes ORDER BY fechaModificacion ASC;")
    public LiveData<List<Note>> getNotesOrderByAscModificationDate();

    @Query("SELECT * FROM notes WHERE uuid LIKE :uuid ")
    public List<Note> findNotebyUuid(int uuid);

    @Query("SELECT * FROM notes WHERE titulo LIKE :word ")
    public List<Note> findNotebyTitle(String word);

}
