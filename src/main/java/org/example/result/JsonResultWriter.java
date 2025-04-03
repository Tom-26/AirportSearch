package org.example.result;

import org.example.ObjectMapperProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonResultWriter {
    public static void write(String outputPath, long initTime, List<SearchResult> results) throws IOException {
        ObjectMapperProvider.get().writeValue(new File(outputPath), new FullResult(initTime, results));
    }
}