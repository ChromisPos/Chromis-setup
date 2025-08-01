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


package uk.chromis.data.user;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DirtyManager implements DocumentListener, ChangeListener, ActionListener, PropertyChangeListener {

    private boolean m_bDirty;
    private boolean m_dataValidated;

    /**
     *
     */
    //  protected Vector listeners = new Vector();
    protected ArrayList listeners = new ArrayList();
    private List synchList = Collections.synchronizedList(listeners);

    /**
     * Creates a new instance of DirtyManager
     */
    public DirtyManager() {
        m_bDirty = false;
        m_dataValidated = false;
    }

    /**
     *
     * @param l
     */
    public void addDirtyListener(DirtyListener l) {
        listeners.add(l);
    }

    /**
     *
     * @param l
     */
    public void removeDirtyListener(DirtyListener l) {
        listeners.remove(l);
    }

    /**
     *
     */
    protected void fireChangedDirty() {
        int j = 0;
        while (synchList.size() > j) {
            DirtyListener l = (DirtyListener) synchList.get(j);
            l.changedDirty(m_bDirty);
            j++;
        }
    }

    /**
     *
     * @param bValue
     */
    public void setDirty(boolean bValue) {
        if (m_bDirty != bValue) {
            m_bDirty = bValue;
            fireChangedDirty();
        }
    }

    
    public boolean isValidated(){
        return m_dataValidated;
    }
    
    public void setValidated(boolean dataValidated){
        m_dataValidated = dataValidated;
    }
    
    /**
     *
     * @return
     */
    public boolean isDirty() {
        return m_bDirty;
    }

    public void changedUpdate(DocumentEvent e) {
        setDirty(true);
    }

    public void insertUpdate(DocumentEvent e) {
        setDirty(true);
    }

    public void removeUpdate(DocumentEvent e) {
        setDirty(true);
    }

    public void stateChanged(ChangeEvent e) {
        setDirty(true);
    }

    public void actionPerformed(ActionEvent e) {
        setDirty(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        //   if ("image".equals(evt.getPropertyName())) {
        setDirty(true);
        // }
    }

}
