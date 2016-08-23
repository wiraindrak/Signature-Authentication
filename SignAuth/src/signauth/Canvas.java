package signauth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;

class Canvas extends JPanel 
              implements MouseListener, MouseMotionListener, ActionListener {

    private final static int BLACK = 0;

    private final static int
               CURVE = 0,
               LINE = 1;

    Image OSI;  

    int widthOfOSI, heightOfOSI;  
    private int mouseX, mouseY;   
    private int prevX, prevY;     
    private int startX, startY;   
    private boolean dragging;
    private int figure;    
    private Graphics dragGraphics; 

    public Canvas() {
       addMouseListener(this);
       addMouseMotionListener(this);
       setBackground(Color.white);
    }


    private void drawFigure(Graphics g, int shape, int x1, int y1, int x2, int y2) {
       if (shape == LINE) {
          g.drawLine(x1,y1,x2,y2);
          return;
       }
       int x, y;  
       int w, h;  
       if (x1 >= x2) {  
          x = x2;
          w = x1 - x2;
       }
       else {          
          x = x1;
          w = x2 - x1;
       }
       if (y1 >= y2) { 
          y = y2;
          h = y1 - y2;
       }
       else {          
          y = y1;
          h = y2 - y1;
       }
    }


    private void repaintRect(int x1, int y1, int x2, int y2) {
       int x, y;
       int w, h;
       if (x2 >= x1) {  
          x = x1;
          w = x2 - x1;
       }
       else {          
          x = x2;
          w = x1 - x2;
       }
       if (y2 >= y1) { 
          y = y1;
          h = y2 - y1;
       }
       else {          
          y = y2;
          h = y1 - y2;
       }
       repaint(x,y,w+10,h+10);
    }


    private void checkOSI() {
       if (OSI == null || widthOfOSI != getSize().width || heightOfOSI != getSize().height) {
          OSI = null;  
          OSI = createImage(getSize().width, getSize().height);
          widthOfOSI = getSize().width;
          heightOfOSI = getSize().height;
          Graphics OSG = OSI.getGraphics();  
          OSG.setColor(getBackground());
          OSG.fillRect(0, 0, widthOfOSI, heightOfOSI);
          OSG.dispose();
       }
    }

    @Override
    public void paintComponent(Graphics g) {
       checkOSI();
       g.drawImage(OSI, 0, 0, this);
       if (dragging && figure != CURVE) {
          g.setColor(Color.BLACK);
          drawFigure(g,figure,startX,startY,mouseX,mouseY);
       }
    }


    public void actionPerformed(ActionEvent evt) {
       String command = evt.getActionCommand();
       checkOSI();
       Graphics g = OSI.getGraphics();
       g.setColor(getBackground());
       g.fillRect(0,0,getSize().width,getSize().height);
       g.dispose();
       repaint();
    }


    private Color getCurrentColor() {
       return Color.black;
    }


    @Override
    public void mousePressed(MouseEvent evt) {
       if (dragging == true)  
           return;            

       prevX = startX = evt.getX();  
       prevY = startY = evt.getY();

       figure = 0;
       dragGraphics = OSI.getGraphics();
       dragGraphics.setColor(Color.BLACK);

       dragging = true;  

    }


    public void mouseReleased(MouseEvent evt) {
        if (dragging == false)
           return;
        dragging = false;
        mouseX = evt.getX();
        mouseY = evt.getY();
        if (figure == CURVE) {
            drawFigure(dragGraphics,LINE,prevX,prevY,mouseX,mouseY);
            repaintRect(prevX,prevY,mouseX,mouseY);
        }
        else if (figure == LINE) {
           repaintRect(startX,startY,prevX,prevY);
           if (mouseX != startX || mouseY != startY) {
              drawFigure(dragGraphics,figure,startX,startY,mouseX,mouseY);
              repaintRect(startX,startY,mouseX,mouseY);
           }
        }
        else {
           repaintRect(startX,startY,prevX,prevY);
           if (mouseX != startX && mouseY != startY) {
              drawFigure(dragGraphics,figure,startX,startY,mouseX,mouseY);
              repaintRect(startX,startY,mouseX,mouseY);
           }
        }
        dragGraphics.dispose();
        dragGraphics = null;
    }


    @Override
    public void mouseDragged(MouseEvent evt) {
        if (dragging == false)
           return;  

        mouseX = evt.getX();   
        mouseY = evt.getY();   

        if (figure == CURVE) { 
           drawFigure(dragGraphics,LINE,prevX,prevY,mouseX,mouseY);
           repaintRect(prevX,prevY,mouseX,mouseY);
        }
        else {
           repaintRect(startX,startY,prevX,prevY);
           repaintRect(startX,startY,mouseX,mouseY);
        }

        prevX = mouseX;  
        prevY = mouseY;

    } 


    @Override
    public void mouseEntered(MouseEvent evt) { }   
    @Override
    public void mouseExited(MouseEvent evt) { }    
    @Override
    public void mouseClicked(MouseEvent evt) { }   
    @Override
    public void mouseMoved(MouseEvent evt) { }     

}