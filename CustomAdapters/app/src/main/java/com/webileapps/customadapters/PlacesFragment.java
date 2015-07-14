package com.webileapps.customadapters;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by PraveenKatha on 13/07/15.
 */
public abstract class PlacesFragment extends Fragment {

    public abstract void setData(List<Place> places);

    interface ActivityCommunicator {
        String[] getPlaceTypes();
        String[] getPlaceTypesText();
        void searchPlace(String place);

    }
}