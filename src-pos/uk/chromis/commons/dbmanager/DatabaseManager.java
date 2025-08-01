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




package uk.chromis.commons.dbmanager;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javax.swing.JFrame;
import uk.chromis.pos.dialogs.JAlertPane;
import uk.chromis.pos.forms.AppConfig;
import uk.chromis.pos.forms.LocalResource;

/**
 *
 * @author John
 */
public class DatabaseManager {

    private Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    private final int centreX = (int) primaryScreenBounds.getWidth() / 2;
    private final int centreY = (int) primaryScreenBounds.getHeight() / 2;
    public static JFrame uframe;
    private final JFXPanel fxPanel = new JFXPanel();
    private String sDbVersion;
    private DbUser dbUser;

    public static CountDownLatch waitForUpdate;
    public static CountDownLatch waitForConnection;

    private String cssResource;
    private  Object[] result;

    public DatabaseManager() {
        try {
            cssResource = Paths.get(System.getProperty("user.dir") + "/cssStyles/chromis.css").toUri().toURL().toExternalForm();
        } catch (MalformedURLException ex) {
            System.out.println("Uanble to find CSS file");
            System.exit(-1);
        }
    }

    public void checkDatabase() {
        waitForUpdate = new CountDownLatch(1);
        uframe = new JFrame();
        uframe.setUndecorated(true);
        uframe.add(fxPanel);
        uframe.setLocation(centreX - 157, centreY - 50);
        uframe.setSize(315, 100);
        uframe.setVisible(false);
        uframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dbUser = new DbUser();
        dbUser.getUserDetails();

        //Test for a connection and wait for result in Object array
        waitForConnection = new CountDownLatch(1);
        result = DbUtils.connectionTest(dbUser, true);

        try {
            waitForConnection.await();
        } catch (InterruptedException ex) {
        }

        if (((!(Boolean) result[0]) || ((Boolean) result[3] && result[1].equals(""))
                || (result[4].equals("") || (int) result[8] != 1 || !result[6].equals("chromispos") || !result[7].equals("chromis pos")))) {
            JAlertPane.showAlertDialog(JAlertPane.INFORMATION,
                    null,
                    LocalResource.getString("alert.invalidDatabase"),
                    LocalResource.getString("alert.runConfig"),
                    JAlertPane.OK_OPTION, true);
        //    splash.deleteSplashLogo();
            return;
        }

        if (!(Boolean) result[0]) {
            if (JAlertPane.showAlertDialog(JAlertPane.ERROR,
                    null,
                    LocalResource.getString("alert.connectionFailed"),
                    result[1].toString()
                    + "\n\n" + LocalResource.getString("alert.openConfiguration"),
                    JAlertPane.YES_NO_OPTION, true) == 6) {
                System.exit(0);
            } 
        } else if ((Boolean) result[3] && result[1].equals("")) {
            System.out.println("Line 135");
            if (JAlertPane.showAlertDialog(JAlertPane.INFORMATION,
                    null,
                    LocalResource.getString("alert.connectionHeader"),
                    LocalResource.getString("alert.databaseEmpty")
                    + "\n\n" + LocalResource.getString("alert.openConfiguration"),
                    JAlertPane.YES_NO_OPTION, true) == 6) {

                System.exit(0);
            }
        } else {
            // The database found was not identified as Chromis
            if (result[4].equals("") || (int) result[8] != 1 || !result[6].equals("chromispos") || !result[7].equals("chromis pos")) {
                JAlertPane.showAlertDialog(JAlertPane.INFORMATION,
                        null,
                        LocalResource.getString("alert.connectionHeader"), // change this message
                        LocalResource.getString("alert.databaseNotChromis"),
                        JAlertPane.OK_OPTION, true);
                System.exit(0);
            }
        }
    }

    
    public Boolean isConnected(){
        return  result[2] !=null ;
    }
    




}
