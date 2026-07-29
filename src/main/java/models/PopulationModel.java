package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulationModel {

    // Parameters for the models
    private final double rDeer = 0.1; // Growth rate for deer
    private final double rHorse = 0.1; // Growth rate for horses
    private final double rCattle = 0.1; // Growth rate for cattle

    // Competition coefficients between species
    private final double alphaDH = 0.01; // Competition coefficient between deer and horses
    private final double alphaDC = 0.01; // Competition coefficient between deer and cattle
    private final double alphaHD = 0.01; // Competition coefficient between horses and deer
    private final double alphaHC = 0.01; // Competition coefficient between horses and cattle
    private final double alphaCD = 0.01; // Competition coefficient between cattle and deer
    private final double alphaCH = 0.01; // Competition coefficient between cattle and horses

    // Carrying capacities
    private final double deerK = 1698; // Carrying capacity for deer
    private final double horseK = 679; // Carrying capacity for horses
    private final double cattleK = 113; // Carrying capacity for cattle

    // Growth and carrying capacity for the species
    private final int initialDeer = 3200; // Initial population for deer
    private final int initialHorse = 1250; // Initial population for horses
    private final int initialCattle = 250; // Initial population for cattle

    // Method to simulate population dynamics over time
    public Map<String, List<Double>> predictPopulation(int years) {
        Map<String, List<Double>> populationData = new HashMap<>();
    
        // Initialize lists for annual populations
        List<Double> deerPopulation = new ArrayList<>();
        List<Double> horsePopulation = new ArrayList<>();
        List<Double> cattlePopulation = new ArrayList<>();
    
        // Initial populations
        double currentDeer = initialDeer;
        double currentHorse = initialHorse;
        double currentCattle = initialCattle;
    
        // Calculate populations for each year
        for (int year = 0; year < years; year++) {
            // Growth calculations for each species
            double deerGrowth = rDeer * currentDeer * (1 - (currentDeer + alphaDH * currentHorse + alphaDC * currentCattle) / deerK);
            double horseGrowth = rHorse * currentHorse * (1 - (currentHorse + alphaHD * currentDeer + alphaHC * currentCattle) / horseK);
            double cattleGrowth = rCattle * currentCattle * (1 - (currentCattle + alphaCD * currentDeer + alphaCH * currentHorse) / cattleK);
    
            // Update populations, ensuring no negative values
            currentDeer = Math.max(currentDeer + deerGrowth, 0);
            currentHorse = Math.max(currentHorse + horseGrowth, 0);
            currentCattle = Math.max(currentCattle + cattleGrowth, 0);
    
            // Add current populations to their respective lists
            deerPopulation.add(currentDeer);
            horsePopulation.add(currentHorse);
            cattlePopulation.add(currentCattle);
        }
    
        // Store population data in the map
        populationData.put("Deer", deerPopulation);
        populationData.put("Horse", horsePopulation);
        populationData.put("Cattle", cattlePopulation);
    
        return populationData;
    }
    


    // Method to simulate the deer population only
    public double[] simulateDeerPopulation(int initialPopulation, int years) {
        return simulateSpeciesPopulation(initialPopulation, rDeer, deerK, years);
    }

    // Method to simulate the horse population only
    public double[] simulateHorsePopulation(int initialPopulation, int years) {
        return simulateSpeciesPopulation(initialPopulation, rHorse, horseK, years);
    }

    // Method to simulate the cattle population only
    public double[] simulateCattlePopulation(int initialPopulation, int years) {
        return simulateSpeciesPopulation(initialPopulation, rCattle, cattleK, years);
    }

    // Helper method for species population growth
    private double[] simulateSpeciesPopulation(int initialPopulation, double r, double K, int years) {
        double[] populationOverTime = new double[years];
        populationOverTime[0] = initialPopulation;

        for (int t = 1; t < years; t++) {
            double currentPopulation = populationOverTime[t - 1];
            double growth = r * currentPopulation * (1 - currentPopulation / K);
            populationOverTime[t] = Math.max(currentPopulation + growth, 0);
        }
        return populationOverTime;
    }
}
