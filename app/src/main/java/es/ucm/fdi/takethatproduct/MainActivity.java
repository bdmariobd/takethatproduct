package es.ucm.fdi.takethatproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotePreviewListAdapter notePreviewListAdapter;
    RecyclerView notePreviewListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notePreviewListView = findViewById(R.id.notePreviewList);
        notePreviewListAdapter = new NotePreviewListAdapter(this);

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        notePreviewListView.setLayoutManager(new LinearLayoutManager(this));
        notePreviewListView.setAdapter(notePreviewListAdapter);
        if(loaderManager.getLoader(0)!=null){
            loaderManager.initLoader(0,null,null);
        }

        List<String> dummyList = new ArrayList<String>();
        dummyList.add("test");
        dummyList.add("test");
        notePreviewListAdapter.setmNoteList((ArrayList<String>) dummyList);
        notePreviewListAdapter.notifyDataSetChanged();

    }
}