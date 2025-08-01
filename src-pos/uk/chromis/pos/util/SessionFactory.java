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


package uk.chromis.pos.util;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.chromis.data.loader.Session;
import uk.chromis.pos.forms.AppConfig;

public class SessionFactory {

    private static SessionFactory INSTANCE = new SessionFactory();
    private Session session;
    private SessionFactory() {

    }

    public static SessionFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (SessionFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SessionFactory();
                }
            }
        }
        return INSTANCE;
    }

    public Session getSession() {
        if (session != null) {           
            return session;
        }
        try {
            session = new Session(AppConfig.getDatabaseURL(), AppConfig.getDatabaseUser(), AppConfig.getClearDatabasePassword());
            return session;
        } catch (SQLException ex) {
            Logger.getLogger(SessionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
