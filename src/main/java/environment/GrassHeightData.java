package environment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GrassHeightData {

    @JsonProperty("grass_height")
    private List<GrassRecord> grassRecords; // No duplication

    private Map<Integer, GrassRecord> grassRecordsByYear;

    // Constructor
    public GrassHeightData() {
        this.grassRecordsByYear = new HashMap<>();
    }

    // Method to populate the map when setting grass records
    public void setGrassRecords(List<GrassRecord> grassRecords) {
        this.grassRecords = grassRecords;

        // Populate the map with the grass records
        grassRecordsByYear.clear();  // Clear existing entries if any
        for (GrassRecord record : grassRecords) {
            grassRecordsByYear.put(record.getYear(), record);
        }
    }

    // Get the grass height for a specific year
    public double getGrassHeight(int year) {
        GrassRecord record = grassRecordsByYear.get(year);
        return record != null ? record.getGrassHeight() : 0.0;
    }

    // Getter for grassRecords
    public List<GrassRecord> getGrassRecords() {
        return grassRecords;
    }

    // Static inner class for GrassRecord
    public static class GrassRecord {
        private int year;
        @JsonProperty("Grass height (cm) on August 1st")
        private double grassHeight;

        // Getters and setters
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
