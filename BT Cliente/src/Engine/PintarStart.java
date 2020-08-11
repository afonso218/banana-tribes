package Engine;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PintarStart extends JLabel implements Runnable{
	
	private Engine main;
	private boolean single = false;
	private boolean multi = false;
	private boolean musicaOn = true;

	private ImageIcon On = new ImageIcon(getClass().getResource("Image/green.gif"));
	private ImageIcon Off = new ImageIcon(getClass().getResource("Image/red.gif"));

	private ImageIcon piano = new ImageIcon(getClass().getResource("Image/Piano_Dance.gif"));
	
	private ImageIcon multi1 = new ImageIcon(getClass().getResource("Image/Fruit3.gif"));
	private ImageIcon multi2 = new ImageIcon(getClass().getResource("Image/Drinking.gif"));
	private ImageIcon multi3 = new ImageIcon(getClass().getResource("Image/Joy.gif"));
	private ImageIcon multi4 = new ImageIcon(getClass().getResource("Image/apple.gif"));
	private ImageIcon multi5 = new ImageIcon(getClass().getResource("Image/Carrot.gif"));
	
	private ImageIcon macaco1 = new ImageIcon(getClass().getResource("Image/monkey.gif"));
	private ImageIcon banana1 = new ImageIcon(getClass().getResource("Image/Fire_Jump.gif"));
	private ImageIcon macaco2 = new ImageIcon(getClass().getResource("Image/monkey2.gif"));
	private ImageIcon banana2 = new ImageIcon(getClass().getResource("Image/Dance_Steps.gif"));
	
	private ImageIcon back = new ImageIcon(getClass().getResource("Image/backgroundStart.gif"));
	private ImageIcon frame = new ImageIcon(getClass().getResource("Image/wood4frame.gif"));
	
	public PintarStart(Engine main) {
		this.main = main;
	}
	
	public void paint(Graphics g){
	
		// TITULO DO JOGO
		g.drawImage(back.getImage(),0,0,null);
		g.setColor(Color.black);
		g.setFont(new Font("Edwardian Script ITC", Font.BOLD, 100));
		g.drawString("Banana Tribe", 400 - 270, 75);

		//MUSIC ICON
		g.drawImage(piano.getImage() , 675 , 5, null);
		if(musicaOn)
			g.drawImage(On.getImage() , 725 , 50, null);
		else
			g.drawImage(Off.getImage() , 725 , 50 ,null);
		
		// LAYOUT CIRCLE A ESQUERDA <- SINGLE
		g.drawImage(frame.getImage() , 80 , 95 ,null);
		// cor do fundo do Rect
		if(single)
			g.setColor(Color.red);
		else
			g.setColor(Color.orange);
		g.fillRect(127, 370, 225, 75);
		// cor da linha do Rect
		if(single)
			g.setColor(Color.orange);
		else
			g.setColor(Color.red);
		g.drawRect(127, 370, 225, 75);
		//texto
		if(single)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.CENTER_BASELINE, 25));
		g.drawString("Single Player", 155, 417);
		
		
		// LAYOUT CIRCLE A DIREITA <- SINGLE
		g.drawImage(frame.getImage() , 400 , 95 ,null);
		// cor do fundo do Rect
		if(multi)
			g.setColor(Color.red);
		else
			g.setColor(Color.orange);
		g.fillRect(447, 370, 225, 75);
		// cor da linha do Rect
		if(multi)
			g.setColor(Color.orange);
		else
			g.setColor(Color.red);
		g.drawRect(447, 370, 225, 75);
		//texto
		if(multi)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.CENTER_BASELINE, 25));
		g.drawString("Multi Player", 485, 417);
		
		// COLOCAR IMAGENS SINGLE PLAYER
		g.drawImage(macaco1.getImage() , 150 , 320 ,null);
		g.drawImage(banana1.getImage() , 235 , 320, null );
		
		g.drawImage(macaco2.getImage() , 240 , 220 ,null);
		g.drawImage(banana2.getImage() , 150 , 230, null);
		
		// COLOCAR IMAGENS MULTI PLAYER
		g.drawImage(multi2.getImage() , 455 , 325, null);
		g.drawImage(multi1.getImage() , 500 , 150 ,null);
		g.drawImage(multi3.getImage() , 600 , 300 ,null);
		g.drawImage(multi4.getImage() , 545 , 200, null);
		g.drawImage(multi5.getImage() , 590 , 150, null);
	}
	
	// METODO DE MUDANAA DE COR
	public void MouseClicada(MouseEvent e) {
		if(e.getX() > 80 && e.getX() < 400 && e.getY() > 95 && e.getY() < 485 )
			single = true;
		else
			single = false;
		if(e.getX() > 400 && e.getX() < 715 && e.getY() > 95 && e.getY() < 485 )
			multi = true;
		else
			multi = false;	
	}
	
	// METODO PARA ACCIONAR CLICKE
	public void MouseClick(MouseEvent e) throws IOException {
		
		if(e.getX() > 80 && e.getX() < 400 && e.getY() > 95 && e.getY() < 485 )
			main.StartGameSingle();
		if(e.getX() > 400 && e.getX() < 715 && e.getY() > 95 && e.getY() < 485 )
			main.StartGameMulti();
		if(e.getX() > 675 && e.getX() < 675 + piano.getIconWidth() && e.getY() > 5 && e.getY() < 5 + piano.getIconHeight()){
			musicaOn = !musicaOn;
			main.MusicChange(musicaOn);
		}
		
	}
	
	public void run(){
		try{
			while(!Thread.interrupted()){
				repaint();
				Thread.sleep(25);
			}
		}catch(InterruptedException e){}
	}
	
}
