package XServer;


import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import Mapa.Coordenada;
import Mapa.Mapa;
import Mapa.Player;
import MultiPlayer.Informacao;
import Pecas.*;

public class Game extends Thread{

	private int id;
	private int x = 30;
	private int y = 30;
	private Mapa mapa;
	private PintarMapa pinta;
	private JFrame frame;
	
	private Thread mapaThread;
	private Thread pintor;
	
	private Player player1;
	private Player player2;

	private PlayerCliente p1;
	private PlayerCliente p2;
	
	public Game(JFrame janela, PlayerCliente p1, PlayerCliente p2) {
		this.id = 1;
		this.p1 = p1;
		this.p2 = p2;
		this.frame = janela;
		janela.setSize(800,500);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentor = janela.getContentPane();
		contentor.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseMoved(MouseEvent e) {
				pinta.MouseMove(e);
			}
			
			public void mouseDragged(MouseEvent e) {
			}
		});
		
		player1 = new Player("Player 1", 1);
		player2 = new Player("Player 2", 2);
		this.mapa = new Mapa(x,y, player1, player2);
		this.pinta = new PintarMapa(mapa);	
		
		this.pintor = new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					while(!Thread.interrupted()){
						pinta.repaint();
						Thread.sleep(25);
					}
				} catch (InterruptedException e) {
					System.out.println("> Pintor interrompido!");
				}
			}
		});
		
		// PLAYER 1		
		Peca house = new House(id,player1, new Coordenada(3,3));
		player2.add(house);
		mapa.add(house);
		id++;
		
		Peca rei = new King(id, player1,new Coordenada(2,2));
		player1.add(rei);
		mapa.add(rei);
		id++;

		Peca peca3 = new GunMan(id, player1, new Coordenada(5,5));
		player1.add(peca3);
		mapa.add(peca3);
		id++;
		
		Peca peca1 = new Fighter(id, player1, new Coordenada(5,2));
		player1.add(peca1);
		mapa.add(peca1);
		id++;
		
		Peca peca2 = new Fighter(id, player1, new Coordenada(2,5));
		player1.add(peca2);
		mapa.add(peca2);
		id++;
		
		// PLAYER 2 == CPU 
		Peca rei2 = new King(id, player2,new Coordenada(x-3,y-3));
		player2.add(rei2);
		mapa.add(rei2);
		id++;
		
		Peca house2 = new House(id,player2, new Coordenada(x-4,x-4));
		player2.add(house2);
		mapa.add(house2);
		id++;
		
		Fighter f1 = new Fighter(id, player2, new Coordenada(x-7,y-3));
		player2.add(f1);
		mapa.add(f1);
		id++;
		
		Fighter f2 = new Fighter(id, player2, new Coordenada(x-3,y-7));
		player2.add(f2);
		mapa.add(f2);
		id++;
		
		GunMan g1 = new GunMan(id, player2, new Coordenada(x-7,y-7));
		player2.add(g1);
		mapa.add(g1);
		id++;
		
		
		// COLOCAccO DE LIMITES NO MAPA COMO OBSTACULOS
		Coordenada a;
		for(int i = 0; i < x ; i++){
			a = new Coordenada(i,0);
			mapa.add(new Limite(id, a));
			id++;
			a = new Coordenada(i,y-1);
			mapa.add(new Limite(id, a));
			id++;
		}
		for(int i = 1; i < y-1 ; i++){
			a = new Coordenada(0,i);
			mapa.add(new Limite(id, a));
			id++;
			a = new Coordenada(x-1,i);
			mapa.add(new Limite(id, a));
			id++;
		}
		
		// ADICIONA OBSTACULOS
		// add MOUNTAINS
		a = new Coordenada(2,13);
		mapa.add(new Mountain(id, a));
		id++;
		a = new Coordenada(3,8);
		mapa.add(new Mountain(id,a));
		id++;
		a = new Coordenada(22,2);
		mapa.add(new Mountain(id,a));
		id++;
		// add LAKES
		a = new Coordenada(10,2);
		mapa.add(new Lake(id,a));
		id++;
		a = new Coordenada(8,7);
		mapa.add(new Lake(id,a));
		id++;
		a = new Coordenada(23,5);
		mapa.add(new Lake(id,a));
		id++;
		// add HILLS
		a = new Coordenada(7,11);
		mapa.add(new Hills(id,a));
		id++;
		a = new Coordenada(16,5);
		mapa.add(new Hills(id,a));
		id++;
		
		mapaThread = new Thread(mapa);
		pintor.start();
		janela.add(pinta);
		mapaThread.start();
	}
	
	public void Operacco(Informacao ordem){
		
		int ID = ordem.getId();
		if(mapa.getPeca(ID).isHouse()){
			((House)mapa.getPeca(ID)).setOut(true);
		}else{ // SE FIR CASA
			int idFollow = ordem.getIdFollow();
			Coordenada Destino = ordem.getC();
			if(idFollow == -1){
				mapa.getPeca(ID).setDestino(Destino);
				mapa.getPeca(ID).setFollow(null);
			}else{
				mapa.getPeca(ID).setFollow(mapa.getPeca(idFollow));
			}
		}
	}
	
	public PintarMapa getPintorMapa() {
		return pinta;
	}
	
	public JFrame getFrame(){
		return frame;
	}
	
	public PlayerCliente getP1() {
		return p1;
	}
	
	public PlayerCliente getP2() {
		return p2;
	}

	public void run() {
		try{
			p1.Envia(new Informacao(player1));
			p2.Envia(new Informacao(player2));
			while(!mapa.getFim()){
				Informacao ordem = new Informacao(mapa);
				p1.Envia(ordem);
				p2.Envia(ordem);
				sleep(100);
			}
			// envia uma ultima vez o mapa finalizado
			Informacao ordem = new Informacao(mapa);
			p1.Envia(ordem);
			p2.Envia(ordem);
		}catch(InterruptedException e) {
			pintor.interrupt();
			mapaThread.interrupt();
		}

	}
}