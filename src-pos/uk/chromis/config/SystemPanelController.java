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
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import uk.chromis.custom.toggleswitch.ToggleSwitch;
import uk.chromis.globals.SystemProperty;

/**
 * FXML Controller class
 *
 * @author John
 */
public class SystemPanelController implements Initializable, BaseController {
    
    public ToggleSwitch enableAutoLogoff;
    public ToggleSwitch inactivityTimer;
    public Label lblInactivityTimer;
    public ToggleSwitch autologoffAfterSale;
    public Label lblAutologoffAfterSale;
    public ToggleSwitch autoLogoffAfterKitchen;
    public Label lblAutoLogoffAfterKitchen;
    public ToggleSwitch autoLogoffToTables;
    public Label lblAutoLogoffToTables;
    public TextField autoLogoffTime;
    public ToggleSwitch autoLogoffAfterPrint;
    public Label lblAutoLogoffAfterPrint;

    public ToggleSwitch consolidate;
    public ToggleSwitch textOverlay;
    public ToggleSwitch price00;
    public ToggleSwitch disableDefaultProduct;
    public ToggleSwitch updatedbprice;
    public ToggleSwitch categoriesBynumber;
    public ToggleSwitch maxChangeEnable;
    public ToggleSwitch enablePaymentIcons;
    public ToggleSwitch enableSentRefunds;
    public ToggleSwitch enableScreenDrag;
    public ToggleSwitch enableTaxExemption;

    public TextField maxChange;


    public Spinner auditNoSales;
    public Spinner auditRemovedLines;

    public BooleanProperty dirty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        consolidate.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        textOverlay.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        price00.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        updatedbprice.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        disableDefaultProduct.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        categoriesBynumber.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        enablePaymentIcons.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));
        enableTaxExemption.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(TRUE));


        auditNoSales.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        auditRemovedLines.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        enableScreenDrag.selectedProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

        maxChangeEnable.selectedProperty().addListener((arg, oldVal, newVal) -> maxChange.setDisable(!maxChangeEnable.isSelected()));



        IntegerSpinnerValueFactory auditLinesFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 28);
        auditNoSales.setValueFactory(auditLinesFactory);
        auditLinesFactory.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        auditNoSales.setEditable(true);
        auditNoSales.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                auditNoSales.increment(0);
            }
        });

        IntegerSpinnerValueFactory auditNoSaleFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 28);
        auditRemovedLines.setValueFactory(auditNoSaleFactory);
        auditNoSaleFactory.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        auditRemovedLines.setEditable(true);
        auditRemovedLines.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                auditRemovedLines.increment(0);
            }
        });

        load();

    }

    private void updateAutoLogoff() {
        if (enableAutoLogoff.isSelected()) {
            autoLogoffTime.setDisable(false);
            autoLogoffToTables.setDisable(FALSE);
            autoLogoffAfterKitchen.setDisable(FALSE);
            autoLogoffAfterPrint.setDisable(FALSE);
            inactivityTimer.setDisable(FALSE);
            autologoffAfterSale.setDisable(FALSE);

            lblAutoLogoffToTables.setDisable(false);
            lblAutoLogoffAfterKitchen.setDisable(false);
            lblAutoLogoffAfterPrint.setDisable(false);
            lblInactivityTimer.setDisable(false);
            lblAutologoffAfterSale.setDisable(false);
        } else {
            autoLogoffTime.setDisable(true);
            autoLogoffToTables.setDisable(TRUE);
            autoLogoffAfterKitchen.setDisable(TRUE);
            autoLogoffAfterPrint.setDisable(TRUE);
            inactivityTimer.setDisable(TRUE);
            autologoffAfterSale.setDisable(TRUE);

            lblAutoLogoffToTables.setDisable(true);
            lblAutoLogoffAfterKitchen.setDisable(true);
            lblAutoLogoffAfterPrint.setDisable(true);
            lblInactivityTimer.setDisable(true);
            lblAutologoffAfterSale.setDisable(true);
        }
    }

    @Override
    public void load() {
        price00.setSelected(SystemProperty.PRICEWITH00);
        updatedbprice.setSelected(SystemProperty.PRODUCTUPDATE);
        textOverlay.setSelected(SystemProperty.TEXTOVERLAY);
        consolidate.setSelected(SystemProperty.CONSOLIDATED);
        disableDefaultProduct.setSelected(SystemProperty.HIDEDEFAULTPRODUCT);
        categoriesBynumber.setSelected(SystemProperty.CATBYNUMBERORDER);
        maxChangeEnable.setSelected(SystemProperty.ENABLECHANGELIMIT);
        maxChange.setDisable(!maxChangeEnable.isSelected());
        maxChange.setText(Integer.toString(SystemProperty.CHANGELIMIT));
        enableAutoLogoff.setSelected(SystemProperty.ENABLEAUTOLOGOFF);
        inactivityTimer.setSelected(SystemProperty.AUTOLOGOFFINACTIVITYTIMER);
        autoLogoffTime.setText(Integer.toString(SystemProperty.AUTOLOGOFFPERIOD));
        autologoffAfterSale.setSelected(SystemProperty.AUTOLOGOFFAFTERSALE);
        autoLogoffToTables.setSelected(SystemProperty.AUTOLOGOFFTOTABLES);
        autoLogoffAfterKitchen.setSelected(SystemProperty.AUTOLOGOFFAFTERKITCHEN);
        autoLogoffAfterPrint.setSelected(SystemProperty.AUTOLOGOFFAFTERPRINT);
        enablePaymentIcons.setSelected(SystemProperty.PAYMENTICONS);
        auditNoSales.getValueFactory().setValue(SystemProperty.DRAWEROPENEDDAYS);
        auditRemovedLines.getValueFactory().setValue(SystemProperty.REMOVEDLINEDAYS);
        enableSentRefunds.setSelected(SystemProperty.ALLOWSENTITEMREFUND);
        enableScreenDrag.setSelected(SystemProperty.SCREENDRAG);
        enableTaxExemption.setSelected(SystemProperty.TAXEXEMPTION);

        enableAutoLogoff.selectedProperty().addListener((arg, oldVal, newVal) -> updateAutoLogoff());
        updateAutoLogoff();
        dirty.setValue(false);
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("PRICEWITH00", price00.isSelected());
        SystemProperty.updatePropertyValueByConstant("PRODUCTUPDATE", updatedbprice.isSelected());
        SystemProperty.updatePropertyValueByConstant("ENABLEAUTOLOGOFF", enableAutoLogoff.isSelected());
        SystemProperty.updatePropertyValueByConstant("AUTOLOGOFFINACTIVITYTIMER", inactivityTimer.isSelected());
        SystemProperty.updatePropertyValueByConstant("AUTOLOGOFFPERIOD", autoLogoffTime.getText());
        SystemProperty.updatePropertyValueByConstant("AUTOLOGOFFAFTERSALE", autologoffAfterSale.isSelected());
        SystemProperty.updatePropertyValueByConstant("AUTOLOGOFFTOTABLES", autoLogoffToTables.isSelected());
        SystemProperty.updatePropertyValueByConstant("AUTOLOGOFFAFTERKITCHEN", autoLogoffAfterKitchen.isSelected());
        SystemProperty.updatePropertyValueByConstant("AUTOLOGOFFAFTERPRINT", autoLogoffAfterPrint.isSelected());
        SystemProperty.updatePropertyValueByConstant("TEXTOVERLAY", textOverlay.isSelected());
        SystemProperty.updatePropertyValueByConstant("CONSOLIDATED", consolidate.isSelected());
        SystemProperty.updatePropertyValueByConstant("HIDEDEFAULTPRODUCT", disableDefaultProduct.isSelected());
        SystemProperty.updatePropertyValueByConstant("CATBYNUMBERORDER", categoriesBynumber.isSelected());
        SystemProperty.updatePropertyValueByConstant("CHANGELIMIT", maxChange.getText());
        SystemProperty.updatePropertyValueByConstant("ENABLECHANGELIMIT", maxChangeEnable.isSelected());
        SystemProperty.updatePropertyValueByConstant("PAYMENTICONS", enablePaymentIcons.isSelected());
        SystemProperty.updatePropertyValueByConstant("DRAWEROPENEDDAYS", auditNoSales.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("REMOVEDLINEDAYS", auditRemovedLines.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("ALLOWSENTITEMREFUND", enableSentRefunds.isSelected());
        SystemProperty.updatePropertyValueByConstant("SCREENDRAG", enableScreenDrag.isSelected());
        SystemProperty.updatePropertyValueByConstant("TAXEXEMPTION", enableTaxExemption.isSelected());

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
