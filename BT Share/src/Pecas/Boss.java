package Pecas;

import Mapa.Coordenada;
import Mapa.Player;

public class Boss extends Peca {
	
	public Boss(int id, Player player, Coordenada actual) {
		super(id, player,"Image/boss.gif", actual);
		this.setStats(0 ,10,0.1,250,250);
		this.setBoss();
	}
	
}
