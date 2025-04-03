package org.example.model;

import java.util.List;

public class ResultEntry {
    public String search;
    public List<Integer> result;
    public long time;

    public ResultEntry(String search, List<Integer> result, long time) {
        this.search = search;
        this.result = result;
        this.time = time;
    }
}
