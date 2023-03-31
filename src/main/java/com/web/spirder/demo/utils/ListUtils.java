package com.web.spirder.demo.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author hezifeng
 * @create 2023/3/31 15:12
 */
public class ListUtils {
    private ListUtils() {
    }

    public static <T> Stream<List<T>> batching(Collection<T> list, int batchSize) {
        return batching(list, batchSize, false);
    }

    public static <T> Stream<List<T>> batching(Collection<T> collection, int batchSize, boolean padToBatchSize) {
        int listSize = collection.size();
        if (listSize == 0) {
            return Stream.empty();
        } else {
            int numBatches = (int)Math.ceil((double)((float)listSize / (float)batchSize));
            ArrayList<List<T>> batches = new ArrayList(numBatches);
            IntStream.range(0, numBatches).forEach((b) -> {
                int start = b * batchSize;
                int end = (b + 1) * batchSize;
                if (end > listSize) {
                    end = listSize;
                }

                Object list;
                if (collection instanceof List) {
                    list = (List)collection;
                } else {
                    list = new ArrayList(collection);
                }

                if (padToBatchSize) {
                    batches.add(RepeatingListPaddingUtils.padToSize(((List)list).subList(start, end), batchSize));
                } else {
                    batches.add(((List)list).subList(start, end));
                }

            });
            return batches.stream();
        }
    }

    public static <T, R> List<R> batchExecuteAggregate(Collection<T> collection, int batchSize, Function<List<T>, List<R>> batchFunctor) {
        return batchExecuteAggregate(collection, batchSize, true, batchFunctor);
    }

    public static <T, R> List<R> batchExecuteAggregate(Collection<T> collection, int batchSize, boolean padToBatchSize, Function<List<T>, List<R>> batchFunctor) {
        return (List)batching(collection, batchSize, padToBatchSize).map(batchFunctor).collect(() -> {
            return new ArrayList(collection.size());
        }, List::addAll, List::addAll);
    }

    public static <T, U> Map<T, U> toMap(List<U> list, Function<U, T> keyGetter) {
        return toMap(list, keyGetter, Function.identity());
    }

    public static <K, V, E> Map<K, V> toMap(List<E> list, Function<E, K> keyGetter, Function<E, V> valueGetter) {
        return (Map)list.stream().filter((i) -> {
            return Objects.nonNull(valueGetter.apply(i));
        }).collect(Collectors.toMap(keyGetter, valueGetter, (u, u2) -> {
            return u2;
        }, () -> {
            return new HashMap(list.size() * 2);
        }));
    }

    public static <T, U> List<T> pluck(@Nullable Collection<U> collection, @NonNull Function<U, T> pluckFunction) {
        return collection == null ? null : (List)collection.stream().map(pluckFunction).collect(() -> {
            return new ArrayList(collection.size());
        }, ArrayList::add, ArrayList::addAll);
    }

    public static <T, U> Set<T> pluckIntoSet(Collection<U> collection, Function<U, T> pluckFunction) {
        return collection == null ? null : (Set)collection.stream().map(pluckFunction).collect(() -> {
            return new HashSet(collection.size() * 2);
        }, HashSet::add, AbstractCollection::addAll);
    }

    public static <T, U> Map<T, List<U>> groupElementsByKey(Collection<U> collection, Function<U, T> keyGetter) {
        return (Map)collection.stream().collect(Collectors.groupingBy(keyGetter));
    }

    public static <T> Collector<T, List<T>, List<T>> makeListCollector(int initialCapacity) {
        return Collector.of(() -> {
            return new ArrayList(initialCapacity);
        }, List::add, (l, r) -> {
            l.addAll(r);
            return l;
        });
    }

    public static <T, U, V> Map<T, List<V>> groupByKeyAndMappingValue(Collection<U> collection, Function<U, T> keyGetter, Function<U, V> valueElementGetter) {
        return (Map)collection.stream().collect(Collectors.groupingBy(keyGetter, Collectors.mapping(valueElementGetter, Collectors.toList())));
    }
}
