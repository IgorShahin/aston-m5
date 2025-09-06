package ru.aston.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class AdapterDemo {
    private static final Logger log = LoggerFactory.getLogger(AdapterDemo.class);

    public static void main(String[] args) throws Exception {
        Path csvPath = resolveCsvPath(args);

        CsvReader reader = new LegacyCsvAdapter(new LegacyCsvLib());
        List<List<String>> rows = reader.read(csvPath);

        rows.forEach(r -> log.info("Row: {}", r));
    }

    private static Path resolveCsvPath(String[] args) throws Exception {
        Path path;
        if (args != null && args.length > 0) {
            path = Paths.get(args[0]).toAbsolutePath().normalize();
            log.info("Using CSV file from args: [{}]", path);
        } else {
            path = Files.createTempFile("sample-", ".csv");
            String demo = """
                    id;name;email
                    1;Игорь;igor@example.com
                    2;Мария;maria@example.com
                    """;
            Files.writeString(path, demo);
            log.info("Created temp demo CSV: [{}]", path);
        }

        if (!Files.exists(path)) throw new IllegalArgumentException("File not found: " + path);
        if (!Files.isReadable(path)) throw new IllegalArgumentException("File is not readable: " + path);

        return path;
    }
}