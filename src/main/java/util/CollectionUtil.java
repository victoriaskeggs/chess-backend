package util;

import java.util.*;

public class CollectionUtil {

    /**
     * Merges two generic maps together.
     * @param map1
     * @param map2
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, Set<V>> mergeMaps(Map<K, Set<V>> map1, Map<K, Set<V>> map2) {
        // Construct merged map from map1
        Map<K, Set<V>> merged = new HashMap<>(map1);

        // Add elements in map2 to merged map
        for (K key: map2.keySet()) {
            Set<V> values = map2.get(key);
            if (merged.containsKey(key)) {
                merged.get(key).addAll(values);
            } else {
                merged.put(key, values);
            }
        }
        return merged;
    }

    /**
     * Combines all the values in a collection of sets into a single set.
     * @param collection
     * @param <V>
     * @return
     */
    public static <V> Set<V> combine(Collection<Set<V>> collection) {
        Set<V> combined = new HashSet<>();
        for (Set<V> set : collection) {
            combined.addAll(set);
        }
        return combined;
    }

    /**
     * Adds a key and value to a map of keys to a set of values. If the key does not exist, puts the key and
     * a new set containing the value in the map. If the key does exist, adds the value to the set mapped
     * against the key.
     * @param map
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, Set<V>> addToMap(Map<K, Set<V>> map, K key, V value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            Set<V> set = new HashSet<>();
            set.add(value);
            map.put(key, set);
        }
        return map;
    }

    /**
     * Retrieves the present items from a collection of Optionals.
     * @param items
     * @param <T>
     * @return
     */
    public static <T> Set<T> getPresent(Collection<Optional<T>> items) {
        Set<T> present = new HashSet<>();
        for (Optional<T> item : items) {
            if (item.isPresent()) {
                present.add(item.get());
            }
        }
        return present;
    }
}
