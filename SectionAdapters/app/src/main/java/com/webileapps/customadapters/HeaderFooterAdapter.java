package com.webileapps.customadapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by PraveenKatha on 07/08/15.
 */
public class HeaderFooterAdapter extends RecyclerView.Adapter {

    private static final int HEADER_VIEW = 0;
    private static final int ROW_VIEW = 2;
    private static final int FOOTER_VIEW = 1;
    private final RecyclerView.AdapterDataObserver mDataObserver;


    private RecyclerView.Adapter headerAdapter;
    private RecyclerView.Adapter footerAdapter;
    private RecyclerView.Adapter rowAdapter;
    private String TAG = "HeaderFooterAdapter";

    public HeaderFooterAdapter(RecyclerView.Adapter rowAdapter) {
        this.rowAdapter = rowAdapter;

        this.mDataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }
        };

        this.rowAdapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_VIEW:
                return headerAdapter.onCreateViewHolder(parent, viewType);
            case FOOTER_VIEW:
                return footerAdapter.onCreateViewHolder(parent, viewType);
            default:
                return rowAdapter.onCreateViewHolder(parent, viewType - ROW_VIEW);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER_VIEW:
                headerAdapter.onBindViewHolder(holder,position);
                break;
            case FOOTER_VIEW:
                footerAdapter.onBindViewHolder(holder,position);
                break;
            default: {
                rowAdapter.onBindViewHolder(holder, getIndexOfRowInAdapter(position));
                break;
            }
        }
    }

    private int getIndexOfRowInAdapter(int position) {
        if(headerAdapter == null)
            return position;
        else
            return position - 1;
    }

    @Override
    public int getItemViewType(int position) {

        Log.d(TAG,"Position: "+position);

        if (position == 0 && headerAdapter != null) {
            return HEADER_VIEW;
        } else if (position == getItemCount() - 1 && footerAdapter != null) {
            return FOOTER_VIEW;
        }

        return (ROW_VIEW + rowAdapter.getItemViewType(getIndexOfRowInAdapter(position)));

    }

    @Override
    public int getItemCount() {
        return (headerAdapter == null ? 0 : 1) + (rowAdapter == null ? 0 : rowAdapter.getItemCount()) + (footerAdapter == null ? 0 : 1);
    }


    public void setHeaderAdapter(RecyclerView.Adapter headerAdapter) {
        this.headerAdapter = headerAdapter;
    }

    public void setFooterAdapter(RecyclerView.Adapter footerAdapter) {
        this.footerAdapter = footerAdapter;
    }
}
