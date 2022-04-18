package es.ucm.fdi.takethatproduct.integration.product;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.takethatproduct.searchAmazonProductsFragment;

public class ProductLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Product>>{

    searchAmazonProductsFragment context;
    public ProductLoaderCallbacks(searchAmazonProductsFragment context) {
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public Loader<List<Product>> onCreateLoader(int id, @Nullable @org.jetbrains.annotations.Nullable Bundle args) {
        return new ProductLoader(context.getActivity(), (String) args.get("queryString"));
    }

    @Override
    public void onLoadFinished(@NonNull @NotNull Loader<List<Product>> loader, List<Product> data) {
        context.updateProductResultList((ArrayList<Product>) data);
    }


    @Override
    public void onLoaderReset(@NonNull @NotNull Loader<List<Product>> loader) {

    }
}
