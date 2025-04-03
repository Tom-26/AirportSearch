package org.example.index;

import java.io.IOException;
import java.io.RandomAccessFile;

public class IndexBuilder {
    public static CsvIndex build(String csvPath, int columnId) throws IOException {
        CsvIndex index = new CsvIndex();
        try (RandomAccessFile raf = new RandomAccessFile(csvPath, "r")) {
            long offset = raf.getFilePointer();
            String line;
            while ((line = raf.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < columnId) continue;
                String val = parts[columnId - 1].replace("\"", "");
                String prefix = val.length() > 6 ? val.substring(0, 6) : val;
                index.add(prefix, offset);
                offset = raf.getFilePointer();
            }
        }
        return index;
    }
}