package Mapa;

import java.io.Serializable;

public class Coordenada implements Serializable{

	private int x;
	private int y;
	
	public Coordenada(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isIgual(Coordenada a) {
		if(this.getX() == a.getX() && this.getY() == a.getY())
			return true;
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String toString(){
		return "(" + x + "|" + y + ")";
	}

}
