package util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionUtilTest {
    @Test
    public void testMergeMapsWhenMapsAreEmpty() {
        // Given
        Map<Integer, Set<String>> map1 = new HashMap<>();
        Map<Integer, Set<String>> map2 = new HashMap<>();

        // When
        Map<Integer, Set<String>> actual = CollectionUtil.mergeMaps(map1, map2);

        // Then
        Map<Integer, Set<String>> expected = new HashMap<>();
        assertEquals(expected, actual, "Empty maps cannot be merged");
    }

    @Test
    public void testMergeMapsWhenThereAreNoCommonKeys() {
        // Given
        Map<Integer, Set<String>> map1 = new HashMap<>();
        map1.put(1, createHobbitSet());

        Map<Integer, Set<String>> map2 = new HashMap<>();
        map2.put(2, createBadDudeSet());

        // When
        Map<Integer, Set<String>> actual = CollectionUtil.mergeMaps(map1, map2);

        // Then
        Map<Integer, Set<String>> expected = new HashMap<>();
        expected.put(1, createHobbitSet());
        expected.put(2, createBadDudeSet());

        assertEquals(expected, actual);
    }

    @Test
    public void testMergeMapsWhenThereAreCommonKeys() {
        // Given
        Map<Integer, Set<String>> map1 = new HashMap<>();
        map1.put(1, createHobbitSet());

        Map<Integer, Set<String>> map2 = new HashMap<>();
        map2.put(1, createBadDudeSet());

        // When
        Map<Integer, Set<String>> actual = CollectionUtil.mergeMaps(map1, map2);

        // Then
        Map<Integer, Set<String>> expected = new HashMap<>();
        expected.put(1, createLotrSet());

        assertEquals(expected, actual);
    }

    @Test
    public void testCombineWhenSetsAreEmpty() {
        // Given
        Set<Set<Boolean>> set = new HashSet<>();

        // When
        Set<Boolean> actual = CollectionUtil.combine(set);

        // Then
        Set<Boolean> expected = new HashSet<>();
        assertEquals(expected, actual);
    }

    @Test
    public void testCombineWhenSetsAreNotEmpty() {
        // Given
        Set<Set<String>> set = new HashSet<>();
        set.add(createHobbitSet());
        set.add(createLotrSet());

        // When
        Set<String> actual = CollectionUtil.combine(set);

        // Then
        Set<String> expected = createLotrSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testAddToMapWhenKeyDoesNotExist() {
        // Given
        Map<String, Set<String>> actual = new HashMap<>();

        // When
        CollectionUtil.addToMap(actual, "Spiders", "Shelob");

        // Then
        Map<String, Set<String>> expected = new HashMap<>();
        Set<String> expectedSet = new HashSet<>();
        expectedSet.add("Shelob");
        expected.put("Spiders", expectedSet);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddToMapWhenKeyExists() {
        // Given
        Map<String, Set<String>> actual = new HashMap<>();
        actual.put("Hobbits", createHobbitSet());

        // When
        CollectionUtil.addToMap(actual, "Hobbits", "Samwise");

        // Then
        Map<String, Set<String>> expected = new HashMap<>();
        Set<String> expectedSet = createHobbitSet();
        expectedSet.add("Samwise");
        expected.put("Hobbits", expectedSet);

        assertEquals(expected, actual);
    }

    /**
     * @return a set containing "Bilbo" and "Frodo"
     */
    private Set<String> createHobbitSet() {
        Set<String> hobbits = new HashSet<>();
        hobbits.add("Bilbo");
        hobbits.add("Frodo");
        return hobbits;
    }

    /**
     * @return a set containing "Sauron" and "Gollum"
     */
    private Set<String> createBadDudeSet() {
        Set<String> badDudes = new HashSet<>();
        badDudes.add("Sauron");
        badDudes.add("Gollum");
        return badDudes;
    }

    /**
     * @return a set containing "Bilbo", "Frodo", "Sauron" and "Gollum"
     */
    private Set<String> createLotrSet() {
        Set<String> lotr = new HashSet<>();
        lotr.add("Bilbo");
        lotr.add("Frodo");
        lotr.add("Sauron");
        lotr.add("Gollum");
        return lotr;
    }
}
