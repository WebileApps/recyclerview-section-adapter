package com.webileapps.customadapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PraveenKatha on 13/07/15.
 */
public abstract class SectionAdapter extends BaseAdapter {

    private final BaseAdapter wrappedAdapter;
    private Map<Integer, Section> sections;
    private List<Integer> sectionIndices;

    /**
     * Get the index of section to which the object belongs
     * @param object data item associated with the row inflation
     * @return returns the index of section
     */
    protected abstract int getIndexOfSection(Object object);

    /**
     * Get the section header text of section having specified section index
     * @param sectionIndex
     * @return Returns Section header text
     */
    protected abstract String getSectionText(int sectionIndex);

    /**
     * Returns View corresponding to section header corresponding to section having specified section index
     *
     * @param sectionIndex The position of section among the sections associated with adapter
     * @param sectionText  Section text associated with section
     * @param convertView The old view to reuse if possible
     * @param parent The parent view that this view will attach to
     * @return View corresponding to section header
     */

    protected abstract View getSectionView(int sectionIndex, String sectionText, View convertView, ViewGroup parent);


    /**
     *  Constructs Section Adapter
     * @param wrappedAdapter listAdapter A {@link BaseAdapter} that has to be sectioned.
     */
    public SectionAdapter(BaseAdapter wrappedAdapter) {
        this.wrappedAdapter = wrappedAdapter;
        wrappedAdapter.registerDataSetObserver(mObserver);
        initSections();
    }


    private void initSections() {
        //actualDataSize = wrappedAdapter.getCount();
        int sectionIndex;
        Section newSection;
        if (sections != null) {
            sections.clear();
        }
        sections = new HashMap<>();
        for (int i = 0; i < wrappedAdapter.getCount(); i++) {
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

        if(sectionIndices!=null)
            sectionIndices.clear();

        sectionIndices = new ArrayList<>(sections.keySet());
        Collections.sort(sectionIndices);

        int sectionPositionInList = 0;
        for (int key : sectionIndices) {
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

        for (int key : sectionIndices) {
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
        for (int key : sectionIndices) {
            position--;
            currSection = sections.get(key);
            currSectionSize = currSection.getSize();

            if (position < currSectionSize) {
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
            return wrappedAdapter.getView(getActualPosition(position), convertView, parent);
        } else {
            return getSectionView(sectionIndex, getSectionText(sectionIndex), convertView, parent);
        }

    }

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
