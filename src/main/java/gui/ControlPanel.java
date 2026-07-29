package gui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class ControlPanel extends VBox {

    private boolean usingWolves;
    private Graphs graphs;

    public ControlPanel(Graphs graphs) {
        this.graphs = graphs;
        this.usingWolves = true; // Start with using wolves for the prediction
        System.out.println("ControlPanel initialized!");


        // "Show Grass Height Chart" Button
        Button showGrassChartButton = new Button("Show Grass Height Chart");
        showGrassChartButton.setOnAction(e ->{
            System.out.println("Show Grass Chart button clicked!");
             GrassChart.showGrassChart();
        });

        // "Recalculate without wolves" Button
        Button recalculateButton = new Button("Recalculate without Wolves");
        recalculateButton.setOnAction(e -> {
            // Toggle model: with wolves or without wolves
            usingWolves = !usingWolves;

            // Update button text and recalculate population dynamics
            recalculateButton.setText(usingWolves ? "Recalculate without Wolves" : "Recalculate with Wolves");
            graphs.updatePopulationModel(usingWolves);
        });

        Button plotWolvesButton = new Button("Plot Wolves Population");
        plotWolvesButton.setOnAction(e -> {
            graphs.plotWolvesPopulation();
        });



        // Add controls to the layout
        getChildren().addAll(
            showGrassChartButton,
            recalculateButton,
            plotWolvesButton
        );

        
        // // Add a button to simulate population with wolves
        // Button simulateButton = new Button("Simulate Population with Wolves");
        // simulateButton.setOnAction(event -> {
        //     // Call the method in Graphs to start the simulation
        //     graphs.simulatePopulationWithWolves(6); // For example, use year = 10
        // });
    
        // Add the button to the panel
        // this.getChildren().add(simulateButton);
        
    }
}
