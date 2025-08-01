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

import static java.lang.Boolean.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import uk.chromis.custom.toggleswitch.ToggleSwitch;
import uk.chromis.commons.dbmanager.DbUser;
import uk.chromis.commons.dbmanager.DbUtils;
import uk.chromis.data.loader.ConnectionFactory;
import uk.chromis.globals.SystemProperty;
import uk.chromis.pos.dialogs.JAlertPane;
import uk.chromis.pos.forms.LocalResource;

/**
 * FXML Controller class
 *
 * @author John
 */
public class CardPanelController implements Initializable, BaseController {

    public RadioButton earnx;
    public RadioButton collectx;

    public TextField magneticLeading;
    public TextField magneticTrailing;
    public TextField loyaltyCardPrefix;
    public TextField earnXPoints;
    public TextField voucherPoints;
    public TextField giftCardPrefix;
    public TextField voucherRedeemValue;

    public ToggleSwitch tswLoyaltyEnabled;
    public ToggleSwitch tswGiftEnabled;
    public ToggleSwitch tswShowAllOffers;
    public ToggleSwitch tswAccountLoyalty;
    public ToggleSwitch tswRegisterCustomer;

    public Label lblLoyaltyCardPrefix;
    public Label schemeOptions;
    public Label getXPoints;
    public Label voucherXPoints;
    public Label giftaCardScheme;
    public Label lblGiftCardPrefix;
    public Label warningLabel;
    public Label loyaltyEnabled;
    public Label voucherValue;
    public Label lblShowRewards;
    public Label lblLoyaltyCustomer;
    public Label lblCustomerMessage;
    public Label lblScheme;
    public Label lblAccountEnabled;

    public Label lblMessage;
    public Label lblBackupDB;
    public TextField txtBackupdb;
    public Button btnCreateBackup;
    public Button btnbackupTest;

    private final ToggleGroup radioGroup = new ToggleGroup();
    private String tableRetain;
    private String lineRemovedRetain;
    private String ticketLine;

    private Task doTest;
    private Task doCreate;
    private Object[] result;
    private DbUser dbUser;
    private CountDownLatch latchToWaitForConnectionTest;
    public ProgressBar pb;
    public Label lblProgressMsg;
    private Boolean dbBuilt = true;

    public BooleanProperty dirty = new SimpleBooleanProperty();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        earnx.setToggleGroup(radioGroup);
        collectx.setToggleGroup(radioGroup);
        earnx.setSelected(true);

        earnx.setOnAction(event -> {
            getXPoints.setDisable(false);
            earnXPoints.setDisable(false);
            voucherXPoints.setDisable(false);
            voucherPoints.setDisable(false);
            voucherValue.setDisable(false);
            voucherRedeemValue.setDisable(false);
            tswShowAllOffers.setVisible(FALSE);
            lblShowRewards.setVisible(FALSE);
            dirty.setValue(true);
        });

        collectx.setOnAction(event -> {
            getXPoints.setDisable(true);
            earnXPoints.setDisable(true);
            voucherXPoints.setDisable(true);
            voucherPoints.setDisable(true);
            voucherValue.setDisable(true);
            voucherRedeemValue.setDisable(true);
            tswShowAllOffers.setVisible(TRUE);
            lblShowRewards.setVisible(TRUE);
            dirty.setValue(true);
        });

        int maxCharacters = 1;

        textFieldListener(magneticLeading);
        textFieldListener(magneticTrailing);
        textFieldListener(giftCardPrefix);
        textFieldListener(loyaltyCardPrefix);
        textFieldListener(earnXPoints);
        textFieldListener(voucherPoints);
        textFieldListener(voucherRedeemValue);

        magneticLeading.setOnKeyTyped(event -> {
            if (magneticLeading.getText().length() >= maxCharacters) {
                event.consume();
                dirty.setValue(true);
            }
        });

        magneticTrailing.setOnKeyTyped(event -> {
            if (magneticTrailing.getText().length() >= maxCharacters) {
                event.consume();
                dirty.setValue(true);
            }
        });

        //dirty.addListener((arg, oldVal, newVal) -> updateEnabled());
        load();

        //check for active cards and deactivate if any found
        if (getLoyaltyCount() > 0) {
            warningLabel.setText(LocalResource.getString("lbl.loyaltyActiveWarning"));
            warningLabel.setVisible(true);
            getXPoints.setDisable(true);
            earnXPoints.setDisable(true);
            voucherXPoints.setDisable(true);
            voucherPoints.setDisable(true);
            voucherValue.setDisable(true);
            voucherRedeemValue.setDisable(true);
            earnx.setDisable(true);
            collectx.setDisable(true);
        }

        dbUser = new DbUser();
        txtBackupdb.setText("chromis_trans");

    }

    // m_GiftCount = new PreparedSentence(session, "select count(*) from giftscards ", null, SerializerReadInteger.INSTANCE); 
    private void updateGiftEnabled() {
        if (tswGiftEnabled.isSelected() && (getGiftCount() == 0)) {
            giftCardPrefix.setDisable(false);
            lblGiftCardPrefix.setDisable(false);
            dirty.setValue(true);
        } else {
            giftCardPrefix.setDisable(true);
            lblGiftCardPrefix.setDisable(true);
            dirty.setValue(true);
        }
    }

    private void updateEnabled() {
        if (tswLoyaltyEnabled.isSelected() && (getLoyaltyCount() == 0)) {
            if (SystemProperty.LOYALTYTYPE.equalsIgnoreCase("earnx")) {
                earnx.setSelected(true);
                earnx.setDisable(false);
                collectx.setDisable(false);
                getXPoints.setDisable(false);
                earnXPoints.setDisable(false);
                voucherXPoints.setDisable(false);
                voucherPoints.setDisable(false);
                voucherValue.setDisable(false);
                voucherRedeemValue.setDisable(false);
                loyaltyCardPrefix.setDisable(false);
                lblLoyaltyCardPrefix.setDisable(false);
                lblLoyaltyCustomer.setDisable(false);
                lblCustomerMessage.setDisable(false);
                lblAccountEnabled.setDisable(false);
                lblShowRewards.setDisable(false);
                lblScheme.setDisable(false);
                tswAccountLoyalty.setDisable(false);
                tswShowAllOffers.setDisable(false);
                tswRegisterCustomer.setDisable(false);
            } else {
                collectx.setSelected(true);
                collectx.setDisable(false);
                earnx.setDisable(false);
                getXPoints.setDisable(true);
                earnXPoints.setDisable(true);
                voucherXPoints.setDisable(true);
                voucherPoints.setDisable(true);
                voucherValue.setDisable(true);
                voucherRedeemValue.setDisable(true);
                loyaltyCardPrefix.setDisable(false);
                lblLoyaltyCardPrefix.setDisable(false);
                lblLoyaltyCustomer.setDisable(false);
                lblCustomerMessage.setDisable(false);
                lblAccountEnabled.setDisable(false);
                lblShowRewards.setDisable(false);
                lblScheme.setDisable(false);
                tswAccountLoyalty.setDisable(false);
                tswShowAllOffers.setDisable(false);
                tswRegisterCustomer.setDisable(false);
            }
            dirty.setValue(true);
        } else {
            getXPoints.setDisable(true);
            earnXPoints.setDisable(true);
            voucherXPoints.setDisable(true);
            voucherPoints.setDisable(true);
            voucherValue.setDisable(true);
            voucherRedeemValue.setDisable(true);
            earnx.setDisable(true);
            collectx.setDisable(true);
            loyaltyCardPrefix.setDisable(true);
            lblLoyaltyCardPrefix.setDisable(true);
            lblLoyaltyCustomer.setDisable(true);
            lblCustomerMessage.setDisable(true);
            lblScheme.setDisable(true);
            lblAccountEnabled.setDisable(true);
            lblShowRewards.setDisable(true);
            tswAccountLoyalty.setDisable(true);
            tswShowAllOffers.setDisable(true);
            tswRegisterCustomer.setDisable(true);
            dirty.setValue(true);
        }

        if (tswLoyaltyEnabled.isSelected()) {
            lblMessage.setDisable(false);
            lblBackupDB.setDisable(false);
            txtBackupdb.setDisable(false);
            btnCreateBackup.setDisable(false);
            btnbackupTest.setDisable(false);
        } else {
            lblMessage.setDisable(true);
            lblBackupDB.setDisable(true);
            txtBackupdb.setDisable(true);
            btnCreateBackup.setDisable(true);
            btnbackupTest.setDisable(true);
        }

    }

    private void textFieldListener(TextField field) {
        field.textProperty().addListener((new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                dirty.setValue(true);
            }
        }));
    }

    @Override
    public void load() {
        magneticLeading.setText(SystemProperty.MAGNETICLEADING);
        magneticTrailing.setText(SystemProperty.MAGNETICTRAILING);

        if (SystemProperty.LOYALTYTYPE.equalsIgnoreCase("earnx")) {
            earnx.setSelected(true);
            getXPoints.setDisable(true);
            earnXPoints.setDisable(true);
            voucherXPoints.setDisable(true);
            voucherPoints.setDisable(true);
            voucherValue.setDisable(true);
            voucherRedeemValue.setDisable(true);
        } else {
            collectx.setSelected(true);
            getXPoints.setDisable(true);
            earnXPoints.setDisable(true);
            voucherXPoints.setDisable(true);
            voucherPoints.setDisable(true);
            voucherValue.setDisable(true);
            voucherRedeemValue.setDisable(true);
        }

        earnXPoints.setText(String.valueOf(SystemProperty.EARNXPOINTS));
        voucherPoints.setText(String.valueOf(SystemProperty.VOUCHERPOINTS));
        voucherRedeemValue.setText(String.valueOf(SystemProperty.REDEEMVALUE));
        loyaltyCardPrefix.setText(SystemProperty.LOYALTYSTART);
        tswAccountLoyalty.setSelected(SystemProperty.LOYALTYONACCOUNTENABLED);
        tswRegisterCustomer.setSelected(SystemProperty.REGISTERCUSTOMER);

        giftCardPrefix.setText(SystemProperty.GIFTCARDSTART);
        tswGiftEnabled.setSelected(SystemProperty.GIFTCARDSENABLED);
        tswGiftEnabled.selectedProperty().addListener((arg, oldVal, newVal) -> updateGiftEnabled());
        updateGiftEnabled();

        tswLoyaltyEnabled.setSelected(SystemProperty.LOYALTYENABLED);

        tswShowAllOffers.setSelected(SystemProperty.SHOWALLOFFERS);
        tswLoyaltyEnabled.selectedProperty().addListener((arg, oldVal, newVal) -> updateEnabled());
        updateEnabled();

        dirty.setValue(false);
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("MAGNETICLEADING", magneticLeading.getText());
        SystemProperty.updatePropertyValueByConstant("MAGNETICTRAILING", magneticTrailing.getText());
        SystemProperty.updatePropertyValueByConstant("LOYALTYTYPE", (earnx.isSelected()) ? "earnx" : "collectx");
        SystemProperty.updatePropertyValueByConstant("EARNXPOINTS", earnXPoints.getText());
        SystemProperty.updatePropertyValueByConstant("VOUCHERPOINTS", voucherPoints.getText());
        SystemProperty.updatePropertyValueByConstant("REDEEMVALUE", voucherRedeemValue.getText());
        SystemProperty.updatePropertyValueByConstant("LOYALTYENABLED", tswLoyaltyEnabled.isSelected());
        SystemProperty.updatePropertyValueByConstant("LOYALTYSTART", loyaltyCardPrefix.getText());
        SystemProperty.updatePropertyValueByConstant("GIFTCARDSENABLED", tswGiftEnabled.isSelected());
        SystemProperty.updatePropertyValueByConstant("GIFTCARDSTART", giftCardPrefix.getText());
        SystemProperty.updatePropertyValueByConstant("SHOWALLOFFERS", tswShowAllOffers.isSelected());
        SystemProperty.updatePropertyValueByConstant("LOYALTYONACCOUNTENABLED", tswAccountLoyalty.isSelected());
        SystemProperty.updatePropertyValueByConstant("REGISTERCUSTOMER", tswRegisterCustomer.isSelected());

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

    private int getGiftCount() {
        try {
            ConnectionFactory.getInstance().getConnection();
            String sql = "select count(*) from giftcards ";
            Statement stmt = ConnectionFactory.getInstance().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int rowCount = 0;
            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
            return rowCount;
        } catch (SQLException ex) {
        }
        return 0;
    }

    private int getLoyaltyCount() {
        try {
            ConnectionFactory.getInstance().getConnection();
            String sql = "select count(*) from loyaltycards ";
            Statement stmt = ConnectionFactory.getInstance().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int rowCount = 0;
            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
            return rowCount;
        } catch (SQLException ex) {
        }
        return 0;
    }

    private Task tryConnection(DbUser dbUser, Boolean dbDialog) {
        latchToWaitForConnectionTest = new CountDownLatch(1);

        return new Task() {
            @Override
            protected Void call() throws Exception {
                result = DbUtils.connectionTest(dbUser);
                if (dbDialog) {
                    doTest.cancel(true);
                }
                return null;
            }
        ;
    }

    ;
    } 
    
    public void doConnectionTest() {
        //if (txtBackupdb.getText().isBlank()) {
        if (txtBackupdb.getText() == null || txtBackupdb.getText().equals("")) {
            showAlert(JAlertPane.INFORMATION,
                    null,
                    LocalResource.getString("alert.backDatabase"),
                    LocalResource.getString("alert.databaseNameEmpty"),
                    JAlertPane.OK_OPTION);
            return;
        }
        dbUser.setDatabaseName(txtBackupdb.getText().toLowerCase().trim());
        doDBWork(false);
    }

    private void doDBWork(Boolean createNewDB) {
        Platform.runLater(() -> {
            pb.requestFocus();
        });

        pb.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        lblProgressMsg.setText(LocalResource.getString("msg.attemptingConnection"));
        lblProgressMsg.setTextFill(Color.web("#000000"));
        doTest = tryConnection(dbUser, true);
        new Thread(doTest).start();
        doTest.setOnCancelled((Event e) -> {
            pb.setProgress(0);
            if ((Boolean) result[0]) {
                pb.setProgress(100);
                if (createNewDB) {
                    if ((!(Boolean) result[3] && result[4].equals("")) || (!(Boolean) result[3])) {
                        showAlert(JAlertPane.WARNING,
                                null,
                                LocalResource.getString("alert.connectionHeader"),
                                LocalResource.getString("alert.databaseNotEmpty"),
                                JAlertPane.OK_OPTION);
                        pb.setProgress(0);
                        lblProgressMsg.setText(LocalResource.getString("msg.createDatabaseFailed"));
                        lblProgressMsg.setTextFill(Color.web("#ff0000"));
                    } else {
                        //OK to create the database
                        doCreate = createdb((Connection) result[2]);
                        pb.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                        lblProgressMsg.setText(LocalResource.getString("msg.creatingDatabase"));
                        lblProgressMsg.setTextFill(Color.web("#000000"));
                        new Thread(doCreate).start();
                        doCreate.setOnCancelled((Event e1) -> {
                            String content = "";
                            if (dbBuilt) {
                                showAlert(JAlertPane.INFORMATION,
                                        null,
                                        LocalResource.getString("alert.createNewDatabase"),
                                        LocalResource.getString("alert.newDatabaseReady"),
                                        JAlertPane.OK_OPTION);
                            } else {
                                showAlert(JAlertPane.WARNING,
                                        null,
                                        LocalResource.getString("alert.createNewDatabase"),
                                        LocalResource.getString("alert.createDatabaseFailed"),
                                        JAlertPane.OK_OPTION);
                            }
                        });
                    }
                } else {
                    if (!(Boolean) result[3] && result[4].equals("")) {
                        showAlert(JAlertPane.INFORMATION,
                                null,
                                LocalResource.getString("alert.connectionHeader"),
                                LocalResource.getString("alert.databaseNotChromis"),
                                JAlertPane.OK_OPTION);
                    } else if ((Boolean) result[3]) {
                        showAlert(JAlertPane.INFORMATION,
                                null,
                                LocalResource.getString("alert.connectionHeader"),
                                LocalResource.getString("alert.databaseEmpty"),
                                JAlertPane.OK_OPTION);
                    } else if ((Boolean) result[0]) {
                        showAlert(JAlertPane.INFORMATION,
                                null,
                                LocalResource.getString("alert.connectionHeader"),
                                LocalResource.getString("alert.databaseGood") + result[4],
                                JAlertPane.OK_OPTION);
                    }
                    lblProgressMsg.setText(LocalResource.getString("msg.connectionSuccessful"));
                    lblProgressMsg.setTextFill(Color.web("#000000"));
                }
            } else {
                pb.setProgress(0);
                showAlert(JAlertPane.ERROR,
                        null,
                        LocalResource.getString("alert.connectionFailed"),
                        result[1].toString(),
                        JAlertPane.OK_OPTION);
                lblProgressMsg.setText(LocalResource.getString("msg.connectionUnsuccessful"));
                lblProgressMsg.setTextFill(Color.web("#FF0000"));
            }
        });
    }

    private int showAlert(int alertType, String title, String header, String context, int option) {
        Alert alert = null;
        switch (alertType) {
            case 0:
                alert = new Alert(Alert.AlertType.WARNING);
                break;
            case 1:
                alert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case 2:
                alert = new Alert(Alert.AlertType.ERROR);
                break;
            default:
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                break;
        }

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.initModality(Modality.APPLICATION_MODAL);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(LocalResource.cssResource);
        dialogPane.getStyleClass().add("myDialog");

        alert.showAndWait();

        return 0;
    }

    private Task createdb(Connection connection) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                dbBuilt = true;
                try {
                    PreparedStatement pstmt;
                    String SQL = "create table if not exists `databasechangelog` ("
                            + " `id` varchar(255) not null,"
                            + " `author` varchar(255) not null,"
                            + " `filename` varchar(255) not null,"
                            + " `dateexecuted` datetime not null,"
                            + " `orderexecuted` int(11) not null,"
                            + " `exectype` varchar(10) not null,"
                            + " `md5sum` varchar(35) default null,"
                            + " `description` varchar(255) default null,"
                            + " `comments` varchar(255) default null,"
                            + " `tag` varchar(255) default null,"
                            + " `liquibase` varchar(20) default null,"
                            + " `contexts` varchar(255) default null,"
                            + " `labels` varchar(255) default null,"
                            + " `deployment_id` varchar(10) default null"
                            + ") engine=innodb default charset=utf8mb4";
                    pstmt = connection.prepareStatement(SQL);
                    pstmt.executeUpdate();

                    SQL = "create table if not exists `databasechangeloglock` ("
                            + " `id` int(11) not null,"
                            + " `locked` bit(1) not null,"
                            + " `lockgranted` datetime default null,"
                            + " `lockedby` varchar(255) default null,"
                            + " primary key (`id`)"
                            + " ) engine=innodb default charset=utf8mb4";
                    pstmt = connection.prepareStatement(SQL);
                    pstmt.executeUpdate();

                    String changelog = "uk/chromis/pos/liquibase/create/new_cardbackup.xml";
                    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    database.setDatabaseChangeLogTableName("databasechangelog");
                    database.setDatabaseChangeLogLockTableName("databasechangeloglock");
                    Liquibase liquibase = new Liquibase(changelog, new ClassLoaderResourceAccessor(), database);
                    liquibase.setChangeLogParameter("dbName", txtBackupdb.getText());
                    liquibase.update("implement");

                } catch (DatabaseException ex) {
                    dbBuilt = false;
                } catch (LiquibaseException ex) {
                    dbBuilt = false;
                }

                Platform.runLater(() -> {
                    if (dbBuilt) {
                        pb.setProgress(100);
                        lblProgressMsg.setText(LocalResource.getString("alert.newDatabaseReady"));
                        lblProgressMsg.setTextFill(Color.web("#000000"));
                    } else {
                        pb.setProgress(0);
                        lblProgressMsg.setText(LocalResource.getString("msg.createDatabaseFailed"));
                        lblProgressMsg.setTextFill(Color.web("#ff0000"));
                    }
                });
                doCreate.cancel(true);
                return null;
            }
        ;
    }

    ;
}    

    public void createBackup() {
        //if (txtBackupdb.getText().isBlank()) {
        if (txtBackupdb.getText() == null || txtBackupdb.getText().equals("")) {
            showAlert(JAlertPane.INFORMATION,
                    null,
                    LocalResource.getString("alert.backDatabase"),
                    LocalResource.getString("alert.databaseNameEmpty"),
                    JAlertPane.OK_OPTION);
            return;
        }

        dbUser.setDatabaseName(txtBackupdb.getText().toLowerCase().trim());
        doConnectionTest();
        doDBWork(true);
    }

}
