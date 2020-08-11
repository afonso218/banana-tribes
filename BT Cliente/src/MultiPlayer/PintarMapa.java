package MultiPlayer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import Mapa.Coordenada;
import Mapa.Mapa;
import Mapa.Player;
import Pecas.*;

public class PintarMapa extends JLabel{
	
	private Player player;
	private Mapa mapa;
	private ObjectOutputStream out;
	private int x = 20;
	private int y = 15;
	private int limiteMinX;
	private int limiteMinY;
	private int limiteMaxX;
	private int limiteMaxY;
	
	private boolean baixo = false;
	private boolean cima = false;
	private boolean dir = false;
	private boolean esq = false;

	private int xRec;
	private int yRec;
	private Peca PecaActiva = null;

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
	private ImageIcon score = new ImageIcon(getClass().getResource(
	"Image/1052.gif"));

	public PintarMapa(ObjectOutputStream out, Player player) {
		this.out = out;
		this.player = player;	
		
		if(player.getID() == 1){
			limiteMinX = 0;
			limiteMinY = 0;
			limiteMaxX = 20;
			limiteMaxY = 15;
		}
		
		if(player.getID() == 2){
			limiteMinX = 10;
			limiteMinY = 15;
			limiteMaxX = 30;
			limiteMaxY = 30;
		}
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
							g.drawImage(follow.getImage(),
									xRec * (i - limiteMinX) + xRec
											- imagem.getIconWidth() / 2,
									yRec * (j - limiteMinY) + yRec
											- imagem.getIconHeight() / 2, null);
						if (a.inCombat()) {
							g.drawImage(atak.getImage(),
									xRec * (i - limiteMinX) + xRec
											- imagem.getIconWidth() / 2, yRec
											* (j - limiteMinY), null);
							g.setColor(Color.red);
							g.fillRect(xRec * (i - limiteMinX), yRec
									* (j - limiteMinY), xRec, yRec / 15);
							g.setColor(Color.green);
							g.fillRect(xRec * (i - limiteMinX), yRec
									* (j - limiteMinY), (xRec * a.getLife())
									/ a.getTotallife(), yRec / 15);
						}
					}
					if(a.isHouse()){
						House house = (House) mapa.getMapa()[i][j];
						g.setColor(Color.black);
						g.fillOval(xRec * (i - limiteMinX) + xRec / 2
							, yRec * (j - limiteMinY) + yRec/ 2 + 12
							, 10,10);
						g.setColor(Color.black);
						g.fillOval(xRec * (i - limiteMinX) + xRec / 2 + 12
							, yRec * (j - limiteMinY) + yRec/ 2 + 12
							, 10,10);
						if(house.LotacaoActual() > 0){
							g.setColor(Color.ORANGE);
							g.fillOval(xRec * (i - limiteMinX) + xRec / 2 + 12
									, yRec * (j - limiteMinY) + yRec/ 2 + 12
									, 10,10);
						}
						if(house.LotacaoActual() > 1){
							g.setColor(Color.ORANGE);
							g.fillOval(xRec * (i - limiteMinX) + xRec / 2
									, yRec * (j - limiteMinY) + yRec/ 2 + 12
									, 10,10);
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
			 "!" ,x +25,y+50);
			 g.setFont(new Font("Arial", Font.BOLD, 25));
			 g.drawString(mapa.getLoser() + " ... is the Loser." ,x
			 +25,y+150);
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
		
		//SCORE
		if(player.getID() == 1)
			g.setColor(Color.blue);
		if(player.getID() == 2)
			g.setColor(Color.orange);
		g.fillRect(getWidth()/2 -50, getHeight()-35, 100, 35);
		g.setColor(Color.white);
		g.drawRect(getWidth()/2 -50, getHeight()-35, 100, 35);
		
		g.drawImage(score.getImage(), getWidth()/2 - score.getIconWidth() -10, getHeight() - score.getIconHeight() , null);
		g.setColor(Color.yellow);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString("x" + player.getScore(), getWidth()/2 - 15, getHeight() - 5);
		
		
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

	public void MouseClicada(MouseEvent e){
		if (e.getButton() == 3) {
			PecaActiva = null;
		}

		// SELECIONA IMAGEM
		Object obj = mapa.getMapa()[(e.getX() / xRec) + limiteMinX][(e.getY() / yRec)
				+ limiteMinY];
		// QUANDO O OBJECTO CLICADO EXISTE
		if (obj != null) {
			if(((Peca)obj).getPlayer().getID() == player.getID()){
				// CASOS DE PEcAS ACTIVA EXISTENTE
				if(PecaActiva != null){
					if(((Peca) obj).isHouse()){
						if(PecaActiva == obj){
							Informacao ordem = new Informacao(PecaActiva.getID());
							try {
								out.reset();
								out.writeObject(ordem);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						if(PecaActiva != null && ((Peca) obj).isHouse()){
							Coordenada a = new Coordenada((e.getX() / xRec)
									+ limiteMinX, (e.getY() / yRec) + limiteMinY);
							Informacao ordem = new Informacao(PecaActiva.getID(), a ,-1);
							try {
								out.reset();
								out.writeObject(ordem);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}			
					// caso a peca seja movivel e obj clicado != da activa colocando em follow
					if (!((Peca) obj).isObstaculo() && !((Peca) obj).isHouse() && !(obj.equals(PecaActiva)) 
							&& PecaActiva != null && !PecaActiva.isHouse()) {
						Informacao ordem = new Informacao(PecaActiva.getID(), null ,((Peca)obj).getID());
						try {
							out.reset();
							out.writeObject(ordem);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						
					}
				}
				// CASOS DE PEcAS ACTIVA ESTA NULL  && OBJECTO CLICADO EXISTE
				if (PecaActiva == null){
					PecaActiva = (Peca) mapa.getMapa()[(e.getX() / xRec)
							+ limiteMinX][(e.getY() / yRec) + limiteMinY];
				}
	
			// QUANDO O OBJECTO CLICADO NcO EXISTE / ESTA VAZIO
			}else{
				if(PecaActiva != null){
					Coordenada a = new Coordenada((e.getX() / xRec)
							+ limiteMinX, (e.getY() / yRec) + limiteMinY);
					Informacao ordem = new Informacao(PecaActiva.getID(), a ,-1);
					try {
						out.reset();
						out.writeObject(ordem);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
				
		} else {
			if (PecaActiva != null && !PecaActiva.isHouse() && !PecaActiva.isObstaculo()) {
				
				Coordenada a = new Coordenada((e.getX() / xRec)
						+ limiteMinX, (e.getY() / yRec) + limiteMinY);
				Informacao ordem = new Informacao(PecaActiva.getID(), a ,-1);
				try {
					out.reset();
					out.writeObject(ordem);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void setMapa(Mapa NovoMapa){
		this.mapa = NovoMapa;
		if(mapa.getP1().getID() == player.getID())
			player = mapa.getP1();
		if(mapa.getP2().getID()== player.getID())
			player = mapa.getP2();
		if(PecaActiva != null){
			int id = PecaActiva.getID();
			for(int i = 0; i < mapa.getMapa().length; i++)
				for(int j = 0; j < mapa.getMapa()[i].length; j++){
					if( mapa.getMapa()[i][j] != null ){
						if(id == mapa.getMapa()[i][j].getID()){
							PecaActiva = mapa.getMapa()[i][j];
						}
					}
				}					
		}
	}	
}
