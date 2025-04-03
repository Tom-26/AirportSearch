package org.example.result;

import java.util.List;

public class FullResult {
    public long initTime;
    public List<SearchResult> result;

    public FullResult(long initTime, List<SearchResult> result) {
        this.initTime = initTime;
        this.result = result;
    }

    public long getInitTime() {
        return initTime;
    }

    public List<SearchResult> getResult() {
        return result;
    }
}