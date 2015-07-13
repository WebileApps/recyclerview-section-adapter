package com.webileapps.customadapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PraveenKatha on 13/07/15.
 */
public abstract class SectionAdapter extends BaseAdapter {

    private final BaseAdapter wrappedAdapter;
    private Map<Integer, Section> sections;


    protected abstract int getIndexOfSection(Object object);

    protected abstract String getSectionText(int sectionIndex);


    public SectionAdapter(BaseAdapter wrappedAdapter) {
        this.wrappedAdapter = wrappedAdapter;
        wrappedAdapter.registerDataSetObserver(mObserver);
        initSections();
    }


    private void initSections() {
        //actualDataSize = wrappedAdapter.getCount();
        int sectionIndex;
        Section newSection;
        sections = new HashMap<>();
        for (int i = 0; i < wrappedAdapter.getCount() ; i++) {
            Object object = wrappedAdapter.getItem(i);
            sectionIndex = getIndexOfSection(object);
            if (sections.containsKey(sectionIndex))
                sections.get(sectionIndex).addObjectIndex(i);
            else {
                newSection = new Section();
                newSection.addObjectIndex(i);
                sections.put(sectionIndex, newSection);
            }
        }
        initSectionIndexesInList();
    }

    private void initSectionIndexesInList() {
        int sectionPositionInList = 0;
        for (int key : sections.keySet()) {
            Section section = sections.get(key);
            section.setListIndex(sectionPositionInList);
            sectionPositionInList += section.getSize() + 1;
        }
    }

    DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }
    };

    /*@Override
    public void registerDataSetObserver(DataSetObserver observer) {
        if(wrappedAdapter != null) {
            wrappedAdapter.registerDataSetObserver(observer);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if(wrappedAdapter!=null) {
            wrappedAdapter.unregisterDataSetObserver(observer);
        }
    }*/

    @Override
    public int getCount() {
        return sections.size() + wrappedAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private int getSectionIndexInList(int position) {
        int sectionIndex;
        for (int key : sections.keySet()) {
            sectionIndex = sections.get(key).getIndexOfSectionInList();
            if (sectionIndex == position)
                return key;
            else if (position < sectionIndex)
                return -1;
        }
        return -1;
    }

    private int getActualPosition(int position) {
        Section currSection;
        int currSectionSize;
        for(int key : sections.keySet()) {
            position--;
            currSection = sections.get(key);
            currSectionSize = currSection.getSize();

            if(position < currSectionSize) {
                return currSection.getObjectIndex(position);
            }

            position -= currSectionSize;
        }
        return -1;
    }

    @Override
    public void notifyDataSetChanged() {
        initSections();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int sectionIndex = getSectionIndexInList(position);

        if (sectionIndex == -1) {
            return wrappedAdapter.getView(getActualPosition(position),convertView, parent);
        } else {
            return getSectionView(sectionIndex, getSectionText(sectionIndex),convertView, parent);
        }

    }

    protected abstract View getSectionView(int sectionIndex, String sectionText, View convertView , ViewGroup parent);

    class Section {

        private int listIndex;
        private List<Integer> objectIndexes;

        public Section() {
            objectIndexes = new ArrayList<>();
        }

        public void addObjectIndex(int objectIndex) {
            objectIndexes.add(objectIndex);
        }

        public int getObjectIndex(int position) {
            return objectIndexes.get(position);
        }

        public void setListIndex(int listIndex) {
            this.listIndex = listIndex;
        }

        public int getIndexOfSectionInList() {
            return listIndex;
        }

        public int getSize() {
            return objectIndexes.size();
        }
    }

}
