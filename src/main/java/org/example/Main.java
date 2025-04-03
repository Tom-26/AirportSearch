package org.example;

import org.example.args.Arguments;
import org.example.model.ResultEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        long startInit = System.nanoTime();

        Arguments arguments = new Arguments(args);

        if (!arguments.isValid()) {
            System.err.println("Ошибка: Не все аргументы переданы.");
            System.err.println("Ожидается: --data <путь> --indexed-column-id <id> --input-file <путь> --output-file <путь>");
            System.exit(1);
        }

        String dataFile = arguments.get("--data");
        int columnId = Integer.parseInt(arguments.get("--indexed-column-id"));
        String inputFile = arguments.get("--input-file");
        String outputFile = arguments.get("--output-file");

        System.out.println("Файл данных: " + dataFile);
        System.out.println("Индексируемая колонка: " + columnId);
        System.out.println("Файл входных запросов: " + inputFile);
        System.out.println("Файл вывода: " + outputFile);

        try (RandomAccessFile raf = new RandomAccessFile(dataFile, "r")) {
            TreeMap<String, List<Long>> index = new TreeMap<>();
            long offset = raf.getFilePointer();
            String line;

            while ((line = raf.readLine()) != null) {
                String[] parts = line.split(",", -1); // грубый CSV разбор, позже заменим
                if (parts.length < columnId) {
                    offset = raf.getFilePointer();
                    continue;
                }
                String value = parts[columnId - 1].replaceAll("\"", ""); // убрать кавычки
                String prefix = value.length() > 6 ? value.substring(0, 6) : value;
                index.computeIfAbsent(prefix, k -> new ArrayList<>()).add(offset);
                offset = raf.getFilePointer();
            }

            long initTime = (System.nanoTime() - startInit) / 1_000_000;
            System.out.println("Инициализация завершена за " + initTime + " мс");

            List<String> searchQueries = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String query;
                while ((query = reader.readLine()) != null) {
                    if (!query.isBlank()) {
                        searchQueries.add(query.trim());
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла запросов: " + e.getMessage());
                System.exit(1);
            }

            System.out.println("Прочитано запросов: " + searchQueries.size());

            List<ResultEntry> results = new ArrayList<>();

            for (String query : searchQueries) {
                long searchStart = System.nanoTime();
                List<Integer> matchedIds = new ArrayList<>();

                for (var entry : index.tailMap(query, true).entrySet()) {
                    if (!entry.getKey().startsWith(query)) break;
                    for (Long pos : entry.getValue()) {
                        raf.seek(pos);
                        String fullLine = raf.readLine();
                        String[] parts = fullLine.split(",", -1);
                        if (parts.length < columnId) continue;
                        String value = parts[columnId - 1].replaceAll("\"", "");
                        if (value.startsWith(query)) {
                            try {
                                int id = Integer.parseInt(parts[0].replaceAll("\"", ""));
                                matchedIds.add(id);
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }

                matchedIds.sort(Integer::compareTo); // строковая сортировка
                long searchTime = (System.nanoTime() - searchStart) / 1_000_000;
                results.add(new ResultEntry(query, matchedIds, searchTime));
            }

            long totalTime = (System.nanoTime() - startInit) / 1_000_000;
            System.out.println("Поиск завершён. Записей: " + results.size());

            try {
                ObjectMapper mapper = new ObjectMapper();
                File outFile = new File(outputFile);
                mapper.writeValue(outFile, new Object() {
                    public final long initTime = totalTime;
                    public final List<ResultEntry> result = results;
                });
                System.out.println("Результаты записаны в файл: " + outputFile);
            } catch (IOException e) {
                System.err.println("Ошибка при записи JSON-файла: " + e.getMessage());
                System.exit(1);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при чтении CSV-файла: " + e.getMessage());
            System.exit(1);
        }
    }
}