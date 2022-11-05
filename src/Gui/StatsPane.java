package Gui;

import Controller.Controller;
import Model.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.HashMap;

public class StatsPane extends GridPane implements Observer {

    private final ComboBox<Category> cbCategories = new ComboBox<>();
    private final ComboBox<Arrangement> cbArrangements = new ComboBox<>();
    private final ListView<String> lvwProducts = new ListView<>();
    private final DatePicker datePickerFrom = new DatePicker();
    private final DatePicker datePickerTo = new DatePicker();

    public StatsPane() {

        this.setPadding(new Insets(25));
        this.setVgap(10);
        this.setHgap(10);

        cbArrangements.setPromptText("Salgssituation");
        cbArrangements.getItems().addAll(Controller.getArrangements());
        cbArrangements.setMinSize(150, 25);
        cbArrangements.setOnAction(event -> selectionChanged());

        cbCategories.setPromptText("Produkt kategori");
        cbCategories.getItems().addAll(Controller.getCategories());
        cbCategories.setMinSize(150, 25);
        cbCategories.setOnAction(event -> selectionChanged());

        this.add(datePickerTo, 3, 1);
        this.add(datePickerFrom, 4, 1);
        this.add(cbArrangements, 1, 1);
        this.add(cbCategories, 2, 1);
        this.add(lvwProducts, 1, 2, 4, 4);

    }


    private void selectionChanged() {
        lvwProducts.getItems().clear();
        Category selectedCategory = cbCategories.getSelectionModel().getSelectedItem();
        Arrangement selectedArrangement = cbArrangements.getSelectionModel().getSelectedItem();
        LocalDate from = (LocalDate) datePickerFrom.getUserData();
        LocalDate to = (LocalDate) datePickerTo.getUserData();
        if (selectedCategory == null || selectedArrangement == null){
            return;
        }
        HashMap<ProductComponent, Integer> map = addProducts(selectedCategory, selectedArrangement);

        if (!map.isEmpty())
            lvwProducts.getItems().addAll(map.keySet() + ", " + map.values());
    }

    private HashMap<ProductComponent, Integer> addProducts(Category selectedCategory, Arrangement selectedArrangement){
        HashMap<ProductComponent, Integer> map = new HashMap<>();
        if (selectedArrangement != null) {
            for (Order order : Controller.getOrders()) {
                HashMap<ProductComponent, Integer> tempMap = order.countSoldProduct(selectedCategory, selectedArrangement);
                if (!tempMap.isEmpty()) {
                    for (ProductComponent product : tempMap.keySet()){
                        if (map.containsKey(product)){
                            map.put(product, map.get(product) + tempMap.get(product));
                        }
                        else
                            map.put(product, tempMap.get(product));
                    }
                }
            }
        } else
            lvwProducts.getItems().clear();

        return map;
    }


    @Override
    public void update() {

    }
}
