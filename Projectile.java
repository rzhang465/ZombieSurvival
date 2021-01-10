import java.awt.Image;

public class Projectile 
{
   private double x;
   private double y; 
   private double velX;
   private double velY;
   private double xPoint;
   private double yPoint;   
   private double distanceTraveled;

   public Projectile(double x, double y, double velx, double vely)
   {
      this.x = x;
      this.y = y;
      velX = velx;
      velY = vely;
   }

   public double getX()
   {
      return x;
   }

   public double getY()
   {
      return y; 
   }

   public void setX(double x)
   {
      this.x = x;
   }
   
   public void setY(double y)
   {
      this.y = y;
   }
   
   public double getVelX()
   {
      return velX;
   }
   
   public double getVelY()
   {
      return velY;
   }
   
   public void setVelX(double x)
   {
      velX = x; 
   }
   
   public void setVelY(double y)
   {
      velY = y;
   }
   
   public double getxPoint()
   {
      return xPoint;
   }
   
   public void setxPoint(double x)
   {
      xPoint = x;
   }
   
   public double getyPoint()
   {
      return yPoint;
   }
   
   public void setyPoint(double y)
   {
      yPoint = y;
   }
   
   public void tick()
   {
      x+= velX;
      y+= velY;
      distanceTraveled += velX;
   }
   

}