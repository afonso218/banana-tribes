package SinglePlayer;

import Mapa.Coordenada;
import Mapa.Mapa;
import Mapa.Player;
import Pecas.*;

public class Level3Cria{

	private int id = 0;
	private int x = 30;
	private int y = 30;
	private Mapa tabuleiro;
	
	private Player player1;
	private Player player2;
	private Thread threadTab;
	
	public Level3Cria() {

		player1 = new Player("Player 1", 1);
		player2 = new Player();
		this.tabuleiro = new Mapa(x,y, player1, player2);
		
		// PLAYER 1		
		Peca house = new House(id, player1, new Coordenada(3,3));
		player2.add(house);
		tabuleiro.add(house);
		
		Peca rei = new King(id, player1,new Coordenada(2,2));
		player1.add(rei);
		tabuleiro.add(rei);

		Peca peca3 = new GunMan(id, player1, new Coordenada(5,5));
		player1.add(peca3);
		tabuleiro.add(peca3);
		
		Peca peca1 = new Fighter(id, player1, new Coordenada(5,2));
		player1.add(peca1);
		tabuleiro.add(peca1);
		
		Peca peca2 = new Fighter(id, player1, new Coordenada(2,5));
		player1.add(peca2);
		tabuleiro.add(peca2);
		
		// PLAYER 2 == CPU 
		Peca rei2 = new King(id, player2,new Coordenada(x-3,y-3));
		player2.add(rei2);
		tabuleiro.add(rei2);
		
		Peca house2 = new House(id, player2, new Coordenada(x-4,x-4));
		player2.add(house2);
		tabuleiro.add(house2);
		
		Fighter f1 = new Fighter(id, player2, new Coordenada(x-7,y-3));
		player2.add(f1);
		tabuleiro.add(f1);
		
		Fighter f2 = new Fighter(id, player2, new Coordenada(x-3,y-7));
		player2.add(f2);
		tabuleiro.add(f2);
		
		GunMan g1 = new GunMan(id, player2, new Coordenada(x-7,y-7));
		player2.add(g1);
		tabuleiro.add(g1);
		
		
		// COLOCAccO DE LIMITES NO MAPA COMO OBSTACULOS
		Coordenada a;
		for(int i = 0; i < x ; i++){
			a = new Coordenada(i,0);
			tabuleiro.add(new Limite(id, a));
			a = new Coordenada(i,y-1);
			tabuleiro.add(new Limite(id, a));
		}
		for(int i = 1; i < y-1 ; i++){
			a = new Coordenada(0,i);
			tabuleiro.add(new Limite(id, a));
			a = new Coordenada(x-1,i);
			tabuleiro.add(new Limite(id, a));
		}
		
		// ADICIONA OBSTACULOS
		// add MOUNTAINS
		a = new Coordenada(2,13);
		tabuleiro.add(new Mountain(id, a));
		a = new Coordenada(3,8);
		tabuleiro.add(new Mountain(id, a));
		a = new Coordenada(22,2);
		tabuleiro.add(new Mountain(id, a));
		// add LAKES
		a = new Coordenada(10,2);
		tabuleiro.add(new Lake(id, a));
		a = new Coordenada(8,7);
		tabuleiro.add(new Lake(id, a));
		a = new Coordenada(23,5);
		tabuleiro.add(new Lake(id, a));
		// add HILLS
		a = new Coordenada(7,11);
		tabuleiro.add(new Hills(id, a));
		a = new Coordenada(16,5);
		tabuleiro.add(new Hills(id, a));
		
		threadTab = new Thread(tabuleiro);
		threadTab.start();
		player2.start();
	}
	
	public void executa(){
	}
	
	public Thread getThread(){
		return threadTab;
	}
	
	public Mapa getMapa() {
		return tabuleiro;
	}
	
}
