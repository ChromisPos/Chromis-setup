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


package uk.chromis.pos.liquibase.create;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import liquibase.database.Database;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import uk.chromis.data.loader.ConnectionFactory;

/**
 * @author John Lewis
 */
public class CreateCardTriggers implements liquibase.change.custom.CustomTaskChange {

    private String dbaseName;

    private ResourceAccessor resourceAccessor;

    public String getDbaseName() {
        return dbaseName;
    }

    public void setDbaseName(String dbaseName) {
        this.dbaseName = dbaseName;
    }

    @Override
    public void execute(Database dtbs) throws CustomChangeException {
        PreparedStatement pstmt;
        Statement stmt;
        ClassLoader cloader;

        Connection conn = ConnectionFactory.getInstance().getConnection();

        try {
            String sql = " drop trigger if exists loyalty_trans_insert; ";
            stmt = conn.createStatement();
            stmt.execute(sql);

            String sqlTrigger1 = " create definer = current_user "
                    + " trigger loyalty_trans_insert after insert on loyaltytrans "
                    + " for each row"
                    + " begin "
                    + "	insert into "
                    + dbaseName
                    + ".loyaltytrans ("
                    + " id, cardnumber, customerid, activitydate, activity, "
                    + " activitypoints, activitytype, cashier, cardbalance, "
                    + " ticketid, ticketdetails, voucherid, sflag, siteguid "
                    + "	) values ( "
                    + " new.id, new.cardnumber, new.customerid, new.activitydate, new.activity, "
                    + " new.activitypoints, new.activitytype, new.cashier, new.cardbalance,"
                    + " new.ticketid, new.ticketdetails, new.voucherid, new.sflag, new.siteguid);"
                    + " end; ";
            stmt = conn.createStatement();
            stmt.execute(sqlTrigger1);

            sql = " drop trigger if exists gift_trans_insert; ";
            stmt = conn.createStatement();
            stmt.execute(sql);

            String sqlTrigger2 = " create definer = current_user "
                    + " trigger gift_trans_insert after insert on giftcardtrans "
                    + " for each row"
                    + " begin "
                    + "	insert into "
                    + dbaseName
                    + ".giftcardtrans ("
                    + " id, cardnumber, activitydate, activity, "
                    + " spendvalue, cardbalancevalue, ticketid, ticketdetails, "
                    + " sflag, siteguid "
                    + "	) values ( "
                    + " new.id, new.cardnumber, new.activitydate, new.activity, "
                    + " new.spendvalue, new.cardbalancevalue, new.ticketid, new.ticketdetails, "
                    + " new.sflag, new.siteguid );"
                    + " end; ";
            stmt = conn.createStatement();
            stmt.execute(sqlTrigger2);

            sql = " drop trigger if exists giftcard_insert; ";
            stmt = conn.createStatement();
            stmt.execute(sql);

            sql = " create definer = current_user "
                    + " trigger giftcard_insert after update on giftcards "
                    + " for each row"
                    + " begin "
                    + "	replace into "
                    + dbaseName
                    + ".giftcards ("
                    + " id, cardnumber, processdate, active, "
                    + " currentvalue, customerid, sflag, siteguid "
                    + "	) values ( "
                    + " new.id, new.cardnumber, new.processdate, new.active, "
                    + " new.currentvalue, new.customerid, new.sflag, new.siteguid );"
                    + " end; ";
            stmt = conn.createStatement();
            stmt.execute(sql);

            sql = " drop trigger if exists loyalty_insert; ";
            stmt = conn.createStatement();
            stmt.execute(sql);

            sql = " create definer = current_user "
                    + " trigger loyalty_insert after update on loyaltycards "
                    + " for each row"
                    + " begin "
                    + "	replace into "
                    + dbaseName
                    + ".loyaltycards ("
                    + " id, cardnumber,  processdate, active, cardlocked, "
                    + " replaced, removed,oldcardnumber, currentpoints,"
                    + " customerid, sflag, siteguid"
                    + "	) values ( "
                    + "  new.id, new.cardnumber, new.processdate, new.active, new.cardlocked, "
                    + "  new.replaced, new.removed, new.oldcardnumber, new.currentpoints,"
                    + "  new.customerid, new.sflag, new.siteguid );"
                    + " end; ";
            stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException ex) {
            System.out.println("Error card triggers");
        }

    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor ra) {

    }

    @Override
    public ValidationErrors validate(Database dtbs) {
        return null;
    }

}
