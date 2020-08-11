package Mapa;

import Pecas.Peca;

public class ThreadMove extends Thread {

	private Peca peca;
	private Mapa mapa;
	
	public ThreadMove(Peca peca, Mapa mapa) {
		this.peca = peca;
		this.mapa = mapa;
	}
	
	public void run() {
		mapa.movePeca(peca);
	}
}
