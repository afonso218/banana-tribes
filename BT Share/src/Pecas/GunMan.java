package Pecas;

import Mapa.Coordenada;
import Mapa.Player;

public class GunMan extends Peca {

	public GunMan(int id, Player a, Coordenada actual) {
		super(id, a, "Image/Gunman.gif", actual);
		setStats(2, 40, 0.1, 2, 150);
	}

}
