package com.webileapps.volleypoc.com.webileapps.volleypoc.utils;

/**
 * Created by PraveenKatha on 09/07/15.
 */
public class Url {
    String url;

    public Url(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }


    public static Url buildUrl(String baseUrl) {
        return new Url(baseUrl);
    }

    public  Url addParameter(String key,String value) {
        url += "&" + key + "=" + value;
        return this;
    }

}