/*
**    Chromis POS  - Open Source Point of Sale
**
**    This file is part of Chromis POS Version Chromis V1.5.2
**
**    Copyright (c) 2015-2023 Chromis & previous Openbravo POS related works   
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
package uk.chromis.pos.forms;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChromisFonts {

    public static Font DEFAULTFONT;
    public static Font DEFAULTFONTBOLD;
    public static Font DEFAULTBUTTONFONT;
    public static Font CHROMISFONT;
    public static Font CHROMISFONTBOLD;

    public static void setFont(String fontName) {

        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/fonts/LiberationSerif-Regular.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + "/fonts/LiberationSerif-Bold.ttf"));
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

        } catch (IOException ex) {
            System.out.println(ex);
            System.out.println("Font not found !!");
        } catch (FontFormatException ex) {
            Logger.getLogger(ChromisFonts.class.getName()).log(Level.SEVERE, null, ex);
        }

        DEFAULTFONT = new Font(fontName, Font.PLAIN, 16);
        DEFAULTFONTBOLD = new Font(fontName, Font.BOLD, 16);
        DEFAULTBUTTONFONT = new Font(fontName, Font.BOLD, 18);

    }

}
