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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import uk.chromis.custom.toggleswitch.ToggleSwitch;
import uk.chromis.globals.SystemProperty;
import uk.chromis.pos.payment.PaymentConfiguration;

public class PaymentPanelController implements Initializable, BaseController {

    public Pane providerPane;
    public ComboBox cardReader;
    public ComboBox paymentGateway;

    public ToggleSwitch paymentTest;
    public ToggleSwitch enableFees;
    public Spinner feePercent;

    private final Map<String, PaymentConfiguration> paymentsName = new HashMap<>();
    private PaymentConfiguration pc;

    public BooleanProperty dirty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> readers = FXCollections.observableArrayList("Not defined");

        SpinnerValueFactory.DoubleSpinnerValueFactory feesFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 50, 0.5, 0.1);
        feePercent.setValueFactory(feesFactory);
        feesFactory.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        feePercent.setEditable(true);
        feePercent.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                feePercent.increment(0);
            }
        });

        feePercent.disableProperty().bind(enableFees.selectedProperty().not());
        feePercent.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

       
        ObservableList<String> gateways = FXCollections.observableArrayList("Not Defined");
        cardReader.setItems(gateways);
        cardReader.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> dirty.setValue(true));

        ObservableList<String> cardReaders = FXCollections.observableArrayList("Not Defined", "external");
        paymentGateway.setItems(cardReaders);
        paymentGateway.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> dirty.setValue(true));

//        paymentGateway.getComboBox().getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
//            pc = paymentsName.get(comboValue(paymentGateway.getSelected()));
//
//            if (pc != null) {
//                providerPane.getChildren().clear();
//                providerPane.getChildren().add(pc.getFXComponent());
//
//            }
//
//        });
        paymentTest.selectedProperty().addListener((arg, oldVal, newVal) -> dirty.setValue(true));

        // Payment Provider                
        // initPayments("Not defined", new ConfigPaymentPanelEmpty());
        // initPayments("external", new ConfigPaymentPanelEmpty());
        load();
    }

    private void initPayments(String name, PaymentConfiguration pc) {
//        paymentGateway.getComboBox().getItems().add(name);
//        paymentsName.put(name, pc);
    }

    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public void load() {
        cardReader.getSelectionModel().select(SystemProperty.MAGREADER);
        paymentGateway.getSelectionModel().select(SystemProperty.GATEWAY);

        paymentTest.setSelected(SystemProperty.TESTMODE);
        enableFees.setSelected(SystemProperty.HANDLINGFEES);
       
        feePercent.getValueFactory().setValue(SystemProperty.HANDLINGFEE);

        //    pc.loadProperties();
        dirty.setValue(false);
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("MAGREADER", cardReader.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("GATEWAY", paymentGateway.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("TESTMODE", paymentTest.isSelected());
        SystemProperty.updatePropertyValueByConstant("HANDLINGFEES", enableFees.isSelected());
        SystemProperty.updatePropertyValueByConstant("HANDLINGFEE", feePercent.getValue().toString());

    //    pc.saveProperties();
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
