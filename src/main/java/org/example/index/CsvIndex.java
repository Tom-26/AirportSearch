package org.example.index;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CsvIndex {
    private final TreeMap<String, List<Long>> prefixMap = new TreeMap<>();

    public void add(String prefix, long offset) {
        prefixMap.computeIfAbsent(prefix, k -> new ArrayList<>()).add(offset);
    }

    public NavigableMap<String, List<Long>> getTailMap(String prefix) {
        return prefixMap.tailMap(prefix, true);
    }
}