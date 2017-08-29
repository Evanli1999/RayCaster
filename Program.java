//run and render methods from: http://www.instructables.com/id/Making-a-Basic-3D-Engine-in-Java/
//this entire class is very similar to the one there
//as for the rest of this class: thank god for stackexchange 
//I hate gooey

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Program extends JFrame implements Runnable
{
	
	private static final long serialVersionUID = 1L;
	public int mapWidth = 15;
	public int mapHeight = 15;
	private Thread thread;
	private boolean running;
	private BufferedImage image;  
	public int[] pixels; 
	public ArrayList<Texture> textures; 
	public Character char1;
	public Calcs screen;
	public static int[][] map = //map layout. Cool
		{
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,1},
			{1,0,3,3,3,3,3,0,0,0,0,0,0,0,1},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,1},
			{1,0,3,0,0,0,3,0,2,2,2,0,2,2,1},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,1},
			{1,3,3,0,3,3,3,0,2,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,1},
			{1,0,1,0,2,0,0,0,3,4,4,0,4,4,1},
			{1,0,0,0,0,0,0,0,4,0,0,0,0,0,1},
			{1,0,0,0,0,0,2,0,4,0,0,0,0,0,1},
			{1,0,0,0,0,0,2,0,4,3,3,3,3,0,1},
			{1,0,0,0,0,0,3,0,4,3,3,3,3,0,1},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
		};
	public Program() {
		thread = new Thread(this);
		image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		textures = new ArrayList<Texture>();
		
		textures.add(Texture.wood);
		textures.add(Texture.brick);
		textures.add(Texture.bluestone);
		textures.add(Texture.stone);
		
		char1 = new Character(4.5, 4.5, 1, 0, 0, .66);
		screen = new Calcs(map, mapWidth, mapHeight, textures, 640, 480);
		addKeyListener(char1);
		setSize(640, 480);
		setResizable(false);
		setTitle("Raycaster v0.1.0");
		
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start();
		
		
		//this is all pretty whatever tbh 
	}
	private synchronized void start() 
	{
		running = true; //cool 
		thread.start();
	}
	public synchronized void stop() {
		running = false; //cool 
		try 
		{
			thread.join();
		} 
		catch(InterruptedException e) 
		{
			e.printStackTrace(); //not cool 
			//system.out.print("Method Stop, Thread.join()");
		}
	}
	public void render() 
	{
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) 
		{
			createBufferStrategy(3);
			return; 
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null); //draw the generated image on screen 
		bs.show();
	}
	public void run() //Credits for this entire class to this tutorial: http://www.instructables.com/id/Making-a-Basic-3D-Engine-in-Java/
	{
		long lastTime = System.nanoTime(); //find a way to update the screen 60 times a second (this is to reduce tearing) 
		final double ns = 1000000000.0 / 60.0;//yis 
		double delta = 0;
		requestFocus(); 
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				screen.update(char1, pixels);
				char1.update(map);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	public static void main(String [] args) {
		Program game = new Program();
	}
}
