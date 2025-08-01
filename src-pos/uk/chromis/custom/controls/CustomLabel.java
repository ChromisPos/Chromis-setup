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
package uk.chromis.custom.controls;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import uk.chromis.globals.IconFactory;

/**
 *
 * @author John
 */
public class CustomLabel extends HBox {

    private final Label label = new Label();
    private final ImageView selectedImage = new ImageView();

    public CustomLabel(String text) {
        setPrefHeight(35);
        setPrefWidth(210);

        label.getStyleClass().add("menu-label");
        label.setPrefHeight(35);
        label.setPrefWidth(210);
        label.setText(text);               
        
        selectedImage.setVisible(false);
        selectedImage.setImage(IconFactory.getImage("selected.png"));
        getChildren().addAll(label, selectedImage);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setSelected(Boolean value) {
        selectedImage.setVisible(value);
    }
}
