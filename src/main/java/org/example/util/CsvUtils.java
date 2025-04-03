package org.example.util;

public class CsvUtils {
    /**
     * Удаляет все двойные кавычки из строки.
     * Возвращает пустую строку, если вход равен null.
     */
    public static String stripQuotes(String s) {
        return s == null ? "" : s.replace("\"", "");
    }
}