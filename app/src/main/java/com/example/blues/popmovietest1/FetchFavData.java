package com.example.blues.popmovietest1;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.blues.popmovietest1.data.MovieContract.MovieEntry;

import org.json.JSONException;

import java.util.Vector;

/**
 * Created by Blues on 23/03/2016.
 */
public class FetchFavData extends AsyncTask<Void, Void, Void> {

    private String JSONSTR;
    private Context context;

    private final String LOG_TAG = FetchFavData.class.getSimpleName();
    private String movieinfo(String jsonstr, String info_name) throws JSONException {
                return perference.getMovieInfoFromJSON(jsonstr, info_name);
    }


    public FetchFavData(String jSONSTR, Context mContext) {
        this.JSONSTR = jSONSTR;
        this.context = mContext;
    }

    private void getMovieDataFromJson()
            throws JSONException {
        try {

            Vector<ContentValues> cVVector = new Vector<ContentValues>(6);
            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieinfo(JSONSTR, "id"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieinfo(JSONSTR, "original_title"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieinfo(JSONSTR, "release_date"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieinfo(JSONSTR, "vote_average"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, movieinfo(JSONSTR, "overview"));
            movieValues.put(MovieEntry.COLUMN_MOVIE_ID, "Y");

            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);

                inserted = context.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");

        }catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void...Params) {
        try {
            getMovieDataFromJson();
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();


        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        CharSequence text = "Add Fav Movie!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast  = Toast.makeText(context, text, duration);
        toast.show();
    }


}