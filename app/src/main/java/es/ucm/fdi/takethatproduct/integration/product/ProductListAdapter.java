package es.ucm.fdi.takethatproduct.integration.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import es.ucm.fdi.takethatproduct.NoteTotalViewActivity;
import es.ucm.fdi.takethatproduct.R;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {
    private ArrayList<Product> mProductList;
    private final LayoutInflater mInflater;
    private final NoteTotalViewActivity context;

    public void setmProductList(ArrayList<Product> mProductList){
        this.mProductList = mProductList;
    }

    public ProductListAdapter(NoteTotalViewActivity context) {
        this.mInflater = LayoutInflater.from(context);
        this.mProductList = new ArrayList<Product>();
        this.context = context;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        final ProductListAdapter mAdapter;
        public final TextView productTitle;
        public final TextView productPrecio;
        public final ImageView productImage;
        public final Button addProduct;
        public final View itemView;
        public ProductViewHolder(View itemView, ProductListAdapter adapter) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrecio = itemView.findViewById(R.id.priceLabel);
            productImage = itemView.findViewById(R.id.productImage);
            addProduct = itemView.findViewById(R.id.addProduct);
            this.mAdapter = adapter;
            this.itemView = itemView;
        }
    }

    @NonNull
    @NotNull
    @Override
    public ProductListAdapter.ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(
                R.layout.product_list_element, parent, false);

        return new ProductViewHolder(mItemView, this);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductListAdapter.ProductViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.productTitle.setText(product.getTitulo());
        //https://stackoverflow.com/questions/2394935/can-i-underline-text-in-an-android-layout
        holder.productTitle.setPaintFlags(holder.productTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        //holder.bookAuthor.setText(book.getAuthors());

        holder.productPrecio.setText( " "+String.valueOf(product.getPrecio()) + " €");

        holder.productTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(product.getUrl().toString()));
                context.startActivity(browserIntent);
            }
        });

        holder.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d("Product clicked" , product.getTitulo());
                    context.someEvent(product);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        //https://github.com/bumptech/glide
        Glide.with(context)
                .load(product.getUrlImagen().toString())
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(holder.productImage);
    }


    @Override
    public int getItemCount() {
        return this.mProductList.size();
    }
}

