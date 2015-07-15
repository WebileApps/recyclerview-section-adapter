package com.webileapps.customadapters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Response;
import com.webileapps.volleypoc.com.webileapps.volleypoc.request.GsonRequest;
import com.webileapps.volleypoc.com.webileapps.volleypoc.singleton.VolleySingleton;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.ArrayUtils;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.Url;


public class DemoActivity extends AppCompatActivity implements PlacesFragment.ActivityCommunicator {

    public final String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDLH3Is2COdpFAC-lKbvzOusqo0ygKHvmw";
    private SimpleSectionAdapter<Place> moviesAdapter;
    private String url;
    private String[] place_types;
    private String[] place_types_text;
    private PlacesFragment placesFragment;
    private PlacesFragment recyclerFragment;
    private PlacesFragment listViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        listViewFragment = new ListViewFragment();
        recyclerFragment = new RecyclerViewFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment,listViewFragment).commit();

        placesFragment = listViewFragment;
        String location = "17.4416,78.3826";

        place_types = new String[]{"hospital", "bakery", "beauty_salon", "shopping_mall", "home_goods_store", "movie_theatre"};
        place_types_text = new String[]{"HOSPITAL", "BAKERY", "BEAUTY SALON", "SHOPPING MALL", "HOME GOODS STORE", "MOVIE THEATRE"};

        url = Url.buildUrl(baseUrl)
                .addParameter("location", location)
                .addParameter("radius", "50000")
                .addParameter("types", ArrayUtils.join(place_types, "|")).toString();

        EditText et = (EditText) findViewById(R.id.movie_name);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPlace(s.toString());
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.recycler_view_btn:
                placesFragment = recyclerFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,recyclerFragment).commit();
                break;
            case R.id.list_view_btn:
                placesFragment = listViewFragment;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,listViewFragment).commit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchPlace(String place) {

        String searchUrl = Url.buildUrl(url).addParameter("name", place).toString();
        System.out.println("Search Place Url " + searchUrl);

        GsonRequest req = new GsonRequest<>(searchUrl, PlaceResults.class, null,
                new Response.Listener<PlaceResults>() {
                    @Override
                    public void onResponse(PlaceResults placeResults) {
                        System.out.println(placeResults.results.size());
                        placesFragment.setData(placeResults.results);
                        //placesAdapter.notifyDataSetChanged();
                    }
                }, null);

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(req);

    }

    @Override
    public String[] getPlaceTypes() {
        return place_types;
    }

    @Override
    public String[] getPlaceTypesText() {
        return place_types_text;
    }
}

