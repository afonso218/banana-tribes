package SinglePlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class End extends JLabel implements Runnable{

	private ImageIcon polar = new ImageIcon(getClass().getResource(
	"Image/party.gif"));
	
	public void paint(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(polar.getImage(), 122 , 100 ,null);
		//WELL DONE
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("WELL DONE!!!", 400 - 125, 70);
		//1 FRASE
		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("YOU ARE READY TO START MULTIPLAYER!", 400 - 250, 500);
		// 2 FRASE
		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.BOLD, 25));
		g.drawString("CHALLENGE ACCEPTED!", 400 - 150, 550);
		
	}
	
	public void run(){
		try {
			while(true){
				repaint();
				Thread.sleep(25);		
			}
		} catch (InterruptedException e) {
			System.out.println("END PAINT: interrompido");
		}
	}
}


