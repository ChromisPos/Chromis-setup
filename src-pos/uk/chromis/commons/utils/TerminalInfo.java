/*
**    Chromis POS  - The New Dynamic Open Source POS
**
**    This file is part of Chromis POS Version Chromis V1.00.2102.12
**    Copyright (c)2015-2023 
**    
**    Chromis and previous contributing parties (Unicenta & Openbravo)
**    http://www.chromis.co.uk
**
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
**
**    You should have received a copy of the GNU General Public License
**    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>

**  POS
**
 */
package uk.chromis.commons.utils;


import java.util.UUID;
import java.util.prefs.Preferences;

public class TerminalInfo {

    private static final String TERMINAL = "$1308823401";
    private static final Preferences preferences;
    private static String tuid;

    static {
        // preferences = Preferences.userRoot().node("chromis");
        preferences = Preferences.systemRoot().node("chromis");
        tuid = preferences.get(TERMINAL, null);
        if (tuid == null) {
            tuid = UUID.randomUUID().toString();
            preferences.put(TERMINAL, tuid);
        }
    }

    public static String getTerminalID() {
        return tuid;
    }

    public static void setTerminalName(String posName) {
        preferences.put("posname", posName);
    }

    public static String getTerminalName() {
        return preferences.get("posname", "Unknown");
    }

    public static void enableWebCam() {
        preferences.putBoolean("webcam", true);
    }

    public static void setWebCam(Boolean value) {
        preferences.putBoolean("webcam", value);
    }

    public static Boolean isWebCamActive() {
        return preferences.getBoolean("webcam", false);
    }

    public static void setLocation(String location) {
        preferences.put("location", location);
    }

    public static String getLocation() {
        return preferences.get("location", "Not defined");
    }

    public static void setScales(String scales) {
        preferences.put("scales", scales);
    }

    public static String getScales() {
        return preferences.get("scales", "Not defined");
    }

    public static void setScalesSet2(String scales) {
        preferences.put("scalesset2", scales);
    }

    public static String getScalesSet2() {
        return preferences.get("scalesset2", "Not defined");
    }

    public static void setScanner(String scanner) {
        preferences.put("scanner", scanner);
    }

    public static String getScanner() {
        return preferences.get("scanner", "Not defined");
    }

    public static void setDisplay(String display) {
        preferences.put("display", display);
    }

    public static String getDisplay() {
        return preferences.get("display", "Not defined");
    }

    public static void enableCustomerDisplay(Boolean display) {
        preferences.putBoolean("customerdisplay", display);
    }

    public static Boolean hasCustomerDisplay() {
        return preferences.getBoolean("customerdisplay", false);
    }

    public static void setReceiptPrinter(String printer) {
        preferences.put("receiptprinter", printer);
    }

    public static String getReceiptPrinter() {
        return preferences.get("receiptprinter", "Not defined");
    }

    public static void setKitchenPrinter(String printer) {
        preferences.put("kitchenprinter", printer);
    }

    public static String getKitchenPrinter() {
        return preferences.get("kitchenprinter", "Not defined");
    }

    public static void setKitchenPrinter2(String printer) {
        preferences.put("kitchenprinter2", printer);
    }

    public static String getKitchenPrinter2() {
        return preferences.get("kitchenprinter2", "Not defined");
    }

    public static void setBarPrinter(String printer) {
        preferences.put("barprinter", printer);
    }

    public static String getBarPrinter() {
        return preferences.get("barprinter", "Not defined");
    }

    public static void setPosType(String posType) {
        preferences.put("postype", posType);
    }

    public static String getPosType() {
        return preferences.get("postype", "standard");
    }

    public static void enableTimer() {
        preferences.putBoolean("timer", true);
    }

    public static void setTimer(Boolean value) {
        preferences.putBoolean("timer", value);
    }

    public static Boolean isTimerActive() {
        return preferences.getBoolean("timer", false);
    }

    public static void setTimerPeriod(String period) {
        preferences.put("timerperiod", period);
    }

    public static String getTimerPeriod() {
        return preferences.get("timerperiod", "0");
    }

    public static String getReceiptPrefix() {
        return preferences.get("prefix", "");
    }

    public static void setReceiptPrefix(String prefix) {
        preferences.put("prefix", prefix);
    }

}
