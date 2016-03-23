package com.example.blues.popmovietest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private String Currentsort;
    private final String FRAGMENTMOVIE_TAG = "FMTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Currentsort = perference.getCurrentsort();
        setContentView(R.layout.activity_main);
       if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FragmentMovie(), FRAGMENTMOVIE_TAG)
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        String csort = perference.getCurrentsort();
        if (csort != Currentsort) {
            FragmentMovie FM = (FragmentMovie) getSupportFragmentManager().findFragmentByTag(FRAGMENTMOVIE_TAG);
            if(null != FM)
                FM.onSortChanged();
        }
    }
}
