package Mapa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import Pecas.*;

public class Player extends Thread implements Serializable{

	private int ID;
	private String nome;
	private King rei;
	private int score = 0;
	private ArrayList<Peca> pecas = new ArrayList<Peca>();
	
	public Player(String nome, int ID) {
		this.nome = nome;
		this.ID = ID;
	}
	
	public Player(){
		this.ID = 0;
		this.nome = "CPU";
	}
	
	public void add(Peca x){
		pecas.add(x);
		if(x.isRei())
			rei = (King) x;
	}
	
	public void remove(Peca x){
		pecas.remove(pecas.indexOf(x));
	}
	
	public String getNome() {
		return nome;
	}
	
	public int getID() {
		return ID;
	}
	
	public King getRei(){
		return rei;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(){
		score += new Random().nextInt(3);
	}
	
	public String toString() {
		return "Player " + ID + " [" + nome + "]"; 
	}
	
	public void run(){
		try{
			pecas.get(4).setDestino(new Coordenada(26,26));
			while(!interrupted()){
				pecas.get(3).setDestino(new Coordenada(pecas.get(3).getActual().getX() - 1, pecas.get(3).getActual().getY()));
				pecas.get(2).setDestino(new Coordenada(pecas.get(2).getActual().getX() - 1, pecas.get(2).getActual().getY()));
				sleep(1000);
				pecas.get(3).setDestino(new Coordenada(pecas.get(3).getActual().getX(), pecas.get(3).getActual().getY()-1));
				pecas.get(2).setDestino(new Coordenada(pecas.get(2).getActual().getX(), pecas.get(2).getActual().getY()-1));
				sleep(1000);
				pecas.get(3).setDestino(new Coordenada(pecas.get(3).getActual().getX() + 1, pecas.get(3).getActual().getY()));
				pecas.get(2).setDestino(new Coordenada(pecas.get(2).getActual().getX() + 1, pecas.get(2).getActual().getY()));
				sleep(1000);
				pecas.get(3).setDestino(new Coordenada(pecas.get(3).getActual().getX(), pecas.get(3).getActual().getY()+1));
				pecas.get(2).setDestino(new Coordenada(pecas.get(2).getActual().getX(), pecas.get(2).getActual().getY()+1));
				sleep(1000);
			}
		}catch(InterruptedException e){
			System.out.println("CPU PAROU!");
		}
	}
}

