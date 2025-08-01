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


package uk.chromis.globals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author John
 */
public class IconFactory {

    private static final HashMap<String, ImageIcon> iconFactoryCache = new HashMap<String, ImageIcon>();
    private static final IconFactory iconFactory = null;

    private IconFactory() {
    }

    public static void setDefaultIcon(String iconName) {
        iconFactoryCache.get(iconName);
        try {
            ImageIcon icon = iconFactoryCache.get(iconName);
            iconFactoryCache.put("default_icon", icon);
        } catch (Exception x) {
        }
    }

    public static void setDefaultIcon(String path, String iconName) {
        try {
            ImageIcon icon = new ImageIcon(path + iconName);
            iconFactoryCache.put("default_icon", icon);
        } catch (Exception x) {
        }
    }

    public static void addGradientImage(int width, int height, Color gradient1, Color gradient2, String imageName) {
        try {
            iconFactoryCache.put(imageName, createGradientIcon(width, height, gradient1, gradient2));
        } catch (Exception x) {
        }
    }

    public static ImageIcon getDefaultIcon() {
        return iconFactoryCache.get("default_icon");

    }

    public static Image getImage(String iconName) {
        ImageIcon icon = getIcon(iconName);
        if (icon != null) {
            BufferedImage bi = new BufferedImage(
                    icon.getIconWidth(),
                    icon.getIconHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            g.drawImage(icon.getImage(), 0, 0, null);
            g.dispose();
            return SwingFXUtils.toFXImage(bi, null);
        }
        return null;
    }

    public static ImageIcon createGradientIcon(int width, int height, Color gradient1, Color gradient2) {
        BufferedImage gradientImage = createCompatibleImage(width, height);
        //GradientPaint gradient = new GradientPaint(0, 0, gradient1, 0, height, gradient2, false);
        GradientPaint gradient = new GradientPaint(0, 0, gradient1, 0, height, gradient2, false);
        Graphics2D g2 = (Graphics2D) gradientImage.getGraphics();
        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        return new ImageIcon(gradientImage);
    }

    private static BufferedImage createCompatibleImage(int width, int height) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);
    }

    public static ImageIcon getIcon(String iconName) {
        ImageIcon icon = iconFactoryCache.get(iconName);
        if (icon == null) {
            try {
                icon = iconFactoryCache.get("default_icon");
            } catch (Exception x) {
                return null;
            }
        }
        return icon;
    }

    public static ImageIcon getResizedIcon(String iconName, Dimension size) {
        ImageIcon icon = iconFactoryCache.get(iconName);
        if (icon == null) {
            icon = iconFactoryCache.get("default_icon");
        }

        if (icon != null) {
            icon = new ImageIcon(icon.getImage().getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH));
        }
        return icon;
    }

    public static ImageIcon resizeIcon(ImageIcon icon, Dimension size) {
        return new ImageIcon(icon.getImage().getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH));
    }


    public static void cacheIconFromFile(String path, String iconName) {
        try {
            ImageIcon icon = new ImageIcon(path + iconName);
            iconFactoryCache.put(iconName, icon);
        } catch (Exception x) {
        }
    }

    public static void cacheIconsFromFolder(String folder) {
        File dir = new File("./" + folder + "/");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".gif"));
        for (File imagefile : files) {
            ImageIcon icon = new ImageIcon(imagefile.getAbsolutePath());
            iconFactoryCache.put(imagefile.getName(), icon);
        }
    }

    public static void cacheIconsFromResource(String resource) throws IOException {
        try {
            JarFile jarFile = new JarFile(new File(System.getProperty("user.dir") + resource));
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry entry = (JarEntry) e.nextElement();
                String name = entry.getName();
                if (name.endsWith(".png") || name.endsWith(".jpg")) {
                    InputStream is = IconFactory.class.getResourceAsStream("/" + name);
                    BufferedImage bf = ImageIO.read(is);
                    iconFactoryCache.put(name.substring(name.lastIndexOf('/') + 1), new ImageIcon(bf));
                }
            }
        } catch (IOException ex) {
        }
    }

}
