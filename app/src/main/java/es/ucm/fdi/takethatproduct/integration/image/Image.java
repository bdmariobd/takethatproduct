package es.ucm.fdi.takethatproduct.integration.image;

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
        return json.toString();
    }

    public static String bodyWithOutJsonImage (String body){
        //LO Q HA COSTAO
        //https://stackoverflow.com/questions/5633533/regular-expression-for-matching-parentheses
        String cleaned = body.replaceAll("[\\{].*[\\}]", "");
        return cleaned;
    }

}
