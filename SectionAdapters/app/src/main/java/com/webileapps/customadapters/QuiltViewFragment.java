package com.webileapps.customadapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import webileapps.quiltview.QuiltView;


/**
 * Created by PraveenKatha on 23/07/15.
 */
public class QuiltViewFragment extends PlacesFragment {

    private QuiltView quiltView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.quit_view_fragment, container, false);

        getActivity().setTitle("Quilt View");

        quiltView = (QuiltView) root.findViewById(R.id.quilt);
        quiltView.setChildPadding(5);
        quiltView.setOrientation(true);
        addTestQuilts(6);

        return root;
    }

    public void addTestQuilts(int num){
        ArrayList<ImageView> images = new ArrayList<ImageView>();
        for(int i = 0; i < num; i++){
            ImageView image = new ImageView(getActivity().getApplicationContext());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if(i % 2 == 0)
                image.setImageResource(R.drawable.mayer);
            else
                image.setImageResource(R.drawable.mayer1);
            images.add(image);
        }
        quiltView.addPatchImages(images);
    }

    @Override
    public void setData(List<Place> places) {

    }
}
