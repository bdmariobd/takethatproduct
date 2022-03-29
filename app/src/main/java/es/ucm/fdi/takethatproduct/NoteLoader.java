package es.ucm.fdi.takethatproduct;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class NoteLoader extends AsyncTaskLoader<List<String>> {


    public NoteLoader(Context context) {
        super(context);

    }

    @Override
    public List<String> loadInBackground() {
        Log.d(this.getClass().getSimpleName(), "Loading notes");
        try {
            List<String> dummyList = new ArrayList<String>();
            dummyList.add("test");
            return dummyList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
