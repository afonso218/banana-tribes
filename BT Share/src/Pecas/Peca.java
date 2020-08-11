package Pecas;

import java.io.Serializable;
import java.util.Random;

import javax.swing.ImageIcon;

import Mapa.Coordenada;
import Mapa.Player;


public class Peca implements Serializable{
	
	private int id;
	private Player player;
	private String ImagemNome;
	
	private Coordenada actual;
	private Coordenada destino;
	private boolean obstaculo = true;
	private boolean boss = false;
	
	private boolean house;
	private Peca follow;
	private boolean combat;
	
	// STATS DA PEcA
	private int SpeedLeft;	// decrementado a medida que passa um ciclo
	private int speed;		// tempo que demora a a saltar para a proxima casa
	private int ataque;
	private double defesa;
	private int range;
	private int life;
	private int Totallife;
	
	public Peca(int id, Player a,String nome, Coordenada actual) {

		setStats(1, 25, 0.1, 1, 100);

		this.id = id;		
		this.combat = false;
		this.follow = null;
		this.player = a;
		this.actual = actual;
		this.destino = actual;
		this.obstaculo = false;
		this.house = false;
		this.ImagemNome = nome;
	}
	
	public Peca(int ID, String nome, boolean obstaculo, Coordenada coordenada) {
		this.obstaculo = obstaculo;
		this.ImagemNome = nome;
		this.actual = coordenada;
		this.player = new Player();
		this.follow = null;
		this.house = false;
	}
	
	public boolean hasFollow(){
		if(follow != null){
			if(follow.getLife() <= 0){
				follow = null;
				return false;
			}
			if(follow.getPlayer() == this.getPlayer())
				return true;
		}
		return false;
	}
	
	public void setStats(int speed, int ataque, double defesa, int range, int Totallife){
		this.speed = speed;
		this.SpeedLeft = speed;
		this.ataque = ataque;
		this.defesa = defesa;	// percentagem
		this.range = range;
		this.Totallife = Totallife;
		this.life = Totallife; 
	}
	
	public boolean readyToSpeed(){
		if(SpeedLeft > 0){
			SpeedLeft--;
			return false;
		}else{
			// pronto a mover
			SpeedLeft = speed;
			return true;
		}
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
		this.SpeedLeft = speed;
	}
	
	public void setFollow(Peca peca){
		this.follow = peca;
	}
	
	public Peca getFollow(){
		return follow;
	}
	
	public ImageIcon Imagem(){
		return new ImageIcon(getClass().getResource(ImagemNome));	
	}
	
	public int getLife() {
		return life;
	}
	
	public int getTotallife() {
		return Totallife;
	}
	
	public boolean isCombat(){
		return combat;
	}
	
	public void setCombat(boolean a){
		combat = a;
	}
	
	public boolean isHouse(){
		return house;
	}
	
	public boolean isObstaculo(){
		return obstaculo;
	}
	
	public boolean isBoss() {
		return boss;
	}
	
	public void setBoss(){
		boss = true;
	}

	public boolean isRei(){
		return false;
	}
	
	public int getRange() {
		return range;
	}
	
	public boolean inCombat(){
		return combat;
	}

	public Coordenada getActual() {
		return actual;
	}
	
	public Coordenada getDestino() {
		return destino;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	// DEFENDE E DEDUZ NA VIDA O MONTANTE
	public void Defende(int dano){
		double valor = dano*(1 - defesa);
		life -= (int)valor;
	}

	// CALCULO DO VALOR DO ATAQUE E ENVIA PARA A DEFESA DA OUTRA PEcA 
	public int Ataca(int nrEnimigos){
		combat = true;
		int damage = ataque/nrEnimigos;
		if(ataque != 0)
			damage = new Random().nextInt(damage);
		return damage;
	}
	
	public void setDestino(Coordenada a) {
		destino = a;
	}

	public void setActual(Coordenada nova) {
		actual = nova;
	}
	
	public int getID(){
		return id;
	}

}
