package com.caribresort.client.view;

import java.awt.Component;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

public class Utilities {
    // Create a rounded border
    public static class RoundedBorder extends AbstractBorder  {
        
        private int radius; // Radius of the border
        private Color borderColor; // Border color
        private int thickness; // Thickness of the border
        
        // Constructor
        public RoundedBorder(int radius, Color borderColor, int thickness) {
            this.radius = radius;
            this.borderColor = borderColor;
            this.thickness = thickness;
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = thickness;
            return insets;
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        // Paint the border
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int bottomLineY = height - thickness;

            RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                    0,
                    0,
                    width - thickness,
                    bottomLineY,
                    radius,
                    radius
            );

            Area area = new Area(bubble);

            // Paint the BG color of the parent, everywhere outside the clip
            // of the text bubble.
            Component parent  = c.getParent();
            if (parent!=null) {
                Color bg = parent.getBackground();
                Rectangle rect = new Rectangle(0,0,width, height);
                Area borderRegion = new Area(rect);
                borderRegion.subtract(area);
                g2.setClip(borderRegion);
                g2.setColor(bg);
                g2.fillRect(0, 0, width, height);
                g2.setClip(null);
            }

            g2.setColor(borderColor);

            // Set the stroke for the border with the specified thickness
            g2.setStroke(new BasicStroke(thickness));

            // Draw the rounded border with padding based on the thickness
            int inset = thickness / 2;
            g2.drawRoundRect(x + inset, y + inset, width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }
    }
}
