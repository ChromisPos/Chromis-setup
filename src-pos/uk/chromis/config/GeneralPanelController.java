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

import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import uk.chromis.custom.toggleswitch.ToggleSwitch;
import uk.chromis.globals.IconFactory;
import uk.chromis.globals.SystemProperty;
import uk.chromis.start.ChromisSystemConfig;

/**
 * FXML Controller class
 *
 * @author John
 */
public class GeneralPanelController implements Initializable, BaseController {

    public AnchorPane anchorPane;
    public ComboBox skin;
    public ComboBox screenType;
    public ComboBox swingFont;

    public ToggleSwitch enableNewLogin;

    public Button btnLogo;
    public Button btnClearLogo;

    public Spinner fontSize;
    public Spinner ticketLineSize;

    public ToggleGroup iconColour;
    public RadioButton rbtnBlue;
    public RadioButton rbtnGreen;
    public RadioButton rbtnBlack;
    public RadioButton rbtnRoyalBlue;
    public RadioButton clear;

    public Pane iconPane;
    public Pane paneBlue;
    public Pane paneBlack;
    public Pane paneGreen;
    public Pane paneRoyalBlue;

    public ImageView imgBlue;
    public ImageView imgGreen;
    public ImageView imgBlack;
    public ImageView imgRoyalBlue;

    public Label startUpLogoImage;

    private String strIconColour;
    private File logoFile;
    private Boolean resetImage = false;
    private final LinkedHashMap<String, GeneralPanelController.LAFInfo> lafskins = new LinkedHashMap();

    public BooleanProperty dirty = new SimpleBooleanProperty(false);

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        enableNewLogin.getStyleClass().add("toggleSwitch");
//        anchorPane.getStyleClass().add("customcombo");
        ObservableList<String> skinNames = FXCollections.observableArrayList();
        skinNames.add("Aero");
        lafskins.put("Aero", new GeneralPanelController.LAFInfo("Aero", "com.jtattoo.plaf.aero.AeroLookAndFeel"));
        skinNames.add("Darcula");
        lafskins.put("Darcula", new GeneralPanelController.LAFInfo("Darcula", "com.bulenkov.darcula.DarculaLaf"));
        skinNames.add("Fast");
        lafskins.put("Fast", new GeneralPanelController.LAFInfo("Fast", "com.jtattoo.plaf.fast.FastLookAndFeel"));
        skinNames.add("Graphite");
        lafskins.put("Graphite", new GeneralPanelController.LAFInfo("Graphite", "com.jtattoo.plaf.graphite.GraphiteLookAndFeel"));
        skinNames.add("HiFi");
        lafskins.put("HiFi", new GeneralPanelController.LAFInfo("HiFi", "com.jtattoo.plaf.hifi.HiFiLookAndFeel"));
        skinNames.add("Mint");
        lafskins.put("Mint", new GeneralPanelController.LAFInfo("Mint", "com.jtattoo.plaf.mint.MintLookAndFeel"));

        skin.setItems(skinNames);
        skin.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> dirty.setValue(true));

        ObservableList<String> machineScreens = FXCollections.observableArrayList("window", "fullscreen", "kiosk", "kiosk - full screen");
        screenType.setItems(machineScreens);
        screenType.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> dirty.setValue(true));

        ObservableList<String> fontFamilies = FXCollections.observableArrayList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        swingFont.setItems(fontFamilies);
        swingFont.getItems().add(0, "Chromis Default");
        swingFont.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> dirty.setValue(true));

        SpinnerValueFactory.IntegerSpinnerValueFactory fontSizeFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 24, 16);
        fontSize.setValueFactory(fontSizeFactory);
        fontSizeFactory.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        fontSize.setEditable(true);
        fontSize.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fontSize.increment(0);
            }
        });

        ImageView imageView = new ImageView(IconFactory.getImage("fileopen.png"));
        imageView.setFitHeight(18);
        imageView.setFitWidth(18);
        ImageView imageView2 = new ImageView(IconFactory.getImage("fileopen.png"));
        imageView2.setFitHeight(18);
        imageView2.setFitWidth(18);

        btnLogo.setGraphic(imageView);

        imgBlue.setImage(IconFactory.getImage("blueuser.png"));
        imgGreen.setImage(IconFactory.getImage("greenuser.png"));
        imgRoyalBlue.setImage(IconFactory.getImage("rblueuser.png"));
        imgBlack.setImage(IconFactory.getImage("blackuser.png"));

        paneGreen.setOnMouseClicked((MouseEvent event) -> rbtnGreen.fire());
        paneBlue.setOnMouseClicked((MouseEvent event) -> rbtnBlue.fire());
        paneRoyalBlue.setOnMouseClicked((MouseEvent event) -> rbtnRoyalBlue.fire());
        paneBlack.setOnMouseClicked((MouseEvent event) -> rbtnBlack.fire());

        rbtnGreen.setOnAction(event -> strIconColour = "green");
        rbtnBlue.setOnAction(event -> strIconColour = "blue");
        rbtnRoyalBlue.setOnAction(event -> strIconColour = "royalblue");
        rbtnBlack.setOnAction(event -> strIconColour = "black");

        iconColour.selectedToggleProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

        ticketLineSize.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

        SpinnerValueFactory.IntegerSpinnerValueFactory ticketLineFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(28, 100, 40);
        ticketLineSize.setValueFactory(ticketLineFactory);
        ticketLineFactory.valueProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));
        ticketLineSize.setEditable(true);
        ticketLineSize.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                ticketLineSize.increment(0);
            }
        });

        if (ChromisSystemConfig.newScreen) {
            iconPane.setVisible(false);
        }

        load();

    }

    public void handleSelectLogoFile() {
        FileChooser fileChooser = new FileChooser();
        logoFile = fileChooser.showOpenDialog(null);
        if (logoFile != null) {
            loadImageFromFile(logoFile);
        }
    }

    public void handleClearLogo() {
        startUpLogoImage.setGraphic(null);
        resetImage = true;
        dirty.setValue(true);
    }

    @Override
    public void load() {
        enableNewLogin.setSelected(SystemProperty.NEWLOGIN);
        ticketLineSize.getValueFactory().setValue(SystemProperty.LINESIZE);

        String lafclass = SystemProperty.LAF;
        skin.getSelectionModel().select(null);
        for (int i = 0; i < skin.getItems().size(); i++) {
            LAFInfo lafinfo = lafskins.get(skin.getItems().get(i));
            if (lafinfo.getClassName().equals(lafclass)) {
                skin.getSelectionModel().select(i);
                break;
            }
        }
        screenType.getSelectionModel().select(SystemProperty.SCREENMODE);

        if (SystemProperty.SWINGFONT.isBlank()) {
            swingFont.getSelectionModel().selectFirst();
        } else {
            swingFont.getSelectionModel().select(SystemProperty.SWINGFONT);
        }
        fontSize.getValueFactory().setValue(SystemProperty.SWINGFONTSIZE);

        switch (SystemProperty.ICONCOLOUR) {
            case "blue":
                iconColour.selectToggle(rbtnBlue);
                strIconColour = "blue";
                break;
            case "green":
                iconColour.selectToggle(rbtnGreen);
                strIconColour = "green";
                break;
            case "black":
                iconColour.selectToggle(rbtnBlack);
                strIconColour = "black";
                break;
            case "royalblue":
                iconColour.selectToggle(rbtnRoyalBlue);
                strIconColour = "royalblue";
                break;
            default:
                iconColour.selectToggle(clear);
                strIconColour = "blue";
                break;
        }
        loadImage();
        dirty.setValue(false);
    }

    private void loadImage() {
        BufferedImage image = (BufferedImage) (SystemProperty.STARTLOGO).getImage();
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(400);
        imageView.setFitHeight(200);
        imageView.maxWidth(400);
        imageView.maxHeight(200);
        startUpLogoImage.setAlignment(Pos.CENTER);
        startUpLogoImage.setGraphic(imageView);
        resetImage = false;
    }

    private void loadImageFromFile(File imageFile) {
        BufferedImage in;
        try {
            in = ImageIO.read(imageFile);
            BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();
            ImageView imageView = new ImageView(SwingFXUtils.toFXImage(image, null));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(400);
            imageView.setFitHeight(200);
            imageView.maxWidth(400);
            imageView.maxHeight(200);
            startUpLogoImage.setAlignment(Pos.CENTER);
            startUpLogoImage.setGraphic(imageView);
            resetImage = false;
            dirty.setValue(true);
        } catch (IOException ex) {
            Logger.getLogger(GeneralPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("LINESIZE", ticketLineSize.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("NEWLOGIN", enableNewLogin.isSelected());
        GeneralPanelController.LAFInfo laf = (GeneralPanelController.LAFInfo) lafskins.get(skin.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("LAF", laf.getClassName());
        SystemProperty.updatePropertyValueByConstant("SCREENMODE", screenType.getValue().toString());
        SystemProperty.updatePropertyValueByConstant("ICONCOLOUR", strIconColour);

        if (swingFont.getSelectionModel().isSelected(0)) {
            SystemProperty.updatePropertySetToNull("SWINGFONT");
        } else {
            SystemProperty.updatePropertyValueByConstant("SWINGFONT", swingFont.getValue().toString());
        }
        SystemProperty.updatePropertyValueByConstant("SWINGFONTSIZE", fontSize.getValue().toString());

        if (logoFile != null) {
            SystemProperty.updateSystemBlob("STARTLOGO", logoFile);
        }
        if (resetImage) {
            SystemProperty.resetSystemBlob("STARTLOGO");
        }

        dirty.setValue(false);
    }

    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public Boolean isDirty() {
        return dirty.getValue();
    }

    @Override
    public void setDirty(Boolean value) {
        dirty.setValue(value);
    }

    private static class LAFInfo {

        private final String name;
        private final String classname;

        public LAFInfo(String name, String classname) {
            this.name = name;
            this.classname = classname;
        }

        public String getName() {
            return name;
        }

        public String getClassName() {
            return classname;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
