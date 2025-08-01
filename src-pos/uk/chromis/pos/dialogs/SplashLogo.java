/*
**    Chromis Setup Config  - Open Source Point of Sale
**
**    This file is part of Chromis Setup Config Version Chromis V1.5.4
**
**    Copyright (c) 2015-2023 Chromis   
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


package uk.chromis.pos.dialogs;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import uk.chromis.globals.IconFactory;

/**
 *
 * @author John
 */
public class SplashLogo extends JFrame {
    
    private final JPanel splashPanel = new JPanel(new MigLayout("insets 3 0 3 0", "[][]", "[]0[50:20:50]"));
    private final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private final int centreX = (int) primaryScreenBounds.getWidth() / 2;
    private final int centreY = (int) primaryScreenBounds.getHeight() / 2;
    
    public SplashLogo() {
        createPage();
        setLocation(centreX - 280, centreY - 135);
        setSize(560, 280);
        setUndecorated(true);
        add(splashPanel);
        setVisible(true);
    }
    
    private void createPage() {
        String currentPath = System.getProperty("user.dir");
        JLabel imageLabel = new JLabel();
        JLabel textLabel = new JLabel();
        JLabel spinning = new JLabel();
        JLabel version = new JLabel();
        imageLabel.setIcon(IconFactory.getIcon("verchromislogo.png"));
        textLabel.setIcon(IconFactory.getIcon("starttext1.png"));
        spinning.setIcon(IconFactory.getIcon("spinning.gif"));              
        
        splashPanel.add(imageLabel, "height ::157, span, wrap");
        splashPanel.add(version, "height ::20, span, wrap, align right");
        splashPanel.add(textLabel, "width ::500 , height :45:");
        
        splashPanel.add(spinning, "height :50:");
        splashPanel.setBackground(Color.white);
        splashPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        spinning.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });
    }
    
    public void deleteSplashLogo() {
        this.setVisible(false);
        this.dispose();
    }
}
