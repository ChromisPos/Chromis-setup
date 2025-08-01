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

import uk.chromis.pos.forms.AppConfig;

public class DbUser {

    private String dbEngine;
    private String dbLibrary;
    private String dbClass;
    private String serverName;
    private String serverPort;
    private String databaseName;
    private String userName;
    private String userPassword;
    private String encryptedUserPassword;
    private String URL;
    private String currentPath = null;

    public DbUser() {
        dbClass = AppConfig.getDatabaseProviderClass();
        dbLibrary = AppConfig.getDatabaseLibraryPath();
        serverName = AppConfig.getDatabaseServer();
        serverPort = AppConfig.getDatabasePort();
        databaseName = AppConfig.getDatabaseName();
        URL = AppConfig.getDatabaseURL();
        encryptedUserPassword = AppConfig.getDatabasePassword();
        userName = AppConfig.getDatabaseUser();
        userPassword = AppConfig.getClearDatabasePassword();
    }

    public DbUser(String serverName, String serverPort, String databaseName, String userName, String userPassword) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.databaseName = databaseName;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public void getUserDetails() {
        dbClass = AppConfig.getDatabaseProviderClass();
        dbLibrary = AppConfig.getDatabaseLibraryPath();
        serverName = AppConfig.getDatabaseServer();
        serverPort = AppConfig.getDatabasePort();
        databaseName = AppConfig.getDatabaseName();
        URL = AppConfig.getDatabaseURL();
        encryptedUserPassword = AppConfig.getDatabasePassword();
        userName = AppConfig.getDatabaseUser();
        userPassword = AppConfig.getClearDatabasePassword();
        dbEngine="MySQL";
    }

    private String buildURL() {
        StringBuilder url;
        url = new StringBuilder();
        url.append("jdbc:mysql://");
        url.append(serverName);
        url.append(":");
        url.append(serverPort);
        url.append("/");
        url.append(databaseName);
        url.append("?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC");
        return url.toString();
    }

    private String buildURLNoDatabase() {
        StringBuilder url;
        url = new StringBuilder();
        url.append("jdbc:mysql://");
        url.append(serverName);
        url.append(":");
        url.append(serverPort);
        url.append("?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC");
        return url.toString();
    }

    public void save() {
        AppConfig.setDatabaseServer(serverName);
        AppConfig.setDatabasePort(serverPort);
        AppConfig.setDatabaseName(databaseName);
        AppConfig.setDatabasePassword(userName, userPassword);
        AppConfig.setDatabaseUser(userName);
    }

    public void displayUser() {
        System.out.println("SeverName      : " + serverName);
        System.out.println("Port           : " + serverPort);
        System.out.println("Database       : " + databaseName);
        System.out.println("UserName       : " + userName);
        System.out.println("Password       : " + userPassword);
        System.out.println("URL            : " + buildURL());
        System.out.println("**********************************************");
    }

    public String getURL() {
        URL = buildURL();
        return URL;
    }

    public String getServerURL() {
        URL = buildURLNoDatabase();
        return URL;
    }

    public String getDbLibrary() {
        return dbLibrary;
    }

    public String getDbClass() {
        return dbClass;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getEncryptedUserPassword() {
        return encryptedUserPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

}
