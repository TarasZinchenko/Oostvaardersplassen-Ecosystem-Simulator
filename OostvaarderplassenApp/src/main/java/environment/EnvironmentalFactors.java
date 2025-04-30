package environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import environment.AnimalsData.AnimalRecord;
import javafx.scene.chart.XYChart;

public class EnvironmentalFactors {
    private static final String POPULATION_FILE_PATH = "src/main/resources/animals_on_january_1st.json";
    private static final String GRASS_HEIGHT_FILE_PATH = "src/main/resources/grass_height.json";

    private final XYChart.Series<Number, Number> populationSeries;

    private ObjectMapper objectMapper;
    public List<AnimalRecord> animalRecords;
    private Map<Integer, GrassHeightData.GrassRecord> grassRecordsByYear;


    public EnvironmentalFactors() {
        objectMapper = new ObjectMapper();

        this.populationSeries = new XYChart.Series<>();
        this.populationSeries.setName("Total Population");

        loadAnimalRecords();
        loadGrassRecords();
        // Validate and update the chart
        validateAnimalRecords();
    }


    public XYChart.Series<Number, Number> getPopulationSeries() {
        return this.populationSeries;
    }

    public List<AnimalRecord> getAnimalRecords() {
        return this.animalRecords;
    }

    public void loadAnimalRecords() {
        try {
            AnimalsData animalsData = objectMapper.readValue(new File(POPULATION_FILE_PATH), AnimalsData.class);
            if (animalsData != null && animalsData.getAnimalRecords() != null) {
                this.animalRecords = animalsData.getAnimalRecords();
    
                // Update the chart data after loading records
                updatePopulationSeries();
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.animalRecords = List.of(); // Initialize empty list to avoid null pointer exceptions
        }
    }
    
    
    private void loadGrassRecords() {
        try {
            // Deserialize GrassHeightData from JSON
            GrassHeightData grassHeightData = objectMapper.readValue(new File(GRASS_HEIGHT_FILE_PATH), GrassHeightData.class);
    
            // Initialize the map for storing grass records by year
            grassRecordsByYear = new HashMap<>();
    
            // Loop through each GrassRecord in GrassHeightData
            for (GrassHeightData.GrassRecord grassRecord : grassHeightData.getGrassRecords()) {
                // Directly add the record into the map (key is the year)
                grassRecordsByYear.put(grassRecord.getYear(), grassRecord);
            }
    
        } catch (IOException e) {
            e.printStackTrace();
            grassRecordsByYear = Map.of(); // Initialize as empty map in case of error
        }
    }
        

    private void updatePopulationSeries() {
        if (populationSeries == null) {
            throw new IllegalStateException("Population series is not initialized.");
        }

        // Clear and add data to the series
        populationSeries.getData().clear();
        for (AnimalsData.AnimalRecord record : animalRecords) {
            populationSeries.getData().add(new XYChart.Data<>(record.getYear(), record.getTotal()));
        }
    }

    private void validateAnimalRecords() {
        if (animalRecords == null || animalRecords.isEmpty()) {
            System.err.println("No animal records available.");
        }
    }

    public int getPopulationForSpecies(String species, int year, boolean wolvesIntroduced) {
        for (AnimalRecord record : animalRecords) {
            if (record.getYear() == year) {
                int population = switch (species.toLowerCase()) {
                    case "cattle" -> record.getHeck_cattle();
                    case "deer" -> record.getRed_deer();
                    case "horses" -> record.getKonik_horses();
                    default -> 0;
                };

                return Math.max(population, 0);
            }
        }
        return 0;
    }
}
