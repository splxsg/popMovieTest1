package com.example.blues.popmovietest1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

/**
 * Created by Blues on 11/24/2015.
 */
public class FragmentMovie extends Fragment {
    ImageAdapter movieImageAdapter;
    JSONObject[] movieJSONObject;
    View rootView;
    GridView movieGridView;
    int moviePerPage;
    String currentSort = "";


    public FragmentMovie() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        FetchMovieTask weatherTask = new FetchMovieTask();

        weatherTask.execute(new String[]{"popularity", "1"});

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_pop) {
          FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(new String[]{"popularity","1"});

            return true;
        }
        if (id == R.id.action_re_date) {
            FetchMovieTask movieTask = new FetchMovieTask();
           movieTask.execute(new String[]{"vote_average","1"});

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieJSONObject = null;
        movieImageAdapter = new ImageAdapter(getActivity());
        movieGridView = (GridView) rootView.findViewById(R.id.movie_gridView);
        movieGridView.setAdapter(movieImageAdapter);

        movieImageAdapter.notifyDataSetChanged();
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String LOG_TAG = "onItemClick_fragmentMovie";

               // String moviedata = movieJSONObject[position].toString();
                String moviedata = "test";
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, moviedata);
                startActivity(intent);

            }

        });


        return rootView;
    }


    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private final String LOG_TAG = "ImageAdapter";

        public ImageAdapter(Context context) {
            mContext = context;
        }




        @Override
        public int getCount() {
            if(movieJSONObject != null)
                return movieJSONObject.length;
            else
               //return 0;
                return 20;
        }

        @Override
        public String getItem(int position) {
            return "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg";
           /* String s = null;
            if(movieJSONObject != null)
            {try {
                s = getPosterUri(movieJSONObject[position].getString("poster_path")).toString();
            } catch (JSONException e) {
            }
                return s;}
            else
                return null;*/
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {

            ImageView imageView;



//            check to see if we have a view
            if (convertView == null) {

                imageView = new ImageView(mContext);
                imageView.setAdjustViewBounds(true);
                imageView.setPadding(0, 0, 0, 0);
//                no view - so create a new one

            } else {
//                use the recycled view object

                imageView = (ImageView) convertView;
                //textView = (TextView) convertView;
            }


            if(position == getCount()-3)
            {
             //   FetchMovieTask movieTask = new FetchMovieTask();
               // movieTask.execute(currentSort, getCount()/moviePerPage+1+"");
            }




            String s = getItem(position);
            Picasso.with(getActivity())
                    .load(s)
                    .placeholder(R.raw.placeholder)
                    .into(imageView);
            return imageView;

        }
        private Uri getPosterUri(String location) {
            final String MOVIE_DATABASE_URL = "http://image.tmdb.org/t/p/w154" + location;
            Uri builtUri = Uri.parse(MOVIE_DATABASE_URL).buildUpon().build();
            return builtUri;
        }



    }



    public class FetchMovieTask extends AsyncTask<String, Void, String> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private boolean sortChange = false;

        private void UpdatemovieJSONObject(String JSONstr)
                throws JSONException {

            try {
                JSONObject jsonobject = new JSONObject(JSONstr);
                JSONObject[] tempjsonobject;

                final String js_RESULT = "results";

                JSONArray jsonarray = new JSONObject(JSONstr).getJSONArray(js_RESULT);

                /*if(movieJSONObject == null) {             //if app just start or request a new sort method, movieJSONObject will be initial
                    moviePerPage = jsonarray.length();
                    movieJSONObject = new JSONObject[jsonarray.length()];
                    for (int i = 0; i < jsonarray.length(); i++)
                        movieJSONObject[i] = jsonarray.getJSONObject(i);
                }
                else                                                    //To list more content under the same sort method
                {
                    int original_length = movieJSONObject.length;
                    tempjsonobject = movieJSONObject;              //assign temp space for swapping
                    movieJSONObject = new JSONObject[jsonarray.length()+original_length];   //enlarge size of movieJSONObject
                    for (int i = 0; i < original_length; i++) {
                        movieJSONObject[i] = tempjsonobject[i];       //put original data back
                    }
                    for (int i = 0; i < jsonarray.length(); i++)       //assign new data to movieJSONObject
                        movieJSONObject[i+original_length] = jsonarray.getJSONObject(i);
                }*/
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
            }

        }



        @Override
        protected String doInBackground(String[] params) {

           Log.v(LOG_TAG,"PARAMS LENGTH "+params.length);
            if (params.length == 0) {
                return null;
            }
            if(currentSort != params[0])
                sortChange = true;
            currentSort = params[0];


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
            if(params[1]=="1")  //if request the first page of sorting, it means we need to initial movieJSONObject
                movieJSONObject = null;
            return MovieJsonStr;

        }

        //this is going to happen after the JSON data obtained.
        @Override
        protected void onPostExecute(String result) {
            Log.v(LOG_TAG, "JSON results" + result);
            if (result != null) {
                try {
                    UpdatemovieJSONObject(result);  //update the movie infomation to movieJSONObject and notify the adapter data has changed
                    movieImageAdapter.notifyDataSetChanged();
                    if(sortChange)
                        movieGridView.setAdapter(movieImageAdapter);  //if the sort type changed by menu, gridview goes to top
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error ", e);
                }
            }
        }
    }
}

