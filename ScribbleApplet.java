import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class ScribbleApplet extends JComponent {

    static JSlider red = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    static JSlider green = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    static JSlider blue = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    static int rVal,gVal,bVal;
    
    static JTextField txtField;
    static GraphicsPanel palette;
	
	public static void main(String[] args){
		
		//making interfaces for the canvas
		JFrame canvas = new JFrame();
	    final DrawingPad drawPad = new DrawingPad();
	    canvas.add(drawPad, BorderLayout.CENTER);
		
	    //making panel for adjustment of size and color
		JPanel adjust = new JPanel(new GridLayout(2,1,0,0));
	    TitledBorder border = new TitledBorder("Adjustment Panel");
	    adjust.setBorder(border);
	    
	    JPanel colorPanel = new JPanel(new GridLayout(1,3,0,0));
	    JPanel sizeAndClear = new JPanel(new GridLayout(3,1,5,0));
	    
	    JButton clearButton = new JButton("Clear Canvas");
	    
	    red.setBackground(Color.RED);
	    green.setBackground(Color.GREEN);
	    blue.setBackground(Color.BLUE);
	    
	    palette = new GraphicsPanel();
	    
	    ChangeListener al = new ColorListener();
	    
	    JSlider size = new JSlider(JSlider.HORIZONTAL,0,100,10);
	    
		size.setMajorTickSpacing(20);
		size.setMinorTickSpacing(1);
		size.setPaintTicks(true);
		size.setPaintLabels(true);
		
		//textbox
		txtField = new JTextField();
		txtField.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtField.setText("Color(" + rVal + ", " + gVal + ", " + bVal +
		")" );
		
		
		txtField.setEditable(false);
		
		
		
		red.addChangeListener(al);
		red.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					rVal = (int) source.getValue();
				}
			}
		});
		
		green.addChangeListener(al);
		green.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					gVal = (int) source.getValue();
				}
			}
		});
		
		blue.addChangeListener(al);
		blue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					bVal = (int) source.getValue();
				}
			}
		});
	    
		size.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					drawPad.brushSize = (int) source.getValue();
				}
			}
		});
	    
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawPad.clear();
			}
		});

		//adding left and right component to adjustment panel
		adjust.add(colorPanel);
		adjust.add(sizeAndClear);
		
		colorPanel.add(red);	colorPanel.add(green);	colorPanel.add(blue);
		colorPanel.add(palette);
		
		sizeAndClear.add(txtField);
		sizeAndClear.add(size,BorderLayout.NORTH);
		sizeAndClear.add(clearButton);
		
		canvas.add(adjust, BorderLayout.WEST);
		
		//set canvas size
		canvas.setSize(900,700);
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setVisible(true);
	}	
}

class DrawingPad extends ScribbleApplet {
    Image image;
    Graphics2D graphics2D;
    int currentX, currentY, oldX, oldY;
    static int brushSize = 10;
    
    public DrawingPad() {
    	setDoubleBuffered(false);
            
    	addMouseListener(new MouseAdapter() {
           	public void mousePressed(MouseEvent e) {
           		oldX = e.getX();
           		oldY = e.getY();
           	}
        });
            
        addMouseMotionListener(new MouseMotionAdapter() {
           	public void mouseDragged(MouseEvent e) {
           		currentX = e.getX();
           		currentY = e.getY();
           		graphics2D.setColor(new Color(rVal,gVal,bVal));
           		if (graphics2D != null)
           			graphics2D.setStroke(new BasicStroke(brushSize));
           		
           		graphics2D.drawLine(oldX, oldY, currentX, currentY);
           		repaint();
           		oldX = currentX;
           		oldY = currentY;
           	}
        });
    }
   
    public void paintComponent(Graphics g) {
    	if (image == null) {
    		image = createImage(getSize().width, getSize().height);
    		graphics2D = (Graphics2D) image.getGraphics();
    		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    				RenderingHints.VALUE_ANTIALIAS_ON);
    		clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getSize().width, getSize().height);
        repaint();
    }
}

class GraphicsPanel extends ScribbleApplet {
	public GraphicsPanel() {
		this.setPreferredSize(new Dimension(20, 20));
		this.setBackground(Color.white);
		this.setForeground(Color.black);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(rVal, gVal, bVal)); // Set the color
		g.fillRect(0, 0, 50,50);//fill the small box
	}
}


class ColorListener extends ScribbleApplet implements ChangeListener{
	public void stateChanged(ChangeEvent ae) {
	rVal = red.getValue();
	gVal = green.getValue();
	bVal = blue.getValue();
	
	txtField.setText("Color(" + rVal + ", " + gVal + ", " + bVal +
			")");
	
	palette.repaint();
	}
}

