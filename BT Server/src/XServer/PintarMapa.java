package XServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Mapa.Coordenada;
import Mapa.Mapa;
import Pecas.House;
import Pecas.Peca;

public class PintarMapa extends JLabel{

	private Mapa mapa;
	private int x = 20;
	private int y = 15;
	private int limiteMinX = 0;
	private int limiteMinY = 0;
	private int limiteMaxX = 20;
	private int limiteMaxY = 15;

	private boolean baixo = false;
	private boolean cima = false;
	private boolean dir = false;
	private boolean esq = false;

	private int xRec;
	private int yRec;
	private Peca PecaActiva = null;

	private boolean drag = false;
	private int xIDragged = -1;
	private int yIDragged = -1;
	private int xFDragged = -1;
	private int yFDragged = -1;

	private ImageIcon follow = new ImageIcon(getClass().getResource(
			"Image/seta.png"));
	private ImageIcon atak = new ImageIcon(getClass().getResource(
			"Image/swords.gif"));
	private ImageIcon background = new ImageIcon(getClass().getResource(
			"Image/background.gif"));
	private ImageIcon setaBaixo = new ImageIcon(getClass().getResource(
			"Image/setaBaixo.png"));
	private ImageIcon setaCima = new ImageIcon(getClass().getResource(
			"Image/setaCima.png"));
	private ImageIcon setaDir = new ImageIcon(getClass().getResource(
			"Image/setaDir.png"));
	private ImageIcon setaEsq = new ImageIcon(getClass().getResource(
			"Image/setaEsq.png"));

	public PintarMapa(Mapa mapa) {
		this.mapa = mapa;
	}

	public void paint(Graphics g) {
		
		xRec = getWidth() / x;
		yRec = getHeight() / y;
		g.drawImage(background.getImage(), 0, 0, null);

		// PINTA PRIMEIRO OS OBSTcCULOS DO MAPA
		for (int i = limiteMinX; i < limiteMaxX; i++) {
			for (int j = limiteMinY; j < limiteMaxY; j++) {
				if (mapa.getMapa()[i][j] != null) {
					Peca a = (Peca) mapa.getMapa()[i][j];
					if (a.isObstaculo()) {
						ImageIcon imagem = a.Imagem();
						g.drawImage(
								imagem.getImage(),
								xRec * (i - limiteMinX) + xRec / 2
										- imagem.getIconWidth() / 2,
								yRec * (j - limiteMinY) + yRec / 2
										- imagem.getIconHeight() / 2, null);
					}
				}
			}
		}

		// PINTA AS PEcAS DO MAPA
		for (int i = limiteMinX; i < limiteMaxX; i++) {
			for (int j = limiteMinY; j < limiteMaxY; j++) {
				if (mapa.getMapa()[i][j] != null) {
					Peca a = (Peca) mapa.getMapa()[i][j];
					if (!a.isObstaculo()) {
						a = (Peca) mapa.getMapa()[i][j];
						ImageIcon imagem = a.Imagem();
						// PINTA O MARCO DE JOGADOR
						if (a.getPlayer().getID() == 0) {	// CPU
							g.setColor(Color.black);
							g.drawOval(xRec * (i - limiteMinX) + xRec / 2 - 25,
									yRec * (j - limiteMinY)
											+ (int) (yRec - yRec / 3.5), 50, 10);
							g.setColor(Color.white);
							g.fillOval(xRec * (i - limiteMinX) + xRec / 2 - 25,
									yRec * (j - limiteMinY)
											+ (int) (yRec - yRec / 3.5), 50, 10);
						}
						if (a.getPlayer().getID() == 1) { // PLAYER 1
							g.setColor(Color.green);
							g.drawOval(xRec * (i - limiteMinX) + xRec / 2 - 25,
									yRec * (j - limiteMinY)
											+ (int) (yRec - yRec / 3.5), 50, 10);
							g.setColor(Color.blue);
							g.fillOval(xRec * (i - limiteMinX) + xRec / 2 - 25,
									yRec * (j - limiteMinY)
											+ (int) (yRec - yRec / 3.5), 50, 10);
						}
						if (a.getPlayer().getID() == 2) { // PLAYER 2
							g.setColor(Color.red);
							g.drawOval(xRec * (i - limiteMinX) + xRec / 2 - 25,
									yRec * (j - limiteMinY)
											+ (int) (yRec - yRec / 3.5), 50, 10);
							g.setColor(Color.orange);
							g.fillOval(xRec * (i - limiteMinX) + xRec / 2 - 25,
									yRec * (j - limiteMinY)
											+ (int) (yRec - yRec / 3.5), 50, 10);

						}
						g.drawImage(
								imagem.getImage(),
								xRec * (i - limiteMinX) + xRec / 2
										- imagem.getIconWidth() / 2,
								yRec * (j - limiteMinY) + yRec / 2
										- imagem.getIconHeight() / 2, null);
						if (a == PecaActiva) {
							// RANGE
							g.setColor(Color.yellow);
							int comp = xRec * (a.getRange() * 2 + 1);
							int larg = yRec * (a.getRange() * 2 + 1);
							g.drawOval(xRec * (i - limiteMinX) + xRec / 2
									- comp / 2, yRec * (j - limiteMinY) + yRec
									/ 2 - larg / 2, comp, larg);
							// VIDA
							g.setColor(Color.red);
							g.fillRect(xRec * (i - limiteMinX), yRec
									* (j - limiteMinY), xRec, yRec / 15);
							g.setColor(Color.green);
							g.fillRect(xRec * (i - limiteMinX), yRec
									* (j - limiteMinY), (xRec * a.getLife())
									/ a.getTotallife(), yRec / 15);
						}
						if (a.hasFollow())
							g.drawImage(
									follow.getImage(),
									xRec * (i - limiteMinX) + xRec
											- imagem.getIconWidth() / 2,
									yRec * (j - limiteMinY) + yRec
											- imagem.getIconHeight() / 2, null);
						if (a.inCombat()) {
							g.drawImage(
									atak.getImage(),
									xRec * (i - limiteMinX) + xRec
											- imagem.getIconWidth() / 2, yRec
											* (j - limiteMinY), null);
							// mostra a vida do inimigo
							g.setColor(Color.red);
							g.fillRect(xRec * (i - limiteMinX), yRec
									* (j - limiteMinY), xRec, yRec / 15);
							g.setColor(Color.green);
							g.fillRect(xRec * (i - limiteMinX), yRec
									* (j - limiteMinY), (xRec * a.getLife())
									/ a.getTotallife(), yRec / 15);
						}
					}
				}
			}
		}

		if (mapa.getFim()){
			 int x = 100;
			 int y = 150;
			 g.setColor(Color.orange);
			 g.fillRect(x, y, 600, 200);
			 g.setColor(Color.black);
			 g.drawRect(x, y, 600, 200);
			 g.setFont(new Font("Arial", Font.BOLD, 25));
			 g.drawString("The WINNER IS...  "+ mapa.getWinner() +
			 "!!!" ,x +25,y+50);
			 g.setFont(new Font("Arial", Font.BOLD, 25));
			 g.drawString(mapa.getLoser() + " ... is the Losser." ,x
			 +25,y+150);
			 
		}else{
//			// TAPA ZONA NcO DESCOBERTA PARA O PLAYER 1
//			int limiteX = limiteMaxX;
//			int limiteY = limiteMaxY;
//			if (limiteMaxX < mapa.getMapa().length)
//				limiteX = 1 + limiteMaxX;
//			if (limiteMaxY < mapa.getMapa()[0].length)
//				limiteY = 1 + limiteMaxY;
//			for (int i = limiteMinX; i < limiteX; i++) {
//				for (int j = limiteMinY; j < limiteY; j++) {
//					if (!mapa.getDescobertoP1()[i][j]) {
//						g.setColor(Color.black);
//						g.fillRect(xRec * (i - limiteMinX),
//								yRec * (j - limiteMinY), xRec, yRec);
//					}
//				}
//			}
		}

		// rectandulo de arrasto/ seleccco
		if (drag) {
			int SizeX = xFDragged - xIDragged;
			int SizeY = yFDragged - yIDragged;
			if (SizeX < 0 && SizeY < 0) {
				g.setColor(Color.orange);
				g.drawRect(xIDragged + SizeX, yIDragged + SizeY, -SizeX, -SizeY);
			}
			if (SizeX < 0 && SizeY > 0) {
				g.setColor(Color.red);
				g.drawRect(xIDragged + SizeX, yFDragged - SizeY, -SizeX, SizeY);
			}
			if (SizeX > 0 && SizeY < 0) {
				g.setColor(Color.blue);
				g.drawRect(xIDragged, yFDragged, SizeX, -SizeY);
			}
			if (SizeX > 0 && SizeY > 0) {
				g.setColor(Color.green);
				g.drawRect(xIDragged, yIDragged, SizeX, SizeY);
			}
		}

		if (baixo)
			g.drawImage(setaBaixo.getImage(),
					getWidth() / 2 - setaBaixo.getIconWidth() / 2, getHeight()
							- setaBaixo.getIconHeight(), null);
		if (cima)
			g.drawImage(setaCima.getImage(),
					getWidth() / 2 - setaCima.getIconWidth() / 2, 0, null);
		if (esq)
			g.drawImage(setaEsq.getImage(), 0,
					getHeight() / 2 - setaEsq.getIconHeight() / 2, null);
		if (dir)
			g.drawImage(setaDir.getImage(),
					getWidth() - setaDir.getIconWidth(), getHeight() / 2
							- setaDir.getIconHeight() / 2, null);

		// // PINTA QUADRO e Numeros
		// for(int i = limiteMinX; i < limiteMaxX ; i++){
		// for(int j = limiteMinY; j < limiteMaxY ; j++){
		// g.setColor(Color.black);
		// g.drawRect( xRec*(i - limiteMinX) , yRec*(j - limiteMinY) , xRec ,
		// yRec );
		// g.setColor(Color.yellow);
		// g.drawString(i+","+j, xRec*(i - limiteMinX) + xRec/2 -5 , yRec*(j -
		// limiteMinY) + yRec/2 +5);
		// }
		// }
	}

	public void MouseMove(MouseEvent e) {
		// mexe mapa para <-- a esquerda
		if (e.getX() >= 0 && e.getX() <= 10)
			if (limiteMinX > 0) {
				esq = true;
				limiteMinX--;
				limiteMaxX--;
				repaint();
			}
		// mexe mapa para --> a direita
		if (e.getX() <= getWidth() && e.getX() >= getWidth() - 10)
			if (limiteMaxX < mapa.getMapa().length) {
				dir = true;
				limiteMinX++;
				limiteMaxX++;
				repaint();
			}
		// mexe mapa para cima /\
		if (e.getY() >= 0 && e.getY() <= 10)
			if (limiteMinY > 0) {
				cima = true;
				limiteMinY--;
				limiteMaxY--;
				repaint();
			}
		// mexe mapa para baixo \/
		if (e.getY() <= getHeight() && e.getY() >= getHeight() - 10)
			if (limiteMaxY < mapa.getMapa()[0].length) {
				baixo = true;
				limiteMinY++;
				limiteMaxY++;
				repaint();
			}
		if (e.getX() < getWidth() - 10 && e.getX() > 10
				&& e.getY() < getHeight() - 10 && e.getY() > 10) {
			baixo = false;
			cima = false;
			dir = false;
			esq = false;
		}
	}

	public void MouseDragged(MouseEvent e) {
		drag = true;
		xFDragged = e.getX();
		yFDragged = e.getY();
	}

	public void MouseDraggedInicio(MouseEvent e) {
		xIDragged = e.getX();
		yIDragged = e.getY();
		repaint();
	}

	public void MouseDraggedFim(MouseEvent e) {
		xIDragged = 0;
		yIDragged = 0;
		xFDragged = 0;
		yFDragged = 0;
		drag = false;
	}

	public void MouseClicada(MouseEvent e) {
		if (e.getButton() == 3) {
			PecaActiva = null;
		}

		// SELECIONA IMAGEM
		Object obj = mapa.getMapa()[(e.getX() / xRec) + limiteMinX][(e.getY() / yRec)
				+ limiteMinY];
		// QUANDO O OBJECTO CLICADO EXISTE
		if (obj != null) {
			if(((Peca)obj).getPlayer().getID() == 1){
				// CASOS DE PEcAS ACTIVA EXISTENTE
				if(PecaActiva != null){	
					if(((Peca) obj).isHouse()){
						if(PecaActiva == obj){
							((House)obj).setOut(true);
						}
						PecaActiva.setDestino(new Coordenada((e.getX() / xRec)
								+ limiteMinX, (e.getY() / yRec) + limiteMinY));
					}
					if (!((Peca) obj).isObstaculo() && !((Peca) obj).isHouse() && !(obj.equals(PecaActiva)) 
							&& PecaActiva != null && !PecaActiva.isHouse()) {
						PecaActiva.setFollow((Peca) obj);
					}
				}
				// CASOS DE PEcAS ACTIVA ESTA NULL  && OBJECTO CLICADO EXISTE
				if (PecaActiva == null){
					PecaActiva = (Peca) mapa.getMapa()[(e.getX() / xRec)
							+ limiteMinX][(e.getY() / yRec) + limiteMinY];
				}
	
			// QUANDO O OBJECTO CLICADO NcO EXISTE / ESTA VAZIO
			}
		} else {
			if (PecaActiva != null && !PecaActiva.isHouse() && !PecaActiva.isObstaculo()) {
				PecaActiva.setFollow(null);
				PecaActiva.setDestino(new Coordenada((e.getX() / xRec)
						+ limiteMinX, (e.getY() / yRec) + limiteMinY));
			}
		}
	}

	public void run() {
		try {
			while (true) {
				Thread.sleep(25);
				repaint();
			}
		} catch (InterruptedException e) {
		}
	}

}
