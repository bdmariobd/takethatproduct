package es.ucm.fdi.takethatproduct.integration.image;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Image {
    //clase utilidad para gestionar imagenes
    private static int id = 1;
    public static String imageToJson(String uri) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", "image");
        json.put("uri", uri);
        json.put("id", ++id);
        return System.getProperty("line.separator") + json.toString() + System.getProperty("line.separator");
    }

    public static String bodyWithOutJsonImage (String body){
        //LO Q HA COSTAO
        //https://stackoverflow.com/questions/5633533/regular-expression-for-matching-parentheses
        String cleaned = body.replaceAll("[\\{].*[\\}]", "");
        return cleaned;
    }

    public static String getImagePathFromUri(Uri uri, ContentResolver contentResolver){
        String result;
        Cursor cursor = contentResolver.query(uri,null,null,null,null);
        if(cursor == null){
            result = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

}
