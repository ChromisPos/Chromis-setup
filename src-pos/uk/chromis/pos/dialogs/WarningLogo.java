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
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import uk.chromis.globals.IconFactory;
import uk.chromis.pos.forms.LocalResource;

/**
 *
 * @author John
 */
public class WarningLogo extends JDialog {

    private final JPanel splashPanel = new JPanel(new MigLayout("insets 3 0 3 0", "[][]", "[]0[50:20:50]"));
    private final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private final int centreX = (int) primaryScreenBounds.getWidth() / 2;
    private final int centreY = (int) primaryScreenBounds.getHeight() / 2;

    public WarningLogo() {
        super((Window) null);
        setModal(true);
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
        // JLabel spinning = new JLabel();
        JButton btnExit = new JButton("Exit");
        JLabel version = new JLabel();
        imageLabel.setIcon(IconFactory.getIcon("verchromislogo.png"));
        textLabel.setIcon(IconFactory.getIcon("runningtext.png"));
        // spinning.setIcon(new javax.swing.ImageIcon(currentPath + "/configuiration/spinning.gif"));

        version.setText("V" + LocalResource.APP_VERSION + "       ");
        version.setForeground(new Color(29, 15, 191));

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/fonts/Courgette-Regular.ttf"));
            font = font.deriveFont(Font.BOLD, 16);
            version.setFont(font);
        } catch (FontFormatException | IOException ex) {
            version.setFont(new Font("Serif", 3, 16));
        }

        splashPanel.add(imageLabel, "height ::157, span, wrap");
        splashPanel.add(version, "height ::20, span, wrap, align right");
        splashPanel.add(textLabel, "width ::480 , height :45:");

        splashPanel.add(btnExit, "width ::200, height :40:");
        splashPanel.setBackground(Color.white);
        splashPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        btnExit.addMouseListener(new MouseAdapter() {
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
