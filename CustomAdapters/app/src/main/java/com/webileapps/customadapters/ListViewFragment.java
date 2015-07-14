package com.webileapps.customadapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PraveenKatha on 13/07/15.
 */
public class ListViewFragment extends PlacesFragment {

    private PlaceAdapter placesAdapter;
    private SectionAdapter placesSectionAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.list_view,container,false);

        getActivity().setTitle("List View");

        ListView lv = (ListView) root.findViewById(R.id.list_view);




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

                String [] place_types = ((ActivityCommunicator)getActivity()).getPlaceTypes();

                for (int i = 0; i < place_types.length; i++) {
                    if (((Place) object).types.contains(place_types[i]))
                        return i;
                }
                return -1;
            }


            @Override
            protected String getSectionText(int sectionIndex) {
                String [] place_types_text = ((ActivityCommunicator)getActivity()).getPlaceTypesText();
                return place_types_text[sectionIndex];
            }

            @Override
            protected View getSectionView(int sectionIndex, String sectionText, View convertView, ViewGroup parent) {
                View v = inflater.inflate(R.layout.section_item, parent, false);
                TextView tv = (TextView) v.findViewById(R.id.section_name);
                tv.setText(sectionText);
                return v;
            }

        };

        lv.setAdapter(placesSectionAdapter);

        return root;
    }

    @Override
    public void setData(List<Place> places) {
        placesAdapter.setData(places);
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
            LayoutInflater inflater = getLayoutInflater(null);
                    View v = inflater.inflate(R.layout.list_item, parent, false);
            TextView tv = (TextView) v.findViewById(R.id.name);
            tv.setText(places.get(position).name);
            ImageView iv = (ImageView) v.findViewById(R.id.icon);
            Picasso.with(getActivity().getApplicationContext()).load(places.get(position).icon).fit().centerInside().into(iv);

            return v;
        }
    }

}

