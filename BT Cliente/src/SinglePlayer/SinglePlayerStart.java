package SinglePlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;

import javax.swing.JTextArea;

public class SinglePlayerStart extends Thread {

	private JTextArea text;
	private ScrollPane consola = new ScrollPane();
	private Container contentor;

	private int level = 1;
	private Level1 L1;
	private Level2 L2;
	private Level3 L3;
	private Level3Cria level3A;
	private Level LevelActivo;
	private Thread ThreadActiva;

	public SinglePlayerStart(Container contentor) {
		this.contentor = contentor;

		text = new JTextArea();
		text.setFont(new Font("Arial", Font.BOLD, 15));
		text.setForeground(Color.white);
		text.setBackground(Color.black);
		text.setEditable(false);
		consola.add(text);
		consola.setPreferredSize(new Dimension(800, 100));
		this.contentor.add(consola, BorderLayout.SOUTH);
		text("> Welcome to Single Player of Banana Tribe (BT)!");

		L1 = new Level1(text);
		LevelActivo = L1;

		ThreadActiva = new Thread(L1);
		ThreadActiva.start();
		contentor.add(L1);
	}

	public void mesmoNivel() {
		if (LevelActivo == L1) {
			text("> Level 1 !");
			contentor.remove(L1);
			L1 = new Level1(text);
			LevelActivo = L1;
			ThreadActiva = new Thread(L1);
			contentor.add(L1);
			contentor.validate();
			contentor.repaint();
			ThreadActiva.start();
		}
		if (LevelActivo == L2) {
			text("> Level 2 !");
			contentor.remove(L2);
			L2 = new Level2(text);
			LevelActivo = L2;
			ThreadActiva = new Thread(L2);
			contentor.add(L2);
			contentor.validate();
			contentor.repaint();
			ThreadActiva.start();
		}
		if (LevelActivo == L3) {
			text("> Level 3!");
			contentor.remove(L3);
			Level3Cria a = new Level3Cria();
			L3 = new Level3(a.getMapa());
			LevelActivo = L3;
			ThreadActiva = new Thread(a.getThread());
			Thread pinta3 = new Thread(L3);
			contentor.add(L3);
			contentor.validate();
			pinta3.start();
			ThreadActiva.start();
		}
	}

	public void proximoNivel() {
		level++;
		if (LevelActivo == L1) {
			text("> Level 2 Unlocked!");
			L2 = new Level2(text);
			LevelActivo = L2;
			ThreadActiva = new Thread(L2);
			contentor.remove(L1);
			contentor.add(L2);
			contentor.validate();
			contentor.repaint();
			ThreadActiva.start();
		} else {
			if (LevelActivo == L2) {
				text("> Level 3 Unlocked!");
				Level3Cria a = new Level3Cria();
				L3 = new Level3(a.getMapa());
				LevelActivo = L3;
				ThreadActiva = new Thread(a.getThread());
				Thread pinta3 = new Thread(L3);
				contentor.remove(L2);
				contentor.add(L3);
				contentor.validate();
				pinta3.start();
				ThreadActiva.start();
			} else {
				if (LevelActivo == L3) {
					level3A.getMapa().getP2().interrupt();
					contentor.remove(L3);
					End end = new End();
					Thread a = new Thread(end);
					contentor.add(end);
					contentor.validate();
					a.start();
				}
			}

		}
	}

	public void executa() {
	}

	public Level getMapa() {
		return LevelActivo;
	}

	public Level getLevelActivo() {
		return LevelActivo;
	}

	public int getLevel() {
		return level;
	}

	public void text(String s) {
		if (text.getText().length() > 1000)
			text.setText("");
		text.setText(text.getText() + "\n" + s);
	}

	public void run() {
		while (level < 4) {
			try {
				if (LevelActivo == L1) {
					while (!L1.jumpLevel() && ThreadActiva.isAlive()) {
						sleep(500);// ThreadActiva.join();
					}
					if (L1.jumpLevel()) {
						proximoNivel();
					}
				}
				if (LevelActivo == L2) {
					while (!L2.jumpLevel() && ThreadActiva.isAlive()) {
						sleep(500);
					}
					if (L2.jumpLevel()) {
						proximoNivel();
					}
				}
				ThreadActiva.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (LevelActivo.getComplete()) {
				text("> New Level Unlocked!");
				proximoNivel();
				level++;
			}
			if (LevelActivo.getLoss()) {
				text("> Doing the same Level!");
				mesmoNivel();
			}
		}
		proximoNivel();
	}
}
