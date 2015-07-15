package com.webileapps.volleypoc.com.webileapps.volleypoc.utils;

/**
 * Created by PraveenKatha on 09/07/15.
 */
public class ArrayUtils {

    public static String join(String[] array, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : array) {
            sb.append(str);
            sb.append(delimiter);
        }
        return sb.toString();
    }

}
