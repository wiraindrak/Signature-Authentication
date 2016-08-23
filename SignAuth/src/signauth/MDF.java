/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package signauth;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author user
 */
public class MDF {
    
    private BufferedImage bimg;
    private ImageHandler ih;
    private final double[][] features = new double[2][1180];
    
    public void extractFeature(ImageHandler ih)
    {
        this.ih = ih;
        this.bimg = this.ih.getBufferedImage();
        
        int height = this.bimg.getHeight();
        int width = this.bimg.getWidth();
        
        int[][] directionMatrix = this.getDirectionMatrix();

        double[][] leftTF = new double[height][width];
        double[][] rightTF = new double[height][width];
        double[][] topTF = new double[width][height];
        double[][] bottomTF = new double[width][height];
        double[][] leftDF = new double[height][width];
        double[][] rightDF = new double[height][width];
        double[][] topDF = new double[width][height];
        double[][] bottomDF = new double[width][height];
        double[] totalTF = new double[height];
        double[] totalDF = new double[height];
        int transition = 10;
        double bagil = 10.0;
        double bagip = 10.0;
        int t = 0;
        int prev = 0;
        int idx = 0;
        int count = 0;
        int idxTF = 0;
        int idxDF = 0;
        double[] TF = new double[1180];
        double[] DF = new double[1180];
        
        for(int i=1; i<(height-1); i++)
        {
            count++;
            for(int j=1; j<(width-1); j++)
            {
                if((directionMatrix[i][j] != 0) && (t < transition))
                {
                    if(prev == 0)
                    {
                        idx++;
                        prev = directionMatrix[i][j];
                        
                        if(j == 1)
                            leftTF[i - 1][idx] = 1;
                        else
                            leftTF[i - 1][idx] = (double) (1 - ((j - 1.0) / (width - 2.0)));
                        
                        leftDF[i - 1][idx] = (double) (directionMatrix[i][j] / 10.0);

                        t++;
                        
                        if(count == 1)
                        {
                            totalTF[idx] = leftTF[i - 1][idx];
                            totalDF[idx] = leftDF[i - 1][idx];
                        }
                        else
                        {
                            totalTF[idx] += leftTF[i - 1][idx];
                            totalDF[idx] += leftDF[i - 1][idx];
                        }
                    }
                }
                else if(directionMatrix[i][j] == 0)
                    prev = 0;
            }
            
            if(t < transition)
            {
                for(int k=(t+1); k<transition; k++)
                {
                    idx++;
                    leftTF[i - 1][idx] = 0;
                    leftDF[i - 1][idx] = 0;
                    if(count == 1)
                    {
                        totalTF[idx] = leftTF[i - 1][idx];
                        totalDF[idx] = leftDF[i - 1][idx];
                    }else
                    {
                        totalTF[idx] += leftTF[i - 1][idx];
                        totalDF[idx] += leftDF[i - 1][idx];
                    }
                }
            }
            if(count % bagip == 0)
            {
                for(int ll=0; ll<transition; ll++)
                {
                    idxTF++;
                    idxDF++;
                    TF[idxTF-1] = (double) (totalTF[ll] / bagip);
                    DF[idxDF-1] = (double) (totalDF[ll] / bagip);
                }
                count = 0;
            }
            t = 0;
            prev = 0;
            idx = 0;
        }
        idx = 0;
        prev = 0;
        count = 0;
        
        for(int i=1; i<(height-1); i++)
        {
            count++;
            for(int j=(width-1); j>1; j--)
            {
                if((directionMatrix[i][j] != 0) && (t < transition))
                {
                    if(prev == 0)
                    {
                        idx++;
                        prev = directionMatrix[i][j];
                        rightTF[i-1][idx] = (double) ((j - 1.0) / (width - 2.0));
                        rightDF[i-1][idx] = (double) (directionMatrix[i][j] / 10.0);
                        
                        t++;
                                
                        if(count == 1)
                        {
                            totalTF[idx] = rightTF[i-1][idx];
                            totalDF[idx] = rightDF[i-1][idx];
                        }
                        else
                        {
                            totalTF[idx] += rightTF[i-1][idx];
                            totalDF[idx] += rightDF[i-1][idx];
                        }
                    }
                }
                else if(directionMatrix[i][j] == 0)
                    prev = 0;
            }
            
            if(t < transition)
            {
                for(int k=(t+1); k<transition; k++)
                {
                    idx++;
                    rightTF[i-1][idx] = 0;
                    rightDF[i-1][idx] = 0;
                    if(count == 1)
                    {
                        totalTF[idx] = rightTF[i-1][idx];
                        totalDF[idx] = rightDF[i-1][idx];
                    }
                    else
                    {
                        totalTF[idx] += rightTF[i-1][idx];
                        totalDF[idx] += rightDF[i-1][idx];
                    }
                }
            }
            
            if(count % bagip == 0)
            {
                for(int ll=0; ll<transition; ll++)
                {
                    idxTF++;
                    idxDF++;
                    TF[idxTF-1] = (double) (totalTF[ll] / bagip);
                    DF[idxDF-1] = (double) (totalDF[ll] / bagip);
                }
                count = 0;
            }
            t = 0;
            prev = 0;
            idx = 0;
        }
        idx = 0;
        prev = 0;
        count = 0;
        
        for(int i=1; i<(width-1); i++)
        {
            count++;
            for(int j=1; j<(height-1); j++)
            {
                if(directionMatrix[j][i] != 0 && t < transition)
                {
                    if(prev == 0)
                    {
                        idx++;
                        prev = directionMatrix[j][i];
                        
                        if(j == 1)
                            topTF[i-1][idx] = 1;
                        else
                            topTF[i-1][idx] = (double) (1 - ((j - 1.0) / (height - 2.0)));
                        
                        topDF[i-1][idx] = (double) (directionMatrix[j][i] / 10.0);
                        
                        t++;
                        
                        if(count == 1)
                        {
                            totalTF[idx] = topTF[i-1][idx];
                            totalDF[idx] = topDF[i-1][idx];
                        }
                        else
                        {
                            totalTF[idx] += topTF[i-1][idx];
                            totalDF[idx] += topDF[i-1][idx];
                        }
                    }
                }
                else if(directionMatrix[j][i] == 0)
                    prev = 0;
            }
            
            if(t < transition)
            {
                for(int k=(t+1); k<transition; k++)
                {
                    idx++;
                    topTF[i-1][idx] = 0;
                    topDF[i-1][idx] = 0;
                    if(count == 1)
                    {
                        totalTF[idx] = topTF[i-1][idx];
                        totalDF[idx] = topDF[i-1][idx];
                    }
                    else
                    {
                        totalTF[idx] += topTF[i-1][idx];
                        totalDF[idx] += topDF[i-1][idx];
                    }
                }
            }
            
            if(count % bagil == 0)
            {
                for(int ll=0; ll<transition; ll++)
                {
                    idxTF++;
                    idxDF++;
                    TF[idxTF-1] = (double) (totalTF[ll] / bagil);
                    DF[idxDF-1] = (double) (totalDF[ll] / bagil);
                }
                count = 0;
            }
            
            t = 0;
            prev = 0;
            idx = 0;
        }
        idx = 0;
        prev = 0;
        
        for(int i=1; i<(width-1); i++)
        {
            count++;
            for(int j=(height-1); j>1; j--)
            {
                if(directionMatrix[j][i] != 0 && t < transition)
                {
                    if(prev == 0)
                    {
                        idx++;
                        prev = directionMatrix[j][i];
                        bottomTF[i-1][idx] = (double) ((j - 1.0) / (height - 2.0));
                        bottomDF[i-1][idx] = (double) (directionMatrix[j][i] / 10.0);
                        t++;
                        
                        if(count == 1)
                        {
                            totalTF[idx] = bottomTF[i-1][idx];
                            totalDF[idx] = bottomDF[i-1][idx];
                        }
                        else
                        {
                            totalTF[idx] += bottomTF[i-1][idx];
                            totalDF[idx] += bottomDF[i-1][idx];
                        }
                    }
                }
                else if(directionMatrix[j][i] == 0)
                    prev = 0;
            }
            
            if(t < transition)
            {
                for(int k=(t+1); k<transition; k++)
                {
                    idx++;
                    bottomTF[i-1][idx] = 0;
                    bottomDF[i-1][idx] = 0;
                    if(count == 1)
                    {
                        totalTF[idx] = bottomTF[i-1][idx];
                        totalDF[idx] = bottomDF[i-1][idx];
                    }
                    else
                    {
                        totalTF[idx] += bottomTF[i-1][idx];
                        totalDF[idx] += bottomDF[i-1][idx];
                    }
                }
            }
            
            if(count % bagil == 0)
            {
                for(int ll=0; ll<transition; ll++)
                {
                    idxTF++;
                    idxDF++;
                    TF[idxTF-1] = (double) (totalTF[ll] / bagil);
                    DF[idxDF-1] = (double) (totalDF[ll] / bagil);
                }
                count = 0;
            }
            
            t = 0;
            prev = 0;
            idx = 0;
        }
        
        this.features[0] = TF;
        this.features[1] = DF;
    }
    
    public void saveFeatures(String filename) throws FileNotFoundException
    {
        this.ih.saveToImage(filename);
        
        double[] TF = this.features[0];
        double[] DF = this.features[1];
        
        String feature = "";
        
        for(int ii=0; ii<1180; ii++)
        {
            feature += TF[ii] +"-"+ DF[ii] +";";
        }
            
        try (PrintWriter w = new PrintWriter("D:/Signature Dataset/MDF features/" + filename +".txt")) {
            w.write(feature);
            w.flush();
            w.close();
        }
    }
    
    private int[][] getDirectionMatrix()
    {
        int h = this.bimg.getHeight();
        int w = this.bimg.getWidth();
        
        int[][] dirMatrix = new int[h][w];
        
        for(int i=1; i<(h-1); i++)
        {
            for(int j=1; j<(w-1); j++)
            {
                int[] pixelRGB = new int[3];
                
                pixelRGB[0] = new Color(this.bimg.getRGB(j, i)).getRed();
                pixelRGB[1] = new Color(this.bimg.getRGB(j, i)).getGreen();
                pixelRGB[2] = new Color(this.bimg.getRGB(j, i)).getBlue();

                if(this.ih.isBlack(pixelRGB))
                {
                    if(this.ih.isBlack(this.ih.getPixel(j-1, i-1)))
                        dirMatrix[i][j] = 5;
                    else if(this.ih.isBlack(this.ih.getPixel(j, i-1)))
                        dirMatrix[i][j] = 2;
                    else if(this.ih.isBlack(this.ih.getPixel(j+1, i-1)))
                        dirMatrix[i][j] = 3;
                    else if(this.ih.isBlack(this.ih.getPixel(j+1, i)))
                        dirMatrix[i][j] = 4;
                    else if(this.ih.isBlack(this.ih.getPixel(j+1, i+1)))
                        dirMatrix[i][j] = 5;
                    else if(this.ih.isBlack(this.ih.getPixel(j, i+1)))
                        dirMatrix[i][j] = 2;
                    else if(this.ih.isBlack(this.ih.getPixel(j-1, i+1)))
                        dirMatrix[i][j] = 3;
                    else if(this.ih.isBlack(this.ih.getPixel(j-1, i)))
                        dirMatrix[i][j] = 4;
                }
                else dirMatrix[i][j] = 0;
            }
        }
        
        return dirMatrix;
    }
    
    public double[][] getFeatures()
    {
        return this.features;
    }
}



