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
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

import es.ucm.fdi.takethatproduct.integration.note.Note;
import es.ucm.fdi.takethatproduct.integration.product.ProductListAdapter;

public class NoteTotalViewActivity extends AppCompatActivity {

    EditText noteText;
    Bitmap bitmap = null;
    String titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_total_view);

        Note note = (Note) getIntent().getSerializableExtra("note");
        EditText titleInput = findViewById(R.id.noteTotalViewTitle);
        noteText = findViewById(R.id.noteTotalViewBody);
        View searchProductsContainer = findViewById(R.id.searchProductsFragmentContainer);
        //if (note != null) {
        titleInput.setText(note.getTitulo(), TextView.BufferType.EDITABLE);
        //titleInput.setText((note.getTitulo() == null ? " " : note.getTitulo()), TextView.BufferType.EDITABLE);
        noteText.setText(note.getCuerpo(), TextView.BufferType.EDITABLE);
        //}
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }


        //titulo = getIntent().getExtras().getString("Titulo");
        // noteText.setText(noteText.length(), TextView.BufferType.valueOf(titulo));

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

                noteText.setText(noteText.length(), TextView.BufferType.valueOf(titulo));

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


        if (requestCode == 1) { // el "1" es el numero que pasaste como parametro
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("titulo");
                // tu codigo para continuar procesando
                noteText.setText(result); // CAMBIAR
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // código si no hay resultado
                Log.d("1", "ERROR");
            }
        }

    }

    public void segunda_pantalla(View view){
        Intent i=new Intent(this, ProductListAdapter.class);
        startActivityForResult(i, 1);
    }

    /*public void addProduct(String productTitle, ImageView productImage)
    {
        // SpannableString string = new SpannableString("Bottom: span.\nBaseline: span.");
        // string.setSpan(new ImageSpan(this, R.mipmap.ic_launcher), 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // string.setSpan(new ImageSpan(this, R.mipmap.ic_launcher, DynamicDrawableSpan.ALIGN_BASELINE),
        //        22, 23, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(productTitle);
        //noteText.setText(builder);
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 10);

        return builder;
    }*/

}