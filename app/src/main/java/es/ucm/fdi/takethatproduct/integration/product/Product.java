package es.ucm.fdi.takethatproduct.integration.product;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Product {

    @PrimaryKey(autoGenerate=true)
    private int uuid;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "urlImagen")
    private String urlImagen;

    @ColumnInfo(name = "fechaCreacion")
    private String fechaCreacion;

    @ColumnInfo (name = "variedad")
    private String variedad;

    @ColumnInfo(name = "precio")
    private Double precio;

    public String getTitulo() {
        return titulo;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getUuid() {
        return uuid;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getJsonObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", "product");
        json.put("urlImage", this.urlImagen);
        json.put("id", this.getUuid());
        json.put("title", this.getTitulo());
        json.put("url", this.getUrl());
        json.put("price", this.getPrecio());
        return json.toString();
    }

    public Product(String titulo, String url, String urlImagen, String variedad, Double precio) {
        this.titulo = titulo;
        this.url = url;
        this.urlImagen = urlImagen;
        this.variedad = variedad;
        this.precio = precio;
        this.fechaCreacion = new Date().toString();
    }

    public static Product fromJSONObject(JSONObject JSON) throws JSONException {
        //TODO parse array of prices and labels
        String type = "default";
        Double price = JSON.getJSONArray("prices").getJSONObject(0).getDouble("price");
        return new Product(JSON.getString("title"), JSON.getString("productUrl"), JSON.getString("imageUrl"), type, price);
    }
}