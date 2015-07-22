package com.webileapps.customadapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PraveenKatha on 14/07/15.
 */
public class RecyclerViewFragment extends PlacesFragment {


    private RowAdapter mRowAdapter;
    private SectionHeaderAdapter mSectionHeaderAdapter;
    private RecyclerViewSectionAdapter sectionAdapter;
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recyler_view, container, false);

        getActivity().setTitle("Recycler View");

        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        mRowAdapter = new RowAdapter();
        mSectionHeaderAdapter = new SectionHeaderAdapter();

        sectionAdapter = new RecyclerViewSectionAdapter<RowAdapter>(mSectionHeaderAdapter, mRowAdapter) {

            @Override
            public int getIndexOfSection(Object place) {

                String[] place_types = ((ActivityCommunicator) getActivity()).getPlaceTypes();

                for (int i = 0; i < place_types.length; i++) {
                    if (((Place) place).types.contains(place_types[i]))
                        return i;
                }

                return -1;
            }
        };

        recyclerView.setAdapter(sectionAdapter);
        recyclerView.setLayoutManager(new VerticalLayoutManager());
        return root;
    }

    @Override
    public void setData(List<Place> places) {
        mRowAdapter.setData(places);
    }

    class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> implements RecyclerViewSectionAdapter.GetObject {

        List<Place> places;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item, parent, false);
            return new RowAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Place place = places.get(position);
            Picasso.with(getActivity().getApplicationContext()).load(place.icon).fit().centerInside().into(holder.iv);
            holder.tv.setText(place.name);
        }

        @Override
        public int getItemCount() {
            if (places == null)
                return 0;
            return places.size();
        }

        public void setData(List<Place> places) {
            this.places = places;
            notifyDataSetChanged();
        }

        @Override
        public Place getObject(int position) {
            return places.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView iv;
            TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                this.iv = (ImageView) itemView.findViewById(R.id.icon);
                this.tv = (TextView) itemView.findViewById(R.id.name);

            }
        }
    }


    class SectionHeaderAdapter extends RecyclerView.Adapter<SectionHeaderAdapter.ViewHolder> {

        String[] placeTypesText;

        public SectionHeaderAdapter() {
            placeTypesText = ((ActivityCommunicator) getActivity()).getPlaceTypesText();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.section_item, parent, false);
            return new SectionHeaderAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(placeTypesText[position]);
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                this.tv = (TextView) itemView.findViewById(R.id.section_name);

            }
        }
    }


}


