
package com.spatialtranscriptomics.util;

import com.spatialtranscriptomics.model.Chip;
import com.spatialtranscriptomics.model.Feature;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Experimental class for computing a greyscale image from the (log) accumulated feature
 * expressions in each feature position (x,y).
 */
public class ComputeFeatureImage {
    
    private static final Logger logger = Logger.getLogger(ComputeFeatureImage.class);
    
    /**
     * Computes a greyscale image of log accumulated hit counts of each coordinate
     * position (x,y).
     * @param chip the chip. May be null for a cropped image.
     * @param features the unique events.
     * @return the buffered image.
     * @throws IOException 
     */
    public static BufferedImage computeImage(Chip chip, Feature[] features) throws IOException {
        BufferedImage img = null;
        try {
            int xdim = -1, ydim = -1;
            if (chip == null) {
                for (Feature f : features) {
                    if (f.getX() > xdim) xdim = f.getX();
                    if (f.getY() > ydim) ydim = f.getY();
                }
                xdim += 1;
                ydim += 1;
            } else {
                xdim = chip.getX2_total() /* - chip.getX1_total()*/ + 1;
                ydim = chip.getY2_total() /* - chip.getY1_total()*/ + 1;
            }
            logger.info("Creating image of size " + xdim + " * " + ydim);
            int[][] intensity = new int[xdim][ydim];
            //int xoff = chip.getX1_total();
            //int yoff = chip.getY1_total();
            int max = -1;
            for (Feature f : features) {
                int x = f.getX() /* - xoff*/;
                int y = f.getY() /* - yoff*/;
                intensity[x][y] += f.getHits();
                if (intensity[x][y] > max) {
                    max = intensity[x][y];
                }
            }

            // Normalize logged. Include pseudocounts.
            double logmax = Math.log10(max + 1);
            img = new BufferedImage(xdim, ydim, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < xdim; x++) {
                for (int y = 0; y < ydim; y++) {
                    double val = Math.log10(intensity[x][y] + 1) / logmax * 255;
                    int v = (int) Math.round(val);
                    int rgb = (v << 16) | (v << 8) | v;
                    img.setRGB(x, y, rgb);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            logger.error("Error constructing image: Array index out of bounds.");
            throw new IOException(ex);
        }
        return img;
    }
}
