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

import uk.chromis.basic.BasicException;
import java.math.*;
import uk.chromis.pos.forms.LocalResource;


public class SentenceUpdateResultSet implements DataResultSet {

    private int m_iUpdateCount;


    public SentenceUpdateResultSet(int iUpdateCount) {
        m_iUpdateCount = iUpdateCount;
    }


    public Integer getInt(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public String getString(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public BigDecimal getBigDecimal(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public Double getDouble(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public Boolean getBoolean(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public java.util.Date getTimestamp(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public byte[] getBytes(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public Object getObject(int columnIndex) throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public DataField[] getDataField() throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }


    public Object getCurrent() throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }

    public boolean next() throws BasicException {
        throw new BasicException(LocalResource.getString("exception.nodataset"));
    }

    public void close() throws BasicException {
    }

    public int updateCount() throws BasicException {
        return m_iUpdateCount;
    }
}
