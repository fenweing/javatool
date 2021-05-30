package com.parrer.util;

import org.apache.commons.lang.ArrayUtils;

import java.util.*;

/**
 * @description: CollectionUtil
 * @version: 1.0.0
 */
public final class CollectionUtil {
    public static String ASC = "asc";
    public static String DESC = "desc";

    private CollectionUtil() {
    }

    public static <T> ArrayList<T> ofList(T... ts) {
        ArrayList<T> arrayList = new ArrayList<>();
        if (ts == null || ts.length <= 0) {
            return arrayList;
        }
        for (T t : ts) {
            arrayList.add(t);
        }
        return arrayList;
    }

    public static <T> LinkedList<T> ofLinkedList(T... ts) {
        return new LinkedList<>(ofList(ts));
    }

    public static <T> HashSet<T> ofHashSet(T... ts) {
        return new HashSet<>(ofList(ts));
    }

    public static <T> List<T> sort(List<T> src, Comparator<T> comparator) {
        if (src == null || src.size() <= 0) {
            return src;
        }
        Collections.sort(src, comparator);
        return src;
    }

    public static <T> List<T> reverse(List<T> src) {
        if (src == null || src.size() <= 0) {
            return src;
        }
        Collections.reverse(src);
        return src;
    }

    public static <T> Map<String, T> toMap(List<T> src, StrValueGetter<T> columnStrValue) {
        Map<String, T> resMap = new HashMap<>();
        if (isEmpty(src)) {
            return resMap;
        }
        for (T item : src) {
            resMap.put(columnStrValue.get(item), item);
        }
        return resMap;
    }

    public static <T> Map<String, List<T>> groupMap(List<T> src, StrValueGetter<T> columnStrValue) {
        Map<String, List<T>> resMap = new HashMap<>();
        if (isEmpty(src)) {
            return resMap;
        }
        for (T item : src) {
            String key = columnStrValue.get(item);
            if (!resMap.containsKey(key)) {
                resMap.put(key, new ArrayList<T>());
            }
            resMap.get(key).add(item);
        }
        return resMap;
    }

    public static <T> List<T> operate(List<T> src, Operate<T> operate) {
        if (isEmpty(src)) {
            return src;
        }
        List<T> resList = new ArrayList<>(src.size());
        for (T item : src) {
            resList.add(operate.operate(item));
        }
        return resList;
    }


    public static <V, T> List<V> toValueList(List<T> src, ValueGetter<T> valueGetter) {
        List resList = new ArrayList<>();
        if (isEmpty(src)) {
            return resList;
        }
        for (T item : src) {
            resList.add(valueGetter.get(item));
        }
        return resList;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() <= 0;
    }

    public static <T> List<T> remove(List<T> src, Condition<T> condition) {
        if (isEmpty(src)) {
            return src;
        }
        List<T> resList = new ArrayList<>();
        for (T item : src) {
            if (!condition.get(item)) {
                resList.add(item);
            }
        }
        return resList;
    }

    public static <T> List<T> removeDuplicate(List<T> src, List<T> remove, ValueGetter<T> valueGetter) {
        if (isEmpty(src) || isEmpty(remove)) {
            return src;
        }
        Map<Object, T> removeMap = new HashMap<>(remove.size());
        for (T rem : remove) {
            removeMap.put(valueGetter.get(rem), rem);
        }
        List<T> resList = new ArrayList<>();
        for (T item : src) {
            if (!removeMap.containsKey(valueGetter.get(item))) {
                resList.add(item);
            }
        }
        return resList;
    }

    public static Object[] nullToEmptyString(Object[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return array;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = "";
            }
        }
        return array;
    }

    public static List<Object> nullToEmptyString(List<Object> src) {
        if (isEmpty(src)) {
            return src;
        }
        for (int i = 0; i < src.size(); i++) {
            if (src.get(i) == null) {
                src.set(i, "");
            }
        }
        return src;
    }

    public interface ValueGetter<T> {
        Object get(T t);
    }

    public interface StrValueGetter<T> {
        String get(T t);
    }

    public interface Condition<T> {
        Boolean get(T t);
    }

    public interface Operate<T> {
        T operate(T t);
    }
}
