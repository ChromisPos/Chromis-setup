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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
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
public class RestaurantPanelController implements Initializable, BaseController {

    public ToggleSwitch tsCustomerDetails;
    public ToggleSwitch tsWaiterDetails;
    public ToggleSwitch tsTransparent;
    public ToggleSwitch tsTableRefresh;
    public ToggleSwitch tsColouredLines;

    public ColorPicker cpCustomerColour;
    public ColorPicker cpWaiterColour;
    public ColorPicker cpTableColour;
    public ColorPicker cpLineSentBackground;
    public ColorPicker cpLineWaitingBackground;

    public TextField poPrefix;

    public BooleanProperty dirty = new SimpleBooleanProperty();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cpCustomerColour.setOnAction((ActionEvent event) -> dirty.setValue(true));
        cpWaiterColour.setOnAction((ActionEvent event) -> dirty.setValue(true));
        cpTableColour.setOnAction((ActionEvent event) -> dirty.setValue(true));
        cpLineSentBackground.setOnAction((ActionEvent event) -> dirty.setValue(true));
        cpLineWaitingBackground.setOnAction((ActionEvent event) -> dirty.setValue(true));

        tsCustomerDetails.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(true));
        tsWaiterDetails.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(true));
        tsTransparent.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(true));
        tsTableRefresh.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(true));
        tsColouredLines.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(true));

        cpLineSentBackground.disableProperty().bind(tsColouredLines.selectedProperty().not());
        cpLineWaitingBackground.disableProperty().bind(tsColouredLines.selectedProperty().not());

        load();

    }

    @Override
    public void load() {
        tsCustomerDetails.setSelected(SystemProperty.SHOWCUSTOMERDETAILS);
        tsWaiterDetails.setSelected(SystemProperty.SHOWWAITERDETAILS);
        tsTransparent.setSelected(SystemProperty.TRANSPARENTBUTTONS);
        tsTableRefresh.setSelected(SystemProperty.AUTOREFRESH);
        tsColouredLines.setSelected(SystemProperty.COLOURTICKETLINES);
        cpCustomerColour.setValue(Color.valueOf(SystemProperty.CUSTOMERCOLOUR));
        cpWaiterColour.setValue(Color.valueOf(SystemProperty.WAITERCOLOUR));
        cpTableColour.setValue(Color.valueOf(SystemProperty.TABLECOLOUR));
        cpLineSentBackground.setValue(Color.valueOf(SystemProperty.SENTBACKGROUNDCOLOUR));
        cpLineWaitingBackground.setValue(Color.valueOf(SystemProperty.WAITINGBACKGROUNDCOLOUR));
        poPrefix.setText(SystemProperty.POPREFIX);

    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("SHOWCUSTOMERDETAILS", tsCustomerDetails.isSelected());
        SystemProperty.updatePropertyValueByConstant("SHOWWAITERDETAILS", tsWaiterDetails.isSelected());
        SystemProperty.updatePropertyValueByConstant("CUSTOMERCOLOUR", getColour(cpCustomerColour));
        SystemProperty.updatePropertyValueByConstant("WAITERCOLOUR", getColour(cpWaiterColour));
        SystemProperty.updatePropertyValueByConstant("TABLECOLOUR", getColour(cpTableColour));
        SystemProperty.updatePropertyValueByConstant("TRANSPARENTBUTTONS", tsTransparent.isSelected());
        SystemProperty.updatePropertyValueByConstant("AUTOREFRESH", tsTableRefresh.isSelected());
        SystemProperty.updatePropertyValueByConstant("COLOURTICKETLINES", tsColouredLines.isSelected());
        SystemProperty.updatePropertyValueByConstant("SENTBACKGROUNDCOLOUR", getColour(cpLineSentBackground));
        SystemProperty.updatePropertyValueByConstant("WAITINGBACKGROUNDCOLOUR", getColour(cpLineWaitingBackground));
        SystemProperty.updatePropertyValueByConstant("POPREFIX", poPrefix.getText());
        dirty.setValue(false);
    }

    private String getColour(ColorPicker colourPicker) {
        return String.format("#%02X%02X%02X",
                (int) (colourPicker.getValue().getRed() * 255),
                (int) (colourPicker.getValue().getGreen() * 255),
                (int) (colourPicker.getValue().getBlue() * 255));
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
