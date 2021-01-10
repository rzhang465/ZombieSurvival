public class Player
{
   
   private int x;				//our true location in pixel space on the screen
   private int y;				//	to be used for collision detection
   private int velocityX=0;
   private int velocityY=0;
   private int h=100;
   
   public Player(int X, int Y, int H )
   {          
      x=X;
      y=Y;
      h=H;
   }
   public void setH(int H)
   {
      this.h=H;
   }
   
   public int getH()
   {
      return h;
   }
   
   public void setX(int X)
   {
      this.x = X;
   }  
    	
   public void setY(int Y)
   {
      this.y = Y;
   }
   
   public int getX()
   {
      return x;
   }
   	
   public int getY()
   {
      return y;
   } 
   
   public int getVelX()
   {
      return velocityX;
   }
   
   public int getVelY()
   {
      return velocityY;
   }
   
   public void setVelX(int x)
   {
      velocityX = x;
   }
   
   public void setVelY(int y)
   {
      velocityY = y;
   }
   public void tick() 
   {
      x += velocityX;
      y += velocityY;
   } 
}