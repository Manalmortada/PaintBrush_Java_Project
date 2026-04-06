/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintbrush;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MyPanel extends JPanel{
    int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    int color = 0, shape = 0;
    DShape[] shapes = new DShape[10000];
    int shapeCount = 0;
    private BufferedImage image;

    JCheckBox fillCheckBox = new JCheckBox("Fill Shapes");
    //color
    JButton b_red = new JButton("red");
    JButton b_blue = new JButton("blue");
    JButton b_green = new JButton("green");
    //shape 
    JButton b_Rect = new JButton("Rectangle");
    JButton b_Oval = new JButton("oval");
    JButton b_Line = new JButton("Line");
    JButton b_Freehand = new JButton("FreeHand");
    //control
    JButton b_Eraser = new JButton("Eraser");
    JButton b_ClearAll = new JButton("Clear all");
    JButton b_Undo = new JButton("Undo");
    JButton b_Save = new JButton("Save");
    JButton b_Open = new JButton("Open");


    ArrayList<Point> freePoints = new ArrayList<>();

    public MyPanel(){
        setFocusable(true);
        setLayout(new FlowLayout());

        //fillcheck
        fillCheckBox.setSelected(false);
        fillCheckBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                repaint();
            }
        });
        add(fillCheckBox);

        addMouseListener(new MouseListener(){
            @Override
            public void mousePressed(MouseEvent e){
                x1 = e.getX();
                y1 = e.getY();
                if (shape == 4 || shape== 5) {
                    freePoints.clear();
                    freePoints.add(new Point(x1, y1));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                x2 = e.getX();
                y2 = e.getY();
                if (shape == 4) {
                    for (int i = 1; i < freePoints.size(); i++) {
                        int fx1 = freePoints.get(i - 1).x;
                        int fy1 = freePoints.get(i - 1).y;
                        int fx2 = freePoints.get(i).x;
                        int fy2 = freePoints.get(i).y;
                        if (shapeCount < shapes.length) {
                            shapes[shapeCount++] = new DShape(fx1, fy1, fx2, fy2, color, 4, false);
                        }
                    }
                    freePoints.clear();
                } else {
                    if (shapeCount < shapes.length) {
                        shapes[shapeCount++] = new DShape(x1, y1, x2, y2, color, shape, fillCheckBox.isSelected());
                    }
                }
                repaint();
            }

            @Override public void mouseClicked(MouseEvent e){}
            @Override public void mouseEntered(MouseEvent e){}
            @Override public void mouseExited(MouseEvent e){}
        });

        addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent e){
                x2 = e.getX();
                y2 = e.getY();
                if (shape == 4) {
                    freePoints.add(new Point(x2, y2));
                    if (freePoints.size() >= 2) {
                        Point p1 = freePoints.get(freePoints.size() - 2);
                        Point p2 = freePoints.get(freePoints.size() - 1);
                        if (shapeCount < shapes.length) {
                            shapes[shapeCount++] = new DShape(p1.x, p1.y, p2.x, p2.y, color, 4, false);
                        }
                    }
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e){}
        });
        //color
        b_red.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                color = 1;
                repaint();
            }
        });

        b_blue.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                color = 2;
                repaint();
            }
        });

        b_green.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                color = 3;
                repaint();
            }
        });
        //shape
        b_Rect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                shape = 1;
                if (color == 4) color = 0;
                repaint();
            }
        });

        b_Oval.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                shape = 2;
                if (color == 4) color = 0;
                repaint();
            }
        });

        b_Line.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                shape = 3;
                if (color == 4) color = 0;
                repaint();
            }
        });

        b_Freehand.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                shape = 4;
                if (color == 4) color = 0;
                repaint();
            }
        });
        
        b_Eraser.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                shape = 4;
                color = 4;
                repaint();
            }
        });
        
        b_ClearAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapeCount = 0; 
                repaint();
            }
        });
        
        b_Undo.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (shapeCount > 0) {
                shapeCount--; 
                repaint();   
            }
        }
        });
        b_Save.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        Dimension size = getSize();
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size.width, size.height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        paint(g); 
        g.dispose();
        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            int option = fileChooser.showSaveDialog(null);
            if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new java.io.File(file.getAbsolutePath() + ".png");
                }
                javax.imageio.ImageIO.write(image, "png", file);
            }
        } catch (Exception ex) {
            System.out.println("catch an error");
        }
    }
});
        b_Open.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                setImage(image);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error opening image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
});



        //color
        add(b_red);
        b_red.setBackground(Color.RED);
        b_red.setPreferredSize(new Dimension(20, 20));
        add(b_blue);
        b_blue.setBackground(Color.BLUE);
        b_blue.setPreferredSize(new Dimension(20, 20));
        add(b_green);
        b_green.setBackground(Color.GREEN);
        b_green.setPreferredSize(new Dimension(20, 20));
        //shape
        add(b_Rect);
        add(b_Oval);
        add(b_Line);
        add(b_Freehand);
        add(b_Eraser);
        add(b_ClearAll);
        add(b_Undo);
        add(b_Save);
        add(b_Open);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < shapeCount; i++) {
            DShape s = shapes[i];
            switch (s.color) {
                case 1:
                    g.setColor(Color.RED);
                    break;
                case 2:
                    g.setColor(Color.BLUE);
                    break;
                case 3:
                    g.setColor(Color.GREEN);
                    break;
                case 4:
                    g.setColor(Color.WHITE);
                    break;
                default:
                    g.setColor(Color.BLACK);
                    break;
            }
            if (s.fill) {
                switch (s.shape) {
                    case 1:
                        g.fillRect(Math.min(s.x1, s.x2), Math.min(s.y1, s.y2), Math.abs(s.x2 - s.x1), Math.abs(s.y2 - s.y1));
                        break;
                    case 2:
                        g.fillOval(Math.min(s.x1, s.x2), Math.min(s.y1, s.y2), Math.abs(s.x2 - s.x1), Math.abs(s.y2 - s.y1));
                        break;
                    case 3:
                        g.drawLine(s.x1, s.y1, s.x2, s.y2);
                        break;
                    case 4:
                        g.drawLine(s.x1, s.y1, s.x2, s.y2);
                        break;
                    default:
                    break;
                }
            } else {
                switch (s.shape) {
                    case 1:
                        g.drawRect(Math.min(s.x1, s.x2), Math.min(s.y1, s.y2), Math.abs(s.x2 - s.x1), Math.abs(s.y2 - s.y1));
                        break;
                    case 2:
                        g.drawOval(Math.min(s.x1, s.x2), Math.min(s.y1, s.y2), Math.abs(s.x2 - s.x1), Math.abs(s.y2 - s.y1));
                        break;
                    case 3:
                        g.drawLine(s.x1, s.y1, s.x2, s.y2);
                        break;
                    case 4:
                        g.drawLine(s.x1, s.y1, s.x2, s.y2);
                        break;
                    default:
                    break;
                }
            }
        }
        if (image != null) {
        g.drawImage(image, 80, 80, getWidth(), getHeight(), this);
    }
    }
    public void setImage(BufferedImage image) {
    this.image = image;
    repaint();
}

    static class DShape {
        int x1, y1, x2, y2, color, shape;
        boolean fill;

        DShape(int x1, int y1, int x2, int y2, int color, int shape, boolean fill) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.shape = shape;
            this.fill = fill;
        }
    }
}








































//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import javax.swing.JButton;
//import javax.swing.JPanel;
//
///**
// *
// * @author samar
// */
//public class MyPanel extends JPanel{
//    int x1=0,y1=0,x2=0,y2=0;
//    int color=0, shape=0;
//    DShape[] shapes = new DShape[100];
//    int shapeCount = 0;
//
//    
//    public MyPanel() {
//        setFocusable(true);
//
//        addMouseListener(new MouseListener() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                x1 =e.getX();
//                y1 =e.getY();
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                x2 = e.getX();
//                y2 = e.getY();
//                if (shapeCount < shapes.length) {
//                    shapes[shapeCount++] = new DShape(x1, y1, x2, y2, color, shape);
//                }
//                repaint();
//            }
//
//            @Override
//            public void mouseClicked(MouseEvent e){}
//            @Override
//            public void mouseEntered(MouseEvent e){}
//            @Override
//            public void mouseExited(MouseEvent e){}
//        });
//        
//        //color
//        JButton b1 = new JButton("red");
//        JButton b2 = new JButton("blue");
//        JButton b3 = new JButton("green");
//
//        b1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                color = 1;
//                repaint();
//
//            }
//        });
//        
//        b2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                color = 2;
//                repaint();
//            }
//        });
//        
//        b3.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                color = 3;
//                repaint();
//            }
//        });
//        
//        add(b1);
//        add(b2);
//        add(b3);
//        b1.setBackground(Color.RED);
//        b2.setBackground(Color.BLUE);
//        b3.setBackground(Color.GREEN);
//        b1.setPreferredSize(new Dimension(20, 20)); // Small red square
//        b2.setPreferredSize(new Dimension(20, 20)); // Small blue square
//        b3.setPreferredSize(new Dimension(20, 20));
//        
//        //shape 
//        JButton bs1 = new JButton("Rectangle");
//        JButton bs2 = new JButton("oval");
//        JButton bs3 = new JButton("Line");
//        
//        bs1.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                shape= 1;
//                repaint();
//            }
//        });
//        
//        bs2.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                shape= 2;
//                repaint();
//            }
//        });
//        
//        bs3.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                shape= 3;
//                repaint();
//            }
//        });
//
//        add(bs1);
//        add(bs2);
//        add(bs3);
//    }
//
//    
//    @Override
//     public void paint(Graphics g){
//        super.paint(g);
//        for (int i = 0; i < shapeCount; i++) {
//            DShape s = shapes[i];
//            switch(s.color){
//                case 1:
//                    g.setColor(Color.RED);
//                    break;
//                case 2:
//                    g.setColor(Color.BLUE);
//                    break;
//                case 3:
//                    g.setColor(Color.GREEN);
//                    break;
//                default:
//                    g.setColor(Color.BLACK);
//                    break;
//            }
//
//            switch (s.shape){
//            case 1:
//                g.drawRect(Math.min(s.x1,s.x2), Math.min(s.y1, s.y2), Math.abs(s.x2 - s.x1), Math.abs(s.y2 - s.y1));
//                break;
//            case 2: 
//                g.drawOval(Math.min(s.x1, s.x2), Math.min(s.y1, s.y2), Math.abs(s.x2 - s.x1), Math.abs(s.y2 - s.y1));
//                break;
//            case 3: 
//               g.drawLine(s.x1, s.y1, s.x2, s.y2);
//               break;
//            default:
//                break;
//            }
//        }
//     }
//
//    static class DShape {
//       int x1, y1, x2, y2, color, shape;
//
//       DShape(int x1, int y1, int x2, int y2, int color, int shape) {
//           this.x1 = x1;
//           this.y1 = y1;
//           this.x2 = x2;
//           this.y2 = y2;
//           this.color = color;
//           this.shape = shape;
//       }
//    }
//
//}