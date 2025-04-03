package org.example;

import org.example.args.ArgumentsParser;
import org.example.index.CsvIndex;
import org.example.index.IndexBuilder;
import org.example.query.QueryProcessor;
import org.example.result.JsonResultWriter;
import org.example.result.SearchResult;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        long start = System.nanoTime();

        ArgumentsParser arguments = new ArgumentsParser(args);
        if (!arguments.isValid()) {
            System.err.println("Ошибка: Не все аргументы переданы.");
            System.exit(1);
        }

        try {
            CsvIndex index = IndexBuilder.build(arguments.getDataFile(), arguments.getIndexedColumnId());
            QueryProcessor qp = new QueryProcessor(index, arguments.getDataFile(), arguments.getIndexedColumnId());
            List<SearchResult> results = qp.processQueries(arguments.getInputFile());
            long init = (System.nanoTime() - start) / 1_000_000;
            JsonResultWriter.write(arguments.getOutputFile(), init, results);
        } catch (IOException e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }
}