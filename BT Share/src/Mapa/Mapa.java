package Mapa;
/**
 * @author Ricardo Afonso
 * @author Diogo Pedreira 
 * 
 * Mapa do Jogo.
 */
import java.io.Serializable;
import java.util.ArrayList;

import Mapa.Coordenada;
import Mapa.ThreadMove;
import Pecas.*;


public class Mapa implements Serializable, Runnable{

	private int xMapa;
	private int yMapa;
	private Peca[][] mapa;;
	private Player player1;
	private Player player2;
	private transient boolean[][] descobertoP1;

	private boolean fim;
	private Player winner = null;
	private Player loser = null;
	private int xMaximo;
	private int yMaximo;
	/**
	 * Construtor de um novo Mapa(Mundo).
	 * @param x coordenada X no Mapa
	 * @param y coordenada Y no Mapa
	 * @param player1 Jogador 1 que pretende iniciar um jogo novo
	 * @param player2 Jogador 2 que pretende iniciar um jogo novo
	 */
	public Mapa(int x, int y, Player player1, Player player2) {
		this.xMapa = x;
		this.yMapa = y;
		this.fim = false;
		this.mapa = new Peca[x][y];
		this.player1 = player1;
		this.player2 = player2;
		this.descobertoP1 = new boolean[x][y];
		this.xMaximo = descobertoP1.length -1;
		this.yMaximo = descobertoP1[0].length -1;
		// coloca o descoberto todo a false
		for(int i = 0; i < descobertoP1.length ; i++){
			for(int j = 0; j < descobertoP1[i].length ; j++){
				descobertoP1[i][j] = false;
			}
		}
	}
	/**
	 * Mctodo que devolve o Mapa actual.
	 * @return devolve o Mapa actual
	 */
	public Peca[][] getMapa(){
		return mapa;
	}
	/**
	 * Este mctodo recebe um mapa actualizado e faz replace do mapa Actual pelo Novo.
	 * @param novoMapa Mapa Actualizado
	 */
	public void setMapa(Peca[][] novoMapa){
		this.mapa = novoMapa;
	}
	/**
	 * Este mctodo vai buscar o Jogador que Ganhou o jogo na forma toString().
	 * @return retorna o Vencedor
	 */
	public String getWinner(){
		return winner.toString();
	}
	/**
	 * Este mctodo vai buscar o Jogador que Perdeu o jogo na forma toString().
	 * @return retorna o Perdedor
	 */
	public String getLoser(){
		return loser.toString();
	}
	/**
	 * Este mctodo devolve se o jogo jc acabou.
	 * @return boolean fim que representa o estado do jogo(Terminado ou nco)
	 */
	public boolean getFim(){
		return fim;
	}
	/**
	 * Este mctododo devolve o Jogador1.
	 * @return retorna o Jogador1
	 */
	public Player getP1(){
		return player1;
	}
	/**
	 * Este mctododo devolve o Jogador2.
	 * @return retorna o Jogador2
	 */
	public Player getP2(){
		return player2;
	}
	/**
	 * Este mctodo devolve uma matriz boolean que identifica as coordenadas que estco descobertas.
	 * @return devolve a vcrivel booleana se aquela coordenada estc descoberta
	 */
	public boolean[][] getDescobertoP1(){
		return descobertoP1;
	}
	/**
	 * Este mctododo define se o Rei de um dos jogadores morre, esse jogador torna-se o Perdedor ou Vencedor
	 * e remove o Rei apcs este morrer e todos os seu aliados.
	 * 
	 * @param player ID do Jogador
	 */
	public void ReiMorre(int player){
		if(player == player1.getID()){
			loser = player1;
			winner = player2;
		}else{
			loser = player2;
			winner = player1;
		}

		for(int i = 0; i < mapa.length ; i++){
			for(int j = 0; j < mapa[i].length ; j++){
				if(((Peca)mapa[i][j]) != null)
					if(!((Peca)mapa[i][j]).isObstaculo())
						if(((Peca)mapa[i][j]).getPlayer().getID() == player)
							remove(((Peca)mapa[i][j]).getActual());
			}
		}
		fim = true;
	}
	/**
	 * Mctodo sincronizado para adicionar uma peca a uma coordenada alvo caso esta nco seja ocupada.
	 * 
	 * @param peca Peca que c pretendido adicionar ao Mapa
	 */
	public synchronized void add(Peca peca){
		// Sc adiciona se o lugar estiver livre
		if(isLivre(peca.getActual())){
			mapa[peca.getActual().getX()][peca.getActual().getY()] = peca;
			if(!peca.isObstaculo()){
				if(peca.getPlayer().getID() == 1)
					descobreP1Mapa(peca.getActual().getX(),peca.getActual().getY());
			}
		}
	}
	/**
	 * Mctodo Sincronizado para mover Pecas para as coordenadas novas descobrindo o mapa pelo caminho da Peca.
	 * @param peca Peca que se pretende mover
	 * @param actual coordenada Actual da Peca
	 * @param nova coordenada Nova da Peca
	 * @return verdadeiro ou falso caso a Peca se tenha movido para a coordenada alvo
	 */
	public synchronized boolean movePara(Peca peca, Coordenada actual, Coordenada nova) {
		if(mapa[nova.getX()][nova.getY()] == null){
			mapa[actual.getX()][actual.getY()] = null;
			mapa[nova.getX()][nova.getY()] = peca;
			if(peca.getPlayer().getID() == 1)
				descobreP1Mapa(nova.getX(),nova.getY());
			return true;
		}else{
			if(((Peca)mapa[nova.getX()][nova.getY()]).isHouse()){
				House a = (House) mapa[nova.getX()][nova.getY()];
				if(a.Entra(peca))
					mapa[actual.getX()][actual.getY()] = null;
				return true;
			}
			return false;
		}
	}
	/**
	 * Mctodo que remove a Peca na coordenada a.
	 * @param a coordenada onde se vai retirar o seu contecdo
	 */
	public void remove(Coordenada a){
		mapa[a.getX()][a.getY()] = null;
	}
	/**
	 * Mctodo Sincronizado que devolve verdadeiro caso a Coordenada a esteja livre. 
	 * @param a coordenada recebida para a verificacco
	 * @return verdadeiro caso a coordenada esteja livre
	 */
	public synchronized boolean isLivre(Coordenada a){
		if(mapa[a.getX()][a.getY()] == null)
			return true;
		else
			return false;
	}
	/**
	 * Mctodo Sincronizado que devolve verdadeiro se a coordenada alvo estc livre e caso seja Casa,
	 * se esta se encontra ocupada ou nco.
	 * @param a Coordenada Alvo
	 * @param peca Peca a inserir na Casa
	 * @return verdadeiro caso a coordenada esteja livre e a casa esteja livre
	 */
	public synchronized boolean isLivre(Coordenada a, Peca peca){
		if(mapa[a.getX()][a.getY()] == null)
			return true;
		else{
			Peca x = ((Peca)mapa[a.getX()][a.getY()]);
			if(x.isHouse() && x.getActual().isIgual(peca.getDestino())){
				if(!((House)x).isFull())
					return true;
				return false;
			}
			return false;
		}
	}

	/**
	 * Mctodo que descobre o Mapa do Player1 nas coordenadas x e y.
	 * @param x coordenada X da posicco do jogador1
	 * @param y coordenada y da posicco do jogador1
	 */
	public void descobreP1Mapa(int x, int y){

		descobertoP1[x][y] = true;
		//(1,0) e (1,1)
		if (x < xMaximo) {
			descobertoP1[x + 1][y] = true;
			if (y < yMaximo)
				descobertoP1[x + 1][y + 1] = true;
		}
		// (-1,0) e (-1,-1)
		if (x > 0) {
			descobertoP1[x - 1][y] = true;
			if (y > 0)
				descobertoP1[x - 1][y - 1] = true;
		}
		// (0,1) e (-1,1)
		if (y < yMaximo) {
			descobertoP1[x][y + 1] = true;
			if (x > 0)
				descobertoP1[x - 1][y + 1] = true;
		}
		// (0,-1) e (1,-1)
		if (y > 0) {
			descobertoP1[x][y - 1] = true;
			if (x < xMaximo)
				descobertoP1[x + 1][y - 1] = true;
		}
	}
	/**
	 * Mctodo que devolve a Peca na coordenada a.
	 * @param a coordenada da Peca
	 * @return devolve a peca na coordenada alvo
	 */
	public Peca getPeca(Coordenada a){
		// verifica se a coordenada pertence ao mapa se sim devolve se nao, devolve null
		if(a.getX() >= xMapa || a.getX() < 0 || a.getY() < 0 || a.getY() >= yMapa)
			return null;
		return (Peca)mapa[a.getX()][a.getY()];
	}
	
	public Peca getPeca(int id){
		// verifica se a coordenada pertence ao mapa se sim devolve se nao, devolve null
		Peca peca = null;
		for(int i = 0; i < mapa.length; i++){
			for(int j = 0; j < mapa[i].length; j++){
				if(mapa[i][j] != null){
					if(mapa[i][j].getID() == id)
						peca = mapa[i][j];
				}
			}
		}
		return peca;
	}

	/**
	 * Mctodo que move a Peca alvo para a coordenada destino. Guarda num vector todas as coordenadas
	 * posscveis para o movimento e calcula o caminho mais rcpido para chegar ao seu destino e caso 
	 * esteja em Follow, define a coordenada destino como igual c da peca a seguir. Se alguma coordenada 
	 * estiver ocupada no decorrer do movimento, essa coordenada c retirada da lista de coordenadas livres.
	 * @param peca Peca que se pretende mover
	 */
	public void movePeca(Peca peca) {
		// CALCULAR PROXIMO SALTO
		Peca follow = peca.getFollow();
		Coordenada destino = peca.getDestino();
		Coordenada actual = peca.getActual(); 

		if(follow != null){
			if(follow.getLife() > 0){
				if(Math.sqrt(Math.pow(actual.getX() - follow.getActual().getX(), 2)
						+ Math.pow(actual.getY() - follow.getActual().getY(), 2)) > Math.sqrt(2)){
					peca.setDestino(follow.getActual());
				}else
					peca.setDestino(actual);
			}else
				peca.setFollow(null);
		}
		if (!peca.getActual().isIgual(peca.getDestino())) {

			// adiciona ao vector livres todas as coordenadas possiveis de movimento
			ArrayList<Coordenada> livres = new ArrayList<Coordenada>();
			if (actual.getX() >= 1)
				livres.add(new Coordenada(actual.getX() - 1, actual.getY()));
			if (actual.getX() <= this.getMapa().length - 2)
				livres.add(new Coordenada(actual.getX() + 1, actual.getY()));
			if (actual.getY() >= 1)
				livres.add(new Coordenada(actual.getX(), actual.getY() - 1));
			if (actual.getY() <= this.getMapa()[0].length - 2)
				livres.add(new Coordenada(actual.getX(), actual.getY() + 1));

			// COORDENADAS OCUPADAS RETIRA
			for (int i = 0; i < livres.size(); i++) {
				if (!this.isLivre(livres.get(i), peca)) {
					livres.remove(i);
				}
			}
			// Jc ESTAO COLOCADAS NO ARRAYLIST AS POSSIVEIS COORDENADAS LIVRES
			if (livres.size() > 0) {
				// CALCULAR QUAL c A MAIS PROXIMA DO ACTUAL
				Coordenada nova = livres.get(0);
				// (raiz quadrada)((X - Xorigem)^2 + (Y - Yorigem)^2) TEOREMA DE PITAGURA COM ORIGEM NO ACTUAL
				double distancia = Math.sqrt(Math.pow(
						destino.getX() - nova.getX(), 2)
						+ Math.pow(destino.getY() - nova.getY(), 2));
				for (int i = 1; i < livres.size(); i++) {
					double distanciaNew = Math.sqrt(Math.pow(destino.getX()
							- livres.get(i).getX(), 2) + Math.pow(destino.getY() - livres.get(i).getY(), 2));
					if (distancia > distanciaNew) {
						nova = livres.get(i);
						distancia = distanciaNew;
					}
				}
				if (this.movePara(peca, actual, nova))
					peca.setActual(nova);
				else
					peca.setDestino(actual);
			} else {
				peca.setDestino(actual);
			}
		}
	}


	/**
	 * Mctodo que verifica se a peca vizinha da actual c um Obstaculo ou Enimiga. caso seja enimiga estas
	 * iniciam o ataque e defesa. O Ataque a multiplos enimigos adjacentes c dividido pelos mesmos.
	 * 
	 * @param vizinha Peca vizinha da Peca Actual
	 * @param main Peca Actual
	 * @param nrEnimigos ncmero de enimigos adjacentes
	 * 
	 * @see Peca.Ataca
	 * @see Peca.Defende
	 */
	private void VerificaPeca(Peca vizinha, Peca main, int nrEnimigos) {
		if(!vizinha.isObstaculo()){
			if(vizinha.getPlayer() != main.getPlayer()){
				vizinha.Defende(main.Ataca(nrEnimigos));
				main.getPlayer().setScore();
				if(vizinha.getLife() <= 0){
					mapa[vizinha.getActual().getX()][vizinha.getActual().getY()] = null;
					if(vizinha.isRei())
						ReiMorre(vizinha.getPlayer().getID());
				}
			}
		}
	}

	/**
	 * Mctodo que vai verificar se existem pecas adjacentes c peca enimigas actual e caso existam, 
	 * sco adicionadas a uma lista de Pecas. c entco iniciado um ataque a todas as pecas em torno
	 * da peca actual.
	 * 
	 * @param peca Peca actual
	 * 
	 * @see Mapa.VerificaPeca
	 */
	public void VerificarAtaque(Peca peca){

		peca.setCombat(false);

		int x = peca.getActual().getX();
		int y = peca.getActual().getY();

		ArrayList<Peca> ataques = new ArrayList<Peca>();
		Coordenada c;
		// (1,1)
		c = new Coordenada(x+1,y+1);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (-1,-1)
		c = new Coordenada(x-1,y-1);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (0,-1)
		c = new Coordenada(x,y-1);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (0,1)
		c = new Coordenada(x,y+1);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (1,-1)
		c = new Coordenada(x+1,y-1);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (-1,0)
		c = new Coordenada(x-1,y);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (1,0)
		c = new Coordenada(x+1,y);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));
		// (-1,1)
		c = new Coordenada(x-1,y+1);
		if(getPeca(c) != null)
			ataques.add(getPeca(c));

		for(int i = 0; i < ataques.size() ; i++ ){
			VerificaPeca(ataques.get(i), peca, ataques.size());
		}
	}

	/**
	 * Mctodo que retira o contecdo da Casa alvo, calculando coordenadas novas que estejam
	 * disponcveis em seu redor, e adiciona as pecas novamente ao Mapa.
	 * @param house Casa que pretendemos retirar as unidades
	 */
	public void RetiraDaCasa(House house){
		int x = house.getActual().getX();
		int y = house.getActual().getY();
		// RETIRAR PEcAS DA CASA
		for(int i = 0; i < house.LotacaoActual(); i++){
			Peca a = house.getPecas().get(i);
			//verificar se esta livre e escolher posicco
			Coordenada nova = new Coordenada(-1, -1);
			// (1,0)
			if(isLivre(new Coordenada(x+1,y)))
				nova = new Coordenada(x+1,y);
			// (-1,0)
			if(isLivre(new Coordenada(x-1,y)))
				nova = new Coordenada(x-1,y);
			// (0,-1)
			if(isLivre(new Coordenada(x,y-1)))
				nova = new Coordenada(x,y-1);
			// (0,1)
			if(isLivre(new Coordenada(x,y+1)))
				nova = new Coordenada(x,y+1);

			if(nova.getX() != -1 && nova.getY() != -1){
				house.Sai(a);	
				a.setActual(nova);
				a.setDestino(nova);
				this.add(a);	
			}
		}
		house.setOut(false);
	}

	/**
	 * Mctodo que verifica se existem pecas que pretendem mover-se e guarda-as numa lista de pecas
	 * que se estco a mover. verifica tambcm se em cada posicco podem atacar unidades enimigas, 
	 * incluindo Casas.
	 * 
	 * @see Peca.readyToSpeed
	 * @see Mapa.VerificarAtaque
	 * @see Mapa.RetiraDaCasa
	 */
	public void cicloUP(){

		ArrayList<Peca> aMover = new ArrayList<Peca>();

		for(int i = 0; i < mapa.length; i++){
			for(int j = 0; j < mapa[i].length; j++){
				if(mapa[i][j] != null){
					if(!((Peca)mapa[i][j]).isObstaculo()){
						// VERIFICA SE A PEcA QUER MOVER OU NAO
						Peca peca = (Peca)mapa[i][j];
						Coordenada a = peca.getActual();
						Coordenada b = peca.getDestino();
						if( !(a.isIgual(b)) || peca.hasFollow() ){
							if(peca.readyToSpeed()){
								aMover.add(peca);
							}
						}
						VerificarAtaque(peca);
						if(peca.isHouse()){
							House house = (House)mapa[i][j];
							if(house.getOut() && house.LotacaoActual() > 0)
								RetiraDaCasa(house);
						}
					}
				}
			}
		}
		for(Peca peca : aMover){
			ThreadMove b = new ThreadMove(peca, this);
			b.start();
		}
	}

	/**
	 * Este mctodo faz a Thread cicloUp() estar a correr atc ao jogo ser terminado.
	 * 
	 * @see Mapa.cicloUp
	 */
	public void run(){
		try{
			while(!fim){
				Thread.sleep(300);
				cicloUP();
			}
		}catch(InterruptedException e){
			System.out.println("Interrompido CicloUp no Mapa");
		}
	}

}


