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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.chromis.commons.utils.TerminalInfo;
import uk.chromis.pos.forms.DriverWrapper;
import uk.chromis.pos.forms.LocalResource;
import uk.chromis.pos.util.AltEncrypter;

/**
 *
 *
 */
public class DbUtils {

    private static String errorMsg = "";
    private static Boolean isConnected = false;
    private static Connection connection = null;
    private static Boolean isEmpty = true;
    private static AltEncrypter cypher;
    private static String version = "";
    private static String id = "";
    private static String name = "";
    private static int versionInt = 0;
    private static int rowCount = 0;

    public static Object[] connectionTest(DbUser dbUser) {
        return connectionTest(dbUser, false, false, false);
    }

    public static Object[] connectionTest(DbUser dbUser, Boolean useLatch) {
        return connectionTest(dbUser, useLatch, false, false);
    }

    public static Object[] serverConnectionTest(DbUser dbUser) {
        return connectionTest(dbUser, false, true, false);
    }

    public static Object[] connectionTest(DbUser dbUser, Boolean useLatch, Boolean serverConnection, Boolean databaseTest) {
        try {
            ClassLoader cloader = new URLClassLoader(new URL[]{new File(dbUser.getDbLibrary()).toURI().toURL()});
            DriverManager.registerDriver(new DriverWrapper((Driver) Class.forName(dbUser.getDbClass(), true, cloader).getDeclaredConstructor().newInstance()));

            connection = (serverConnection)
                    ? (Connection) DriverManager.getConnection(dbUser.getServerURL(), dbUser.getUserName(), dbUser.getUserPassword())
                    : (Connection) DriverManager.getConnection(dbUser.getURL(), dbUser.getUserName(), dbUser.getUserPassword());

            DriverManager.setLoginTimeout(5);
            isConnected = (connection == null) ? false : connection.isValid(5);

            if (isConnected) {
                isDBEmpty();
                if (!isEmpty) {
                    getVersionDetails();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }
            }
        } catch (SQLException ex) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            switch (ex.getSQLState()) {
                //unable to connect to server                
                case "08S01":
                case "08001":
                    isConnected = false;
                    errorMsg = LocalResource.getString("alert.unableToConnect");
                    break;
                //database not available
                case "42000":
                case "3D000":
                    isConnected = false;
                    errorMsg = LocalResource.getString("alert.databaseNotFound");
                    break;
                //Authentication error
                case "28000":
                case "28P01":
                case "08004":
                    isConnected = false;
                    errorMsg = LocalResource.getString("alert.authenticationError");
                    break;
                default:
                    isConnected = false;
                    errorMsg = LocalResource.getString("alert.unknownError");
            }
        } catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
        }
        if (databaseTest) {
            if (connection != null) {
                try {
                    connection = (Connection) DriverManager.getConnection(dbUser.getURL(), dbUser.getUserName(), dbUser.getUserPassword());
                    isDBEmpty();
                    Object[] result = new Object[]{isConnected, errorMsg, connection, isEmpty, version, versionInt, id.toLowerCase(), name.toLowerCase(), rowCount};
                    return result;

                } catch (SQLException e) {
                    isConnected = true;
                    Object[] result = new Object[]{isConnected, errorMsg, connection, true, version, versionInt, id.toLowerCase(), name.toLowerCase(), rowCount};
                    return result;
                }
            }
        }
        
        
        Object[] result = new Object[]{isConnected, errorMsg, connection, isEmpty, version, versionInt, id.toLowerCase(), name.toLowerCase(), rowCount};
        if (useLatch) {
            DatabaseManager.waitForConnection.countDown();
        }
        return result;
    }

    private static void isDBEmpty() {
        try {
            Statement stmtTables = connection.createStatement();
            ResultSet rsTables = stmtTables.executeQuery("select count(*) from information_schema.tables where table_type = 'BASE TABLE' and TABLE_SCHEMA = database()");
            if (rsTables.next()) {
                isEmpty = (rsTables.getInt(1) == 0);
            }
        } catch (SQLException ex) {
        }
    }

    public static String getTerminalName() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("select id from terminals where terminal_key = ? ");
            pstmt.setString(1, TerminalInfo.getTerminalID());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                return rs.getString(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void getVersionDetails() {
        try {
            String sql = "select count(*) from applications";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
            sql = "Select * from applications ";
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                id = rs.getString("id");
                name = rs.getString("name");
                version = rs.getString("version");
                versionInt = rs.getInt("versionint");
            }
            if (!id.equalsIgnoreCase("chromispos")) {
                id = "";
                version = "";
            }
            if (!name.equalsIgnoreCase("chromis pos")) {
                id = "";
                version = "";
            }

        } catch (SQLException ex) {
        }
    }

    public static Connection getRemoteConnection(String sDBUser, String sDBPassword, String sURL) {
        if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
            cypher = new AltEncrypter("cypherkey" + sDBUser);
            sDBPassword = cypher.decrypt(sDBPassword.substring(6));
        }
        try {
            return DriverManager.getConnection(sURL, sDBUser, sDBPassword);
        } catch (SQLException ex) {
            return null;
        }
    }

}
