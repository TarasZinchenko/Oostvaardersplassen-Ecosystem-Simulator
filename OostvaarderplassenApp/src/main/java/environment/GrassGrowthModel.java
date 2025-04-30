package environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GrassGrowthModel {
    private static final String GRASS_DATA_FILE_PATH = "src/main/resources/grass_height.json";
    private final List<GrassRecord> grassRecords;

    // Constructor
    public GrassGrowthModel() {
        this.grassRecords = loadGrassData();
    }
    private List<GrassRecord> loadGrassData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Dacă JSON-ul are un obiect la rădăcină, cu cheia 'grass_height'
            JsonNode rootNode = objectMapper.readTree(new File(GRASS_DATA_FILE_PATH));
            JsonNode grassHeightNode = rootNode.get("grass_height");

            // Deserializăm array-ul de recorduri din JSON
            return objectMapper.convertValue(grassHeightNode, new TypeReference<List<GrassRecord>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Getter pentru grassRecords
    public List<GrassRecord> getGrassRecords() {
        return grassRecords;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    // Clasa internă pentru GrassRecord
    public static class GrassRecord {
        @JsonProperty("year")
        private int year;

        @JsonProperty("Grass height (cm) on August 1st")
        private double grassHeight;

        // Getters și Setters
        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public double getGrassHeight() {
            return grassHeight;
        }

        public void setGrassHeight(double grassHeight) {
            this.grassHeight = grassHeight;
        }
    }
}
