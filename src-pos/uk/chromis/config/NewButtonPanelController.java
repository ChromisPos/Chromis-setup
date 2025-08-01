/*
**    Chromis Setup Config  - Open Source Point of Sale
**
**    This file is part of Chromis Setup Config Version Chromis V1.5.4
**
**    Copyright (c) 2015-2023 Chromis   
**
**    https://www.chromis.co.uk
**   
**    Chromis POS is free software: you can redistribute it and/or modify
**    it under the terms of the GNU General Public License as published by
**    the Free Software Foundation, either version 3 of the License, or
**    (at your option) any later version.
**
**    Chromis POS is distributed in the hope that it will be useful,
**    but WITHOUT ANY WARRANTY; without even the implied warranty of
**    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**    GNU General Public License for more details.
**
**    You should have received a copy of the GNU General Public License
**    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>
**
 */
package uk.chromis.config;

import static java.lang.Boolean.TRUE;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import uk.chromis.custom.toggleswitch.ToggleSwitch;
import uk.chromis.globals.IconFactory;
import uk.chromis.globals.SystemProperty;

/**
 * FXML Controller class
 *
 * @author John
 */
public class NewButtonPanelController implements Initializable, BaseController {

    public ToggleSwitch historicProductStyle;
    public ToggleSwitch historicCategoryStyle;
    public ToggleSwitch textOpaque;
    public ColorPicker textColour;
    public ColorPicker cpCategoryColour;
    public TextField buttonWidth;
    public TextField buttonHeight;
    public Pane historicPane;

    public ImageView productButton1;
    public ImageView productButton2;
    public ImageView productButton3;

    public HBox productButtonBox;
    public ToggleGroup productSelector;
    public RadioButton productRB1;
    public RadioButton productRB2;
    public RadioButton productRB3;
    private Integer productButton;

    public ToggleGroup categorySelector;
    public RadioButton categoryRB1;
    public RadioButton categoryRB2;
    public ImageView categoryButton1;
    public ImageView categoryButton2;
    private Integer categoryButton;

    public BooleanProperty dirty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        historicProductStyle.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));

        buttonWidth.textProperty().addListener((observable, oldValue, newValue) -> dirty.setValue(TRUE));
        buttonHeight.textProperty().addListener((observable, oldValue, newValue) -> dirty.setValue(TRUE));

        textColour.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

        productButton1.setImage(IconFactory.getImage("productButton1.png"));
        productButton2.setImage(IconFactory.getImage("productButton2.png"));
        productButton3.setImage(IconFactory.getImage("productButton3.png"));

        categoryButton1.setImage(IconFactory.getImage("categoryButton1.png"));
        categoryButton2.setImage(IconFactory.getImage("categoryButton2.png"));

        productButtonBox.visibleProperty().bind(historicProductStyle.selectedProperty().not());
        historicPane.visibleProperty().bind(historicProductStyle.selectedProperty());
        productButtonBox.setLayoutY(300);

        load();

        productButton1.setOnMouseClicked((MouseEvent event) -> productRB1.fire());
        productButton2.setOnMouseClicked((MouseEvent event) -> productRB2.fire());
        productButton3.setOnMouseClicked((MouseEvent event) -> productRB3.fire());

        productRB1.setOnAction(event -> productButton = 1);
        productRB2.setOnAction(event -> productButton = 2);
        productRB3.setOnAction(event -> productButton = 3);

        categoryButton1.setOnMouseClicked((MouseEvent event) -> categoryRB1.fire());
        categoryButton2.setOnMouseClicked((MouseEvent event) -> categoryRB2.fire());

        categoryRB1.setOnAction(event -> categoryButton = 1);
        categoryRB2.setOnAction(event -> categoryButton = 2);

        productSelector.selectedToggleProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        categorySelector.selectedToggleProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

    }

    @Override
    public void load() {
        buttonWidth.setText(Integer.toString(SystemProperty.PRODUCTBUTTONWIDTH));
        buttonHeight.setText(Integer.toString(SystemProperty.PRODUCTBUTTONHEIGHT));
        historicProductStyle.setSelected(SystemProperty.HISTORICPRODUCTICON);
        textOpaque.setSelected(SystemProperty.TEXTOPAQUE);

        if (!SystemProperty.BUTTONTEXTCOLOUR.equals("")) {
            textColour.setValue(Color.valueOf(SystemProperty.BUTTONTEXTCOLOUR));
        }

        if (!SystemProperty.CATEGORYTEXTCOLOUR.equals("")) {
            cpCategoryColour.setValue(Color.valueOf(SystemProperty.CATEGORYTEXTCOLOUR));
        }

        productButton = SystemProperty.PRODUCTBUTTONTYPE;
        switch (productButton) {
            case 1:
                productSelector.selectToggle(productRB1);
                break;
            case 2:
                productSelector.selectToggle(productRB2);
                break;
            case 3:
                productSelector.selectToggle(productRB3);
                break;
        }

        categoryButton = SystemProperty.CATEGORYBUTTONTYPE;
        switch (categoryButton) {
            case 1:
                categorySelector.selectToggle(categoryRB1);
                break;
            case 2:
                categorySelector.selectToggle(categoryRB2);
                break;
        }

        dirty.setValue(false);
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("BUTTONTEXTCOLOUR", getColour(textColour));
        SystemProperty.updatePropertyValueByConstant("CATEGORYTEXTCOLOUR", getColour(cpCategoryColour));
        SystemProperty.updatePropertyValueByConstant("HISTORICPRODUCTICON", historicProductStyle.isSelected());
        SystemProperty.updatePropertyValueByConstant("TEXTOPAQUE", textOpaque.isSelected());
        SystemProperty.updatePropertyValueByConstant("PRODUCTBUTTONHEIGHT", buttonHeight.getText());
        SystemProperty.updatePropertyValueByConstant("PRODUCTBUTTONWIDTH", buttonWidth.getText());
        SystemProperty.updatePropertyValueByConstant("PRODUCTBUTTONTYPE", productButton);
        SystemProperty.updatePropertyValueByConstant("CATEGORYBUTTONTYPE", categoryButton);
        dirty.setValue(false);
    }

    public String getColour(ColorPicker colorPicker) {
        return String.format("#%02X%02X%02X",
                (int) (colorPicker.getValue().getRed() * 255),
                (int) (colorPicker.getValue().getGreen() * 255),
                (int) (colorPicker.getValue().getBlue() * 255));
    }

    @Override
    public Boolean isDirty() {
        return dirty.getValue();
    }

    @Override
    public void setDirty(Boolean value) {
        dirty.setValue(value);
    }
}
