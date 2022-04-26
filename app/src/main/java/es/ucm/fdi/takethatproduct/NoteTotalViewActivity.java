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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.ucm.fdi.takethatproduct.integration.image.Image;
import es.ucm.fdi.takethatproduct.integration.note.Note;

public class NoteTotalViewActivity extends AppCompatActivity {

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


    public void replaceByImage(int start, int end, Uri imageUrl) throws JSONException, IOException {
        String JsonImage = Image.imageToJson(imageUrl.toString());
        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUrl));
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
    }
}