package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Map extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private Image image;
    private double scale = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;
    private int lastX;
    private int lastY;

    public Map(Image image) {
        this.image = image;
        setPreferredSize(new Dimension(900, 600));
        //setName("Shenzhen Subway");
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    //计算图片绘制的位置和大小，使其居中显示在组件上，并根据scale变量进行缩放
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = (int) (image.getWidth(this) * scale);
        int height = (int) (image.getHeight(this) * scale);
        int x = (getWidth() - width) / 2 + offsetX;
        int y = (getHeight() - height) / 2 + offsetY;

        g2d.drawImage(image, x, y, width, height, this);
        g2d.dispose();
    }

    //双击鼠标
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (scale == 1.0) {
                scale = 2.0;
            } else {
                scale = 1.0;
            }
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    //按下鼠标并拖动
    @Override
    public void mouseDragged(MouseEvent e) {
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;
        lastX = e.getX();
        lastY = e.getY();
        offsetX += dx;
        offsetY += dy;
        repaint();
    }

    //滚动鼠标滚轮
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            scale *= 1.1;
        } else {
            scale /= 1.1;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}

    public static void showMap() {
        SwingUtilities.invokeLater(() -> {
            Image image = new ImageIcon("C:\\Users\\Administrator\\IdeaProjects\\DATAPJ1\\picture\\map.jpg").getImage();
            JFrame frame = new JFrame();
            //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Map(image));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
