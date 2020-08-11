package Pecas;

import Mapa.Coordenada;
import Mapa.Player;

public class Fighter extends Peca {

	public Fighter(int id, Player a, Coordenada actual) {
		super(id, a, "Image/Fighter.gif", actual);
		setStats(1, 30, 0.25, 1, 100);
	}

}
