package es.ucm.fdi.takethatproduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.ucm.fdi.takethatproduct.integration.image.Image;
import es.ucm.fdi.takethatproduct.integration.note.Note;
import es.ucm.fdi.takethatproduct.integration.product.Product;
import es.ucm.fdi.takethatproduct.integration.product.ProductListAdapter;

public class NoteTotalViewActivity extends AppCompatActivity implements searchAmazonProductsFragment.onSomeEventListener {

    EditText noteText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_total_view);

        Note note = (Note) getIntent().getSerializableExtra("note");
        EditText titleInput = findViewById(R.id.noteTotalViewTitle);
        noteText = findViewById(R.id.noteTotalViewBody);
        View searchProductsContainer = findViewById(R.id.searchProductsFragmentContainer);
        titleInput.setText(note.getTitulo(), TextView.BufferType.EDITABLE);

        String cuerpo = note.getCuerpo();
        noteText.setText("",TextView.BufferType.EDITABLE);

        Pattern pattern = Pattern.compile("[\\{].*[\\}]");
        Matcher matcher = pattern.matcher(cuerpo);
        // Check all occurrences
        int initial = 0;
        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
            try {
                JSONObject jsonimage = new JSONObject(matcher.group());
                noteText.append(note.getCuerpo().substring(initial, matcher.start()));
                initial = matcher.end();
                replaceByImage(matcher.start(), matcher.end(),jsonimage.getString("uri"));
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        noteText.append((note.getCuerpo().substring(initial)));

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
                if (fragmentManager.getFragments() ==null || fragmentManager.getFragments().size()==0 || fragmentManager.getFragments().size() ==1){
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
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(NoteTotalViewActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 20);
                }
                else{
                    startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 10);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 20 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 10);
            }
            else {
                Toast.makeText(getApplicationContext(), "No tenemos permiso :(", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void replaceByImage(int start, int end, String path) throws JSONException, IOException {
        String JsonImage = Image.imageToJson(path);
        Bitmap bitmaps = BitmapFactory.decodeFile(path);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        //builder.append(text.substring(0,start));
        Bitmap bitmap = Bitmap.createScaledBitmap(bitmaps, 500, 250, false);
        Drawable dr = new BitmapDrawable(getResources(), bitmap);
        dr.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        ImageSpan imgSpan = new ImageSpan(dr, DynamicDrawableSpan.ALIGN_BOTTOM);new ImageSpan(this, bitmap);
        builder.append(JsonImage);
        builder.setSpan(imgSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        noteText.append(builder);
    }
    public void replaceByProduct(int start, int end, Product p) throws JSONException, IOException {
        /*String JsonImage = Image.imageToJson(path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        //builder.append(text.substring(0,start));
        builder.append(JsonImage);
        builder.setSpan(new ImageSpan(this, bitmap), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        noteText.append(builder);
        */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==10 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();

            try {
                replaceByImage(noteText.getSelectionStart(), noteText.getSelectionEnd(), Image.getImagePathFromUri(selectedImage,getContentResolver()));

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

    @Override
    public void someEvent(Product p) {
        Log.d("product" , p.getTitulo());
        replaceByProduct(p);

    }
}