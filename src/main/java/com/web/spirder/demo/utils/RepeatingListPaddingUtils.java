package com.web.spirder.demo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author hezifeng
 * @create 2023/3/31 15:12
 */
public class RepeatingListPaddingUtils {
    public RepeatingListPaddingUtils() {
    }

    public static <T> List<T> padToSize(List<T> list, int size) {
        if (list == null) {
            return Collections.emptyList();
        } else if (!list.isEmpty() && list.size() < size) {
            ArrayList<T> paddedList = new ArrayList(size);
            paddedList.addAll(list);
            T firstElem = list.get(0);

            for(int i = list.size(); i < size; ++i) {
                paddedList.add(firstElem);
            }

            return paddedList;
        } else {
            return list;
        }
    }
}
