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

public class Level2 extends JLabel implements Level, Runnable {

	private int id = 0;
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
	private boolean loss = false;
	private boolean jumpLevel = false;
	private boolean startPress = false;
	private boolean isCompleted = false;

	private Mapa mapa;
	private Thread threadMapa;
	private Peca PecaActiva = null;
	private ImageIcon tabuleiro = new ImageIcon(getClass().getResource(
		"Image/background.gif"));
	private ImageIcon atak = new ImageIcon(getClass().getResource(
		"Image/swords.gif"));
	private ImageIcon exit = new ImageIcon(getClass().getResource(
	"Image/exit.gif"));

	public Level2(JTextArea text) {
		
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
		boss = new Boss(id, cpu, new Coordenada(12, 5));
		boss.setDestino(new Coordenada(7,5));
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
						if (a.inCombat()) {
							g.drawImage(
									atak.getImage(), xRec * i + xRec - imagem.getIconWidth() / 2, yRec* j, null);
							// mostra a vida do inimigo
							g.setColor(Color.red);
							g.fillRect(xRec * i, yRec
									* j, xRec, yRec / 15);
							g.setColor(Color.green);
							g.fillRect(xRec * i, yRec
									* j, (xRec * a.getLife())
									/ a.getTotallife(), yRec / 15);
						}
						g.drawImage(imagem.getImage(), xRec * i + xRec / 2
								- imagem.getIconWidth() / 2, yRec * j + yRec
								/ 2 - imagem.getIconHeight() / 2, null);
					}
				}
			}
		}
		
		if(!startPress){
			g.setColor(Color.yellow);
			g.fillRect(100, 100, 600, 400);
			g.setColor(Color.red);
			g.drawRect(100, 100, 600, 400);
			g.setFont(new Font("Arial", Font.BOLD, 70));
			g.drawString("START", 400-130, 300);
			g.setColor(Color.blue);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("LEVEL 2", 400-100, 400);
		}
		
		g.drawImage(exit.getImage(), getWidth()-exit.getIconWidth() , getHeight()-exit.getIconHeight() ,null);
		
	}


	public boolean jumpLevel(){
		return jumpLevel;
	}
	
	public void MouseMove(MouseEvent e) {}
	
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
		if (e.getX() > 0 && e.getX() < TotalX && e.getY() > 0
				&& e.getY() < TotalY) {
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

	public void run() {
		try {
			text.setText(text.getText() + "\n" + "Welcome to Level 2:\n"
					+ "Objective Level:\n" + ">Kill the monkey!!\n"
					+ "Good Luck ;)");
			threadMapa.start();
			repaint();
			while(!startPress){
				Thread.sleep(1000);
				repaint();
			}
			
			while (boss.getLife() >= 0 || banana.getLife() >= 0) {

				if (!verificaJogo()) {
					threadMapa.interrupt();
					break;
				}
				repaint();
				Thread.sleep(100);
			}

		} catch (InterruptedException e) {
			System.out.println("Thread LVL 2 falhou.");
		}

	}
	
	public boolean getComplete(){
		return isCompleted;
	}
	
	public boolean getLoss(){
		return loss;
	}

	public boolean verificaJogo() {
		//verificar se a vida dum dos boencos c <= 0
		if (banana.getLife() <= 0 || boss.getLife() <= 0) {
			//verificar se BOSS ganha
			if (boss.getLife() > 0 && banana.getLife() <= 0) {
				threadMapa.interrupt();
				text.setText(text.getText() + "\n" + "> You lose! You let the monkey kill you!");
				loss = true;
			} else {
				//verificar se Jogador ganha
				if (banana.getLife() > 0 && boss.getLife() <= 0) {
					threadMapa.interrupt();
					text.setText(text.getText() + "\n" + "> Well Done! Level 2 Completed!");
					isCompleted = true;
				}
			}
			return false;
		}
		return true;
		}
	}
