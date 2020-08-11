package Engine;

/**
 * @author Ricardo Afonso
 * @author Diogo Pedreira 
 * 
 * Motor de Jogo.
 */

import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JApplet;
import javax.swing.JPanel;

import MultiPlayer.MultiPlayerStart;
import SinglePlayer.SinglePlayerStart;

public class Engine extends JApplet{

	private boolean Bstart = true;
	private boolean Bsingle = false;
	
	private Container contentor = getContentPane();
	private PintarStart pintarStart;
	private Thread ThreadStart;
	private JPanel startPainel;
	
	private AudioClip sound1;
	private SinglePlayerStart single;
	private MultiPlayerStart multi;

	/**
	 * Este metodo inicializa os ecras iniciais de jogo.
	 * 
	 * @see SinglePlayerStart.getLevelActivo
	 */
	public void init() {
		
		sound1 = getAudioClip( getCodeBase(), "Banana Phone.wav");	
		setSize(800,700);	

		startPainel = new JPanel();
		pintarStart = new PintarStart(this);
		ThreadStart = new Thread(pintarStart);
		
		// CONFIGURAcOES DO CONTENTOR E START DA APLICAccO
		contentor.setFocusable(true);
		contentor.setBackground(Color.black);

		startPainel.setLayout(new BorderLayout());
		startPainel.add(pintarStart, BorderLayout.CENTER);
		contentor.add(startPainel, BorderLayout.CENTER);
		
		// MOUSE LISTENNER MOTION
		contentor.addMouseMotionListener(new MouseMotionListener() {
			
			public void mouseMoved(MouseEvent e) {
				if(Bstart)
					pintarStart.MouseClicada(e);
				if(Bsingle)
					single.getLevelActivo().MouseMove(e);
			}
			
			public void mouseDragged(MouseEvent e) {
			}
		});
		
		// MOUSE LISTENNER
		contentor.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent e) {
				if(Bstart){
					try {
						pintarStart.MouseClick(e);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else{
					if(Bsingle)
						single.getMapa().MouseClicada(e);
				}
			}
			
			public void mouseExited(MouseEvent e) {
			}
			
			public void mouseEntered(MouseEvent e) {
			}
			
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
	
	/**
	 * Este mctodo inicializa a Thread que refresca o painel e permite visualizar as imagens(.gif).
	 * E caso seleccionada a opcco SinglePlayer ou Multiplayer escolhida pelo utilizador reencaminha-a,
	 * para o respectivo mctodo.
	 */
	public void start() {
		setVisible(true);
		sound1.play();
		ThreadStart.start();
	}
	
	public void MusicChange(boolean on){
		if(on)
			sound1.play();
		else
			sound1.stop();
	
	}
	/**
	 * Este mctodo inicia o Jogo Singleplayer, interrompe a Thread que pinta o ecrc inicial. 
	 * Selecciona a classe SinglePlayer e inicializa-a.
	 */
	public void StartGameSingle(){
		// SINGLE PLAYER
		Bstart = false;
		Bsingle = true;
		startPainel.setVisible(false);
		try{
			ThreadStart.interrupt();
			ThreadStart.join();
		}catch(InterruptedException e){}
		sound1.stop();
		
		single = new SinglePlayerStart(contentor);
		single.start();
	}
	
	/**
	 * Este mctodo inicia o Jogo Multiplayer, interrompe a Thread que pinta o ecrc inicial. 
	 * Selecciona a classe MultiPlayer e inicializa-a.
	 */
	public void StartGameMulti() throws IOException{
		// MULTI PLAYER
		Bstart = false;
		Bsingle = false;
		startPainel.setVisible(false);
		try{
			ThreadStart.interrupt();
			ThreadStart.join();
		}catch(InterruptedException e){}
		sound1.stop();
		
		multi = new MultiPlayerStart(contentor);
		multi.executa();
	}
	
}
