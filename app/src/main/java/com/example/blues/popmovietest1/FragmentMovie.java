package com.example.blues.popmovietest1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.example.blues.popmovietest1.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Blues on 11/24/2015.
 */
public class FragmentMovie extends Fragment{ //implements LoaderManager.LoaderCallbacks<Cursor> {
    ImageAdapter movieImageAdapter;
    JSONObject[] movieJSONObject;
    View rootView;
    GridView movieGridView;
    int moviePerPage;
    String currentSort = "";

    String pop_rank = "popularity";
    String vot_ave_rank = "vote_average";
    boolean Sortchange = false;
    private static final int MOVIE_LOADER = 0;

    public FragmentMovie() {}

    private static final String[] MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_NAME};


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
       // getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

   /* void onSortChanged()
    {
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        FetchMovieTask weatherTask = new FetchMovieTask();

        weatherTask.execute(new String[]{pop_rank, "1"});
        currentSort = pop_rank;


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
            movieTask.execute(new String[]{pop_rank,"1"});
            if(currentSort != pop_rank)
                Sortchange = true;
            currentSort = pop_rank;


            return true;
        }
        if (id == R.id.action_re_date) {
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(new String[]{vot_ave_rank,"1"});
            if(currentSort != vot_ave_rank)
                Sortchange = true;
            currentSort = vot_ave_rank;
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


    /*@Override
    public Loader<Cursor>  onCreateLoader(int i, Bundle bundle) {
        String csort = perference.getCurrentsort();

        // Sort order:  Ascending, by date.
        String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " ASC";
        Uri MovieUri = MovieContract.MovieEntry.buildMovieInfo();
        return new CursorLoader(getActivity(),
                MovieUri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }*/


   /* @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor)
    {
            movieGridView.setAdapter(movieImageAdapter);  //if the sort type changed by menu, gridview goes to top
    }


    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        movieGridView.setAdapter(null);
    }

*/
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
               FetchMovieTask movieTask = new FetchMovieTask();
                movieTask.execute(currentSort, getCount()/moviePerPage+1+"");
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








}

