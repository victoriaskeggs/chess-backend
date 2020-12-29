package testutil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    /**
     * Converts an array into a set
     * @param elements
     * @param <T>
     * @return set
     */
    public static <T> Set<T> createSet(T[] elements) {
        Set<T> set = new HashSet<>();
        for (T element : elements) {
            set.add(element);
        }
        return set;
    }

    /**
     * Maps keys to corresponding values
     * @param keys
     * @param values
     * @param <K>
     * @param <V>
     * @return map
     */
    public static <K, V> Map<K, V> createMap(K[] keys, V[] values) {
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }
}
