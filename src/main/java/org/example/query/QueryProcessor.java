package org.example.query;

import org.example.index.CsvIndex;

import org.example.result.SearchResult;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QueryProcessor {
    private final CsvIndex index;
    private final String dataFile;
    private final int columnId;

    public QueryProcessor(CsvIndex index, String dataFile, int columnId) {
        this.index = index;
        this.dataFile = dataFile;
        this.columnId = columnId;
    }

    public List<SearchResult> processQueries(String inputPath) throws IOException {
        List<String> queries = Files.readAllLines(Path.of(inputPath));
        List<SearchResult> results = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(dataFile, "r")) {
            for (String query : queries) {
                long start = System.nanoTime();
                List<Integer> ids = new ArrayList<>();

                for (var entry : index.getTailMap(query).entrySet()) {
                    if (!entry.getKey().startsWith(query)) break;
                    for (Long pos : entry.getValue()) {
                        raf.seek(pos);
                        String line = raf.readLine();
                        String[] parts = line.split(",", -1);
                        if (parts.length < columnId) continue;
                        String val = parts[columnId - 1].replace("\"", "");
                        if (val.startsWith(query)) {
                            ids.add(Integer.parseInt(parts[0].replace("\"", "")));
                        }
                    }
                }

                ids.sort(Integer::compareTo);
                long time = (System.nanoTime() - start) / 1_000_000;
                results.add(new SearchResult(query, ids, time));
            }
        }

        return results;
    }
}