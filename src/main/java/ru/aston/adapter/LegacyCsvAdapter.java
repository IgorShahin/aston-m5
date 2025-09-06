package ru.aston.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LegacyCsvAdapter implements CsvReader {
    private static final Logger log = LoggerFactory.getLogger(LegacyCsvAdapter.class);

    private final LegacyCsvLib legacy;
    private final Charset charset;

    public LegacyCsvAdapter(LegacyCsvLib legacy) {
        this(legacy, StandardCharsets.UTF_8);
    }

    public LegacyCsvAdapter(LegacyCsvLib legacy, Charset charset) {
        this.legacy = legacy;
        this.charset = charset;
    }

    @Override
    public List<List<String>> read(Path path) throws CsvReadException {
        validatePath(path);
        try {
            long t0 = System.nanoTime();
            String content = legacy.loadWholeFile(path, charset);
            List<List<String>> table = parse(content);
            long tookMs = java.time.Duration.ofNanos(System.nanoTime() - t0).toMillis(); // без «магических» чисел
            log.info("Read CSV {} rows={} in {} ms", path.getFileName(), table.size(), tookMs);
            return table;
        } catch (Exception e) {
            throw new CsvReadException("Failed to read CSV: " + path, e);
        }
    }

    private void validatePath(Path path) throws CsvReadException {
        if (path == null) throw new CsvReadException("Path is null");
        if (!Files.exists(path)) throw new CsvReadException("File not found: " + path);
        if (!Files.isRegularFile(path)) throw new CsvReadException("Not a regular file: " + path);
        if (!Files.isReadable(path)) throw new CsvReadException("File is not readable: " + path);
    }

    private List<List<String>> parse(String content) {
        String[] lines = content.split("\\R");
        List<List<String>> table = new ArrayList<>(lines.length);
        for (String line : lines) {
            if (line.isEmpty()) {
                table.add(List.of());
                continue;
            }
            String[] cells = line.split(";");
            List<String> row = new ArrayList<>(cells.length);
            for (String cell : cells) {
                row.add(cell.trim());
            }
            table.add(row);
        }
        return table;
    }
}
