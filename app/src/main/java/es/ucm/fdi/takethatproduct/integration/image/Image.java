package es.ucm.fdi.takethatproduct.integration.image;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "image")
public class Image implements Serializable {
    @PrimaryKey(autoGenerate=true)
    private int uuid;

    @ColumnInfo(name = "ruta")
    private String ruta;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public Image(String ruta) {
        this.ruta = ruta;
    }
}
