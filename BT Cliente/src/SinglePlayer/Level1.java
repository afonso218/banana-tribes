package SinglePlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import Mapa.Coordenada;
import Mapa.Mapa;
import Mapa.Player;
import Pecas.*;

public class Level1 extends JLabel implements Level, Runnable {

	private int id = 0;
	private String time = "00:00";
	private Boss boss;
	private King banana;
	private Player cpu;

	private int x = 15;
	private int y = 13;
	private int TotalX;
	private int TotalY;

	private int xRec;
	private int yRec;

	private JTextArea text;
	private boolean jumpLevel = false;
	private boolean loss = false;
	private boolean startPress = false;
	private boolean isCompleted = false;

	private Mapa mapa;
	private Thread threadMapa;
	private Peca PecaActiva = null;
	private ImageIcon tabuleiro = new ImageIcon(getClass().getResource(
			"Image/background.gif"));
	private ImageIcon exit = new ImageIcon(getClass().getResource(
		"Image/exit.gif"));

	public Level1(JTextArea text) {
		
		this.text = text;
		this.cpu = new Player();
		Player player = new Player("Player Standard", 1);
		mapa = new Mapa(x, y, player, cpu);
		threadMapa = new Thread(mapa);

		// CRIAccO DE BOOS e a BANANA DO JOGADOR + OBSTACULOS
		banana = new King(id, player, new Coordenada(5, 5));
		mapa.add(banana);
		banana.setSpeed(0);
		id++;
		boss = new Boss(id, cpu, new Coordenada(10, 5));
		boss.setDestino(new Coordenada(5, 5));
		mapa.add(boss);
		id++;
		
		// COLOCAccO DE LIMITES NO MAPA COMO OBSTACULOS
		Coordenada a;
		for (int i = 0; i < x; i++) {
			a = new Coordenada(i, 0);
			mapa.add(new Limite(id, a));
			id++;
			a = new Coordenada(i, y - 1);
			mapa.add(new Limite(id, a));
			id++;
		}
		for (int i = 1; i < y - 1; i++) {
			a = new Coordenada(0, i);
			mapa.add(new Limite(id, a));
			id++;
			a = new Coordenada(x - 1, i);
			mapa.add(new Limite(id, a));
			id++;
		}

		// ADD OBSTACULOS AO MAPA
		a = new Coordenada(2, 11);
		mapa.add(new Mountain(id, a));
		id++;
		a = new Coordenada(3, 8);
		mapa.add(new Mountain(id, a));
		id++;
		a = new Coordenada(11, 3);
		mapa.add(new Mountain(id, a));
		id++;
		// add LAKES
		a = new Coordenada(2, 2);
		mapa.add(new Lake(id, a));
		id++;
		a = new Coordenada(9, 7);
		mapa.add(new Lake(id, a));
		id++;
		a = new Coordenada(8, 5);
		mapa.add(new Lake(id, a));
		id++;
		// add HILLS
		a = new Coordenada(7, 11);
		mapa.add(new Hills(id, a));
		id++;
		a = new Coordenada(1, 5);
		mapa.add(new Hills(id, a));
		id++;
	}

	public void paint(Graphics g) {

		TotalX = getWidth();
		TotalY = getHeight();
		xRec = getWidth() / x;
		yRec = getHeight() / y;

		g.drawImage(tabuleiro.getImage(), 0, 0, null);

		// IMPRIME OBSTACULOS
		for (int i = 0; i < mapa.getMapa().length; i++) {
			for (int j = 0; j < mapa.getMapa()[i].length; j++) {
				if (mapa.getMapa()[i][j] != null) {
					Peca a = (Peca) mapa.getMapa()[i][j];
					if (a.isObstaculo()) {
						ImageIcon imagem = a.Imagem();
						g.drawImage(imagem.getImage(), xRec * i + xRec / 2
								- imagem.getIconWidth() / 2, yRec * j + yRec
								/ 2 - imagem.getIconHeight() / 2, null);
					}
					if (a == PecaActiva) {
						// RANGE
						g.setColor(Color.yellow);
						int comp = xRec * (a.getRange() * 2 + 1);
						int larg = yRec * (a.getRange() * 2 + 1);
						g.drawOval(xRec * i + xRec / 2 - comp / 2, yRec * j
								+ yRec / 2 - larg / 2, comp, larg);
						// VIDA
						g.setColor(Color.red);
						g.fillRect(xRec * i, yRec * j, xRec, yRec / 15);
						g.setColor(Color.green);
						g.fillRect(xRec * i, yRec * j, xRec,
								(a.getLife() / a.getTotallife()) * (yRec / 15));

					}

				}
			}
		}
		// IMPRIME PEcAS
		for (int i = 0; i < mapa.getMapa().length; i++) {
			for (int j = 0; j < mapa.getMapa()[i].length; j++) {
				if (mapa.getMapa()[i][j] != null) {
					Peca a = (Peca) mapa.getMapa()[i][j];
					if (!a.isObstaculo()) {
						ImageIcon imagem = a.Imagem();
						if (a == PecaActiva) {
							// RANGE
							g.setColor(Color.yellow);
							int comp = xRec * (a.getRange() * 2 + 1);
							int larg = yRec * (a.getRange() * 2 + 1);
							g.drawOval(xRec * i + xRec / 2 - comp / 2, yRec * j
									+ yRec / 2 - larg / 2, comp, larg);
							// VIDA
							g.setColor(Color.red);
							g.fillRect(xRec * i, yRec * j, xRec, yRec / 15);
							g.setColor(Color.green);
							g.fillRect(xRec * i, yRec * j, xRec,
									(a.getLife() / a.getTotallife())
											* (yRec / 15));
						}
						g.drawImage(imagem.getImage(), xRec * i + xRec / 2
								- imagem.getIconWidth() / 2, yRec * j + yRec
								/ 2 - imagem.getIconHeight() / 2, null);
					}
				}
			}
		}
		// RELOGIO / CONTADOR
		g.setColor(Color.red);
		g.fillRect(600, 5, 125, 40);
		g.setColor(Color.orange);
		g.drawRect(600, 5, 125, 40);
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString(time, 610, 40);

		if(!startPress){
			g.setColor(Color.yellow);
			g.fillRect(100, 100, 600, 400);
			g.setColor(Color.red);
			g.drawRect(100, 100, 600, 400);
			g.setFont(new Font("Arial", Font.BOLD, 70));
			g.drawString("START", 400-130, 300);
			g.setColor(Color.blue);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("LEVEL 1", 400-100, 400);
		}
		
		g.drawImage(exit.getImage(), getWidth()-exit.getIconWidth() , getHeight()-exit.getIconHeight() ,null);
	}

	public void MouseMove(MouseEvent e) {
	}

	public void MouseClicada(MouseEvent e) {

		if (e.getButton() == 3) {
			PecaActiva = null;
		}
		
		if(e.getX() > getWidth()-exit.getIconWidth() && e.getX() < getWidth() &&
				e.getY() > getHeight()-exit.getIconHeight() && e.getY() < getHeight())
			jumpLevel = true;

		if(!startPress){
			if(e.getX() > 100 && e.getX() < 700 && e.getY() > 100 && e.getY() < 500)
				startPress = true;
		}
		// SELECIONA IMAGEM
		if (e.getX() > 0 && e.getX() < TotalX && e.getY() > 0 && e.getY() < TotalY) {
			Object obj = mapa.getMapa()[e.getX() / xRec][e.getY() / yRec];
			if (obj != null) {
				if (!((Peca) obj).isObstaculo() && !((Peca) obj).isBoss())
					PecaActiva = (Peca) mapa.getMapa()[e.getX() / xRec][e
							.getY() / yRec];
			} else {
				if (PecaActiva != null)
					PecaActiva.setDestino(new Coordenada(e.getX() / xRec, e
							.getY() / yRec));
			}
		}
	}

	public boolean getComplete(){
		return isCompleted;
	}
	
	public boolean getLoss(){
		return loss;
	}
	
	public boolean jumpLevel(){
		return jumpLevel;
	}
	

	public void run() {
		// comeca o run do mapa

		try {
			text.setText(text.getText() + "\n" + "Welcome to Single Play:\n"
					+ "Objective Level:\n"
					+ ">Stay fair away from the monkey!\n"
					+ ">You have 30 seconds!" + "Good Luck ;)");
					
			int tempoFinal = (int) (System.currentTimeMillis() / 1000);
			int tempo = 0;
			repaint();
			
			while(!startPress){
				tempoFinal = (int) (System.currentTimeMillis() / 1000);
				tempo = 0;
				Thread.sleep(1000);
				repaint();
			}
			threadMapa.start();
			PecaActiva = mapa.getPeca(new Coordenada(5, 5));
			
			
			while (tempo < 30 && !verificaPosicao(boss.getActual())) {

				if (boss.getActual().isIgual(boss.getDestino()))
					boss.setDestino(banana.getDestino());

				tempo = (int) ((System.currentTimeMillis()) / 1000 - tempoFinal);
				if (tempo > 9)
					time = "00:" + tempo;
				else
					time = "00:0" + tempo;
				repaint();
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {}
		
		repaint();
		if (verificaPosicao(boss.getActual())) {
			threadMapa.interrupt();
			text.setText(text.getText() + "\n" + "> You lose! You let the monkey to reach you!");
			loss = true;
		} else {
			threadMapa.interrupt();
			text.setText(text.getText() + "\n" + "> Well Done! Level 1 Completed!");
			isCompleted = true;
		}
	}

	public boolean verificaPosicao(Coordenada coord) {
		// SE ESTIVER EM REDOR RETURN TRUE
		int x1 = coord.getX();
		int y1 = coord.getY();
		int x2 = banana.getActual().getX();
		int y2 = banana.getActual().getY();

		if (((x1 - 1 == x2) && (y1 - 1 == y2))
				|| ((x1 - 1 == x2) && (y1 == y2))
				|| ((x1 - 1 == x2) && (y1 + 1 == y2))
				|| ((x1 == x2) && (y1 + 1 == y2))
				|| ((x1 == x2) && (y1 - 1 == y2))
				|| ((x1 + 1 == x2) && (y1 + 1 == y2))
				|| ((x1 + 1 == x2) && (y1 == y2))
				|| ((x1 + 1 == x2) && (y1 - 1 == y2))) {
			return true;
		}
		return false;

	}

}
