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
package uk.chromis.start;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.tinylog.Logger;
import uk.chromis.globals.IconFactory;
import uk.chromis.commons.dbmanager.DatabaseManager;
import uk.chromis.commons.dbmanager.DbUser;
import uk.chromis.pos.dialogs.JAlertPane;
import uk.chromis.pos.dialogs.SplashLogo;
import uk.chromis.pos.forms.ChromisFonts;
import uk.chromis.pos.forms.LocalResource;

/**
 * FXML Controller class
 *
 * @author John
 */
public class ChromisSystemConfig extends Application {

    private final DbUser dbUser = new DbUser();
    private String os;
    private String cssResource;
    private final SplashLogo splash = new SplashLogo();
    private double xOffset = 0;
    private double yOffset = 0;
    public static Boolean newScreen = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //    Font.loadFont(getClass().getResourceAsStream("/uk/chromis/fonts/LiberationSerif-Bold.ttf"), 16);
        //    Font.loadFont(getClass().getResourceAsStream("/uk/chromis/fonts/LiberationSerif-Regular.ttf"), 16);
        //    Font.loadFont(getClass().getResourceAsStream("/uk/chromis/fonts/LiberationSerif-BoldItalic.ttf"), 16);
        //    Font.loadFont(getClass().getResourceAsStream("/uk/chromis/fonts/LiberationSerif-Italic.ttf"), 16);

        Font.loadFont("file:" + System.getProperty("user.dir") + "/fonts/LiberationSerif-Bold.ttf", 16);
        Font.loadFont("file:" + System.getProperty("user.dir") + "/fonts/LiberationSerif-Regular.ttf", 16);
        Font.loadFont("file:" + System.getProperty("user.dir") + "/fonts/LiberationSerif-BoldItalic.ttf", 16);
        Font.loadFont("file:" + System.getProperty("user.dir") + "/fonts/LiberationSerif-Italic.ttf", 16);

        System.setProperty("tinylog.configuration", "setuplog.properties");
        Logger.info("Starting Chromis System Config ... ");
        if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            os = "Linux";
        } else if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            os = "Windows";
        } else {
            os = "OSX";
        }

        DatabaseManager dbMan = new DatabaseManager();
        dbMan.checkDatabase();
        if (!dbMan.isConnected()) {
            System.exit(0);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/uk/chromis/start/MainPanel.fxml"));
            MainPanelController system = new MainPanelController(dbUser);
            loader.setController(system);
            Scene scene = new Scene(loader.load(), 950, 760);
            File cssFile = new File(System.getProperty("user.dir")+ "/" + LocalResource.cssResource);
            scene.getStylesheets().add(cssFile.toURI().toURL().toExternalForm());

            //  scene.getStylesheets().add(LocalResource.cssResource);
        //    system.setCSSString(LocalResource.cssResource);
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setResizable(false);

            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            });

            primaryStage.show();
            splash.deleteSplashLogo();
        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("Log error for bad scene - SystemSetupDialog");
            System.exit(1);
        }

    }

    public static void main(String[] args) {


        if (args.length > 0 && args[0].equalsIgnoreCase("-newscreen")) {
            newScreen = true;
        }

        ChromisFonts.setFont("Liberation Serif");
        setUIFont(new FontUIResource(ChromisFonts.DEFAULTFONT));

        Path p = Paths.get("chromisposconfig.properties");
        if (Files.notExists(p)) {
            JAlertPane.showAlertDialog(JAlertPane.WARNING,
                    null,
                    "Unable to locate 'chromisposconfig.properties'",
                    "Run 'Chromis Terminal Config' or 'ChromisDB Creator' to resolve.",
                    JAlertPane.OK_OPTION, true);
            System.exit(-1);
        }

        IconFactory.cacheIconsFromFolder("configuration");

        String currentPath = null;
        currentPath = System.getProperty("user.dir");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd-HHmm-");
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("/debug")) {
                //send output to log files
                try {
                    System.setErr(new PrintStream(new FileOutputStream(currentPath + "/Logs/" + simpleDateFormat.format(new Date()) + "Config.log")));
                } catch (FileNotFoundException ex) {

                }
            }
        }

        launch(args);

    }

    private static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}
