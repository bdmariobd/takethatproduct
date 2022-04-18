package es.ucm.fdi.takethatproduct.integration.product;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class ProductLoader extends AsyncTaskLoader<List<Product>> {


    String query;
    public ProductLoader(@NonNull @org.jetbrains.annotations.NotNull Context context, String query) {
        super(context);
        this.query = query;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public List<Product> loadInBackground() {
        Log.d(this.getClass().getSimpleName(), "Loading products");
        try {
            return ApiUtil.getProductsFromUrl(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<Product>();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
