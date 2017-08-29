import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture 
{
	public int[]img; //array of pixel values
	private String path; //file path
	public final int SIZE; //number of pixels along one side of (square) image
	
	public Texture(String location, int size) 
	{
		path = location;
		SIZE = size;
		img = new int[SIZE * SIZE];
		load();
	}
	
	private void load() 
	{
		try 
		{
			BufferedImage image = ImageIO.read(new File(path));
			int w = image.getWidth();
			int h = image.getHeight();
			image.getRGB(0,0,w,h,img,0,w);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static Texture wood = new Texture("64bit/fire64.jpg", 64);
	public static Texture brick = new Texture("64bit/brick64.jpg", 64);
	public static Texture bluestone = new Texture("64bit/water64.jpg", 64);
	public static Texture stone = new Texture("64bit/dirt64.jpg", 64);
}
