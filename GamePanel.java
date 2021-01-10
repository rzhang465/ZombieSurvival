import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.io.Serializable;
import java.lang.Thread;
import java.awt.Color;
import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel //implements Runnable
{
   private static int[][] board;       //we will fill with 0s, 1s, and 2s
   private static final int SIZE=40;   //size of cell being drawn
   private static int screenWidth;     //size of the screen in pixels
   private static int screenHeight; 
   private static BufferedImage title;
   private static BufferedImage background;
   private static BufferedImage player; //("images/sniper_sprites/rifleman_east.png");
   private static BufferedImage projectile;
   private static BufferedImage credits;
   private static BufferedImage sniper;
   private static BufferedImage engineer;
   private static BufferedImage rifleman;
   private static BufferedImage player2;
   private static BufferedImage victory;
   private static BufferedImage gameover;
   private int numRows = 900;
   private int numColumns = 1600;
   private static int mouseX, mouseY;
   private static double scale = 1.5;			//scale the player is drawn
   private static double dScale;			//the amount the scale changes each frame
   private static double angle, targetAngle;			//angle the player is rotated, targetAngle is where angle is moving towards
   private static final int DELAY=1;     
   private static Player player1;
   private static ArrayList<Player>enemyList;
   private static Projectile ball;
   private static ArrayList<Projectile>projectileList;
   private boolean check=false;
   private boolean checkB=false;
   private boolean check2=false;
   private boolean checkE=false;
   private int spawnLoc=0;
   private Timer tL; 
   private Timer tR;
   private Timer tU;
   private Timer tD;
   private Timer tMouse;
   private Timer FPS;
   private Timer reload1;
   private Timer enemymove;
   private Timer dps;
   private Timer spawner; 
   private Timer clock;
   private int spawnCount=5;
   private int ammo1=30;
   private int counter=180;
   private static final int MAXVELOCITY=10;
   public static enum CHARSTATE
   {
      SNIPER,ENGINEER,RIFLEMAN,CQC
   };
   public static enum STATE
   {
      MENU,GAME,LOBBY,CREDITS,GAMEOVER,VICTORY
   };
   public static STATE state = STATE.MENU;
   public static CHARSTATE charstate = CHARSTATE.RIFLEMAN;
   private Menu menu;
  
   public GamePanel()
   { 
      screenWidth = numColumns;
      screenHeight = numRows;     
      board=new int[numRows][numColumns];
      int r=(int)(Math.random()*2)+1;
      if (r==1)
      {
         player1=new Player(50, 450, 10000);
      }
      else 
         player1=new Player(1550,450,10000);
      enemyList= new ArrayList();
      ball = new Projectile(0,0, 0,0);
      projectileList = new ArrayList();
      tL = new Timer(10, new ListenerL());
      tR = new Timer(10, new ListenerR());
      tU = new Timer(10, new ListenerU());
      tD = new Timer(10, new ListenerD());
      reload1 =new Timer(3000, new reload1());
      tMouse = new Timer(1, new ListenerMouse());
      tMouse.start(); 
      FPS =new Timer(9, new fps());
      FPS.start();
      enemymove = new Timer (15, new ListenerE());
      dps = new Timer (9, new ListenerDPS());
      spawner = new Timer (1000, new ListenerSpawn());
      clock= new Timer(1000, new clocktime());
      background = null;
      player = null;
      player2 = null;
      ball = null;
      menu = new Menu();
      try {
         background = ImageIO.read(new File("images/red_forest.png"));
      } catch (IOException e) {
      }     
      
      try{
         player = ImageIO.read(new File("images/sniper_sprites/rifleman_north.png"));
      }catch(IOException e) {
      }
      try{
         gameover = ImageIO.read(new File("images/gui/gameover.png"));
      }catch(IOException e) {
      } 
      try{
         victory = ImageIO.read(new File("images/gui/victory.png"));
      }catch(IOException e) {
      } 
      
      try{
         projectile = ImageIO.read(new File("images/sniper_sprites/ball.png"));
      }catch(IOException e) {
      }
      try{
         title = ImageIO.read(new File("images/gui/title.png"));
      }catch(IOException e) {
      }
      
      try{
         credits = ImageIO.read(new File("images/gui/credits.png"));
      }catch(IOException e) {
      }
      try{
         sniper = ImageIO.read(new File("images/guisprofiles/profile_sniper.png"));
      }catch(IOException e) {
      }
   
      try{
         engineer = ImageIO.read(new File("images/gui/profiles/profile_engineer.png"));
      }catch(IOException e) {
      }
   
      try{
         rifleman = ImageIO.read(new File("images/gui/profiles/profile_rifleman.png"));
      }catch(IOException e) {
      }
   
      try{
         player2 = ImageIO.read(new File("images/sniper_sprites/engineer_east.png"));
      }catch(IOException e) {
      }  
   }
   public void showBoard(Graphics g, Graphics2D g2d, AffineTransform at)   
   {
      if(state == STATE.GAME)
      {
         g.drawImage(background, 0, 0, 1600, 900, null);  //draws background
         //g.drawImage(player, player1.getX(), player1.getY(), 30,20, null);
         g2d.drawImage(player,at,null);
         g2d.drawRect(player1.getX(),player1.getY(),11,11);
         if (enemyList.size()>0)
         {
            for (int i=enemyList.size()-1; i>=0; i--)
            {
               g.drawImage (player2, enemyList.get(i).getX(),enemyList.get(i).getY(), 27, 11, null);
            }
         }
         g.setColor(Color.YELLOW);
         g.drawString("("+player1.getX()+":"+player1.getY()+")("+player1.getVelX()+":"+player1.getVelY()+"  ANGLE: "+angle, 12, 12);   
         g.drawString("Health: "+player1.getH()+", Bullets: "+ammo1, 50, 780); 
         g.drawString("Time Remaining: "+counter, 50, 800);     
         for(int i = 0; i < projectileList.size(); i++)
         {
            g.drawImage(projectile, (int)(projectileList.get(i).getX()), (int)(projectileList.get(i).getY()), 4,4,null);
         }
      }
      else if(state == STATE.MENU)
      {
         g.drawImage(title, 0, 0, 1600, 900, null);
         menu.renderMenu(g);
      }
      else if(state == STATE.CREDITS)
      {
         g.drawImage(credits, 0, 0, 1600, 900, null);
         menu.renderCredits(g);
      }
      else if(state == STATE.GAMEOVER)
      {
         g.drawImage(gameover, 0, 0, 1600, 900, null);
         menu.renderCredits(g);
      }
      else if(state == STATE.VICTORY)
      {
         g.drawImage(victory, 0, 0, 1600, 900, null);
         menu.renderCredits(g);
      }
   }  
   public Color checkRGB(int x, int y)
   {   
      if(background!=null && x>0 && x<1600 && y>0 && y<900)
      {
         Color mycolor=new Color (background.getRGB(x,y));
         return mycolor;     
      }
      else 
         return null;
   }
   public boolean checkCollisions()
   {
      OUTER:for (int i=0; i<12; i++)
      {
         for (int j=0; j<12; j++)
         {
            Color mycolor=checkRGB(player1.getX()+player1.getVelX()+j, player1.getY()+player1.getVelY()+i);
            if (mycolor.getRed()==255 && mycolor.getGreen()==0 && mycolor.getBlue()==0)
            {
               check=true;
               break OUTER;
            }
         } 
      }
      return check; 
   }
   public boolean checkColl2(int i) //collision detection for enemies NEED FIXING
   {
      Player jeff=enemyList.get(i);
      OUTER:for (int k=0; k<12; k++)
      {
         for (int j=0; j<12; j++)
         {
            Color mycolor=checkRGB(jeff.getX()+jeff.getVelX()+j, jeff.getY()+jeff.getVelY()+k);
            if (mycolor!=null && mycolor.getRed()==255 && mycolor.getGreen()==0 && mycolor.getBlue()==0)
            {
               check2=true;
               break OUTER;
            } 
         } 
      }
      return check2; 
   }
   public boolean checkCollBull(int i)
   {
      OUTER:for (int k=0; k<4; k++)
      {
         for (int j=0; j<4; j++)
         {
            Color mycolor=checkRGB((int)projectileList.get(i).getX()+(int)projectileList.get(i).getVelX()+j, (int)projectileList.get(i).getY()+(int)projectileList.get(i).getVelY()+k);
            if (mycolor.getRed()==255 && mycolor.getGreen()==0 && mycolor.getBlue()==0)
            {
               checkB=true;
               break OUTER;
            }
         }
      }
      return checkB;
   }
   
   public void enemyHit()
   {
      int px=player1.getX()+player1.getVelX();
      int py=player1.getY()+player1.getVelY();
      if (enemyList.size()>0)
      {
         for (int i=enemyList.size()-1; i>=0; i--)
         {
            int x2=enemyList.get(i).getX()+enemyList.get(i).getVelX();
            int y2=enemyList.get(i).getY()+enemyList.get(i).getVelY();
            OUTER: for (int k=0; k<20; k++)
            {
               for (int j=0; j<20; j++)
               {
                  for (int e=0; e<12; e++)
                  {
                     for (int z=0; z<12; z++)
                     {
                        if (px+e == x2+j && py+z == y2+k)
                        {
                           player1.setH(player1.getH()-1);
                        }
                     }
                  }
               }
            }
         
         }
      }
   }
   
   public boolean enemyCollision(int i)
   {
      if (enemyList.size()>=2)
      {
         int x1=enemyList.get(i).getX()+enemyList.get(i).getVelX();
         int y1=enemyList.get(i).getY()+enemyList.get(i).getVelY();
         for (int k=enemyList.size()-1; k>=0; k--)
         {
            int x2=enemyList.get(k).getX()+enemyList.get(k).getVelX();
            int y2=enemyList.get(k).getY()+enemyList.get(k).getVelY();
            if (i==k)
            {
               continue;
            }
            else 
               OUTER: for (int j=0; j<20; j++)
               {
                  for (int c=0; c<20; c++)
                  {
                     for (int z=0; z<20; z++)
                     {
                        for (int v=0; v<20; v++)
                        {
                           if (x1+c == x2+v && y1+j == y2+z)
                           {
                              checkE=true;
                              break OUTER;
                           }
                        }
                     }    
                  }
               }
         }
      }
      return checkE;
   }
   
   public void collide(int i)
   {
      if (projectileList.size()>0 && enemyList.size()>0)
      {
         int pX=(int)projectileList.get(i).getX()+(int)projectileList.get(i).getVelX();
         int pY=(int)projectileList.get(i).getY()+(int)projectileList.get(i).getVelY();
      
         for (int s=enemyList.size()-1; s>=0; s--)
         { 
            int x2=enemyList.get(s).getX()+enemyList.get(s).getVelX();
            int y2=enemyList.get(s).getY()+enemyList.get(s).getVelY();
            OUTER: for (int k=0; k<4; k++)
            {
               for (int j=0; j<4; j++)
               {
                  for (int e=0; e<20; e++)
                  {
                     for (int z=0; z<20; z++)
                     {
                        if (pX+j == x2+z && pY+k == y2+e)
                        {
                           projectileList.remove(i);
                           enemyList.get(s).setH(enemyList.get(s).getH()-100);
                           if(enemyList.get(s).getH()<=0)
                           {
                              enemyList.remove(enemyList.get(s));
                           }
                           break OUTER;
                        }
                     }
                  }
               }
            }
         } 
      }  
   }  
   public void processKeyPressed(int k)
   {   
      if(k==KeyEvent.VK_A)
      {
         
         tL.start();    
      }
      else if(k==KeyEvent.VK_D)
      {                        
         tR.start();            
      }
      else if(k==KeyEvent.VK_W)
      {            
         tU.start();            
      }
      else if (k==KeyEvent.VK_S)
      {
         tD.start();              
      }    
      else if(k==KeyEvent.VK_SPACE)
      {
         Color temp= new Color (background.getRGB(player1.getX(), player1.getY()));
         System.out.println(temp);
      }
   }  
   public void processKeyReleased(int k)
   { 
      if(k==KeyEvent.VK_A)
      {
         player1.setVelX(0);
         tL.stop();
      }
      else if(k==KeyEvent.VK_D)
      {
         player1.setVelX(0);
         tR.stop();
      }
      else if(k==KeyEvent.VK_W)
      {
         player1.setVelY(0);
         tU.stop();
      }
      else if (k==KeyEvent.VK_S)
      {
         player1.setVelY(0);
         tD.stop();
      }
      else if (k==KeyEvent.VK_R)
      {
         reload1.start();
      }
   }
   
   public void processMousePressed(MouseEvent e)
   {
      int mx = e.getX();
      int my = e.getY();
      if(state == STATE.MENU)
      {
         if(mx >= 730 && mx <= 730 + 150)
         {
            if(my >= 280 && my <= 330)
            {
               state = state.GAME;
               enemymove.start();
               dps.start();
               spawner.start();
               clock.start();
            }
         }
         if(mx >= 730 && mx <= 730 + 150)
         {
            if(my >= 380 && my <= 430)
            {
               state = state.CREDITS;
               
            }
         }
         if(mx >= 730 && mx <= 730 + 150)
         {
            if(my >= 480 && my <= 530)
            {
               System.exit(1);
            }
         }
      }
      if(state == STATE.CREDITS)
      {
         if(mx >= 680 && mx <= 680 + 150)
         {
            if(my >= 730 && my <= 780)
            {
               state = state.MENU;
            }
         }
      }
      if(state == STATE.VICTORY || state == STATE.GAMEOVER)
      {
         if(mx >= 0 && mx <= 1600)
         {
            if(my >= 0 && my <= 900)
            {
               System.exit(1);
            }
         }
      }
      if(state==STATE.GAME)
      {
         if(e.getButton() == MouseEvent.BUTTON1 && ammo1>0 && !reload1.isRunning())
         {
            angle = Math.atan2(my - player1.getY(), mx - player1.getX());
            double run =  Math.cos(angle);
            double rise =  Math.sin(angle);
            ball = new Projectile ((double)(player1.getX()), (double)(player1.getY()), 1.5*run, 1.5*rise);
            
            projectileList.add(ball); 
            ammo1--;   
              
            if (ammo1==0)
            {
               reload1.start();
            }
         }
         else if(e.getButton() == MouseEvent.BUTTON3)
         {
            System.out.println("("+e.getPoint().getX()+","+e.getPoint().getY()+")");
         }
         
         
         else if(e.getButton() == MouseEvent.BUTTON3)
         {
            System.out.println("("+e.getPoint().getX()+","+e.getPoint().getY()+")");
         }
      
      }   
   }
   
   
   public void processMouseReleased()
   {
   }
   
   
   public void processMouseMoved(MouseEvent e)
   {
      mouseX = e.getX();
      mouseY = e.getY();   
   }
   

      
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      AffineTransform at = new AffineTransform();   
      at.translate(player1.getX()+5, player1.getY()+5);	//translate it to the center of the component 
      at.rotate(Math.toRadians(angle));					//do the actual rotation          
      at.scale(scale, scale);									//scale the image
      at.translate(-player.getWidth()/2, -player.getHeight()/2);	//translate so you can rotate around the center 
      Graphics2D g2d = (Graphics2D) g;
      showBoard(g,g2d,at);  
   } 
   private class ListenerR implements ActionListener
   {
      public void actionPerformed(ActionEvent e)   
      {  
         player1.setVelX(2);
         if (checkCollisions()==true)
         {
            player1.setVelX(0);
            if (player1.getVelY()<0)
            {
               player1.setVelY(-1);
            }
            if (player1.getVelY()>0)
            {
               player1.setVelY(1);
            }
         }
         else{
            if (player1.getVelX()+player1.getVelY()>2)
            {
               player1.setVelX(1);
               player1.setVelY(1);
            }
            if (player1.getVelX()+player1.getVelY()<2)
            {
               player1.setVelX(1);
               player1.setVelY(-1);
            }
         }
         player1.tick();
         check=false; 
      }
   }
   private class ListenerL implements ActionListener
   {
      public void actionPerformed(ActionEvent e)   
      {  
         player1.setVelX(-2);
         if (checkCollisions()==true)
         {
            player1.setVelX(0);
            if (player1.getVelY()<0)
            {
               player1.setVelY(-1);
            }
            if (player1.getVelY()>0)
            {
               player1.setVelY(1);
            }
         }
         else{
            if (player1.getVelX()+player1.getVelY()<-2)
            {
               player1.setVelX(-1);
               player1.setVelY(-1);
            }
            if (player1.getVelX()+player1.getVelY()>-2)
            {
               player1.setVelX(-1);
               player1.setVelY(1);
            }
         }
         player1.tick();
         check=false;    
      }
   } 
   private class ListenerU implements ActionListener
   {
      public void actionPerformed(ActionEvent e)   
      {  
         player1.setVelY(-2);
         if (checkCollisions()==true)
         {
            player1.setVelY(0);
            if (player1.getVelX()<0)
            {
               player1.setVelX(-1);
            }
            if (player1.getVelX()>0)
            {
               player1.setVelX(1);
            }
         }
         else{
            if (player1.getVelX()+player1.getVelY()<-2)
            {
               player1.setVelX(-1);
               player1.setVelY(-1);
            }
            if (player1.getVelX()+player1.getVelY()>-2)
            {
               player1.setVelX(1);
               player1.setVelY(-1);
            }
         }
         player1.tick();
         check=false;  
      }
   } 
   private class ListenerD implements ActionListener
   {
      public void actionPerformed(ActionEvent e)   
      {    
         player1.setVelY(2);
         if (checkCollisions()==true)
         {
            player1.setVelY(0);
            if (player1.getVelX()<0)
            {
               player1.setVelX(-1);
            }
            if (player1.getVelX()>0)
            {
               player1.setVelX(1);
            }
         }   
         else {
            if (player1.getVelX()+player1.getVelY()>2)
            {
               player1.setVelX(1);
               player1.setVelY(1);
            }
            if (player1.getVelX()+player1.getVelY()<2)
            {
               player1.setVelX(-1);
               player1.setVelY(1);
            }
         } 
         player1.tick();
         check=false;  
      }
   }  
   private class ListenerE implements ActionListener
   {
      public void actionPerformed (ActionEvent e)
      {
         if (enemyList.size()>0)
         {
            for (int i=enemyList.size()-1; i>=0; i--)
            {
               if (player1.getX() > enemyList.get(i).getX()) 
               {
                  enemyList.get(i).setVelX(2);
               }  
               if (player1.getX() < enemyList.get(i).getX())
               {
                  enemyList.get(i).setVelX(-2);
               } 
               if (player1.getY() > enemyList.get(i).getY())
               {
                  enemyList.get(i).setVelY(2);
               } 
               if (player1.getY()<enemyList.get(i).getY())
               {
                  enemyList.get(i).setVelY(-2);
               }   
               if (checkColl2(i)==true || enemyCollision(i)==true) 
               {
                  enemyList.get(i).setVelX(0);
                  enemyList.get(i).setVelY(0);
               }
               enemyList.get(i).tick();
               check2=false;
               checkE=false;
            }
         }
      }
   }
   
   private class ListenerMouse implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         if (projectileList.size()>0)
         {
            for (int i=projectileList.size()-1; i>=0; i--)
            {
               collide(i);
            }
         }
         if (projectileList.size()>0)
         {
            for (int i=projectileList.size()-1; i>=0; i--)
            { 
               if (checkCollBull(i)==true)
               {
               
                  projectileList.remove(i);
                  checkB=false;
               }
               else
               {
                  projectileList.get(i).tick();
               }
            }
         }   
         if(state == STATE.GAME)
         {
            double diffX = Math.abs((player1.getX() + 32) - mouseX);
            double diffY = Math.abs((player1.getY()) - mouseY);
         
            if(diffY != 0 && diffX != 0)
            {
               angle = Math.toDegrees(Math.atan(diffX / diffY));						//add to the angle, wrap around back to 0 after 360
               
               if((player1.getY()) - mouseY < 0)
               {
                  angle = Math.toDegrees(Math.atan(diffY / diffX)) + 90;
               }
               if((player1.getX() + 32) - mouseX > 0)
               {
                  angle = Math.toDegrees(Math.atan(diffY / diffX)) + 270;
               }
               if(((player1.getX() + 32) - mouseX > 0) && ((player1.getY()) - mouseY < 0))
               {
                  angle = Math.toDegrees(Math.atan(diffX / diffY)) + 180;
               }
            }               
         //angle = 90;
            scale = scale + dScale;							//add or subtract the scale
            if ((scale >= 15 && dScale > 0) || (scale < 0.5 && dScale < 0))
               dScale *= -1;									//flip the scale if we get to a max or min size		
         }
      
      }
   }
   private class reload1 implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         while (ammo1<30)
         {
            ammo1++;
         }
         
         reload1.stop();
      }
   }
   private class ListenerSpawn implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         spawnLoc=(int)(Math.random()*(6-1))+1;
         if (enemyList.size()<25)
         {
            if (spawnLoc==1)
            {
               enemyList.add(new Player((int)(Math.random()*(170-40))+40, (int)(Math.random()*(800-100))+100, 100));
            }
            else 
               if (spawnLoc == 2)
               {
                  enemyList.add(new Player((int)(Math.random()*(420-280))+280, (int)(Math.random()*(800-200))+100, 100));
               }
               else 
                  if (spawnLoc == 3)
                  {
                     enemyList.add(new Player((int)(Math.random()*(650-500))+500, (int)(Math.random()*(800-200))+200, 100));
                  }  
                  else 
                     if (spawnLoc == 4)
                     {
                        enemyList.add(new Player((int)(Math.random()*(950-780))+780, (int)(Math.random()*(800-200))+200, 100));
                     }
                     else 
                        if (spawnLoc == 5)
                        {
                           enemyList.add(new Player((int)(Math.random()*(1250-1160))+1160, (int)(Math.random()*(800-200))+100, 100));
                        }
                        else 
                           if (spawnLoc == 6)
                           {
                              enemyList.add(new Player((int)(Math.random()*(1520-1420))+1420, (int)(Math.random()*(800-200))+200, 100));
                           }
         }
      }
   }
   private class fps implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         repaint();
         if(player1.getH() <= 0)
         {
            state = STATE.GAMEOVER;
         }
      }
   }
   private class ListenerDPS implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         enemyHit();
      }
   }
   private class clocktime implements ActionListener
   {
      public void actionPerformed (ActionEvent e)
      {
         counter--;
         if(counter == 0)
         {
            state = STATE.VICTORY;
         }
      }
   }

}



