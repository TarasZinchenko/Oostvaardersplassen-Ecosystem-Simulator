package interactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static models.CarryingCapacityModel.calculateCarryingCapacity;

public class PredatorPreyInteraction {
    // Growth rates (you can set these if needed)
    static double rDeer = 0.1737;   // Deer growth rate
    static double rHorse = 0.15;    // Horse growth rate
    static double rCattle = 0.1;    // Cattle growth rate
    double competitionFactor = 0.85;

    // Carrying capacities (calculated elsewhere)
    static int deerK = calculateCarryingCapacity(10, 0.5, 28, 0.85);  // Deer
    static int horseK = calculateCarryingCapacity(20, 0.4, 28, 0.85); // Horses
    static int cattleK = calculateCarryingCapacity(30, 0.1, 28, 0.85); // Cattle

    // Predation dynamics
    public static double a = 0.01;  // Predation rate (wolves on prey)
    public static double b = 0.001; // Conversion efficiency
    static double d = 0.5;   // Wolf death rate

    // Initial populations
    public static double NDeer = 3200;    // Deer population
    public static double NHorse = 1250;   // Horse population
    public static double NCattle = 250;   // Cattle population
    public static double NWolves = 5;     // Wolf population
    public static int totalTimeSteps = 17; // Number of time steps

    // Method to simulate predator-prey population dynamics
    public Map<String, List<Double>> predictPreyPredatorPopulation(double currentDeer, double currentHorse, double currentCattle, double currentWolves) {
        Map<String, List<Double>> populationData = new HashMap<>();

        // Initialize lists for annual populations
        List<Double> deerPopulation = new ArrayList<>();
        List<Double> horsePopulation = new ArrayList<>();
        List<Double> cattlePopulation = new ArrayList<>();
        List<Double> wolfPopulation = new ArrayList<>();

        // Simulate dynamics over the time period
        for (int t = 0; t < totalTimeSteps; t++) {
            // Add current populations to lists
            deerPopulation.add(currentDeer);
            horsePopulation.add(currentHorse);
            cattlePopulation.add(currentCattle);
            wolfPopulation.add(currentWolves);

            // Predation dynamics
            currentDeer = Math.max(currentDeer - a * currentDeer * currentWolves, 0);
            currentHorse = Math.max(currentHorse - a * currentHorse * currentWolves, 0);
            currentCattle = Math.max(currentCattle - a * currentCattle * currentWolves, 0);

            // Wolf population dynamics
            double wolfGrowth = a * (currentDeer + currentHorse + currentCattle) * currentWolves - d * currentWolves;
            currentWolves = Math.max(currentWolves + wolfGrowth, 0);

            // Propose a more realistic carrying capacity for wolves
            double deerPerWolfFactor = 250;   // 1 wolf needs around 250 deer a year ideally (considering deer is the main prey)
            double horsePerWolfFactor = 1000; // Horses being less common prey
            double cattlePerWolfFactor = 800; // Similar consideration for cattle dynamically

            double wolfCarryingCapacity = (currentDeer / deerPerWolfFactor) + (currentHorse / horsePerWolfFactor) + (currentCattle / cattlePerWolfFactor);

            currentWolves = Math.min(currentWolves, wolfCarryingCapacity);
        }

        // Store population data in the map
        populationData.put("Deer", deerPopulation);
        populationData.put("Horse", horsePopulation);
        populationData.put("Cattle", cattlePopulation);
        populationData.put("Wolves", wolfPopulation);

        return populationData;
    }
}
