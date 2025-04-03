package org.example.index;

import org.example.util.CsvUtils;

import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexBuilder {
    public static CsvIndex build(String csvPath, int columnId) throws IOException {
        if (columnId <= 0) {
            throw new IllegalArgumentException("columnId должен быть положительным числом");
        }

        CsvIndex index = new CsvIndex();
        try (RandomAccessFile raf = new RandomAccessFile(csvPath, "r")) {
            long offset = raf.getFilePointer();
            String line;
            while ((line = raf.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < columnId) continue;
                String val = CsvUtils.stripQuotes(parts[columnId - 1]);
                String prefix = val.length() > 6 ? val.substring(0, 6) : val;
                index.add(prefix, offset);
                offset = raf.getFilePointer();
            }
        }
        return index;
    }
}