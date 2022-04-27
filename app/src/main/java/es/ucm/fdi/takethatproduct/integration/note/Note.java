package es.ucm.fdi.takethatproduct.integration.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Entity(tableName = "notes")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate=true)
    private int uuid;

    @ColumnInfo(name = "titulo")
    private String titulo;

    @ColumnInfo(name = "cuerpo")
    private String cuerpo;

    @ColumnInfo(name = "fechaCreacion")
    private String fechaCreacion;

    @ColumnInfo(name = "fechaModificacion")
    private String fechaModificacion;



    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getFechaCreacion() { return fechaCreacion; }

    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaModificacion() { return fechaModificacion; }

    public void setFechaModificacion(String fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public boolean empty() {
        boolean ret = true;
        if(this.titulo != "" || this.cuerpo != "")
            ret = false;
        return ret;
    }

    public Note(String titulo, String cuerpo) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.fechaCreacion = new Date().toString();
        this.fechaModificacion = new Date().toString();
    }
}
