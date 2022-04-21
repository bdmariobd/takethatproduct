package es.ucm.fdi.takethatproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

import es.ucm.fdi.takethatproduct.integration.note.Note;

public class NoteTotalViewActivity extends AppCompatActivity {

    EditText noteText;
    Bitmap bitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_total_view);

        Note note = (Note) getIntent().getSerializableExtra("note");
        EditText titleInput = findViewById(R.id.noteTotalViewTitle);
        noteText = findViewById(R.id.noteTotalViewBody);
        View searchProductsContainer = findViewById(R.id.searchProductsFragmentContainer);
        titleInput.setText(note.getTitulo(), TextView.BufferType.EDITABLE);
        noteText.setText(note.getCuerpo(), TextView.BufferType.EDITABLE);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }



        findViewById(R.id.noteTotalViewBack).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                note.setTitulo(titleInput.getText().toString());
                note.setCuerpo(noteText.getText().toString());
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
                if (fragmentManager.getFragments() ==null || fragmentManager.getFragments().size()==0){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    searchAmazonProductsFragment fragment = new searchAmazonProductsFragment();
                    fragmentTransaction.add(R.id.searchProductsFragmentContainer, fragment);
                    fragmentTransaction.commit();
                }

            }
        });

        findViewById(R.id.noteTotalViewAddElementsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 10);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==10 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(noteText.getText().toString()==null?" ":noteText.getText().toString());
                builder.setSpan(new ImageSpan(this, bitmap), builder.length() - 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                noteText.setText(builder);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}