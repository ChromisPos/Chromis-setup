/*
**    Chromis Administration  - Open Source Point of Sale
**
**    This file is part of Chromis Administration Version Chromis V1.5.3
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
package uk.chromis.pos.forms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import uk.chromis.globals.SystemProperty;

/**
 *
 * @author John
 */
public class LocalResource implements Versions {

    public static final String APP_NAME = "Chromis Setup";
    public static final String APP_ID = "chromissetup";

    private static final List<ResourceBundle> localResources = new LinkedList<>();
    public static ResourceBundle resources;
    public static String cssResource ="cssStyles/chromissetup.css";
    private static InputStream propStrStream;
    private static String propertiesFileLocation;

    static {
        resources = getResources("chromissetup");
        localResources.add(resources);
      // cssResource  = System.getProperty("user.dir")+ "/cssStyles/chromissetup.css";
    }

    private static ResourceBundle getResources(String resource) {
        String language = "";
        try {
            language = new Locale(SystemProperty.USERLANGUAGE).toString();
        } catch (ExceptionInInitializerError ex) {
            language = "";
        }

        propertiesFileLocation = System.getProperty("user.dir") + "/locales/" + resource + "_" + language + ".properties";
        //check if file exists first
        File f = new File(propertiesFileLocation);
        if (!f.exists() && !f.isDirectory()) {
            propertiesFileLocation = System.getProperty("user.dir") + "/locales/" + resource + ".properties";
        }

        ResourceBundle newBundle = null;
        try {
            InputStream propStrStream = new FileInputStream(new File(propertiesFileLocation));
            newBundle = new PropertyResourceBundle(propStrStream);
        } catch (FileNotFoundException ex) {
            missingPropertiesFile(propertiesFileLocation);
        } catch (IOException ex) {
            missingPropertiesFile(propertiesFileLocation);
        }
        return newBundle;
    }

    private static void missingPropertiesFile(String resource) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing Properties File");
        alert.setHeaderText("\nUnable to locate file\n\n");
        alert.setContentText(resource);
        alert.showAndWait();
        System.exit(0);
    }

    public static void addResource(String resource) {
        localResources.add(getResources(resource));
    }

    public static String getString(String sKey) {

        if (sKey == null) {
            return null;
        } else {
            for (ResourceBundle r : localResources) {
                try {
                    return r.getString(sKey);
                } catch (MissingResourceException e) {
                }
            }
        }
        return "!! " + sKey + " !!";
    }

    public static String getString(String sKey, Integer length) {
        if (sKey == null) {
            return null;
        } else {
            for (ResourceBundle r : localResources) {
                try {
                    return String.format("%1$-" + length + "s", r.getString(sKey));
                } catch (MissingResourceException e) {
                }
            }
        }
        return "!! " + sKey + " !!";
    }

    public static String getString(Integer length, String sKey) {
        if (sKey == null) {
            return null;
        } else {
            for (ResourceBundle r : localResources) {
                try {
                    return String.format("%1$" + length + "s", r.getString(sKey));
                } catch (MissingResourceException e) {
                }
            }
        }
        return "!! " + sKey + " !!";
    }

    public static String getString(String sKey, Object... sValues) {

        if (sKey == null) {
            return null;
        } else {
            for (ResourceBundle r : localResources) {
                try {
                    return MessageFormat.format(r.getString(sKey), sValues);
                } catch (MissingResourceException e) {
                }
            }
            return "!! " + sKey + " !!";
        }
    }

}
