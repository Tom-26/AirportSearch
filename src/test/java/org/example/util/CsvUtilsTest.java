package org.example.util;

import org.example.util.CsvUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для утильных методов обработки CSV")
public class CsvUtilsTest {

    @Test
    @DisplayName("Должен удалить кавычки, когда они присутствуют")
    void shouldRemoveQuotes_whenQuotesPresent() {
        Assertions.assertEquals("abc", CsvUtils.stripQuotes("\"abc\""));
        assertEquals("abc", CsvUtils.stripQuotes("a\"b\"c"));
    }

    @Test
    @DisplayName("Должен вернуть ту же строку, когда кавычек нет")
    void shouldReturnSameString_whenNoQuotes() {
        assertEquals("a-b-c", CsvUtils.stripQuotes("a-b-c"));
    }

    @Test
    @DisplayName("Должен вернуть пустую строку, когда передан null")
    void shouldReturnEmptyString_whenNullPassed() {
        assertEquals("", CsvUtils.stripQuotes(null));
    }
}