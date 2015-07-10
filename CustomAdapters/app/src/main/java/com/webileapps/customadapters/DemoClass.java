package com.webileapps.customadapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PraveenKatha on 10/07/15.
 */
public class DemoClass {

    static Map<Integer,Integer> map = new HashMap<>();

    public static int totalCountMAXORS(List<Integer> list) {
        processMAXORS(list);

        int count = 0;

        for(Integer val : map.values()) {
            System.out.println("Val "+val);
            count += factorial(val) / (factorial(val -2) * factorial(2));
        }
        return count;
    }

    public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }


    public static List<Integer> processMAXORS(List<Integer> list) {

        if(list.size() == 1) {
            return list;
        }

        int x = list.get(0);

        List<Integer> xors = processMAXORS(list.subList(1, list.size() - 1));

        for(int k : xors) {
            int xorVal = x ^ k;
            xors.add(xorVal);
            if(map.containsKey(xorVal)){
                map.put(xorVal, map.get(xorVal) + 1);
            }
        }

        return xors;
    }

}
