package es.ucm.fdi.takethatproduct;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;

public class NotePreviewLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<String>> {

    MainActivity context;
    public NotePreviewLoaderCallbacks(MainActivity context) {
        this.context=context;
    }
    @NonNull
    @Override
    public Loader<List<String>> onCreateLoader(int id, @Nullable  Bundle args) {
        return new NoteLoader(context);
    }

    @Override
    public void onLoadFinished(@NonNull  Loader<List<String>> loader, List<String> data) {

    }

    @Override
    public void onLoaderReset(@NonNull  Loader<List<String>> loader) {

    }
}
