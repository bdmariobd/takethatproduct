package es.ucm.fdi.takethatproduct;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.takethatproduct.integration.Note;
import es.ucm.fdi.takethatproduct.integration.NoteViewModel;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private NotePreviewListAdapter notePreviewListAdapter;
    RecyclerView notePreviewListView;
    private NoteViewModel mNoteViewModel;

    String[] orderNotesOptions = {"Alfabéticamente", "Por fecha de creación", "Recientes"};
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the adapter.
                notePreviewListAdapter.setmNoteList(notes);
            }
        });

        notePreviewListView = findViewById(R.id.notePreviewList);



        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        //Spinner
        spin = (Spinner) findViewById(R.id.mainViewOrderNotesOptionsSpinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, orderNotesOptions);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        boton_orden_notas = (ImageButton) findViewById(R.id.mainViewNotesOrderButton);

    }

    boolean boton_pulsado = true;
    ImageButton boton_orden_notas;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void pulsado(View view) {
        // boton_orden_notas = (ImageButton) findViewById(R.id.mainViewNotesOrderButton);

        if (boton_pulsado) {
            boton_pulsado = false;

            int g = boton_orden_notas.getLayoutDirection();

            boton_orden_notas.setImageResource(android.R.drawable.arrow_up_float);
            // R.drawable.z_a

            // ordenar las notas en orden inverso
            // ...
        } else {
            boton_pulsado = true;
            boton_orden_notas.setImageResource(android.R.drawable.ic_menu_sort_alphabetically);
            //boton_orden_notas.setPadding(0, 0, 0, 0);
            //boton_orden_notas.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //boton_orden_notas.setAdjustViewBounds(true);

            //R.drawable.a_z


            // ordenar las notas en el orden normal
            // ...
        }
    }

    //spin.setOnItemSelectedListener(new OnItemSelectedListener() {
    //});

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), orderNotesOptions[position], Toast.LENGTH_LONG).show();


        int Hold = spin.getSelectedItemPosition() + 1 ;
        Log.d("PRUEBA", "MENSAJE ITEM POS: " + Hold);

        if (spin.getSelectedItem() == "Recientes")
        {
            //Log.d("PRUEBA", "MENSAJE NOMBRE ITEM: " + spin.getSelectedItem());
            boton_orden_notas.setImageResource(android.R.drawable.arrow_down_float);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}