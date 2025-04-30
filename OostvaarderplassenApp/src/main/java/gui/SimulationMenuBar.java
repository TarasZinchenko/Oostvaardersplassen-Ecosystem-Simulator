package gui;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class SimulationMenuBar extends MenuBar {
    public SimulationMenuBar() {
        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load");
        fileMenu.getItems().addAll(saveItem, loadItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);

        getMenus().addAll(fileMenu, helpMenu);

        saveItem.setOnAction(e -> System.out.println("Save clicked"));
        loadItem.setOnAction(e -> System.out.println("Load clicked"));
        aboutItem.setOnAction(e -> System.out.println("About clicked"));
    }
}
