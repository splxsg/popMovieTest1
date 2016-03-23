package com.example.blues.popmovietest1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Blues on 11/24/2015.
 */
public class FragmentMovie extends Fragment {
    ImageAdapter movieImageAdapter;
    JSONObject[] movieJSONObject;
    View rootView;
    GridView movieGridView;
    //int moviePerPage;
   // String currentSort = "";




    public FragmentMovie() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);




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
            FetchMovieGrid movieTask = new FetchMovieGrid(movieImageAdapter,movieGridView);
            movieTask.execute(new String[]{"popularity","1"});

            return true;
        }
        if (id == R.id.action_re_date) {
            FetchMovieGrid movieTask = new FetchMovieGrid(movieImageAdapter,movieGridView);
            movieTask.execute(new String[]{"vote_average","1"});

            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_main, container, false);



        movieImageAdapter = new ImageAdapter(getActivity());
        //movieImageAdapter.setJSON(movieJSONObject);

        movieJSONObject = null;

        movieGridView = (GridView) rootView.findViewById(R.id.movie_gridView);

        movieGridView.setAdapter(movieImageAdapter);

        //FetchMovieGrid movieTask = new FetchMovieGrid(movieImageAdapter,movieGridView);

        //perference.setCurrentsort("popularity");
        //movieTask.execute(new String[]{"popularity", "1"});

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String LOG_TAG = "onItemClick_fragmentMovie";

                String moviedata = movieJSONObject[position].toString();
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, moviedata);
                startActivity(intent);

            }

        });


        return rootView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private final String LOG_TAG = "ImageAdapter";
        //private JSONObject[] movieJSONObject;
        private String currentSort = perference.getCurrentsort();
        private int moviePerPage = perference.getMoviePerPage();
        public ImageAdapter(Context context) {
            this.mContext = context;

        }





        @Override
        public int getCount() {
            if(movieJSONObject != null)
                return movieJSONObject.length;
            else
                return 0;
        }

        @Override
        public String getItem(int position) {
            String s = null;
            if(movieJSONObject != null)
            {try {

                s = getPosterUri(movieJSONObject[position].getString("poster_path")).toString();

            } catch (JSONException e) {
            }
                return s;}
            else
                return null;
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
                FetchMovieGrid movieTask = new FetchMovieGrid(movieImageAdapter,movieGridView);
                movieTask.execute(currentSort, getCount()/moviePerPage+1+"");
            }



            Log.v("Gridview",currentSort);
            try{Log.v("Gridview",getPosterUri(movieJSONObject[position].getString("poster_path")).toString());}
            catch (JSONException e) {
            }

            String s = getItem(position);
            Picasso.with(mContext)
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





}
