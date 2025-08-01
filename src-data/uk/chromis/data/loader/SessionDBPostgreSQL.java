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

public class SessionDBPostgreSQL implements SessionDB {

    /**
     *
     * @return
     */
    @Override
    public String TRUE() {
        return "True";
    }

    /**
     *
     * @return
     */
    @Override
    public String FALSE() {
        return "False";
    }

    /**
     *
     * @return
     */
    @Override
    public String INTEGER_NULL() {
        return "cast(null as integer)";
    }

    /**
     *
     * @return
     */
    @Override
    public String CHAR_NULL() {
        return "cast(null as char)";
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return "PostgreSQL";
    }

    /**
     *
     * @param s
     * @param sequence
     * @return
     */
    @Override
    public SentenceFind getSequenceSentence(Session s, String sequence) {
        return new StaticSentence(s, "select nextval('" + sequence + "')", null, SerializerReadInteger.INSTANCE);
    }
    
    /**
     *
     * @param s
     * @param sequence
     * @return
     */
    @Override
    public SentenceFind resetSequenceSentence(Session s, String sequence){
        return new StaticSentence(s, "alter sequence " + sequence + " restart with '0'", null, SerializerReadInteger.INSTANCE);   
    }
}
