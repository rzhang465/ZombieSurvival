import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Menu
{
   public Rectangle connectButton = new Rectangle(720, 150, 150, 50);
   public Rectangle hostButton = new Rectangle(720, 250, 150, 50);
   public Rectangle quitButton = new Rectangle(720, 450, 150, 50);
   public Rectangle creditsButton = new Rectangle(720, 350, 150, 50);
   public Rectangle backButton = new Rectangle(670, 700, 150, 50);
   public Rectangle leftButton = new Rectangle(1101, 665, 85, 83);
   public Rectangle rightButton = new Rectangle(1365, 665, 85, 83);
   public void renderMenu(Graphics g)
   {
      Graphics2D g2d = (Graphics2D) g;
      Font fnt0 = new Font("TimesRoman", Font.BOLD, 100);
      g.setFont(fnt0);
      g.setColor(Color.white);
      g.drawString("ZOMBIE SURVIVAL", 450, 100);
      Font fnt1 = new Font("TimesRoman", Font.BOLD, 30);
      g.setFont(fnt1);
      g.drawString("PLAY", hostButton.x + 32, hostButton.y + 35);
      g2d.draw(hostButton);
      g.drawString("QUIT", quitButton.x + 38, quitButton.y + 35);
      g2d.draw(quitButton);
      g.drawString("CREDITS", creditsButton.x + 9, creditsButton.y + 35);
      g2d.draw(creditsButton);
   }
   public void renderCredits(Graphics g)
   {
      Graphics2D g2d = (Graphics2D) g;
      g.setColor(Color.white);
      Font fnt1 = new Font("TimesRoman", Font.BOLD, 30);
      g.setFont(fnt1);
      g.drawString("QUIT", backButton.x + 32, backButton.y + 35);
      g2d.draw(backButton);
   }
   public void renderLobby(Graphics g)
   {
      Graphics2D g2d = (Graphics2D) g;
      g.setColor(Color.white);
      Font fnt1 = new Font("TimesRoman", Font.BOLD, 30);
      g.setFont(fnt1);
      g.drawString("QUIT", backButton.x + 32, backButton.y + 35);
      g2d.draw(backButton);
      g2d.draw(leftButton);
      g2d.draw(rightButton);
   }
}