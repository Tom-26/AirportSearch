package org.example.args;

import org.example.args.ArgumentsParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для парсера аргументов командной строки")
public class ArgumentsParserTest {

    @Test
    @DisplayName("Корректный разбор всех аргументов")
    void shouldParseAllArgumentsCorrectly_whenAllArgumentsAreValid() {
        String[] args = {
                "--data", "airports.csv",
                "--indexed-column-id", "2",
                "--input-file", "input.txt",
                "--output-file", "result.json"
        };
        ArgumentsParser parser = new ArgumentsParser(args);
        assertTrue(parser.isValid());
        assertEquals("airports.csv", parser.getDataFile());
        assertEquals("input.txt", parser.getInputFile());
        assertEquals("result.json", parser.getOutputFile());
        assertEquals(2, parser.getIndexedColumnId());
    }

    @Test
    @DisplayName("Исключение при некорректном значении columnId")
    void shouldThrowException_whenColumnIdIsNotANumber() {
        String[] args = {
                "--data", "airports.csv",
                "--indexed-column-id", "abc",
                "--input-file", "input.txt",
                "--output-file", "result.json"
        };
        ArgumentsParser parser = new ArgumentsParser(args);
        assertThrows(IllegalArgumentException.class, parser::getIndexedColumnId);
    }

    @Test
    @DisplayName("Некорректный набор аргументов — isValid() == false")
    void shouldReturnFalse_whenNotAllArgumentsProvided() {
        String[] args = {
                "--data", "airports.csv",
                "--input-file", "input.txt"
        };
        ArgumentsParser parser = new ArgumentsParser(args);
        assertFalse(parser.isValid());
    }
}