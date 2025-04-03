package org.example.result;

import java.util.List;

public class SearchResult {
    public String search;
    public List<Integer> result;
    public long time;

    public SearchResult(String search, List<Integer> result, long time) {
        this.search = search;
        this.result = result;
        this.time = time;
    }
}