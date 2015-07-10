package com.webileapps.volleypoc.com.webileapps.volleypoc.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import javax.xml.transform.ErrorListener;

/**
 * Created by venkatadinesh on 06/07/15.
 */
public class GenericVolleyErrorListener implements Response.ErrorListener {

     Context context;

    public GenericVolleyErrorListener(Context context)
    {
        this.context = context;
    }



    @Override
    public void onErrorResponse(VolleyError volleyError) {
      String msg= VolleyErrorHelper.getMessage(volleyError,context);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
