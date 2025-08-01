/*
 **    Chromis PRO - Fully Featured Point of Sale
 **
 **    This file is part of Chromis POS Version Chromis V1.5.2
 **    Copyright (c)2015-2023
 **
 **    https://www.chromis.co.uk
 **
 */
package uk.chromis.database;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.tinylog.Logger;

import java.sql.*;
import uk.chromis.pos.forms.DriverWrapper;

public class DatabaseConnection {

    private Connection connection;
    private String userName = null;
    private String password = null;
    private String mariaDbURL = null;
    private String mySqlURL = null;

    //makes an assumption that mysql jdbc is used
    public DatabaseConnection(String server, String port, String userName, String password) {
        mariaDbURL = "jdbc:mariadb://" + server + ":" + port + "/";
        mySqlURL = "jdbc:mysql://" + server + ":" + port + "/";
        connection = getConnection(mySqlURL, "com.mysql.cj.jdbc.Driver", userName, password);
        this.userName = userName;
        this.password = password;
    }

    public Boolean isGoodVersion() {
        if (connection == null) return false;
        
        //try for mariadb first using mariadb jdbc connector
        try {
            DatabaseMetaData metaMySQL = connection.getMetaData();
            if (metaMySQL.getDatabaseProductVersion().contains("MariaDB")) {
                Connection mariaDB = getConnection(mariaDbURL, "org.mariadb.jdbc.Driver", userName, password);
                if (mariaDB != null) {
                    try {
                        DatabaseMetaData metaMariaDB = mariaDB.getMetaData();
                        if (metaMariaDB.getDatabaseMajorVersion() >= 10) {
                            return true;
                        }
                    } catch (SQLException e) {
                        return false;
                    }
                }
            }

            if (metaMySQL.getDatabaseMajorVersion() >= 8) {
                return true;
            }
        } catch (SQLException ignored) {
        }

        Connection mySQL = getConnection(mySqlURL, "com.mysql.cj.jdbc.Driver", userName, password);
        // check the MySql version
        try {
            DatabaseMetaData meta = mySQL.getMetaData();
            if (meta.getDatabaseMajorVersion() >= 8) {
                return true;
            }
        } catch (SQLException ignored) {
        }
        return false;
    }

    private Connection getConnection(String url, String dbClass, String userName, String password) {
        Logger.info("get database connection");
        try {
            ClassLoader cloader = new URLClassLoader(new URL[]{new File(dbClass).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(dbClass, true, cloader).getDeclaredConstructor().newInstance()));
            connection = (Connection) DriverManager.getConnection(url, userName, password);
            DriverManager.setLoginTimeout(1);
        } catch (SQLException ex) {
            System.out.println("In error");
        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {

        }
        return connection;
    }
}
