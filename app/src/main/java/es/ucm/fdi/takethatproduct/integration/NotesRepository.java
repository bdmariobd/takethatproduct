package es.ucm.fdi.takethatproduct.integration;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotesRepository {

    private DAONote mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    NotesRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.NoteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes() {
        //Log.d("Notes", Integer.toString(mAllNotes.getValue().size()));
        return mAllNotes;
    }

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


    public void delete () {
        new DeleteAsyncTask(mNoteDao).execute();
    }

    private static class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {

        private DAONote mAsyncTaskDao;

        DeleteAsyncTask(DAONote dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
