/*
**    Chromis POS  - The New Dynamic Open Source POS
**
**    Copyright (c)2015-2020 
**    
**    Chromis and previous contributing parties (Unicenta & Openbravo)
**    http://www.chromis.co.uk
**
**    This file is part of Chromis POS Version Chromis V1.00.2003.12
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
**
**
 */
package uk.chromis.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import uk.chromis.globals.SystemProperty;
import uk.chromis.format.Formats;
import uk.chromis.pos.util.ComboBoxAutoComplete;

/**
 * FXML Controller class
 *
 * @author John.Lewis
 */
public class LocalePanelController implements Initializable, BaseController {

    public ComboBox comboDate;
    public ComboBox comboTime;
    public ComboBox comboDateTime;
    public ComboBox comboPercent;
    public ComboBox comboInteger;
    public ComboBox comboDouble;
    public ComboBox comboCurrency;

    public ComboBox comboLocale;

    public Label txtlocale;
    public Label chromisLocale;

    public TextField txtDate;
    public TextField txtTime;
    public TextField txtDateTime;

    public BooleanProperty dirty = new SimpleBooleanProperty(false);
    private final static String DEFAULT_VALUE = "(Default)";
    private Locale currentLocale;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        txtlocale.setText(Locale.getDefault().toString());
        currentLocale = Locale.getDefault();

        ObservableList<String> date = FXCollections.observableArrayList(
                "dd.MM.yy", "dd.MM.yyyy", "MM.dd.yy", "MM.dd.yyyy", "EEE, MMM d, yy", "EEE, MMM d, yyyy",
                "EEE, MMMM d, yy", "EEE, MMMM d, yyyy", "EEEE, MMMM d, yy", "EEEE, MMMM d, yyyy",
                "M-d-yyyy", "d-M-yyyy'"
        );

        ObservableList<String> time = FXCollections.observableArrayList(
                "h:mm", "h:mm:ss", "h:mm a", "h:mm:ss a", "H:mm", "H:mm:ss", "H:mm a", "H:mm:ss a",
                "h:mm:ss a"
        );

        ObservableList<String> dateTime = FXCollections.observableArrayList(
                "dd.MM.yy, H:mm", "dd.MM.yy, H:mm", "MM.dd.yy, H:mm", "MM.dd.yy, H:mm",
                "dd.MM.yyyy, H:mm", "dd.MM.yyyy, H:mm", "MM.dd.yyyy, H:mm", "MM.dd.yyyy, H:mm",
                "EEE, MMMM d yyyy, H:mm", "EEEE, MMMM d yyyy, H:mm", "M-d-yyyy h:mm a", "d-M-yyyy h:mm a"
        );

        ObservableList<String> integers = FXCollections.observableArrayList(
                DEFAULT_VALUE, "#0", "#,##0"
        );

        ObservableList<String> currency = FXCollections.observableArrayList(
                DEFAULT_VALUE, "\u00A4 #0.00", " #0.00", "\u00A4 #,##0.00", "#,##0.00", "'$' #,##0.00", "'$' #0.00"
        );

        ObservableList<String> doubles = FXCollections.observableArrayList(
                DEFAULT_VALUE, "#0.0", "#,##0.#"
        );

        ObservableList<String> percent = FXCollections.observableArrayList(
                DEFAULT_VALUE, "#,##0.##%"
        );

        comboDate.setItems(date);
        comboTime.setItems(time);
        comboDateTime.setItems(dateTime);

        comboPercent.setItems(percent);
        comboPercent.getSelectionModel().select(DEFAULT_VALUE);
        comboInteger.setItems(integers);
        comboInteger.getSelectionModel().select(DEFAULT_VALUE);
        comboDouble.setItems(doubles);
        comboDouble.getSelectionModel().select(DEFAULT_VALUE);
        comboCurrency.setItems(currency);
        comboCurrency.getSelectionModel().select(DEFAULT_VALUE);

        List<Locale> availablelocales = new ArrayList<>();
        availablelocales.addAll(Arrays.asList(Locale.getAvailableLocales()));

        Collections.sort(availablelocales, new LocaleComparator());

        for (Locale l : availablelocales) {
            if (!l.getDisplayLanguage().isBlank() && !l.getDisplayCountry().isBlank()) {
                comboLocale.getItems().add(new LocaleInfo(l));
            }
        }
        load();

        comboInteger.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                dirty.setValue(true);
            }
        });

        comboDouble.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                dirty.setValue(true);
            }
        });

        comboPercent.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                dirty.setValue(true);
            }
        });

        comboCurrency.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                dirty.setValue(true);
            }
        });

        comboDate.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (!t1.equals("")) {
                    Formats.setDatePattern(t1.toString());
                }
                txtDate.setText(Formats.DATE.formatValue(new Date()));
                dirty.setValue(true);
            }
        });

        comboTime.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (!t1.equals("")) {
                    Formats.setDatePattern(t1.toString());
                }
                txtTime.setText(Formats.DATE.formatValue(new Date()));
                dirty.setValue(true);
            }
        });

        comboDateTime.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (!t1.equals("")) {
                    Formats.setDatePattern(t1.toString());
                }
                txtDateTime.setText(Formats.DATE.formatValue(new Date()));
                dirty.setValue(true);
            }
        });

        comboLocale.valueProperty().addListener((ov, t, t1) -> {

            if (t1 != null) {
                Locale l = ((LocaleInfo) comboLocale.getSelectionModel().getSelectedItem()).getLocale();
                String cLocale = ((l.getLanguage() == null || l.getLanguage().isEmpty()) ? "" : l.getLanguage())
                        + (((l.getLanguage() == null || l.getLanguage().isEmpty()) && (l.getCountry() == null || l.getCountry().isEmpty())) ? "" : "_")
                        + ((l.getCountry() == null || l.getCountry().isEmpty()) ? "" : l.getCountry());

                cLocale = (cLocale == null || cLocale.isEmpty()) ? currentLocale.toString() : cLocale;

                chromisLocale.setText(cLocale);
            }
            dirty.setValue(true);
        });

        new ComboBoxAutoComplete<>(comboLocale);

    }

    @Override
    public void load() {
        comboDate.getSelectionModel().select(SystemProperty.DATE);
        Formats.setDatePattern(SystemProperty.DATE);
        txtDate.setText(Formats.DATE.formatValue(new Date()));
        comboTime.getSelectionModel().select(SystemProperty.TIME);
        Formats.setDatePattern(SystemProperty.TIME);
        txtTime.setText(Formats.DATE.formatValue(new Date()));
        comboDateTime.getSelectionModel().select(SystemProperty.DATETIME);
        Formats.setDatePattern(SystemProperty.DATETIME);
        txtDateTime.setText(Formats.DATE.formatValue(new Date()));

        comboPercent.getSelectionModel().select((SystemProperty.PERCENT == null || SystemProperty.PERCENT.equals("")) ? DEFAULT_VALUE : SystemProperty.PERCENT);
        comboInteger.getSelectionModel().select((SystemProperty.INTEGER == null || SystemProperty.INTEGER.equals("")) ? DEFAULT_VALUE : SystemProperty.INTEGER);
        comboDouble.getSelectionModel().select((SystemProperty.DOUBLE == null || SystemProperty.DOUBLE.equals("")) ? DEFAULT_VALUE : SystemProperty.DOUBLE);
        comboCurrency.getSelectionModel().select((SystemProperty.CURRENCY == null || SystemProperty.CURRENCY.equals("")) ? DEFAULT_VALUE : SystemProperty.CURRENCY);

        if (!SystemProperty.USERLANGUAGE.isBlank() || !SystemProperty.USERCOUNTRY.isBlank()) {
            Locale currentlocale = new Locale(SystemProperty.USERLANGUAGE, SystemProperty.USERCOUNTRY);
            for (int i = 0; i < comboLocale.getItems().size(); i++) {
                LocaleInfo l = (LocaleInfo) comboLocale.getItems().get(i);
                if (currentlocale.equals(l.getLocale())) {
                    comboLocale.getSelectionModel().select(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < comboLocale.getItems().size(); i++) {
                LocaleInfo l = (LocaleInfo) comboLocale.getItems().get(i);
                if (currentLocale.equals(l.getLocale())) {
                    comboLocale.getSelectionModel().select(i);
                    break;
                }
            }
        }

        String cLocale = ((SystemProperty.USERLANGUAGE.isBlank()) ? "" : SystemProperty.USERLANGUAGE)
                + ((!SystemProperty.USERLANGUAGE.isBlank() && !SystemProperty.USERCOUNTRY.isBlank()) ? "_" : "")
                + ((SystemProperty.USERCOUNTRY.isBlank()) ? "" : SystemProperty.USERCOUNTRY);

        cLocale = (cLocale == null || cLocale.isEmpty()) ? currentLocale.toString() : cLocale;

        chromisLocale.setText(cLocale);

        dirty.setValue(false);
    }

    @Override
    public void save() {

        SystemProperty.updatePropertyValueByConstant("DATE", readWithDefault(comboDate.getSelectionModel().getSelectedItem().toString()));
        SystemProperty.updatePropertyValueByConstant("TIME", readWithDefault(comboTime.getSelectionModel().getSelectedItem().toString()));
        SystemProperty.updatePropertyValueByConstant("DATETIME", readWithDefault(comboDateTime.getSelectionModel().getSelectedItem().toString()));

        SystemProperty.updatePropertyValueByConstant("CURRENCY",
                (comboCurrency.getSelectionModel().getSelectedItem().toString().equals(DEFAULT_VALUE)) ? null : comboCurrency.getSelectionModel().getSelectedItem().toString());
        SystemProperty.updatePropertyValueByConstant("INTEGER",
                (comboInteger.getSelectionModel().getSelectedItem().toString().equals(DEFAULT_VALUE)) ? null : comboInteger.getSelectionModel().getSelectedItem().toString());
        SystemProperty.updatePropertyValueByConstant("DOUBLE",
                (comboDouble.getSelectionModel().getSelectedItem().toString().equals(DEFAULT_VALUE)) ? null : comboDouble.getSelectionModel().getSelectedItem().toString());
        SystemProperty.updatePropertyValueByConstant("PERCENT",
                (comboPercent.getSelectionModel().getSelectedItem().toString().equals(DEFAULT_VALUE)) ? null : comboPercent.getSelectionModel().getSelectedItem().toString());

        //       Locale l = ((LocaleInfo) comboLocale.getSelectionModel().getSelectedItem()).getLocale();
        LocaleInfo p = (LocaleInfo) comboLocale.getSelectionModel().getSelectedItem();
        Locale l = null;
        if (p != null) {
            l = p.getLocale();
        }

        if (l == null) {
            SystemProperty.updatePropertySetToNull("USERLANGUAGE");
            SystemProperty.updatePropertySetToNull("USERCOUNTRY");
            SystemProperty.updatePropertySetToNull("USERLVARIANT");
            return;
        }

        SystemProperty.updatePropertyValueByConstant("USERCOUNTRY", l.getCountry());
        SystemProperty.updatePropertyValueByConstant("USERLVARIANT", l.getVariant());
        SystemProperty.updatePropertyValueByConstant("USERLANGUAGE", l.getLanguage());

        dirty.setValue(false);

    }

    @Override
    public Boolean isDirty() {
        return dirty.getValue();
    }

    @Override
    public void setDirty(Boolean value
    ) {
        dirty.setValue(value);
    }

    private void addLocale(List<Locale> ll, Locale l) {
        if (!ll.contains(l)) {
            ll.add(l);
        }
    }

    private String readWithDefault(Object value) {
        if (DEFAULT_VALUE.equals(value)) {
            return "";
        } else {
            return value.toString();
        }
    }

    private String writeWithDefaultDate(String value) {
        if (value == null || value.equals("")) {
            return "dd.MM.yyyy";
        } else {
            return value.toString();
        }
    }

    private String writeWithDefaultTime(String value) {
        if (value == null || value.equals("")) {
            return "H:mm:ss";
        } else {
            return value.toString();
        }
    }

    private String writeWithDefaultDateTime(String value) {
        if (value == null || value.equals("")) {
            return "dd.MM.yyyy, H:mm ";
        } else {
            return value.toString();
        }
    }

    private static class LAFInfo {

        private final String name;
        private final String classname;

        public LAFInfo(String name, String classname) {
            this.name = name;
            this.classname = classname;
        }

        public String getName() {
            return name;
        }

        public String getClassName() {
            return classname;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class LocaleInfo {

        private Locale locale;

        public LocaleInfo(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            return locale;
        }

        @Override
        public String toString() {
            return locale == null
                    ? "(System default)"
                    : locale.getDisplayName();
        }
    }
}
