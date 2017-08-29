import java.util.ArrayList;
import java.awt.Color;

//REVIEW: cries for help at 2AM (Update method)

public class Calcs 
{
    public int[][] map;
    public int mapWidth, mapHeight, width, height;
    public ArrayList<Texture> textures;
    
    public Calcs(int[][] m, int mapW, int mapH, ArrayList<Texture> tex, int w, int h) {
        map = m;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        width = w;
        height = h;
    }
    
    public int[] update(Character camera, int[] pixels) //DDA algo. Nothing too interesting here 
    {
        for(int n=0; n<pixels.length/2; n++) //painting a floor and ceiling 
        {
            pixels[n] = Color.GREEN.getRGB();
        }
        
        for(int i=pixels.length/2; i<pixels.length; i++){
            pixels[i] = Color.RED.getRGB();
        }
        
        for(int x=0; x<width; x=x+1) //looping through every vertical stripe of the screen 
        {
            int counter = 0; //counting how many times we have "jumped" in the DDA algo. We dont want to search infinitely if there's a hole in the map. 
            double cameraX = 2 * x / (double)(width)-1;
            double rayDirX = camera.xDir + camera.xPlane * cameraX;//calculating the direction of the casted ray: adding the x component of player's direction and the horizontal component of the pixel along the horizontal direction on the screen. 
            double rayDirY = camera.yDir + camera.yPlane * cameraX;//same as above, but with the y-component 
            //floored values of current position 
            int mapX = (int)camera.xPos;
            int mapY = (int)camera.yPos;
            //length of ray from current position to next integer square side 
            double sideDistX;
            double sideDistY;
            //Length of ray from one side to next in map
            double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX)); //distance (along ray)we must travel to get from one vertical line in map grid to another  
            double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY)); //distance along ray we must travel to get from one horizontal line to another in map grid. 
            double perpWallDist;
            //Direction to go in x and y
            int stepX, stepY;
            boolean hit = false;//was a wall hit
            int side=0;//was the wall vertical or horizontal
            //Figure out the step direction and initial distance to a side
            if (rayDirX < 0)//x component is less than 0: ray is extending to the "left" 
            {
                stepX = -1; //go "left": we shift left one square every time we jump by deltaDistX
                sideDistX = (camera.xPos - mapX) * deltaDistX; //(camera.xPos - mapX) is the decimal value of xPos. 
            }
            else //ray extending to "right" 
            {
                stepX = 1; //go "right", shift right when we jump by deltaDistY
                sideDistX = (mapX + 1.0 - camera.xPos) * deltaDistX; //finding the "proportion" of the square it must initially scale to reach next line: hence it is a percentage of deltaDistX: the distance it must travel to scale between two horizontal lines. 
            }
            if (rayDirY < 0)//ray going "down" 
            {
                stepY = -1; //go "down": same thing as above
                sideDistY = (camera.yPos - mapY) * deltaDistY;
            }
            else //ray going "up" 
            {
                stepY = 1; //go "up": same as above 
                sideDistY = (mapY + 1.0 - camera.yPos) * deltaDistY;
            }
            //Loop to find where the ray hits a wall
            counter = 0; //resetting counter 
            while(!hit) {
                counter = 0; //resetting counter 
                //Jump to next square
                if (sideDistX < sideDistY) //we take the smallest jump to get to either horizontal or vertical line
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0; //denoting vertical side
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1; //denoting horizontal side 
                }

                if(map[mapX][mapY] > 0) //wow it actually hit something 
                {
                    hit = true;
            
                }
                if (counter > 500)//if we dont find anything for a while... just ignore it... --> somewhat analagous to render distance? 
                {
                    break; 
                }
                counter++; 
            }
            //Calculate distance to the point of impact
            if(side==0)//vertical walls: 
                perpWallDist = Math.abs((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX); //Find y-component of the distance between player and wall, then divide by distance along ray it takes to move between vertical lines (add one if below because that's how the decimal system works.) 
            else//vertical hit 
                perpWallDist = Math.abs((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY);  //basically the same as above
            //Now calculate the height of the wall based on the distance from the camera
            int lineHeight;
            if(perpWallDist > 0) lineHeight = Math.abs((int)(height / perpWallDist)); //farther it is, shortrer the wall is
            else lineHeight = height;
            //draw the wall for our current vertical section: the wall should be vertically centered, so half of it is below and half of it is above the center of the screen. 
            int drawStart = -lineHeight/2+ height/2; //start drawing from lower end
            if(drawStart < 0)
                drawStart = 0; //cant draw below teh screen bud. 
            int drawEnd = lineHeight/2 + height/2;
            if(drawEnd >= height) 
                drawEnd = height - 1;
            //add a texture
            int texNum = map[mapX][mapY] - 1; //damn this is making more sense at 3am then it was when I got 9 hours of sleep. 
            double wallX;//We now wish to find the exact position of the interesction with the wall: thus we know which part of the texture to draw
            if(side==1) 
            {//If its a y-axis wall
                wallX = (camera.xPos + ((mapY - camera.yPos + (1 - stepY) / 2) / rayDirY) * rayDirX); //apparently this works so im going to go with this. 
            } 
            
            else 
            {//X-axis wall
                wallX = (camera.yPos + ((mapX - camera.xPos + (1 - stepX) / 2) / rayDirX) * rayDirY);
            }
            wallX-=Math.floor(wallX);
            //x coordinate on the texture
            //texture rendering method courtesy of:http://www.instructables.com/id/Making-a-Basic-3D-Engine-in-Java/
            int texX = (int)(wallX * (textures.get(texNum).SIZE));
            if(side == 0 && rayDirX > 0)
            {
                texX = textures.get(texNum).SIZE - texX - 1;
            }
            if(side == 1 && rayDirY < 0) 
            {
                texX = textures.get(texNum).SIZE - texX - 1;
            }
            //calculate y coordinate on texture
            for(int y=drawStart; y<drawEnd; y++) {
                int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if(side==0) color = textures.get(texNum).img[texX + (texY * textures.get(texNum).SIZE)];
                else color = (textures.get(texNum).img[texX + (texY * textures.get(texNum).SIZE)]>>1) & 8355711;//Make y sides darker
                pixels[x + y*(width)] = color;
            }
        }
        return pixels;
    }
}

