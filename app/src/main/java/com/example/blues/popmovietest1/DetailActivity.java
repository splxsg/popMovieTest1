package com.example.blues.popmovietest1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailActivity extends AppCompatActivity {
   static private String JSONSTR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);






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

        public DetailFragment() {
        }

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

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


            Intent intent = getActivity().getIntent();
            ImageView imageView = (ImageView)rootView.findViewById(R.id.detail_image);
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String JSONSTR = intent.getStringExtra(Intent.EXTRA_TEXT);
               try{
                ((TextView) rootView.findViewById(R.id.detail_title)).setText("Title: " + getMovieInfo(JSONSTR, "original_title"));
                   ((TextView) rootView.findViewById(R.id.detail_year)).setText("Release on: " + getMovieInfo(JSONSTR, "release_date"));
                   ((TextView) rootView.findViewById(R.id.avg_rate)).setText("Rate: " + getMovieInfo(JSONSTR, "vote_average"));
                   ((TextView) rootView.findViewById(R.id.detail_text)).setText("Title: " + getMovieInfo(JSONSTR, "original_title"));

                ((TextView) rootView.findViewById(R.id.detail_text)).setText("Synopsis: " + getMovieInfo(JSONSTR, "overview"));
                   Picasso.with(getActivity())
                           .load(getPosterUri(getMovieInfo(JSONSTR, "poster_path")).toString())
                           .into(imageView);
            }
               catch (JSONException e) {
               }}

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

            }




