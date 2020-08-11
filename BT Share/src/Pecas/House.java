package Pecas;

import java.util.ArrayList;

import Mapa.Coordenada;
import Mapa.Player;

public class House extends Peca {

	// lotacco maxima de 2 pecas
	private boolean out;
	private int lotacao = 2;
	private boolean house;
	private ArrayList<Peca> pecas = new ArrayList<Peca>();
	
	public House(int id, Player a, Coordenada actual) {
		super(id, a, "Image/house.png", actual);
		setStats(1000 ,0, 0.40, 1, 1000);
		this.house = true;
		this.out = false;
	}
	
	public boolean getOut(){
		return out;
	}
	
	public void setOut(boolean x){
		out = x;
	}
	
	public boolean isHouse(){
		return house;
	}
	
	public boolean isFull(){
		if(pecas.size() < lotacao)
			return false;
		return true;
	}
	
	public int LotacaoActual(){
		return pecas.size();
	}
	
	public synchronized boolean Entra(Peca a){
		if(!isFull()){
			pecas.add(a);
			return true;
		}else
			return false;
	}

	public void Sai(Peca a){
		pecas.remove(a);
		if(pecas.size() == 0){
			out = false;
		}
	}
	
	public ArrayList<Peca> getPecas(){
		return pecas;
	}
	
	@Override
	public void Defende(int dano) {
		super.Defende(dano);
		if(super.getLife() < 50){
			out = true;
		}
	}
}
