package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataManager {
    private static final String FILE_PATH = "player.json";
    private static final Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
            .create();

    public static void savePlayerData(Player player) throws IOException {
        String json = gson.toJson(player);
        Files.write(Paths.get(FILE_PATH), json.getBytes());
    }

    public static Player loadPlayerData() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
        return gson.fromJson(json, Player.class);
    }
}