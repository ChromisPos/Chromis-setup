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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.chromis.pos.forms.DriverWrapper;

/**
 *
 * @author John
 */
public class DbConnection {

    private static StringBuilder newUrl = new StringBuilder();
    private static Connection conn = null;

    public static Connection getConnection(String dbEngine, String server, String port, String dbName, String driverClass, String dbDriver, String userName, String password) {
        newUrl.append("jdbc:mysql://");
        newUrl.append(server);
        newUrl.append(":");
        newUrl.append(port);
        newUrl.append("/");
        newUrl.append(dbName);
        newUrl.append("?useSSL=false");

        try {
            ClassLoader cloader = new URLClassLoader(new URL[]{new File(dbDriver).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(driverClass, true, cloader).getDeclaredConstructor().newInstance()));
            establishConnection(newUrl.toString(), userName, password);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | MalformedURLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public static Connection getConnection(DbUser dbUser) {
        conn = null;
        try {
            ClassLoader cloader = new URLClassLoader(new URL[]{new File(dbUser.getDbLibrary()).toURI().toURL()});
            DriverWrapper driverWrapper = new DriverWrapper((Driver) Class.forName(dbUser.getDbClass(), true, cloader).getDeclaredConstructor().newInstance());
            DriverManager.registerDriver(driverWrapper);
            establishConnection(dbUser.getURL(), dbUser.getUserName(), dbUser.getUserPassword());
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | MalformedURLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException ex) {
            conn = null;
        }
        return conn;
    }

    private static void establishConnection(String url, String userName, String password) {
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }
}
