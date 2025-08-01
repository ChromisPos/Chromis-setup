/*
**    Chromis POS  - The New Dynamic Open Source POS
**
**    Copyright (c)2015-2020 
**    
**    Chromis and previous contributing parties (Unicenta & Openbravo)
**    http://www.chromis.co.uk
**
**    This file is part of Chromis POS Version Chromis V1.00.2003.12
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
**
**    You should have received a copy of the GNU General Public License
**    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>
**
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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import uk.chromis.custom.toggleswitch.ToggleSwitch;

import uk.chromis.globals.SystemProperty;

/**
 * FXML Controller class
 *
 * @author John
 */
public class ButtonPanelController implements Initializable, BaseController {


    public ToggleSwitch historicProductStyle;
    public ToggleSwitch historicCategoryStyle;
    public ToggleSwitch textOpaque;
    public ColorPicker textColour;
    public TextField buttonWidth;
    public TextField buttonHeight;


    public BooleanProperty dirty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        historicProductStyle.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        historicCategoryStyle.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));

        buttonWidth.textProperty().addListener((observable, oldValue, newValue) -> dirty.setValue(TRUE));
        buttonHeight.textProperty().addListener((observable, oldValue, newValue) -> dirty.setValue(TRUE));

        textColour.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        textColour.setStyle("-fx-font: 15px 'Liberation Serif';");
        
        load();

    }

    @Override
    public void load() {
        buttonWidth.setText(Integer.toString(SystemProperty.PRODUCTBUTTONWIDTH));
        buttonHeight.setText(Integer.toString(SystemProperty.PRODUCTBUTTONHEIGHT));
        historicProductStyle.setSelected(SystemProperty.HISTORICPRODUCTICON);
        historicCategoryStyle.setSelected(SystemProperty.HISTORICCATEGORYICON);
        textOpaque.setSelected(SystemProperty.TEXTOPAQUE);

        if (!SystemProperty.BUTTONTEXTCOLOUR.equals("")) {
            textColour.setValue(Color.valueOf(SystemProperty.BUTTONTEXTCOLOUR));
        }

        dirty.setValue(false);
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("BUTTONTEXTCOLOUR", getColour(textColour));
        SystemProperty.updatePropertyValueByConstant("HISTORICPRODUCTICON", historicProductStyle.isSelected());
        SystemProperty.updatePropertyValueByConstant("HISTORICCATEGORYICON", historicCategoryStyle.isSelected());
        SystemProperty.updatePropertyValueByConstant("TEXTOPAQUE", textOpaque.isSelected());
        SystemProperty.updatePropertyValueByConstant("PRODUCTBUTTONHEIGHT", buttonHeight.getText());
        SystemProperty.updatePropertyValueByConstant("PRODUCTBUTTONWIDTH", buttonWidth.getText());
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
