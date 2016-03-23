package com.example.blues.popmovietest1;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blues.popmovietest1.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;


public class DetailActivity extends AppCompatActivity {
   static JSONObject[] movieReviewJSONObject;
   static private String MovieID;
    static private FetchMovieReview MovieReview;
    static View rootView;
    static int ReviewAmount;




    public DetailActivity()
    {}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);







        MovieReview = new FetchMovieReview();
      if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }





    }



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_detail, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }


    public static class DetailFragment extends Fragment {



        public DetailFragment(){}








        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

          //This is attempt to create custom adapter for display image, text detail information
          /*  View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            LinearLayout linearlayout = (LinearLayout) rootView.findViewById(R.id.detaillist);
            DetailInfoAdapter adapter = new DetailInfoAdapter(getActivity());
            ImageView imageView = (ImageView) rootView.findViewById(R.id.detail_image);

            Intent intent = getActivity().getIntent();
                if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                    JSONSTR = intent.getStringExtra(Intent.EXTRA_TEXT);
                }
            return rootView;*/



            rootView = inflater.inflate(R.layout.fragment_detail, container, false);



            ImageView imageView = (ImageView)rootView.findViewById(R.id.detail_image);
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String JSONSTR = intent.getStringExtra(Intent.EXTRA_TEXT);
               try{
                   MovieID = getMovieInfo(JSONSTR,"id") ;
                ((TextView) rootView.findViewById(R.id.detail_title)).setText("Title: " + getMovieInfo(JSONSTR, "original_title"));
                   ((TextView) rootView.findViewById(R.id.detail_year)).setText("Release on: " + getMovieInfo(JSONSTR, "release_date"));
                   ((TextView) rootView.findViewById(R.id.avg_rate)).setText("Rate: " + getMovieInfo(JSONSTR, "vote_average"));
                ((TextView) rootView.findViewById(R.id.detail_text)).setText("Synopsis: " + getMovieInfo(JSONSTR, "overview"));

                   Picasso.with(getActivity())
                           .load(getPosterUri(getMovieInfo(JSONSTR, "poster_path")).toString())
                           .into(imageView);
                   Log.d("storeID", "BEFORE");

            }
               catch (JSONException e) {
               }}


            MovieReview.execute(MovieID);

            final Button trailerbtn = (Button) rootView.findViewById(R.id.trailer);
            trailerbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=QBvCgiv7x0E"));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });


            return rootView;


        }


        private String getMovieInfo(String JSONSTR, String info_name)
                throws JSONException {
            JSONObject MovieJson = new JSONObject(JSONSTR);
            return MovieJson.getString(info_name);
        }

        private Uri getPosterUri(String Poster_path)
        {
            final String MOVIE_DATABASE_URL = "http://image.tmdb.org/t/p/w780"+Poster_path;
            Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon().build();
            return builtUri;
        }



//This is attempt to create custom adapter for display image, text detail information
        /*private class DetailInfoAdapter extends BaseAdapter {
            private Context mContext;
            private final String LOG_TAG = "ImageAdapter";

            public DetailInfoAdapter(Context context) {
                mContext = context;
            }


            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public String getItem(int position) {

                return null;
            }

            @Override
            public long getItemId(int position) {

                return 0;
            }

            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                ViewHolder holder = null;

                if (convertView == null) {

                    convertView = mInflater.inflate(R.layout.fragment_detail, null);
                    holder = new ViewHolder();
                    holder.titleView = (TextView) convertView.findViewById(R.id.detail_title);
                    holder.posterView = (ImageView) convertView.findViewById(R.id.detail_image);
                    holder.yearView = (TextView) convertView.findViewById(R.id.detail_year);
                    holder.rateView = (TextView) convertView.findViewById(R.id.avg_rate);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                try {
                    Picasso.with(getActivity())
                            .load(getPosterUri(getMovieInfo(JSONSTR, "poster_path")).toString())
                            .into(holder.posterView);
                    holder.titleView.setText("Title: " + getMovieInfo(JSONSTR, "original_title"));
                    holder.yearView.setText("Release on: " + getMovieInfo(JSONSTR, "release_date"));
                    holder.rateView.setText("Rate: " + getMovieInfo(JSONSTR, "vote_average"));
                   // holder.describeView.setText("Synopsis: " + getMovieInfo(JSONSTR, "overview"));

                }
                catch (JSONException e) {
                }
                return convertView;
            }

            private Uri getPosterUri(String Poster_path)
            {
                final String MOVIE_DATABASE_URL = "http://image.tmdb.org/t/p/w780"+Poster_path;
                Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon().build();
                return builtUri;
            }

            private class ViewHolder {
                ImageView posterView;
                TextView titleView;
                TextView yearView;
                TextView rateView;
                TextView describeView;

        }
            private String getMovieInfo(String JSONSTR, String info_name)
                    throws JSONException {
                JSONObject MovieJson = new JSONObject(JSONSTR);
                return MovieJson.getString(info_name);
            }



        }*/

    }




    public class FetchMovieReview extends AsyncTask<String, Void, String>{
        private final String LOG_TAG = FetchMovieReview.class.getSimpleName();

        private final Context mContext;
        public FetchMovieReview(Context context) {
            mContext = context;
        }

        private void MovieReviewJSONObject(String JSONstr)
                throws JSONException {

            try {



                final String js_RESULT = "results";

                JSONArray jsonarray = new JSONObject(JSONstr).getJSONArray(js_RESULT);
                movieReviewJSONObject = null;

                    movieReviewJSONObject = new JSONObject[jsonarray.length()];
                    for (int i = 0; i < jsonarray.length(); i++)
                        movieReviewJSONObject[i] = jsonarray.getJSONObject(i);
                    ReviewAmount = jsonarray.length();
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }


        }

        private void storeID()
        {
            Log.d("storeID", "1111111111");
            ContentValues movieValue = new ContentValues();
            Log.d("storeID", "22222222222");

                Vector<ContentValues> cVVector = new Vector<ContentValues>(100);
                movieValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "testID");
                movieValue.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, "testMOVIENAME");
                cVVector.add(movieValue);
                ContentValues[] cvArray = new ContentValues[2];
                cVVector.toArray(cvArray);
                int inserted;
                Log.d("storeID", cVVector.toString());
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI,cvArray);
                Log.d("intert SQL", "FetchWeatherTask Complete. " + inserted + " Inserted");

        }


      @Override
      protected String doInBackground(String[] params) {

          Log.v(LOG_TAG,"PARAMS LENGTH "+params.length);
          if (params.length == 0) {
              return null;
          }


          // These two need to be declared outside the try/catch
          // so that they can be closed in the finally block.
          HttpURLConnection urlConnection = null;
          BufferedReader reader = null;

          String MovieReviewJsonStr = null;


          try {
              // Construct the URL for requesting data from themoviedb via legal API
              final String MOVIE_DATABASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"/reviews";
              final String APPID_PARAM = "api_key";
              Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon()
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
              MovieReviewJsonStr = buffer.toString();

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
              MovieReviewJsonStr = buffer.toString();
              Log.v(LOG_TAG, "Movie JSON String: " + MovieReviewJsonStr);
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
          storeID();
          return MovieReviewJsonStr;
      }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    MovieReviewJSONObject(result);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error ", e);
                }
                try {
                    Log.v("123", movieReviewJSONObject[0].toString());
                    ((TextView) rootView.findViewById(R.id.review_text)).setText("Review:\n\n");
                    for(int i=0;i<ReviewAmount;i++)
                    {
                        ((TextView) rootView.findViewById(R.id.review_text)).append("From: "+ getMovieReview(movieReviewJSONObject[i].toString(), "author")+"\n");
                        ((TextView) rootView.findViewById(R.id.review_text)).append(getMovieReview(movieReviewJSONObject[i].toString(), "content")+"\n\n");
                    }
                        } catch (JSONException e) {
                }
            }
        }

        private String getMovieReview(String JSONSTR,String info_name)
                throws JSONException {
            JSONObject ReviewJson = new JSONObject(JSONSTR);
            return ReviewJson.getString(info_name);
        }





    }



            }




