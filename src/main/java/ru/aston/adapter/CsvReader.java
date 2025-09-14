package ru.aston.adapter;

import java.nio.file.Path;
import java.util.List;

public interface CsvReader {
    List<List<String>> read(Path path) throws CsvReadException;
}
