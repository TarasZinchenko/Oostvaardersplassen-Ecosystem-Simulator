package gui;

import environment.GrassGrowthModel;
import environment.GrassGrowthModel.GrassRecord;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class GrassChart {

    public static void showGrassChart() {
        Stage stage = new Stage();

        // X and Y Axis
        NumberAxis xAxis = new NumberAxis("Year", 1990, 2030, 5);
        NumberAxis yAxis = new NumberAxis("Grass Height (cm)", 0, 30, 5);

        xAxis.setTickUnit(1);  // Use integer increments for the ticks
        xAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return object.intValue() + ""; // Convert to integer (years)
            }
        
            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string); // Parse the string back to an integer
            }
        });

        // Line Chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Grass Height Over the Years");

        // Data Series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Grass Height");

        // Load Grass Data
        GrassGrowthModel grassGrowthModel = new GrassGrowthModel();
        for (GrassRecord record : grassGrowthModel.getGrassRecords()) {
            series.getData().add(new XYChart.Data<>(record.getYear(), record.getGrassHeight()));
        }

        // Add Series to Chart
        lineChart.getData().add(series);

        // Show Chart
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("See the grass height over the year");
        stage.show();
    }
}
