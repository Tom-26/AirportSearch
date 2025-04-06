package org.example.query;

import org.example.index.CsvIndex;
import org.example.index.IndexBuilder;
import org.example.result.SearchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для QueryProcessor")
public class QueryProcessorTest {

    @Test
    @DisplayName("Должен вернуть совпадающие ID, когда префикс найден")
    void shouldReturnMatchingIds_whenQueryMatchesPrefix() throws IOException {
        Path csvFile = Files.createTempFile("test", ".csv");
        Files.writeString(csvFile, "1,\"Boston\"\n2,\"Bowman\"\n3,\"Atlanta\"\n");

        CsvIndex index = IndexBuilder.build(csvFile.toString(), 2);

        Path inputFile = Files.createTempFile("input", ".txt");
        Files.writeString(inputFile, "Bos\nBow");

        QueryProcessor processor = new QueryProcessor(index, csvFile.toString(), 2);
        List<SearchResult> results = processor.processQueries(inputFile.toString());

        assertEquals(2, results.size());
        assertEquals(List.of(1), results.get(0).getResult());
        assertEquals(List.of(2), results.get(1).getResult());

        Files.deleteIfExists(csvFile);
        Files.deleteIfExists(inputFile);
    }

    @Test
    @DisplayName("Должен вернуть пустой список, если совпадений нет")
    void shouldReturnEmptyList_whenNoMatchFound() throws IOException {
        Path csvFile = Files.createTempFile("test", ".csv");
        Files.writeString(csvFile, "1,\"Paris\"\n");

        CsvIndex index = new CsvIndex();
        index.add("Paris", 0L);

        Path inputFile = Files.createTempFile("input", ".txt");
        Files.writeString(inputFile, "XYZ");

        QueryProcessor processor = new QueryProcessor(index, csvFile.toString(), 2);
        List<SearchResult> results = processor.processQueries(inputFile.toString());

        assertEquals(1, results.size());
        assertTrue(results.get(0).getResult().isEmpty());

        Files.deleteIfExists(csvFile);
        Files.deleteIfExists(inputFile);
    }

    @Test
    @DisplayName("Должен игнорировать строки CSV с недостающими колонками")
    void shouldHandleMalformedCsvLines_gracefully() throws IOException {
        Path csvFile = Files.createTempFile("test_csv", ".csv");
        // первая строка невалидная (одна колонка), вторая валидная
        String csvContent = "\"1\"\n\"2\",\"Bowman\"\n";
        long validOffset = "\"1\"\n".getBytes().length;
        Files.writeString(csvFile, csvContent);

        Path inputFile = Files.createTempFile("input_txt", ".txt");
        Files.writeString(inputFile, "Bow");

        CsvIndex index = IndexBuilder.build(csvFile.toString(), 2);
        System.out.println("Ожидаемый оффсет второй строки: " + validOffset);
        System.out.println("DEBUG: Индекс после IndexBuilder:");
        index.getTailMap("Bow").forEach((k, v) -> System.out.println(k + " -> " + v));

        QueryProcessor processor = new QueryProcessor(index, csvFile.toString(), 2);
        List<SearchResult> results = processor.processQueries(inputFile.toString());

        System.out.println("Результаты поиска: " + results.get(0).getResult());
        assertEquals(1, results.size());
        assertEquals(List.of(2), results.get(0).getResult());

        Files.deleteIfExists(csvFile);
        Files.deleteIfExists(inputFile);
    }
}