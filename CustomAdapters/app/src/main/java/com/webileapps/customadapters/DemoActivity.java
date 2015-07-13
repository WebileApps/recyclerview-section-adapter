package com.webileapps.customadapters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;
import com.webileapps.volleypoc.com.webileapps.volleypoc.request.GsonRequest;
import com.webileapps.volleypoc.com.webileapps.volleypoc.singleton.VolleySingleton;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.StringUtils;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.Url;

import java.util.List;


public class DemoActivity extends AppCompatActivity {

    public final String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyDLH3Is2COdpFAC-lKbvzOusqo0ygKHvmw";
    private SimpleSectionAdapter<Place> moviesAdapter;
    private String url;
    private SectionAdapter placesSectionAdapter;
    private PlaceAdapter placesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        String location = "17.4416,78.3826";

        final String[] place_types = {"hospital", "bakery", "beauty_salon", "shopping_mall", "home_goods_store", "movie_theatre"};
        final String[] place_types_text = {"HOSPITAL", "BAKERY", "BEAUTY SALON", "SHOPPING MALL", "HOME GOODS STORE", "MOVIE THEATRE"};


        url = Url.buildUrl(baseUrl)
                .addParameter("location", location)
                .addParameter("radius", "50000")
                .addParameter("types", StringUtils.join(place_types, "|")).toString();


        ListView lv = (ListView) findViewById(R.id.list_view);
        View headerView = getLayoutInflater().inflate(R.layout.header, null);

        EditText et = (EditText) headerView.findViewById(R.id.movie_name);
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


        lv.addHeaderView(headerView);

        /*moviesAdapter = new SimpleSectionAdapter<Place>(getApplicationContext(),
                R.layout.section_item, R.id.section_name) {


            @Override
            protected int getIndexOfSection(Place object) {
                for (int i = 0; i < place_types.length; i++) {
                    if (object.types.contains(place_types[i]))
                        return i;
                }
                return -1;
            }

            @Override
            protected String getSectionText(int sectionIndex) {
                return place_types_text[sectionIndex];
            }

            @Override
            protected View getListItemView(Place object, ViewGroup parent) {
                View v = getInflater().inflate(R.layout.list_item, parent, false);
                TextView tv = (TextView) v.findViewById(R.id.name);
                tv.setText(object.name);
                ImageView iv = (ImageView) v.findViewById(R.id.icon);
                Picasso.with(getApplicationContext()).load(object.icon).fit().centerInside().into(iv);

                return v;
            }
        }*/;

        placesAdapter = new PlaceAdapter();

        placesSectionAdapter = new SectionAdapter(placesAdapter) {

            @Override
            protected int getIndexOfSection(Object object) {
                for (int i = 0; i < place_types.length; i++) {
                    if (((Place) object).types.contains(place_types[i]))
                        return i;
                }
                return -1;
            }


            @Override
            protected String getSectionText(int sectionIndex) {
                return place_types_text[sectionIndex];
            }

            @Override
            protected View getSectionView(int sectionIndex, String sectionText, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.section_item, parent, false);
                TextView tv = (TextView) v.findViewById(R.id.section_name);
                tv.setText(sectionText);
                return v;
            }

        };

        lv.setAdapter(placesSectionAdapter);
    }

    private void searchPlace(String place) {

        String searchUrl = Url.buildUrl(url).addParameter("name", place).toString();
        System.out.println("Search Place Url " + searchUrl);
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new GsonRequest<>(searchUrl, PlaceResults.class, null,
                new Response.Listener<PlaceResults>() {
                    @Override
                    public void onResponse(PlaceResults placeResults) {

                        System.out.println(placeResults.results.size());
                        placesAdapter.setData(placeResults.results);
                        //placesAdapter.notifyDataSetChanged();
                    }
                }, null));

    }


    class PlaceAdapter extends BaseAdapter {

        List<Place> places;

        public void setData(List<Place> places) {
            this.places = places;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if(places == null) {
                return 0;
            }
            return places.size();
        }

        @Override
        public Object getItem(int position) {
            return places.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item, parent, false);
            TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(places.get(position).name);
            ImageView iv = (ImageView) v.findViewById(R.id.icon);
            Picasso.with(getApplicationContext()).load(places.get(position).icon).fit().centerInside().into(iv);

            return v;
        }
    }

}

