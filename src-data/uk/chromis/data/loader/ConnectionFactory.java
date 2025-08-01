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


package uk.chromis.data.loader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.chromis.commons.dbmanager.DbUser;
import uk.chromis.pos.forms.AppConfig;
import uk.chromis.pos.forms.DriverWrapper;

/**
 *
 * @author John
 */
public class ConnectionFactory {

    private static ConnectionFactory INSTANCE = new ConnectionFactory();
    private static Connection connection;

    private ConnectionFactory() {
    }

    public static ConnectionFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (ConnectionFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConnectionFactory();
                }
            }
        }
        return INSTANCE;
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            ClassLoader cloader = new URLClassLoader(new URL[]{new File(AppConfig.getDatabaseLibraryPath()).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(AppConfig.getDatabaseProviderClass(), true, cloader).getDeclaredConstructor().newInstance()));
            connection = (Connection) DriverManager.getConnection(AppConfig.getDatabaseURL(), AppConfig.getDatabaseUser(), AppConfig.getClearDatabasePassword());
            return connection;
        } catch (SQLException ex) {
        } catch (SecurityException | IllegalArgumentException | ClassNotFoundException | MalformedURLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Connection getConnection(DbUser dbUser) {
        if (connection != null) {
            return connection;
        }
        try {
            ClassLoader cloader = new URLClassLoader(new URL[]{new File(dbUser.getDbLibrary()).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(dbUser.getDbClass(), true, cloader).getDeclaredConstructor().newInstance()));
            connection = (Connection) DriverManager.getConnection(dbUser.getURL(), dbUser.getUserName(), dbUser.getUserPassword());
            return connection;
        } catch (SQLException ex) {
//            System.out.println("Unable to connect to database. Bad connection details");
//            System.out.println(ex.getSQLState());
//            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException | IllegalArgumentException | ClassNotFoundException | MalformedURLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
