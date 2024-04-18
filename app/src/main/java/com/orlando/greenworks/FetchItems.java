package com.orlando.greenworks;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */


public class FetchItems {

    private final ItemInformationFragment itemInformationFragment;
    private FetchItemsTask fetchItemsTask; // Keep a reference to the FetchItemsTask

    public FetchItems(ItemInformationFragment itemInformationFragment) {
        this.itemInformationFragment = itemInformationFragment;
    }

    public void fetchItems(String query) {
//        Log.d("FetchItems", "Fetching items for query: " + query);
//        new FetchItemsTask().execute(query);

        Log.d("FetchItems", "Fetching items for query: " + query);

        // Cancel the previous FetchItemsTask if it's still running
        if (fetchItemsTask != null) {
            fetchItemsTask.cancel(true);
        }

        // Start a new FetchItemsTask
        fetchItemsTask = new FetchItemsTask();
        fetchItemsTask.execute(query);
    }


    private class FetchItemsTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String query = params[0];
            try {
                String formattedQuery = query.replace(" ", "+");
                String searchUrl = "https://curry-home.cheetoh-python.ts.net/items?q=" + formattedQuery;
                String jsonResponse = performHttpGet(searchUrl);
                JSONArray searchResults = new JSONArray(jsonResponse);

                if (searchResults.length() > 0) {
                    JSONObject item = searchResults.getJSONObject(0);
                    int id = item.getInt("id");

                    String detailsUrl = "https://curry-home.cheetoh-python.ts.net/items/" + id;
                    String itemDetails = performHttpGet(detailsUrl);

                    return new JSONObject(itemDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject itemDetails) {
            if (itemDetails != null) {
                itemInformationFragment.displayItemInformation(itemDetails);
            }
        }

        private String performHttpGet(String urlString) throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                return "{}";
            }
        }
    }
}
