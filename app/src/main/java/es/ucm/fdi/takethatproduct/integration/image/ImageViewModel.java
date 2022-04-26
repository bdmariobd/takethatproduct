package es.ucm.fdi.takethatproduct.integration.image;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.ucm.fdi.takethatproduct.integration.note.Note;
import es.ucm.fdi.takethatproduct.integration.product.NotesRepository;

public class ImageViewModel extends AndroidViewModel {

    private NotesRepository mRepository;

    public ImageViewModel(@NonNull @NotNull Application application) {
        super(application);
    }


}
