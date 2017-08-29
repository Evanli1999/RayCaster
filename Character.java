import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Fix: xPlane and yPlane can simply be scalar multiples of xDir and yDir. Reduces unnecessary rotation in update method. (this will probably never get fixed or seen so ... *shrug*)

public class Character implements KeyListener 
{
    //@Param Xpos, YPos: Position vector of player (i.e. position on the map)
    //@param xDir yDir: Direction player is facing (i.e. direction vector) 
    //@Param xPlane yPlane: Normal vector of local camera plane (fixed distance away from player position)
    
    public double xPos, yPos, xDir, yDir, xPlane, yPlane;
    
    //@param left, right: direction of rotation (true = rotate in this direction)
    //@param forward, back: direction of movement
    
    public boolean left, right, forward, back;
    
    //@param MOVE_F_SPEED, MOVE_B_SPEED: speed of player movement forwards and backwards
    public final double MOVE_F_SPEED = .08;
    public final double MOVE_B_SPEED = .05; 
    
    //@param ROTATION_SPEED: rate of screen rotation 
    public final double ROTATION_SPEED = .045;
    
    public Character(double x, double y, double xd, double yd, double xp, double yp) 
    {
        //more formalities
        xPos = x;
        yPos = y;
        xDir = xd;
        yDir = yd;
        xPlane = xp;
        yPlane = yp;
    }
    public void keyPressed(KeyEvent key) 
    {
        //which key is pressed? 
        
        //left/right arrow = left/right rotation 
        //up/down arrow = forward/backward movement
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
        {
            left = true;
        }
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
        {
            right = true;
        }
        if((key.getKeyCode() == KeyEvent.VK_UP))
        {
            forward = true;
        }
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
        {
            back = true;
        }   
    }
    
    public void keyReleased(KeyEvent key) 
    {
        //"cancel" command if we let go of the key (i.e. no longer being pressed)
        
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
        {
            left = false;
        }
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
        {
            right = false;
        }
        if((key.getKeyCode() == KeyEvent.VK_UP))
        {
            forward = false;
        }
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
        {
            back = false;
        }
    }
    public void update(int[][] map) 
    {
        //updating position and direction vector based in what keys are pressed
        
        if(forward) //trying to move forward
        {
            if(map[(int)(xPos + xDir * MOVE_F_SPEED)][(int)yPos] == 0) //if where you're moving to is empty (i.e. 0 on the map array)
            {
                xPos+=xDir*MOVE_F_SPEED; //go for it
            }
            if(map[(int)xPos][(int)(yPos + yDir * MOVE_F_SPEED)] ==0) //likewise, for the y direction 
            {
                yPos+=yDir*MOVE_F_SPEED;
            }
        }
        if(back) //same thing as above, but reversed
        {
            if(map[(int)(xPos - xDir * MOVE_B_SPEED)][(int)yPos] == 0)
            {
                xPos-=xDir*MOVE_B_SPEED;
            }
            if(map[(int)xPos][(int)(yPos - yDir * MOVE_B_SPEED)]==0)
            {
                yPos-=yDir*MOVE_B_SPEED;
            }
        }
        
        //NOTE: DIRECTIONS ARE INVERTED HERE BECAUSE I EFFECTIVELY MADE EVERYTHING GO UPSIDE DOWN. IT STILL WORKS AND IM TOO LAZY TO FIX IT SO THERE'S THAT
        if(right) 
        { //rotationg direction vector according to direction of rotation 
            double oldxDir=xDir; //x component of current direction vector 
            
            //we essentially mutiply the direction vector by the rotation matrix: [cos(x) -sin(x)]
            //                                                                    [sin(x)  cos(x)]
            //where x is the rotation speed. We rotate counter clockwise here, so x > 0. 
            
            xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
            yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
            
            double oldxPlane = xPlane;
            xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
        }
        if(left) {
            
            //rotating counterclockwise so x < 0
            double oldxDir=xDir;
            xDir=xDir*Math.cos(-ROTATION_SPEED) - yDir*Math.sin(-ROTATION_SPEED); //HAHAHAHAHA SOMETHING IN ALGEO ACTUALLY CAME INTO USE FOR ONCE HAHAHAHAHAHA
            yDir=oldxDir*Math.sin(-ROTATION_SPEED) + yDir*Math.cos(-ROTATION_SPEED);
            double oldxPlane = xPlane;
            xPlane=xPlane*Math.cos(-ROTATION_SPEED) - yPlane*Math.sin(-ROTATION_SPEED);
            yPlane=oldxPlane*Math.sin(-ROTATION_SPEED) + yPlane*Math.cos(-ROTATION_SPEED);
        }
    }
    public void keyTyped(KeyEvent arg0) 
    {
        
        //Because Java likes making life difficult
        //no seriously why is this necessary 
        //IF I DONT EXPLICITLY ASK FOR A BOTTLE OF WATER IT IS IMPLIED I DO NOT WANT A BOTTLE OF WATER
        //why is that so hard to understand
        //:( 
        
	}
}