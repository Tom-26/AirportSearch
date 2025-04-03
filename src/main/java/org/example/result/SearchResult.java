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

    public String getSearch() {
        return search;
    }

    public List<Integer> getResult() {
        return result;
    }

    public long getTime() {
        return time;
    }
}