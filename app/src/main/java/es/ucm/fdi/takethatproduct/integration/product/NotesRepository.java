package es.ucm.fdi.takethatproduct.integration.product;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import es.ucm.fdi.takethatproduct.integration.databasemanagement.NoteRoomDatabase;
import es.ucm.fdi.takethatproduct.integration.note.DAONote;
import es.ucm.fdi.takethatproduct.integration.note.Note;

public class NotesRepository {

    private DAONote mNoteDao;
    private LiveData<List<Note>> mAllNotes;
    private LiveData<List<Note>> mNotesByDescTitle;
    private LiveData<List<Note>> mNotesByDescCreationDate;
    private LiveData<List<Note>> mNotesByAscCreationDate;
    private LiveData<List<Note>> mNotesByDescModificationDate;
    private LiveData<List<Note>> mNotesByAscModificationDate;

    public NotesRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.NoteDao();
        mAllNotes = mNoteDao.getAllNotes();
        mNotesByDescTitle = mNoteDao.getNotesOrderByDescTitle();
        mNotesByDescCreationDate = mNoteDao.getNotesOrderByDescCreationDate();
        mNotesByAscCreationDate = mNoteDao.getNotesOrderByAscCreationDate();
        mNotesByDescModificationDate = mNoteDao.getNotesOrderByDescModificationDate();
        mNotesByAscModificationDate = mNoteDao.getNotesOrderByAscModificationDate();
    }

    public LiveData<List<Note>> getAllNotes() {
        //Log.d("Notes", Integer.toString(mAllNotes.getValue().size()));
        return mAllNotes;
    }
    public LiveData<List<Note>> getNotesOrderByDescTitle() { return mNotesByDescTitle; }
    public LiveData<List<Note>> getNotesOrderByDescCreationDate() { return mNotesByDescCreationDate; }
    public LiveData<List<Note>> getNotesOrderByAscCreationDate() { return mNotesByAscCreationDate; }
    public LiveData<List<Note>> getNotesOrderByDescModificationDate() { return mNotesByDescModificationDate; }
    public LiveData<List<Note>> getNotesOrderByAscModificationDate() { return mNotesByAscModificationDate; }


    public void insert (Note Note) {
        new insertAsyncTask(mNoteDao).execute(Note);
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private DAONote mAsyncTaskDao;

        insertAsyncTask(DAONote dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete (Note note) {
        new DeleteAsyncTask(mNoteDao).execute(note);
    }

    private static class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

        private DAONote mAsyncTaskDao;

        DeleteAsyncTask(DAONote dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.deleteOneNote(params[0].getUuid());
            return null;
        }
    }
}
