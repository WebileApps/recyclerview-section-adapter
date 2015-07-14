package com.webileapps.volleypoc.com.webileapps.volleypoc.activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.webileapps.volleypoc.R;
import com.webileapps.volleypoc.com.webileapps.volleypoc.Fragments.WebViewActivityFragment;
import com.webileapps.volleypoc.com.webileapps.volleypoc.adapter.CourseAdapter;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.EndlessRecyclerOnScrollListener;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.GenericVolleyErrorListener;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.URLConstants;
import com.webileapps.volleypoc.com.webileapps.volleypoc.request.GsonRequest;
import com.webileapps.volleypoc.com.webileapps.volleypoc.response.CourseResponse;
import com.webileapps.volleypoc.com.webileapps.volleypoc.singleton.VolleySingleton;
import com.webileapps.volleypoc.com.webileapps.volleypoc.utils.VolleyErrorHelper;


/**
 *
 *
 */
public class MainActivity extends ActionBarActivity {

    private SuperRecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (SuperRecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        VolleySingleton volleySingleton = VolleySingleton.getInstance(this.getApplicationContext());

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        };


        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
//                Toast.makeText(MainActivity.this, o.toString(), Toast.LENGTH_SHORT).show();
                CourseResponse courseResponse = (CourseResponse) o;
                mAdapter = new CourseAdapter(courseResponse.courses);
                mRecyclerView.setAdapter(mAdapter);
            }
        };


        mRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                // do something...

            }
        });
        mRecyclerView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                // Fetch more from Api or DB
            }
        }, 10);
        mRecyclerView.setupSwipeToDismiss(new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int i) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] ints) {

            }
        });
        GsonRequest<CourseResponse> courseGsonRequest =
                new GsonRequest(URLConstants.COURSES_URL, CourseResponse.class,
                        null, responseListener, new GenericVolleyErrorListener(MainActivity.this) {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        //custom
                    }
                });

        volleySingleton.addToRequestQueue(courseGsonRequest);

    }


}
