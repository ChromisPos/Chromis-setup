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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import uk.chromis.custom.toggleswitch.ToggleSwitch;
import uk.chromis.globals.SystemProperty;

/**
 * FXML Controller class
 *
 * @author John
 */
public class TicketPanelController implements Initializable, BaseController {

    public Spinner receiptSpinner;
    public Spinner pickupSpinner;
    public Spinner receiptCloseSpinner;

    public Label ticketExample;

    public ToggleSwitch receiptPrinterOff;
    public ToggleSwitch serviceChargeOff;
    public ToggleSwitch SCRestaurant;
    public ToggleSwitch layawayId;
    public ToggleSwitch createOnOrderOnly;
    public ToggleSwitch layawayPopup;
    public ToggleSwitch pickupEnabled;
    public ToggleSwitch sharedaccess;

    public TextField textSCRate;

    public AnchorPane pane;

    private Integer x = 0;
    private int receiptSize;
    private int pickupSize;
    private int receiptClose;

    public BooleanProperty dirty = new SimpleBooleanProperty(false);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        serviceChargeOff.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            SCRestaurant.setDisable(!newValue);
            if (newValue == false) {
                SCRestaurant.setSelected(false);
            }
            dirty.setValue(true);
        });

        receiptPrinterOff.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            dirty.setValue(true);
        });

        SCRestaurant.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            dirty.setValue(true);
        });

        layawayId.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            dirty.setValue(true);
        });

        layawayPopup.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            dirty.setValue(true);
        });

        createOnOrderOnly.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            dirty.setValue(true);
        });

        receiptSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            x = 1;
            while (x < (Integer) receiptSpinner.getValue()) {
                x++;
            }
            dirty.setValue(true);
        });

        pickupSpinner.valueProperty().addListener((obs, oldValue, newValue)
                -> dirty.setValue(true)
        );

        receiptCloseSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            x = 10;
            while (x < (Integer) receiptCloseSpinner.getValue()) {
                x++;
            }
            dirty.setValue(true);
        });

        receiptCloseSpinner.valueProperty().addListener((obs, oldValue, newValue)
                -> dirty.setValue(true)
        );

        pickupEnabled.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                layawayId.setDisable(false);
            } else {
                createOnOrderOnly.setSelected(false);
                layawayId.setSelected(false);
                layawayId.setDisable(true);
            }
            dirty.setValue(true);
        });

        load();

        updateServiceCharge();
    }

    private void updateServiceCharge() {
        if (serviceChargeOff.isSelected()) {
            SCRestaurant.setDisable(FALSE);
        } else {
            SCRestaurant.setDisable(TRUE);
        }
    }

    @Override
    public void load() {
        receiptSize = SystemProperty.RECEIPTSIZE;
        SpinnerValueFactory<Integer> valueFactoryReceipt = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, receiptSize);
        receiptSpinner.setValueFactory(valueFactoryReceipt);

        pickupSize = SystemProperty.PICKUPSIZE;
        SpinnerValueFactory<Integer> valueFactoryPickup = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, pickupSize);
        pickupSpinner.setValueFactory(valueFactoryPickup);

        receiptClose = SystemProperty.RECEIPTAUTOCLOSE;
        SpinnerValueFactory<Integer> valueFactoryClose = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, receiptClose);
        receiptCloseSpinner.setValueFactory(valueFactoryClose);

        x = 1;
        while (x < (Integer) receiptSpinner.getValue()) {
            x++;
        }

        receiptPrinterOff.setSelected(SystemProperty.RECEIPTPRINTOFF);
        textSCRate.setText(SystemProperty.SCRATE.toString());

        serviceChargeOff.setSelected(SystemProperty.SCONOFF);
        SCRestaurant.setSelected(SystemProperty.SCRESTAURANT);
        layawayId.setSelected(SystemProperty.USEPICKUPFORLAYAWAY);
        createOnOrderOnly.setSelected(SystemProperty.CREATEORDER);
        layawayPopup.setSelected(SystemProperty.LAYAWAYPOPUP);
        pickupEnabled.setSelected(SystemProperty.PICKUPENABLED);
        sharedaccess.setSelected(SystemProperty.SHAREDTICKETBYUSER);

        dirty.setValue(false);
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("RECEIPTSIZE", receiptSpinner.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("PICKUPSIZE", pickupSpinner.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("RECEIPTPRINTOFF", receiptPrinterOff.isSelected());
        SystemProperty.updatePropertyValueByConstant("SCONOFF", serviceChargeOff.isSelected());
        SystemProperty.updatePropertyValueByConstant("SCRATE", textSCRate.getText());
        SystemProperty.updatePropertyValueByConstant("SCRESTAURANT", SCRestaurant.isSelected());
        SystemProperty.updatePropertyValueByConstant("USEPICKUPFORLAYAWAY", layawayId.isSelected());
        SystemProperty.updatePropertyValueByConstant("CREATEORDER", createOnOrderOnly.isSelected());
        SystemProperty.updatePropertyValueByConstant("LAYAWAYPOPUP", layawayPopup.isSelected());
        SystemProperty.updatePropertyValueByConstant("PICKUPENABLED", pickupEnabled.isSelected());
        SystemProperty.updatePropertyValueByConstant("SHAREDTICKETBYUSER", sharedaccess.isSelected());
        SystemProperty.updatePropertyValueByConstant("RECEIPTAUTOCLOSE", receiptCloseSpinner.getValue().toString());
        dirty.setValue(false);
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
