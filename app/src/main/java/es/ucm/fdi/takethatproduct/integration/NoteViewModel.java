package es.ucm.fdi.takethatproduct.integration;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NotesRepository mRepository;

    private LiveData<List<Note>> mAllNotes;

    public NoteViewModel (Application application) {
        super(application);
        mRepository = new NotesRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() { return mAllNotes; }

    public void insert(Note Note) { mRepository.insert(Note); }
}
