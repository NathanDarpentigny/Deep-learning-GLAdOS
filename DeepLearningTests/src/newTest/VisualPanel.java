package newTest;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class VisualPanel extends JPanel{
	private double xpos;
	private double ypos;
	private double wpos;
	private double wres;
	private double xres;
	private double yres;
	private static final long serialVersionUID = 1L;
	
	public VisualPanel(double xres, double yres, double wres){
		
		super();
		this.xres = xres;
		this.yres = yres;
		this.wres = wres;
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		System.out.println();
		g.setColor(Color.green);
		g.fillOval((int)xres, (int)this.yres, (int)this.wres, (int)this.wres);
		g.setColor(Color.red);
		g.fillOval((int)xpos, (int)this.ypos, (int)this.wpos, (int)this.wpos);
		
	}
	public void setX(double x){
		xpos = x;
	}
	public void setY(double y){
		ypos = y;
	}
	public void setW(double w){
		wpos = w;
	}
}
