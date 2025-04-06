package org.example.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты для построителя индекса из CSV")
public class IndexBuilderTest {

    @Test
    @DisplayName("Индекс успешно строится из валидного CSV")
    void shouldBuildIndexWithValidCsvInput() throws IOException {
        Path tempFile = Files.createTempFile("test", ".csv");
        Files.writeString(tempFile, "1,\"Boston\"\n2,\"Bowman\"\n3,\"Atlanta\"\n");

        CsvIndex index = IndexBuilder.build(tempFile.toString(), 2);
        assertFalse(index.getTailMap("Bo").isEmpty());

        Files.deleteIfExists(tempFile);
    }

    @Test
    @DisplayName("Исключение при columnId = 0")
    void shouldThrowExceptionWhenColumnIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> IndexBuilder.build("fake.csv", 0));
    }
}