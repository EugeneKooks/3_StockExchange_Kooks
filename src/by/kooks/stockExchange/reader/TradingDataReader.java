package by.kooks.stockExchange.reader;

import by.kooks.stockExchange.exception.WrongDataException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TradingDataReader {
    public static ArrayList<String> readData(String path) throws WrongDataException {
        ArrayList<String> data = new ArrayList<>();
        try {
            data.addAll(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new WrongDataException("File reading failure, " + e.getMessage());
        }
        return data;
    }
}
