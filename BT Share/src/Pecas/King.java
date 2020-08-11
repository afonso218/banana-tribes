package Pecas;

import Mapa.Coordenada;
import Mapa.Player;

public class King extends Peca{

	public King(int id, Player a, Coordenada actual) {
		super(id, a , "Image/Mib.gif", actual);
		setStats(3, 20, 0.2, 2, 300);
	}

	@Override
	public boolean isRei(){
		return true;
	}
}