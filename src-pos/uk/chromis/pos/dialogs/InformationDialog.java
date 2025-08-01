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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import uk.chromis.globals.IconFactory;

/**
 * @author John Lewis
 */
public class InformationDialog extends JDialog {

    private JPanel mainPanel;
    private JPanel logoPanel;
    private JPanel btnPanel;

    private Image img;
    private JTextArea contextArea;
    private JTextArea headerTextArea;
    private JLabel headerText;
    private JLabel iconLabel = new JLabel();

    private JSeparator separator = new JSeparator();
    private Font font;

    protected InformationDialog(Boolean showLogo, JPanel content, Boolean unDecorated, Boolean border) {
        super(new JFrame());
        infoDialog(showLogo, content, unDecorated, border);
        pack();
    }

    protected InformationDialog(Boolean showLogo, JPanel content, Boolean unDecorated) {
        super(new JFrame());
        infoDialog(showLogo, content, unDecorated, false);
        pack();
    }

//    protected InformationDialog(int type, String strTitle, String strHeaderText, String strContextText, int buttons) {
//        super(new JFrame(), strTitle);
//        separator.setOrientation(JSeparator.HORIZONTAL);
//        alertDialog(type, strHeaderText, strContextText, buttons);
//        pack();
//    }
//    private void alertDialog(int type, String headerText, String contextText, int buttons) {
//        alertDialog(type, headerText, contextText, buttons, false);
//    }
    private void infoDialog(Boolean showLogo, JPanel content, Boolean unDecorated, Boolean border) {
        mainPanel = new JPanel(new MigLayout("insets 10 4 5 2"));
        btnPanel = new JPanel();

        if (border) {
            mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }

        if (showLogo) {
            logoPanel = new JPanel(new MigLayout("", "[500]"));
            JLabel imageLabel = new JLabel(IconFactory.getIcon("chromislogo_small.png"));
            logoPanel.add(imageLabel, "align left");
            mainPanel.add(logoPanel, "wrap");
        }

        mainPanel.add(content, "width 500, wrap");

        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(exit, "width 500, wrap, align center");

        //Set the dialog with no minimize or expand icons on title bar
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                dispose();
            }
        });

        setAlwaysOnTop(true);
        setResizable(false);
        setModal(true);
        setUndecorated(unDecorated);

        String laf = UIManager.getLookAndFeel().getClass().toString();
        if ((!UIManager.getLookAndFeel().getClass().toString().contains("com.jtattoo.plaf"))
                && (!UIManager.getLookAndFeel().getClass().toString().contains("com.alee.laf"))) {
            if (unDecorated) {
                logoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            }
        }

        getContentPane().add(mainPanel);

    }

}
