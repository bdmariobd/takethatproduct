package es.ucm.fdi.takethatproduct.integration.product;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiUtil {

    private static final String ENDPOINT_URL =
            "https://amazon-search-products.herokuapp.com/search?";
    private static final String QUERY_PARAM = "q";
    /*private static final String MAX_RESULTS = "maxResults";*/

    static List<Product> getProductsFromUrl(String queryString) throws MalformedURLException, JSONException {

        //TODO handle exceptions
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String productJSONString = null;

        List<Product> products = new ArrayList<Product>();

        try {
            Uri builtURI = Uri.parse(ENDPOINT_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    //.appendQueryParameter(MAX_RESULTS, "40")
                    //.appendQueryParameter(PRINT_TYPE, printType)
                    .build();

            URL requestURL = new URL(builtURI.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoInput(true);

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            productJSONString = builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(ApiUtil.class.getSimpleName(), productJSONString);

        JSONArray results = new JSONArray(productJSONString);

        for(int i = 0 ; i<results.length(); ++i){
            products.add(Product.fromJSONObject(results.getJSONObject(i)));
        }
        return products;
    }


    public static boolean networkAvailable(ConnectivityManager connectivityService) {
        return connectivityService!= null && connectivityService.getActiveNetworkInfo()!=null && connectivityService.getActiveNetworkInfo().isConnected();
    }

}