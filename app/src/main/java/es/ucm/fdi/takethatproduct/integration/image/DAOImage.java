package es.ucm.fdi.takethatproduct.integration.image;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.ucm.fdi.takethatproduct.integration.image.Image;

@Dao
public interface DAOImage {

    @Insert
    public void insert(Image image);

    @Update
    public void updateWords(Image... image);

    @Query("DELETE FROM image")
    public void deleteAll();

    @Query("DELETE FROM image where uuid = :uuid")
    public void deleteOneImage(int uuid);


    @Query("SELECT * FROM image WHERE uuid LIKE :uuid ")
    public List<Image> findImagebyUuid(int uuid);

}
