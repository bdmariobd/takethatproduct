package es.ucm.fdi.takethatproduct;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import es.ucm.fdi.takethatproduct.integration.product.ApiUtil;
import es.ucm.fdi.takethatproduct.integration.product.Product;
import es.ucm.fdi.takethatproduct.integration.product.ProductLoaderCallbacks;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link searchAmazonProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchAmazonProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public searchAmazonProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchAmazonProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static searchAmazonProductsFragment newInstance() {
        searchAmazonProductsFragment fragment = new searchAmazonProductsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search_amazon_products, container, false);

        Object that = this;

        view.findViewById(R.id.searchProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (ApiUtil.networkAvailable(connMgr)) {
                    Bundle queryBundle = new Bundle();
                    queryBundle.putString("queryString", view.findViewById(R.id.productField).toString());
                    //this.setProgressBarLoading(true);

                    LoaderManager.getInstance(getActivity())
                            .restartLoader(0, queryBundle, new ProductLoaderCallbacks((searchAmazonProductsFragment) that));
                }
                else{
                    Toast.makeText(getActivity(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    public void updateProductResultList(ArrayList<Product> data) {
        //booksResult.setVisibility(View.VISIBLE);
        String text = data.size()==0? "No hay resultados" : data.size() + " resultados";
        //booksResult.setText(text);
        //bookListAdapter.setmBookList(bookInfos);
        //bookListAdapter.notifyDataSetChanged();
        //this.setProgressBarLoading(false);

    }
}