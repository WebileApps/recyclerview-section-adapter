package com.webileapps.customadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PraveenKatha on 08/07/15.
 */
public abstract class SimpleSectionAdapter<T> extends BaseAdapter {

    private final int sectionTvId;
    private final int sectionLayoutId;
    private final LayoutInflater inflater;
    private Map<Integer, Section> sections;
    private int actualDataSize;


    protected abstract int getIndexOfSection(T object);

    protected abstract String getSectionText(int sectionIndex);

    protected abstract View getListItemView(T object, ViewGroup parent);


    public SimpleSectionAdapter(Context context, List<T> data, int sectionLayoutId, int sectionTvId) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sectionLayoutId = sectionLayoutId;
        this.sectionTvId = sectionTvId;
        initSections(data);
    }

    public SimpleSectionAdapter(Context context, int sectionLayoutId, int sectionTvId) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.sectionLayoutId = sectionLayoutId;
        this.sectionTvId = sectionTvId;
        sections = new HashMap<>();
    }

    public void setData(List<T> data) {
        initSections(data);
        notifyDataSetChanged();
    }

    private void initSections(List<T> data) {
        actualDataSize = data.size();
        int sectionIndex;
        Section newSection;
        sections = new HashMap<>();
        for (T object : data) {
            sectionIndex = getIndexOfSection(object);
            if (sections.containsKey(sectionIndex))
                sections.get(sectionIndex).addObject(object);
            else {
                newSection = new Section();
                newSection.addObject(object);
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


    @Override
    public int getCount() {
        return sections.size() + actualDataSize;
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

    private T getObject(int position) {
        Section currSection;
        Section prevSection = null;
        for(int key : sections.keySet()) {
            currSection = sections.get(key);
            if(currSection.getIndexOfSectionInList() > position)
                break;
            prevSection = currSection;
        }
        return prevSection.getObject(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int section = getSectionIndexInList(position);

        /*if (convertView != null) {
            return convertView;
        }*/

        if (section == -1) {
            return getListItemView(getObject(position), parent);
        } else {
            return getSectionView(section, parent);
        }

    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    private View getSectionView(int sectionIndex, ViewGroup parent) {

        View sectionView = inflater.inflate(sectionLayoutId, parent, false);
        TextView tv = (TextView) sectionView.findViewById(sectionTvId);
        tv.setText(getSectionText(sectionIndex));

        return sectionView;
    }

    class Section {

        private List<T> sectionData;
        private int listIndex;

        public Section() {
            sectionData = new ArrayList<>();
        }

        public void addObject(T object) {
            sectionData.add(object);
        }

        public List<T> getSectionData() {
            return sectionData;
        }

        public int getSize() {
            return sectionData.size();
        }

        public T getObjectInSection (int index) {
            return sectionData.get(index);
        }

        public T getObject(int indexInList) {
            return getObjectInSection(indexInList - listIndex -1);
        }

        public void setListIndex(int listIndex) {
            this.listIndex = listIndex;
        }

        public int getIndexOfSectionInList() {
            return listIndex;
        }
    }

}
