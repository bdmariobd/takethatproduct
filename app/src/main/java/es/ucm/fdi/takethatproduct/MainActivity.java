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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.takethatproduct.integration.Note;
import es.ucm.fdi.takethatproduct.integration.NoteViewModel;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private NotePreviewListAdapter notePreviewListAdapter;
    RecyclerView notePreviewListView;
    private NoteViewModel mNoteViewModel;
    NotePreviewListAdapter adapter;
    private TextView mainViewInfoText;

    String[] orderNotesOptions = {"Alfabéticamente", "Por fecha de creación", "Recientes"};
    Spinner spin;

    String optionSelected = "Alfabéticamente";

    boolean boton_pulsado = true;
    ImageButton boton_orden_notas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);


        mainViewInfoText = findViewById(R.id.mainViewInfoText);
        notePreviewListView = findViewById(R.id.notePreviewList);
        adapter = new NotePreviewListAdapter(this);
        notePreviewListView.setAdapter(adapter);
        notePreviewListView.setLayoutManager(new LinearLayoutManager(this));
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the adapter.
                adapter.setmNoteList(notes);
                mainViewInfoText.setText("Tienes " + notes.size() + " notas");
            }
        });
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


        findViewById(R.id.addNoteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note n = new Note("Titulo de prueba", "Cuerpo de prueba");
                mNoteViewModel.insert(n);
            }
        });

        findViewById(R.id.deleteLastNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mNoteViewModel.delete();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void pulsado(View view) {

        // Para cambiar el tamaño de ImageButton (el padding)
        int sizeInDp = 10;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);

        if (optionSelected == "Alfabéticamente")
        {
            if (boton_pulsado) {
                boton_pulsado = false;

                boton_orden_notas.setImageResource(R.drawable.z_a);
                boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

                // ordenar las notas en orden inverso
                // ...
            }
            else {
                boton_pulsado = true;

                boton_orden_notas.setImageResource(R.drawable.a_z);
                sizeInDp = 0;
                dpAsPixels = (int) (sizeInDp*scale + 0.5f);
                boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

                // ordenar las notas en el orden normal
                // ...
            }
        }
        else if (optionSelected == "Por fecha de creación")
        {
            if (boton_pulsado) {
                boton_pulsado = false;

                boton_orden_notas.setImageResource(R.drawable.flecha_hacia_arriba);
                boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

                // ordenar las notas en orden inverso
                // ...
            }
            else {
                boton_pulsado = true;

                boton_orden_notas.setImageResource(R.drawable.flecha_hacia_abajo);
                boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

                // ordenar las notas en el orden normal
                // ...
            }
        }
        else // "Recientes"
        {
            if (boton_pulsado) {
                boton_pulsado = false;

                boton_orden_notas.setImageResource(R.drawable.flecha_hacia_arriba);
                boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

                // ordenar las notas en orden inverso
                // ...
            }
            else {
                boton_pulsado = true;

                boton_orden_notas.setImageResource(R.drawable.flecha_hacia_abajo);
                boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

                // ordenar las notas en el orden normal
                // ...
            }
        }
    }

    //spin.setOnItemSelectedListener(new OnItemSelectedListener() {
    //});

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), orderNotesOptions[position], Toast.LENGTH_LONG).show();

        // Para cambiar el tamaño de ImageButton (el padding)
        int sizeInDp = 10;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);

        int Hold = spin.getSelectedItemPosition() + 1 ;
        Log.d("PRUEBA", "MENSAJE ITEM POS: " + Hold);

        if (spin.getSelectedItem() == "Alfabéticamente")
        {
            // Log.d("PRUEBA", "MENSAJE NOMBRE ITEM: " + spin.getSelectedItem());
            optionSelected = "Alfabéticamente";

            boton_orden_notas.setImageResource(R.drawable.a_z);
            sizeInDp = 0;
            dpAsPixels = (int) (sizeInDp*scale + 0.5f);
            boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

            // ordenar las notas en el orden normal
            // ...
        }
        else if (spin.getSelectedItem() == "Por fecha de creación")
        {
            optionSelected = "Por fecha de creación";

            boton_orden_notas.setImageResource(R.drawable.flecha_hacia_abajo);
            boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

            // ordenar las notas en el orden normal
            // ...
        }
        else
        {
            optionSelected = "Recientes";

            boton_orden_notas.setImageResource(R.drawable.flecha_hacia_abajo);
            boton_orden_notas.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels);

            // ordenar las notas en el orden normal
            // ...
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}