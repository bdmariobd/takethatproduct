package es.ucm.fdi.takethatproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import es.ucm.fdi.takethatproduct.integration.note.Note;

public class NoteTotalViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_total_view);

        Note note = (Note) getIntent().getSerializableExtra("note");
        EditText titleInput = findViewById(R.id.noteTotalViewTitle);
        EditText bodyInput = findViewById(R.id.noteTotalViewBody);
        View searchProductsContainer = findViewById(R.id.searchProductsFragmentContainer);
        titleInput.setText(note.getTitulo(), TextView.BufferType.EDITABLE);
        bodyInput.setText(note.getCuerpo(), TextView.BufferType.EDITABLE);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }



        findViewById(R.id.noteTotalViewBack).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                note.setTitulo(titleInput.getText().toString());
                note.setCuerpo(bodyInput.getText().toString());
                Intent i = new Intent();
                i.putExtra("noteResult", note);
                setResult(0, i);
                finish();
            }
        });

        findViewById(R.id.noteTotalViewSearchAPIButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                searchAmazonProductsFragment fragment = new searchAmazonProductsFragment();
                fragmentTransaction.add(R.id.searchProductsFragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        });
    }
}