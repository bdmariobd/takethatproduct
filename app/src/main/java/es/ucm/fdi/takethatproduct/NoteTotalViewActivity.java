package es.ucm.fdi.takethatproduct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

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
    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_total_view);

        note = (Note) getIntent().getSerializableExtra("note");
        EditText titleInput = findViewById(R.id.noteTotalViewTitle);
        noteText = findViewById(R.id.noteTotalViewBody);
        //noteText.setMovementMethod(LinkMovementMethod.getInstance()); // enable clicking on url span
        View searchProductsContainer = findViewById(R.id.searchProductsFragmentContainer);
        titleInput.setText(note.getTitulo(), TextView.BufferType.EDITABLE);

        String cuerpo = note.getCuerpo();
        reloadJsons(cuerpo);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        findViewById(R.id.noteTotalViewBack).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Note nota= new Note(titleInput.getText().toString(),noteText.getText().toString() );
                Intent i = new Intent();
                i.putExtra("noteResult", nota);
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
        ImageSpan imgSpan = new ImageSpan(dr, DynamicDrawableSpan.ALIGN_BOTTOM);
        builder.append(JsonImage);
        builder.setSpan(new ImageSpan(this, bitmap), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(imgSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        noteText.append(builder);
    }





    public void replaceByProduct(int start, int end, Product p) throws JSONException, IOException {

        String JsonImage = p.getJsonObject();
        Context that = this;
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(p.getUrlImagen())
                .into(new CustomTarget<Bitmap>(250,250) {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                        SpannableStringBuilder builder = new SpannableStringBuilder();
                        //builder.append(text.substring(0,start));
                        builder.append(JsonImage);
                        int i = builder.length();
                        builder.setSpan(new ImageSpan(that, resource), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(p.getTitulo());
                        ClickableSpan sp = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                                browserIntent.setData(Uri.parse(p.getUrl()));
                                that.startActivity(browserIntent);
                            }
                        };
                        builder.setSpan(sp , i,i + p.getTitulo().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(" - "+p.getPrecio() + '$');
                        noteText.append(builder);
                    }

                    @Override
                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

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

                String cuerpo = this.noteText.getText().toString();
                String imageJson = Image.imageToJson(Image.getImagePathFromUri(selectedImage,getContentResolver()));
                //this.noteText.setText(cuerpo.substring(0,noteText.getSelectionStart()) + imageJson + cuerpo.substring(noteText.getSelectionEnd()));
                reloadJsons(cuerpo.substring(0,noteText.getSelectionStart()) + imageJson + cuerpo.substring(noteText.getSelectionEnd()));

            } catch (Exception e){
                e.printStackTrace();
            }

        }

    }


    void reloadJsons(String cuerpo){

        noteText.setText("",TextView.BufferType.EDITABLE);

        Pattern pattern = Pattern.compile("[\\{].*[\\}]");
        Matcher matcher = pattern.matcher(cuerpo);
        // Check all occurrences
        int initial = 0;
        Boolean lastfoundproduct = false;

        while (matcher.find()) {
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end());
            System.out.println(" Found: " + matcher.group());
            try {
                JSONObject jsonimage = new JSONObject(matcher.group());
                if (jsonimage.getString("type").matches("image")){
                    lastfoundproduct = false;
                    noteText.append(cuerpo.substring(initial, matcher.start()));
                    initial = matcher.end();
                    replaceByImage(matcher.start(), matcher.end(),jsonimage.getString("uri"));
                }
                else if (jsonimage.getString("type").matches("product")){
                    if(!lastfoundproduct) noteText.append(cuerpo.substring(initial, matcher.start()));
                    initial = matcher.end();
                    JSONObject json = new JSONObject(matcher.group());
                    Product p = new Product(json.getString("title"), json.getString("url"), json.getString("urlImage"), "", json.getDouble("price"));
                    replaceByProduct(matcher.start(), matcher.end(),p);
                    lastfoundproduct = true;
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        if(!lastfoundproduct) noteText.append((cuerpo.substring(initial)));


    }


    @Override
    public void someEvent(Product p) throws IOException, JSONException {
        Log.d("product" , p.getTitulo());
        String cuerpo = this.noteText.getText().toString();
        //this.noteText.setText(cuerpo.substring(0,noteText.getSelectionStart()) + p.getJsonObject() + cuerpo.substring(noteText.getSelectionEnd()));
        reloadJsons(cuerpo.substring(0,noteText.getSelectionStart()) + p.getJsonObject() + cuerpo.substring(noteText.getSelectionEnd()));

    }
}