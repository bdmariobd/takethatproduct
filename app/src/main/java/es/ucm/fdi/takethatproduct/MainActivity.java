package es.ucm.fdi.takethatproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotePreviewListAdapter notePreviewListAdapter;
    RecyclerView notePreviewListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    boolean boton_pulsado = true;
    ImageButton boton_orden_notas;

    public void pulsado(View view)
    {
        boton_orden_notas = (ImageButton)findViewById(R.id.mainViewNotesOrderButton);

        if (boton_pulsado)
        {
            boton_pulsado = false;
            //Drawable d = getResources().getDrawable(R.drawable.z_a);
            //boton_orden_notas.setBackground(d);
            boton_orden_notas.setImageResource(android.R.drawable.arrow_up_float);
            // R.drawable.z_a

            // ordenar las notas en orden inverso
            // ...
        }
        else
        {
            boton_pulsado = true;
            boton_orden_notas.setImageResource(android.R.drawable.ic_menu_sort_alphabetically);
            // R.drawable.a_z

            // ordenar las notas en el orden normal
            // ...
        }
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