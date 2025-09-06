package ru.aston.adapter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class LegacyCsvLib {
    public String loadWholeFile(Path file, Charset charset) throws IOException {
        return Files.readString(file, charset);
    }
}