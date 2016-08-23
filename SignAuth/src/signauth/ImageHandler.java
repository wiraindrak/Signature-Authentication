/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package signauth;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author user
 */
public class ImageHandler {
    private Canvas c;
    private BufferedImage bi;
    
    public ImageHandler() {}
    
    /**
     *
     * @param c
     */
    public ImageHandler(Canvas c) 
    {
        this.c = c;
        this.bi = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        c.paintAll(bi.getGraphics());
    }
    
    public boolean isBlack(int[] px)
    {
        return px[0] == 0 && px[1] == 0 && px[2] == 0;
    }
    
    public boolean isWhite(int[] px)
    {
        return px[0] == 255 && px[1] == 255 && px[2] == 255;
    }
    
    public static int colorToRGB(int alpha, int red, int green, int blue)
    {
        int newPixel = 0;
        newPixel += alpha; newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
        
        return newPixel;
    }
    
    public BufferedImage getBufferedImage()
    {
        return this.bi;
    }
    
    public int[] getPixel(int col, int row)
    {
        int[] val = new int[3];
        
        int red = new Color(this.bi.getRGB(col, row)).getRed();
        int green = new Color(this.bi.getRGB(col, row)).getGreen();
        int blue = new Color(this.bi.getRGB(col, row)).getBlue();
        val[0] = red; val[1] = green; val[2] = blue;
        
        return val;
    }
    
    public void saveToImage(String filename)
    {
        try {
            ImageIO.write(this.bi, "PNG", new File("D:/Signature Dataset/" +filename+ ".png"));
        } catch (IOException ex) {
        
        }
    }
    
}