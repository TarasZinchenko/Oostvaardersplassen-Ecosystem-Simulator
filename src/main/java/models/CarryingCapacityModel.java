package models;

import environment.GrassHeightData;

public class CarryingCapacityModel {

    private static final int TOTAL_FORAGE_KG = 15000000; // Total forage in kg/year
    private static final double COMPETITION_FACTOR_BASE = 0.85; // 15% reduction for competition
    public static final double BASE_GRASS_HEIGHT = 28.79; // Grass height in 1998 (cm)
    private GrassHeightData grassHeightData; // New instance variable to hold grass height data

    // Constructor to initialize both EnvironmentalFactors and GrassHeightData
    public CarryingCapacityModel(GrassHeightData grassHeightData) {
        this.grassHeightData = grassHeightData;  // Initialize grassHeightData
    }

    // Static method to calculate carrying capacity based on various parameters
    public static int calculateCarryingCapacity(int dailyNeedKg, double allocationPercentage, double grassHeight, double competitionFactor) {
        double relativeForageFactor = grassHeight / BASE_GRASS_HEIGHT; // Adjust by grass height
        int annualNeedPerIndividual = dailyNeedKg * 365;
        int allocatedForage = (int) (TOTAL_FORAGE_KG * allocationPercentage * relativeForageFactor);
        return (int) ((allocatedForage / (double) annualNeedPerIndividual) * competitionFactor);
    }

    // Instance method to get carrying capacity by using the GrassHeightData instance
    public int getCarryingCapacity(int dailyNeedKg, double allocationPercentage, int year) {
        // Get the grass height for the given year using the instance of GrassHeightData
        double grassHeight = grassHeightData.getGrassHeight(year); 
        double competitionFactor = calculateDynamicCompetitionFactor();
        return calculateCarryingCapacity(dailyNeedKg, allocationPercentage, grassHeight, competitionFactor);
    }

    // Method to calculate a dynamic competition factor
    private double calculateDynamicCompetitionFactor() {
        // Example logic for dynamic competition factor
        return COMPETITION_FACTOR_BASE * 0.9; // Adjust based on other species population, grass height, etc.
    }

    // Project the population over a certain number of years
    public int projectPopulation(int initialPopulation, double growthRate, double carryingCapacity, int years) {
        double population = initialPopulation;
        for (int i = 0; i < years; i++) {
            population += growthRate * population * (1 - (population / carryingCapacity));
        }
        return (int) Math.max(population, 0);
    }

    // Get the relative forage factor based on the year
    public double getRelativeForageFactor(int year) {
        // Use the instance variable 'environmentalFactors' to access grass height
        double grassHeight = grassHeightData.getGrassHeight(year); 
        return grassHeight / BASE_GRASS_HEIGHT;
    }
}