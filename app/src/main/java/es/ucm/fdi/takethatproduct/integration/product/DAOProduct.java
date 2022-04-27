package es.ucm.fdi.takethatproduct.integration.product;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface DAOProduct {
    @Insert
    void insert(Product product);
    @Update
    public void updateWords(Product... product);

    @Query("DELETE FROM product")
    void deleteAll();

    @Query("DELETE FROM product where uuid = :uuid")
    void deleteOneNote(int uuid);

    @Query("SELECT * from product")
    LiveData<List<Product>> getAllproducts();

    @Query("SELECT * FROM product WHERE uuid LIKE :uuid ")
    public List<Product> findProductbyUuid(int uuid);

    @Query("SELECT * FROM product WHERE titulo LIKE :word ")
    public List<Product> findProductbyTitle(String word);

}