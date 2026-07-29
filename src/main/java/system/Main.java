package system;

import environment.EnvironmentalFactors;
import gui.ControlPanel;
import gui.Graphs;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    private EnvironmentalFactors factors;

    public static void main(String[] args) {
        launch(args);  // Properly initializes the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Main start method invoked!");

        // Initialize EnvironmentalFactors (required for Graphs)
        factors = new EnvironmentalFactors();

        // UI Layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10)); // Add padding to the layout

        // Graphs: Center of the BorderPane
        Graphs graphs = new Graphs(factors);  // Pass `factors` to the constructor
        root.setCenter(graphs.getLineChart()); // Get the LineChart from Graphs class

        // Control Panel: Right side of the BorderPane
        ControlPanel controlPanel = new ControlPanel(graphs); // Control panel interacts with Graphs class
        root.setRight(controlPanel);

        // Scene and Stage
        Scene scene = new Scene(root, 800, 600); // Set the scene size
        primaryStage.setTitle("Ecosystem Simulator"); // Title of the window
        primaryStage.setScene(scene);
        primaryStage.show(); // Display the JavaFX stage
    }
}
