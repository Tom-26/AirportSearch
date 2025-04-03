package org.example;

import org.example.args.ArgumentsParser;
import org.example.exception.FileProcessingException;
import org.example.exception.InvalidArgumentsException;
import org.example.index.CsvIndex;
import org.example.index.IndexBuilder;
import org.example.query.QueryProcessor;
import org.example.result.JsonResultWriter;
import org.example.result.SearchResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        long start = System.nanoTime();

        try {
            ArgumentsParser arguments = new ArgumentsParser(args);
            if (!arguments.isValid()) {
                throw new InvalidArgumentsException("Не все аргументы переданы.");
            }

            String dataFile = arguments.getDataFile();
            String inputFile = arguments.getInputFile();
            String outputFile = arguments.getOutputFile();
            int columnId = arguments.getIndexedColumnId();

            if (!Files.exists(Path.of(dataFile))) {
                throw new FileProcessingException("CSV-файл не найден: " + dataFile);
            }

            if (!Files.exists(Path.of(inputFile))) {
                throw new FileProcessingException("Файл запросов не найден: " + inputFile);
            }

            CsvIndex index = IndexBuilder.build(dataFile, columnId);
            QueryProcessor qp = new QueryProcessor(index, dataFile, columnId);
            List<SearchResult> results = qp.processQueries(inputFile);
            long init = (System.nanoTime() - start) / 1_000_000;
            JsonResultWriter.write(outputFile, init, results);

            logger.info("initTime: " + init + " ms");
            logger.info("result size: " + results.size());

        } catch (InvalidArgumentsException | FileProcessingException | IOException e) {
            logger.log(Level.SEVERE, "Ошибка: " + e.getMessage(), e);
            System.exit(1);
        }
    }
}