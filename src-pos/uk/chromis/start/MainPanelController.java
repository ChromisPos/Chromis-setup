/*
**    Chromis Administration  - Open Source Point of Sale
**
**    This file is part of Chromis Administration Version Chromis V1.5.4
**
**    Copyright (c) 2015-2023 Chromis & previous Openbravo POS related works   
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
package uk.chromis.start;

import uk.chromis.custom.controls.CustomLabel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import uk.chromis.commons.dbmanager.DbUser;
import uk.chromis.config.ButtonPanelController;
import uk.chromis.config.CardPanelController;
import uk.chromis.config.GeneralPanelController;
import uk.chromis.config.LocalePanelController;
import uk.chromis.config.NewButtonPanelController;
import uk.chromis.config.NewSalesScreenPanelController;
import uk.chromis.config.PaymentPanelController;
import uk.chromis.config.RestaurantPanelController;
import uk.chromis.config.SalesScreenPanelController;
import uk.chromis.config.SystemPanelController;
import uk.chromis.config.TicketPanelController;
import uk.chromis.pos.dialogs.JAlertPane;
import uk.chromis.pos.forms.LocalResource;

/**
 *
 * @author John
 */
public class MainPanelController implements Initializable {

    public Pane configPane;
    public AnchorPane anchorPane;
    public VBox selectionPanel;
    public VBox buttonPanel;
    public Label selection1;
    public Label selection2;
    public Pane custom1;
    public CustomLabel currentLabel;
    public Separator separator;

    protected BooleanProperty dirty = new SimpleBooleanProperty();
    private GeneralPanelController genController;
    private LocalePanelController localeController;
    private PaymentPanelController paymentController;
    private SystemPanelController systemController;
    private SalesScreenPanelController salesScreenController;
    private NewSalesScreenPanelController newSalesScreenController;
    private RestaurantPanelController restuarantController;
    private TicketPanelController ticketController;
    private CardPanelController cardController;
    private ButtonPanelController buttonController;
    private NewButtonPanelController newButtonController;

    private final Pane companyPane = new Pane();
    private final Pane generalPane = new Pane();
    private final Pane localePane = new Pane();
    private final Pane paymentPane = new Pane();
    private final Pane systemPane = new Pane();
    private final Pane ticketPane = new Pane();
    private final Pane restaurantPane = new Pane();
    private final Pane salesPane = new Pane();
    private final Pane newSalesPane = new Pane();
    private final Pane cardPane = new Pane();
    private final Pane buttonPane = new Pane();
    private final Pane newButtonPane = new Pane();

    public Label lblConnection;
    public Label lblVersion;

    private CustomLabel general;
    private CustomLabel localeSettings;
    private CustomLabel paymentSettings;
    private CustomLabel systemOptions;
    private CustomLabel buttonConfig;
    private CustomLabel newButtonConfig;
    private CustomLabel ticketSetup;
    private CustomLabel restaurantSettings;
    private CustomLabel salesPanel;
    private CustomLabel newSalesPanel;
    private CustomLabel cardConfig;

    private final DbUser dbUser;
//    private String cssResource;

    public MainPanelController(DbUser dbUser) {
        this.dbUser = dbUser;
    }

//    public void setCSSString(String cssResource) {
//        this.cssResource = cssResource;
//    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        StringBuilder connection = new StringBuilder();
        connection.append("Connected to database : ");
        connection.append(dbUser.getDatabaseName());
        connection.append(" on ");
        connection.append(dbUser.getServerName());

        lblConnection.setText(connection.toString());

        lblVersion.setText("Version : " + LocalResource.APP_VERSION);

        separator.prefWidthProperty().bind(anchorPane.widthProperty().subtract(4));

        selectionPanel.setPrefWidth(210);
        buttonPanel.setPrefWidth(210);
        selectionPanel.prefHeightProperty().bind(anchorPane.heightProperty().subtract(115));
        configPane.prefHeightProperty().bind(anchorPane.heightProperty());
        configPane.prefWidthProperty().bind(anchorPane.widthProperty().subtract(210));
        configPane.setLayoutX(210);
        configPane.setLayoutY(35);

        general = new CustomLabel(LocalResource.getString("tab.general"));
        localeSettings = new CustomLabel(LocalResource.getString("tab.locale"));
        paymentSettings = new CustomLabel(LocalResource.getString("tab.paymentMethod"));
        systemOptions = new CustomLabel(LocalResource.getString("tab.systemOptions"));
        buttonConfig = new CustomLabel(LocalResource.getString("tab.buttons"));
        newButtonConfig = new CustomLabel(LocalResource.getString("tab.buttons"));
        ticketSetup = new CustomLabel(LocalResource.getString("tab.ticketSetup"));
        restaurantSettings = new CustomLabel(LocalResource.getString("tab.restaurant"));
        salesPanel = new CustomLabel(LocalResource.getString("tab.salesScreen"));
        newSalesPanel = new CustomLabel(LocalResource.getString("tab.salesScreen"));
        cardConfig = new CustomLabel(LocalResource.getString("tab.cardSetup"));

        CustomLabel saveConfig = new CustomLabel(LocalResource.getString("btn.save"));
        CustomLabel exitConfig = new CustomLabel(LocalResource.getString("btn.exit"));
        CustomLabel restoreConfig = new CustomLabel(LocalResource.getString("btn.restore"));

        currentLabel = general;
        currentLabel.setSelected(true);
        addListener(general, generalPane);
        addListener(localeSettings, localePane);
        addListener(paymentSettings, paymentPane);
        addListener(systemOptions, systemPane);
        addListener(buttonConfig, buttonPane);
        addListener(newButtonConfig, newButtonPane);
        addListener(ticketSetup, ticketPane);
        addListener(restaurantSettings, restaurantPane);
        addListener(salesPanel, salesPane);
        addListener(newSalesPanel, newSalesPane);
        addListener(cardConfig, cardPane);

        selectionPanel.getChildren().add(general);
        selectionPanel.getChildren().add(localeSettings);
        selectionPanel.getChildren().add(paymentSettings);
        selectionPanel.getChildren().add(systemOptions);
        if (ChromisSystemConfig.newScreen) {
            selectionPanel.getChildren().add(newButtonConfig);
        } else {
            selectionPanel.getChildren().add(buttonConfig);
        }
        selectionPanel.getChildren().add(ticketSetup);
        selectionPanel.getChildren().add(restaurantSettings);
        if (ChromisSystemConfig.newScreen) {
            selectionPanel.getChildren().add(newSalesPanel);
        } else {
            selectionPanel.getChildren().add(salesPanel);
        }
        selectionPanel.getChildren().add(cardConfig);

        restoreConfig.setOnMouseClicked((MouseEvent event) -> {
            doRestore();
        });

        saveConfig.setOnMouseClicked((MouseEvent event) -> {
            doSave();
        });

        exitConfig.setOnMouseClicked((MouseEvent event) -> {
            handleExit();
        });

        buttonPanel.getChildren().add(restoreConfig);
        buttonPanel.getChildren().add(saveConfig);
        buttonPanel.getChildren().add(exitConfig);

        genController = (GeneralPanelController) getController(generalPane, "/uk/chromis/config/GeneralPanel.fxml");
        dirty.bindBidirectional(genController.dirty);

        localeController = (LocalePanelController) getController(localePane, "/uk/chromis/config/LocalePanel.fxml");
        dirty.bindBidirectional(localeController.dirty);

        paymentController = (PaymentPanelController) getController(paymentPane, "/uk/chromis/config/PaymentPanel.fxml");
        dirty.bindBidirectional(paymentController.dirty);

        systemController = (SystemPanelController) getController(systemPane, "/uk/chromis/config/SystemPanel.fxml");
        dirty.bindBidirectional(systemController.dirty);

        buttonController = (ButtonPanelController) getController(buttonPane, "/uk/chromis/config/ButtonPanel.fxml");
        dirty.bindBidirectional(buttonController.dirty);

        newButtonController = (NewButtonPanelController) getController(newButtonPane, "/uk/chromis/config/NewButtonPanel.fxml");
        dirty.bindBidirectional(newButtonController.dirty);

        ticketController = (TicketPanelController) getController(ticketPane, "/uk/chromis/config/TicketPanel.fxml");
        dirty.bindBidirectional(ticketController.dirty);

        restuarantController = (RestaurantPanelController) getController(restaurantPane, "/uk/chromis/config/RestaurantPanel.fxml");
        dirty.bindBidirectional(restuarantController.dirty);

        salesScreenController = (SalesScreenPanelController) getController(salesPane, "/uk/chromis/config/SalesScreenPanel.fxml");
        dirty.bindBidirectional(salesScreenController.dirty);

        newSalesScreenController = (NewSalesScreenPanelController) getController(newSalesPane, "/uk/chromis/config/NewSalesScreenPanel.fxml");
        dirty.bindBidirectional(newSalesScreenController.dirty);

        cardController = (CardPanelController) getController(cardPane, "/uk/chromis/config/CardPanel.fxml");
        dirty.bindBidirectional(cardController.dirty);

        configPane.getChildren().add(generalPane);

    }

    private Object getController(Pane pane, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml), LocalResource.resources);
            Parent root = loader.load();
            pane.getChildren().add(root);
            root.setLayoutX(5);
            root.setLayoutY(5);
            pane.setUserData(loader.getController());
            return (loader.getController());
        } catch (IOException ex) {
            System.out.println("Error loading tab data");
            System.out.println("Error loading FXML : " + fxml);
            System.out.println(ex);
        }
        return null;
    }

    private void createGeneralPane() {

    }

    private void addListener(CustomLabel customLabel, Pane pane) {
        customLabel.setOnMouseClicked((MouseEvent event) -> {
            currentLabel.setSelected(false);
            customLabel.setSelected(true);
            currentLabel = customLabel;
            configPane.getChildren().clear();
            configPane.getChildren().add(pane);
        });
    }

//    private void addActionListener(CustomLabel customLabel) {
//        customLabel.setOnMouseClicked((MouseEvent event) -> {
//            System.out.println("testing ");
//        });
//    }
    public void doRestore() {
        genController.load();
        localeController.load();
        systemController.load();
        if (ChromisSystemConfig.newScreen) {
            newButtonController.load();
        } else {
            buttonController.load();
        }
        restuarantController.load();
        if (ChromisSystemConfig.newScreen) {
            newSalesScreenController.load();
        } else {
            salesScreenController.load();
        }
        salesScreenController.load();
        ticketController.load();
        paymentController.load();
        cardController.load();
    }

    public void reSave() {
        genController.save();
        localeController.save();
        systemController.save();
        if (ChromisSystemConfig.newScreen) {
            newButtonController.save();
        } else {
            buttonController.save();
        }
        if (ChromisSystemConfig.newScreen) {
            newSalesScreenController.save();
        } else {
            salesScreenController.save();
        }
        restuarantController.save();
        ticketController.save();
        paymentController.save();
        cardController.save();
        dirty.setValue(false);
    }

    public void doSave() {
        doSave(false);
    }

    public void doSave(Boolean closing) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        genController.save();
        localeController.save();
        systemController.save();
        if (ChromisSystemConfig.newScreen) {
            newButtonController.save();
        } else {
            buttonController.save();
        }
        if (ChromisSystemConfig.newScreen) {
            newSalesScreenController.save();
        } else {
            salesScreenController.save();
        }
        restuarantController.save();
        ticketController.save();
        paymentController.save();
        cardController.save();

        JAlertPane.showAlertDialog(JAlertPane.INFORMATION,
                null,
                LocalResource.getString("alert.savedConfig"),
                LocalResource.getString("alert.restart"),
                JAlertPane.OK_OPTION, true);
        dirty.setValue(false);
    }

    public void handleExit() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (dirty.getValue()) {
            if (JAlertPane.showAlertDialog(JAlertPane.ERROR,
                    null,
                    LocalResource.getString("alert.unsavedSettings"),
                    LocalResource.getString("alert.saveConfig"),
                    JAlertPane.YES_NO_OPTION, true) == 5) {
                doSave(true);
            }
        }
        System.exit(0);
    }

    public Boolean isDirty() {
        return dirty.getValue();
    }

    public void setDirty(Boolean value) {
        dirty.setValue(value);
    }

}
