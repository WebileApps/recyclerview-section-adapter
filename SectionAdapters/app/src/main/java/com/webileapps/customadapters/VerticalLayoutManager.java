package com.webileapps.customadapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PraveenKatha on 17/07/15.
 */
public class VerticalLayoutManager extends RecyclerView.LayoutManager {

    private int currentOffset;
    private int nextTop;
    private SparseIntArray childTops;
    private int startVisibleIndex;
    private int lastVisibleIndex;

    public VerticalLayoutManager() {
        nextTop = 0;
        currentOffset = 0;
        childTops = new SparseIntArray();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        detachAndScrapAttachedViews(recycler);
        initChildren(recycler, state);

    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    private void initChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        fillGrid2(recycler, state, 0);
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        super.onItemsChanged(recyclerView);
        nextTop = 0;
        currentOffset = 0;
        childTops = new SparseIntArray();
        removeAllViews();
    }

    private void fillGrid(RecyclerView.Recycler recycler, RecyclerView.State state, int scrollDistance) {

        SparseArray<View> viewCache = new SparseArray<>();


        for (int i = 0; i < getChildCount(); i++) {
            viewCache.put(i + startVisibleIndex, getChildAt(i));
        }

        for (int i = 0; i < viewCache.size(); i++) {
            detachView(viewCache.valueAt(i));
        }

        currentOffset += scrollDistance;
        startVisibleIndex = getCurrentStartIndex();
        int totalCount = getItemCount();

        int childHeight;
        int childWidth;

        int top = childTops.get(startVisibleIndex);
        int left = 0;
        int right;
        int bottom;
        int lastItemTop = currentOffset + getActualLayoutHeight();


        int i;

        System.out.println("Child Is Pre Layout " + state.isPreLayout());

        for (i = startVisibleIndex; i < totalCount && top <= lastItemTop; i++) {

            View child = viewCache.get(i);
            if (child == null) {
                child = recycler.getViewForPosition(i);
                measureChildWithMargins(child, 0, 0);
                childHeight = getDecoratedMeasuredHeight(child);
                childWidth = getDecoratedMeasuredWidth(child);
                right = left + childWidth;
                bottom = top + childHeight;
                addView(child);
                childTops.put(i, top);
                layoutDecorated(child, left, top - currentOffset, right, bottom - currentOffset);
                System.out.println("Adding Child " + i + " Top " + child.getTop() + " Bottom " + child.getBottom());
                top += childHeight;
            } else {
                childHeight = getDecoratedMeasuredHeight(child);
                viewCache.remove(i);
                attachView(child);
                System.out.println("Reattaching Child " + i + " Top " + child.getTop() + " Bottom " + child.getBottom());
                top += childHeight;
            }
        }

        System.out.println("Child tops " + childTops);
        System.out.println("Child --------------------------------------------------------");

        lastVisibleIndex = --i;
        viewCache.clear();

        for(i = 0; i < viewCache.size() ; i++) {
            recycler.recycleView(viewCache.valueAt(i));
        }

    }


    private void fillGrid2(RecyclerView.Recycler recycler, RecyclerView.State state, int scrollDistance) {


        detachAndScrapAttachedViews(recycler);

        int top;
        int left = 0;
        int right;
        int bottom;

        int prevOffset = currentOffset;
        currentOffset += scrollDistance;
        startVisibleIndex = getCurrentStartIndex();

        int childHeight;
        int childWidth;


        int lastItemTop = currentOffset + getActualLayoutHeight();

        int totalCount = getItemCount();

        int i;
        View child;

        top = childTops.get(startVisibleIndex);

        System.out.println("Child Is Pre Layout " + state.isPreLayout());

        for (i = startVisibleIndex; i < totalCount && top <= lastItemTop; i++) {

            child = recycler.getViewForPosition(i);
            measureChildWithMargins(child, 0, 0);
            childHeight = getDecoratedMeasuredHeight(child);
            childWidth = getDecoratedMeasuredWidth(child);
            right = left + childWidth;
            bottom = top + childHeight;
            addView(child);
            childTops.put(i, top);
            layoutDecorated(child, left, top - prevOffset, right, bottom - prevOffset);
            System.out.println("Adding Child " + i + " Top " + child.getTop() + " Bottom " + child.getBottom());
            top += childHeight;

        }

        System.out.println("Child tops " + childTops);
        System.out.println("Child --------------------------------------------------------");

        lastVisibleIndex = --i;

    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        int scrollDistance;

        System.out.println("-------- Scrolling -------- " + dy);

        if (dy < 0 && startVisibleIndex == 0) {
            scrollDistance = Math.max(dy, -currentOffset);
        } else if (dy > 0 && lastVisibleIndex == getItemCount() - 1) {
            int layoutHeight = getActualLayoutHeight();
            int lastChildBottom = getDecoratedBottom(getChildAt(getChildCount() -1));
            scrollDistance = Math.min(dy, lastChildBottom - layoutHeight);
        } else {
            scrollDistance = dy;
        }

        fillGrid2(recycler, state, scrollDistance);

        offsetChildrenVertical(-scrollDistance);
        System.out.println("Child 0 top "+getChildAt(0).getTop());
        return dy;
    }

    private int getCurrentStartIndex() {

        for (int i = 0; i < childTops.size(); i++) {
            if (currentOffset == childTops.valueAt(i))
                return i;
            else if (currentOffset < childTops.valueAt(i))
                return i - 1;
        }

        return 0;
    }


    public int getActualLayoutWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public int getActualLayoutHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }
}
