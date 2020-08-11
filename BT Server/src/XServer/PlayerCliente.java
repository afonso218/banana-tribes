package XServer;

import java.io.IOException;
import java.io.ObjectOutputStream;

import Mapa.Mapa;
import MultiPlayer.Informacao;
import XServer.Server.ListenerPlayer;

public class PlayerCliente {

	private int ID;
	private ObjectOutputStream out;
	private ListenerPlayer in;
	
	public PlayerCliente(int NLigacoes, ObjectOutputStream out, ListenerPlayer in) {
		this.ID = NLigacoes;
		this.out = out;
		this.in = in;
	}
	
	public int getID() {
		return ID;
	}
	
	public ListenerPlayer getIn() {
		return in;
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}
	
	public String toString(){
		return "Player " + ID;
	}

	public void Envia(Informacao ordem){
		try {
			out.reset();
			out.writeObject(ordem);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
