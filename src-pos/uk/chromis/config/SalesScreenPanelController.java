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
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import uk.chromis.globals.IconFactory;
import uk.chromis.globals.SystemProperty;

/**
 * FXML Controller class
 *
 * @author John.Lewis
 */
public class SalesScreenPanelController implements Initializable, BaseController {

    public ImageView image0;
    public ImageView image1;

    public ToggleGroup screenLayout;

    public RadioButton rbtnLayout0;
    public RadioButton rbtnLayout1;

    public Pane paneImage0;
    public Pane paneImage1;

    private Image image;
    private String strLayout;

    public BooleanProperty dirty = new SimpleBooleanProperty();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        load();

        image0.setImage(IconFactory.getImage("default.png"));
        image1.setImage(IconFactory.getImage("layout1.png"));

        paneImage0.setOnMouseClicked((MouseEvent event) -> rbtnLayout0.fire());

        paneImage1.setOnMouseClicked((MouseEvent event) -> rbtnLayout1.fire());

        rbtnLayout0.setOnAction(event -> strLayout = "Layout0");

        rbtnLayout1.setOnAction(event -> strLayout = "Layout1");

        screenLayout.selectedToggleProperty().addListener((obs, oldValue, newValue) -> dirty.setValue(true));

        load();

    }

    @Override
    public void load() {
        strLayout = SystemProperty.SALESLAYOUT;
        switch (strLayout) {
            case "Layout1":
                screenLayout.selectToggle(rbtnLayout1);
                break;
            default:
                screenLayout.selectToggle(rbtnLayout0);
                break;
        }
    }

    @Override
    public void save() {
        SystemProperty.updatePropertyValueByConstant("SALESLAYOUT", strLayout);
        dirty.set(false);
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
