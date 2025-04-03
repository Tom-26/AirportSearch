package org.example.result;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.result.SearchResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonResultWriter {
    private static class FullResult {
        public long initTime;
        public List<SearchResult> result;

        public FullResult(long initTime, List<SearchResult> result) {
            this.initTime = initTime;
            this.result = result;
        }
    }

    public static void write(String outputPath, long initTime, List<SearchResult> results) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(outputPath), new FullResult(initTime, results));
    }
}