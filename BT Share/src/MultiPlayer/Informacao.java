package MultiPlayer;

import java.io.Serializable;

import Mapa.Coordenada;
import Mapa.Mapa;
import Mapa.Player;

public class Informacao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int tipo;
	private Mapa mapa = null;
	private String mensagem = null;
	private Coordenada c;
	private int id;
	private int idFollow;
	private Player player;
	
	public Informacao(Mapa mapa) {
		this.tipo = 1;
		this.mapa = mapa;
	}
	
	public Informacao(String mensagem) {
		this.tipo = 2;
		this.mensagem = mensagem;
	}
	
	public Informacao(int id, Coordenada c, int idFollow) {
		this.tipo = 3;
		this.c = c;
		this.id = id;
		this.idFollow = idFollow;
	}
	
	public Informacao(int idHouse) {
		this.tipo = 3;
		this.id = idHouse;
		c = new Coordenada(-1,-1);
	}

	public Informacao(Player player) {
		this.tipo = 5;
		this.player = player;
	}

	public int getTipo(){
		return tipo;
	}
	
	public Mapa getMapa(){
		return mapa;
	}
	
	public String getMensagem() {
		return mensagem;
	}
	
	public Coordenada getC() {
		return c;
	}
	
	public int getId() {
		return id;
	}
	
	public int getIdFollow() {
		return idFollow;
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
