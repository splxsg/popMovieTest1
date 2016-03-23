package com.example.blues.popmovietest1;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Blues on 22/03/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, String> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    /* The date/time conversion code is going to be moved outside the asynctask later,
+         * so for convenience we're breaking it out into its own method now.
+         */
    JSONObject[] movieJSONObject;
    int moviePerPage;
    private boolean sortChange = false; //cant use bool here, dont know why, and this is a flag to show if the sort type changed, if so, the adapter will go to the beginning

    private void UpdatemovieJSONObject(String JSONstr)
            throws JSONException {

        try {
            JSONObject jsonobject = new JSONObject(JSONstr);
            JSONObject[] tempjsonobject;

            final String js_RESULT = "results";

            JSONArray jsonarray = new JSONObject(JSONstr).getJSONArray(js_RESULT);

            if (movieJSONObject == null) {             //if app just start or request a new sort method, movieJSONObject will be initial
                moviePerPage = jsonarray.length();
                movieJSONObject = new JSONObject[jsonarray.length()];
                for (int i = 0; i < jsonarray.length(); i++)
                    movieJSONObject[i] = jsonarray.getJSONObject(i);
            } else                                                    //To list more content under the same sort method
            {
                int original_length = movieJSONObject.length;
                tempjsonobject = movieJSONObject;              //assign temp space for swapping
                movieJSONObject = new JSONObject[jsonarray.length() + original_length];   //enlarge size of movieJSONObject
                for (int i = 0; i < original_length; i++) {
                    movieJSONObject[i] = tempjsonobject[i];       //put original data back
                }
                for (int i = 0; i < jsonarray.length(); i++)       //assign new data to movieJSONObject
                    movieJSONObject[i + original_length] = jsonarray.getJSONObject(i);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error ", e);
        }

    }


    @Override
    protected String doInBackground(String[] params) {
        Log.v(LOG_TAG, "PARAMS LENGTH " + params.length);
        if (params.length == 0) {
            return null;
        }


        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String MovieJsonStr = null;


        try {
            // Construct the URL for requesting data from themoviedb via legal API
            final String MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String Sort = params[0];
            final String PAGE_PARAM = "page";
            final String page = params[1];
            final String Rank = ".desc";
            final String APPID_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, Sort + Rank)
                    .appendQueryParameter(PAGE_PARAM, page)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIE_DATABASE_API_KEY)
                    .build();
            URL url = new URL(builtUri.toString());

            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
            // Create the request to themoviedb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            MovieJsonStr = buffer.toString();

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            MovieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie JSON String: " + MovieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        if (params[1] == "1")  //if request the first page of sorting, it means we need to initial movieJSONObject
            movieJSONObject = null;
        try {
            UpdatemovieJSONObject(MovieJsonStr);
        } catch (JSONException e) {
            Log.e("JSONE", "ERROR", e);
        }
        return MovieJsonStr;
    }

    //this is going to happen after the JSON data obtained.


}