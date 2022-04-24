package es.ucm.fdi.takethatproduct.integration.note;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.ucm.fdi.takethatproduct.integration.product.NotesRepository;

public class NoteViewModel extends AndroidViewModel {

    private NotesRepository mRepository;

    private LiveData<List<Note>> mAllNotes;
    private LiveData<List<Note>> mNotesByDescTitle;
    private LiveData<List<Note>> mNotesByDescCreationDate;
    private LiveData<List<Note>> mNotesByAscCreationDate;
    private LiveData<List<Note>> mNotesByDescModificationDate;
    private LiveData<List<Note>> mNotesByAscModificationDate;

    public NoteViewModel (Application application) {
        super(application);
        mRepository = new NotesRepository(application);
        mAllNotes = mRepository.getAllNotes();
        mNotesByDescTitle = mRepository.getNotesOrderByDescTitle();
        mNotesByDescCreationDate = mRepository.getNotesOrderByDescCreationDate();
        mNotesByAscCreationDate = mRepository.getNotesOrderByAscCreationDate();
        mNotesByDescModificationDate = mRepository.getNotesOrderByDescModificationDate();
        mNotesByAscModificationDate = mRepository.getNotesOrderByAscModificationDate();
    }

    public LiveData<List<Note>> getAllNotes() { return mAllNotes; }
    public LiveData<List<Note>> getNotesOrderByDescTitle() { return mNotesByDescTitle; }
    public LiveData<List<Note>> getNotesOrderByDescCreationDate() { return mNotesByDescCreationDate; }
    public LiveData<List<Note>> getNotesOrderByAscCreationDate() { return mNotesByAscCreationDate; }
    public LiveData<List<Note>> getNotesOrderByDescModificationDate() { return mNotesByDescModificationDate; }
    public LiveData<List<Note>> getNotesOrderByAscModificationDate() { return mNotesByAscModificationDate; }

    public void insert(Note Note) { mRepository.insert(Note); }

    public void delete(Note note) { mRepository.delete(note);}
}
