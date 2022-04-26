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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.ucm.fdi.takethatproduct.integration.image.Image;
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

        String cuerpo = note.getCuerpo();
        noteText.setText(note.getCuerpo(), TextView.BufferType.EDITABLE);

        Pattern pattern = Pattern.compile("[\\{].*[\\}]");
        Matcher matcher = pattern.matcher(cuerpo);
        // Check all occurrences
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
            matcher.replaceFirst("");
            try {
                JSONObject jsonimage = new JSONObject(matcher.group());
                replaceByImage(matcher.start(), matcher.end(),Uri.parse(jsonimage.getString("uri")));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

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

    public void replaceByImage(int start, int end, Uri imageUrl) throws JSONException, IOException {
        String JsonImage = Image.imageToJson(imageUrl.toString());
        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrl);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String text = noteText.getText().toString();
        builder.append(text.substring(0,noteText.getSelectionStart()));
        builder.append(JsonImage);
        builder.setSpan(new ImageSpan(this, bitmap), noteText.getSelectionStart(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(text.substring(noteText.getSelectionEnd(), text.length()));
        noteText.setText(builder);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==10 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                replaceByImage(noteText.getSelectionStart(), noteText.getSelectionEnd(), selectedImage);
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