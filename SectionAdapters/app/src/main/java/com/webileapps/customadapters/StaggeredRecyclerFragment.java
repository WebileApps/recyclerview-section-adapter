package com.webileapps.customadapters;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Praveen Katha on 22/07/15.
 */
public class StaggeredRecyclerFragment extends PlacesFragment{

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    public LoaderManager getLoaderManager() {
        return super.getLoaderManager();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recyler_view, container, false);

        getActivity().setTitle("Staggered Recycler View");

        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        imageAdapter = new ImageAdapter();
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        /*GridLayoutManager  gridLayoutManager = new GridLayoutManager(getActivity(),3);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position  % 3 == 0)
                    return 2;
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);*/

        return root;
    }


    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

        String[] placeTypesText;

        public ImageAdapter() {
            placeTypesText = ((ActivityCommunicator) getActivity()).getPlaceTypesText();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.image_layout, parent, false);
            return new ImageAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.iv.setImageDrawable(getImageDrawable("coffee"+ (position + 1)));
            holder.iv.setScaleType(ImageView.ScaleType.CENTER);
        }

        @Override
        public int getItemCount() {
            return 7;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ImageView iv;

            public ViewHolder(View itemView) {
                super(itemView);
                this.iv = (ImageView) itemView.findViewById(R.id.image);

            }
        }
    }

    private Drawable getImageDrawable (String filename) {
        int image_id = getResources().getIdentifier(filename, "drawable", getActivity().getPackageName());
        return  getActivity().getResources().getDrawable(image_id);
    }

    @Override
    public void setData(List<Place> places) {}
}
