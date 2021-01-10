import javax.swing.*;
import java.awt.event.*;
public class GameDriver						//Driver Program
{
   public static GamePanel screen;					//Game window
  
   
   public static void main(String[]args)
   {
      screen = new GamePanel();
      JFrame frame = new JFrame("Game");	//window title
      frame.setSize(1600, 920);					//Size of game window
      frame.setLocation(100, 50);				//location of game window on the screen
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(new listen());		//Get input from the keyboard
      frame.addMouseListener(new mouse()); 
      frame.addMouseMotionListener(new mouseMotion());  
   }
   
   public static class listen implements KeyListener
   {   
      public void keyTyped(KeyEvent e)
      {         
      }
         
      public void keyPressed(KeyEvent e)
      {
         screen.processKeyPressed(e.getKeyCode());   
      }
      public void keyReleased(KeyEvent e)
      {
         screen.processKeyReleased(e.getKeyCode());
      }
   }   
   
   public static class mouse implements MouseListener
   {
      public void mouseClicked(MouseEvent e)
      {   
      }
      public void mouseEntered(MouseEvent e)
      {   
      }
      
      public void mouseExited(MouseEvent e)
      {
      }
      
      public void mousePressed(MouseEvent e)
      {     
         screen.processMousePressed(e);
      }
      
      public void mouseReleased(MouseEvent e)
      {
      }      
   }   
   
   public static class mouseMotion implements MouseMotionListener
   {
      public void mouseDragged(MouseEvent e)
      {
      }
   
      public void mouseMoved(MouseEvent e)
      {
         screen.processMouseMoved(e);
      }
   }
 
}