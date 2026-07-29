package gui;

import java.util.List;
import java.util.Map;

import environment.AnimalsData;
import environment.EnvironmentalFactors;
import environment.GrassGrowthModel;
import environment.GrassGrowthModel.GrassRecord;
import interactions.PredatorPreyInteraction;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import models.PopulationModel;

public class Graphs {

    // Chart and series
    private final LineChart<Number, Number> lineChart;
    private final XYChart.Series<Number, Number> populationSeries;
    private final EnvironmentalFactors factors;
    private final PopulationModel populationModel; 
    private final AnimalsData animalsData;
    private static final int totalTimeSteps = 2041;

    public Graphs(EnvironmentalFactors factors) {

        
        // Ensure 'factors' is initialized
        if (factors == null) {
            throw new IllegalArgumentException("EnvironmentalFactors cannot be null");
        }
        this.factors = factors;

        // Initialize the chart and series
        NumberAxis xAxis = new NumberAxis(1990, 2040, 5);
        NumberAxis yAxis = new NumberAxis("Population", 0, 3600, 300);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Population Dynamics");
        

        populationSeries = new XYChart.Series<>();
        populationSeries.setName("Population Over Time");

        // Initialize PopulationModel
        this.animalsData = new AnimalsData(); // Create a new instance of AnimalsData
        this.populationModel = new PopulationModel();  // Create an instance of PopulationModel

        lineChart.getData().add(populationSeries);  // Ensure the series is added to the chart

        // Plot the initial chart with historical data
        loadHistoricalData();
        plotHistoricalData();
    }


    private void loadHistoricalData() {
        List<AnimalsData.AnimalRecord> animalRecords = factors.getAnimalRecords();
        if (animalRecords == null || animalRecords.isEmpty()) {
            System.err.println("Warning: No historical data available.");
        }
    }

    // Method to plot historical data (assuming the data is already loaded)
    private void plotHistoricalData() {
        List<AnimalsData.AnimalRecord> animalRecords = factors.getAnimalRecords();
        if (animalRecords == null || animalRecords.isEmpty()) {
            System.err.println("No historical data available to plot.");
            return;
        }
    
        // Create individual series for each species
        XYChart.Series<Number, Number> redDeerSeries = new XYChart.Series<>();
        redDeerSeries.setName("Red Deer Population");
    
        XYChart.Series<Number, Number> heckCattleSeries = new XYChart.Series<>();
        heckCattleSeries.setName("Heck Cattle Population");
    
        XYChart.Series<Number, Number> konikHorsesSeries = new XYChart.Series<>();
        konikHorsesSeries.setName("Konik Horses Population");
    
        XYChart.Series<Number, Number> greyWolvesSeries = new XYChart.Series<>();
        greyWolvesSeries.setName("Grey Wolves Population");
    
        // Iterate over the records and populate data points
        for (AnimalsData.AnimalRecord data : animalRecords) {
            redDeerSeries.getData().add(new XYChart.Data<>(data.getYear(), data.getRed_deer()));
            heckCattleSeries.getData().add(new XYChart.Data<>(data.getYear(), data.getHeck_cattle()));
            konikHorsesSeries.getData().add(new XYChart.Data<>(data.getYear(), data.getKonik_horses()));
            greyWolvesSeries.getData().add(new XYChart.Data<>(data.getYear(), data.getGrey_wolves()));
        }
    
        // Add all series to the chart
        lineChart.getData().clear(); // Clear existing data to avoid duplication
        lineChart.getData().addAll(redDeerSeries, heckCattleSeries, konikHorsesSeries, greyWolvesSeries);
    }
    

    // Method to update the population model (with or without wolves)
    public void updatePopulationModel(boolean withWolves) {
        // Clear existing data
        populationSeries.getData().clear(); 
        lineChart.getData().clear(); 
    
        // Append simulated data after 2024 based on selected mode
        if (withWolves) {
            plotHistoricalData(); 
            plotPopulationSimulationWithWolves(); 
        } else {
            plotHistoricalData(); 
            plotPopulationSimulationWithoutWolves(); 
        }
    }
    

    // Plot simulated population dynamics with wolves (from 2024 to 2030)
    
    public void plotPopulationSimulationWithWolves() {
        // Fetch initial values from PredatorPreyInteraction
        int years = PredatorPreyInteraction.totalTimeSteps; 
        double initialDeer = PredatorPreyInteraction.NDeer;
        double initialHorse = PredatorPreyInteraction.NHorse;
        double initialCattle = PredatorPreyInteraction.NCattle;
        double initialWolves = PredatorPreyInteraction.NWolves;
    
        // Create model instances
        PopulationModel populationModel = new PopulationModel();
        PredatorPreyInteraction predatorPreyInteraction = new PredatorPreyInteraction();
        
        // Prediction without wolves
        Map<String, List<Double>> preyPopulations = populationModel.predictPopulation(years);
        List<Double> deerPopulations = preyPopulations.get("Deer");
        List<Double> horsePopulations = preyPopulations.get("Horse");
        List<Double> cattlePopulations = preyPopulations.get("Cattle");
    
        // Initialize chart series
        XYChart.Series<Number, Number> deerSeries = new XYChart.Series<>();
        deerSeries.setName("Red Deer");
        XYChart.Series<Number, Number> horseSeries = new XYChart.Series<>();
        horseSeries.setName("Konic Horses");
        XYChart.Series<Number, Number> cattleSeries = new XYChart.Series<>();
        cattleSeries.setName("Heck Cattle");
        XYChart.Series<Number, Number> wolvesSeries = new XYChart.Series<>();
        wolvesSeries.setName("Grey Wolves");
    
        double currentDeer = initialDeer;
        double currentHorse = initialHorse;
        double currentCattle = initialCattle;
        double currentWolves = initialWolves;
    
        for (int year = 0; year < years; year++) {
            double preyDeer = deerPopulations.get(year);
            double preyHorse = horsePopulations.get(year);
            double preyCattle = cattlePopulations.get(year);
    
            Map<String, List<Double>> updatedPopulations = predatorPreyInteraction.predictPreyPredatorPopulation(preyDeer, preyHorse, preyCattle, currentWolves);
    
            if (!updatedPopulations.containsKey("Deer") || !updatedPopulations.containsKey("Horse") || !updatedPopulations.containsKey("Cattle") || !updatedPopulations.containsKey("Wolves")) {
                throw new IllegalStateException("predictPreyPredatorPopulation must return keys: Deer, Horse, Cattle, Wolves");
            }
    
            currentDeer = updatedPopulations.get("Deer").get(year);
            currentHorse = updatedPopulations.get("Horse").get(year);
            currentCattle = updatedPopulations.get("Cattle").get(year);
            currentWolves = updatedPopulations.get("Wolves").get(year);
    
            deerSeries.getData().add(new XYChart.Data<>(2024 + year, currentDeer));
            horseSeries.getData().add(new XYChart.Data<>(2024 + year, currentHorse));
            cattleSeries.getData().add(new XYChart.Data<>(2024 + year, currentCattle));
            wolvesSeries.getData().add(new XYChart.Data<>(2024 + year, currentWolves));
        }

    
        lineChart.getData().clear();
        lineChart.getData().addAll(deerSeries, horseSeries, cattleSeries, wolvesSeries);
    }
    
    
    // Plot simulated population dynamics without wolves (from 2024 to 2030)
    private void plotPopulationSimulationWithoutWolves() {
        // Fetch population predictions for the years 2024 to 2040
        Map<String, List<Double>> populationData = populationModel.predictPopulation(2040 - 2024 + 1);
    
        // Extract population lists for each species
        List<Double> deerPopulation = populationData.get("Deer");
        List<Double> horsePopulation = populationData.get("Horse");
        List<Double> cattlePopulation = populationData.get("Cattle");
    
        // Plot data for each species
        XYChart.Series<Number, Number> deerSeries = new XYChart.Series<>();
        deerSeries.setName("Deer Population (No Wolves)");
    
        XYChart.Series<Number, Number> horseSeries = new XYChart.Series<>();
        horseSeries.setName("Horse Population (No Wolves)");
    
        XYChart.Series<Number, Number> cattleSeries = new XYChart.Series<>();
        cattleSeries.setName("Cattle Population (No Wolves)");
    
        // Add data points for each year (from 2024 onward)
        for (int year = 2024; year <= 2040; year++) {
            int index = year - 2024; // Index in the population list
            deerSeries.getData().add(new XYChart.Data<>(year, deerPopulation.get(index)));
            horseSeries.getData().add(new XYChart.Data<>(year, horsePopulation.get(index)));
            cattleSeries.getData().add(new XYChart.Data<>(year, cattlePopulation.get(index)));
        }
    
        // Add the series to the chart
        lineChart.getData().addAll(deerSeries, horseSeries, cattleSeries);
    }

    public void plotWolvesPopulation() {
        // Set up initial values
        double currentDeer = PredatorPreyInteraction.NDeer;
        double currentHorse = PredatorPreyInteraction.NHorse;
        double currentCattle = PredatorPreyInteraction.NCattle;
        double currentWolves = PredatorPreyInteraction.NWolves; // Initial wolves population
    
        // Create a series for the Grey Wolves population dynamically
        XYChart.Series<Number, Number> wolvesSeries = new XYChart.Series<>();
        wolvesSeries.setName("Grey Wolves Population");
    
        // Simulate wolf population dynamics over the years
        PredatorPreyInteraction predatorPreyInteraction = new PredatorPreyInteraction();
        Map<String, List<Double>> populationData = predatorPreyInteraction.predictPreyPredatorPopulation(currentDeer, currentHorse, currentCattle, currentWolves);
        List<Double> wolfPopulation = populationData.get("Wolves");
    
        // Populate the series with the simulated data
        for (int year = 2024, i = 0; year <= 2040; year++, i++) {
            // Ensure population is within the desired range
            double population = Math.max(Math.min(wolfPopulation.get(i), 10), 1);
            wolvesSeries.getData().add(new XYChart.Data<>(year, population));
        }
    
        // Define axes
        NumberAxis xAxis = new NumberAxis("Year", 2024, 2040, 1);
        NumberAxis yAxis = new NumberAxis("Population", 1, 12, 1);
    
        // Create a new chart for wolves
        LineChart<Number, Number> wolvesChart = new LineChart<>(xAxis, yAxis);
        wolvesChart.setTitle("Grey Wolves Population Dynamics");
    
        // Add the series to the chart
        wolvesChart.getData().add(wolvesSeries);
    
        // Display the chart dynamically in a new window
        Stage wolvesStage = new Stage();
        wolvesStage.setTitle("Grey Wolves Population Dynamics");
        Scene scene = new Scene(wolvesChart, 800, 600);
        wolvesStage.setScene(scene);
        wolvesStage.show();
    }
    

    // Return the chart for displaying in the GUI
    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }

    // Open the grass height chart window
    public static void showGrassChart() {
        Stage stage = new Stage();
        NumberAxis xAxis = new NumberAxis("Year", 1990, 2030, 5);
        NumberAxis yAxis = new NumberAxis("Grass Height (cm)", 0, 30, 5);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Grass Height Over the Years");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Grass Height");

        // Assuming GrassGrowthModel is used for the historical data
        GrassGrowthModel grassGrowthModel = new GrassGrowthModel();
        for (GrassRecord record : grassGrowthModel.getGrassRecords()) {
            System.out.println("Adding to graph: Year: " + record.getYear() + ", Grass Height: " + record.getGrassHeight());
            series.getData().add(new XYChart.Data<>(record.getYear(), record.getGrassHeight()));
        }
        lineChart.getData().clear();
        lineChart.getData().add(series);

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Grass Height Chart");
        stage.show();
    }
}
