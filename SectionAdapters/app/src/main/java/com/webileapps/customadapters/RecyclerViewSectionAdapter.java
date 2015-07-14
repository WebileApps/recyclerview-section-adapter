package com.webileapps.customadapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PraveenKatha on 13/07/15.
 */
public abstract class RecyclerViewSectionAdapter<RowAdapter extends RecyclerView.Adapter & RecyclerViewSectionAdapter.GetObject> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.AdapterDataObserver mDataObserver;
    RecyclerView.Adapter mSectionHeaderAdapter;
    RowAdapter mRowAdapter;
    private Map<Integer, Section> sections;

    public final int SECTION_HEADER_VIEW_TYPE = 0;
    public final int ROW_VIEW_TYPE = 1;
    private List<Integer> sectionIndices;


    public abstract int getIndexOfSection(Object object);

    public RecyclerViewSectionAdapter(RecyclerView.Adapter mSectionHeaderAdapter, RowAdapter mListAdapter) {

        this.mSectionHeaderAdapter = mSectionHeaderAdapter;
        this.mRowAdapter = mListAdapter;
        this.mDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                initSections();
                notifyDataSetChanged();
            }
        };

        this.mRowAdapter.registerAdapterDataObserver(mDataObserver);
        initSections();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SECTION_HEADER_VIEW_TYPE:
                return mSectionHeaderAdapter.onCreateViewHolder(parent, viewType);
            case ROW_VIEW_TYPE:
                return mRowAdapter.onCreateViewHolder(parent, viewType);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case SECTION_HEADER_VIEW_TYPE:
                mSectionHeaderAdapter.onBindViewHolder(holder, getSectionPositionInList(position));
                break;
            case ROW_VIEW_TYPE:
                mRowAdapter.onBindViewHolder(holder, getRowActualPosition(position));
                break;
        }
    }

    private int getSectionPositionInList(int position) {
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

    private int getRowActualPosition(int position) {
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

    private void initSections() {
        int sectionIndex;
        Section newSection;
        if (sections != null) {
            sections.clear();
        }
        sections = new HashMap<>();
        for (int i = 0; i < mRowAdapter.getItemCount(); i++) {
            sectionIndex = getIndexOfSection(mRowAdapter.getObject(i));
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
        sectionIndices = new ArrayList<>(sections.keySet());
        Collections.sort(sectionIndices);
        for (int key : sectionIndices) {
            Section section = sections.get(key);
            section.setListIndex(sectionPositionInList);
            sectionPositionInList += section.getSize() + 1;
        }
    }

    @Override
    public int getItemCount() {
        return mRowAdapter.getItemCount() + sections.size();
    }


    @Override
    public int getItemViewType(int position) {
        return getSectionPositionInList(position) == -1 ? ROW_VIEW_TYPE : SECTION_HEADER_VIEW_TYPE ;
    }

    public interface GetObject {
        Object getObject(int position);
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
