package XServer;
/**
 * @author Ricardo Afonso
 * @author Diogo Pedreira 
 * 
 * Servidor para o jogo Multiplayer.
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Mapa.Coordenada;
import MultiPlayer.Informacao;
import Pecas.Peca;

public class Server {

	private JFrame janela = new JFrame("Banana Tribe SERVER");
	private Container contentor = janela.getContentPane();
	private JButton Stop = new JButton("STOP SERVER");
	private JTextArea text = new JTextArea();
	private JTextField consola = new JTextField(25);
	
	private ServerSocket socketServer;
	private GestorLigacao gestor;
	public static final int PORTO = 8080;
	
	private PlayerCliente PlayerEspera;
	private int NLigacoes = 0;
	private ArrayList<PlayerCliente> Players = new ArrayList<PlayerCliente>();
	private ArrayList<Game> games = new ArrayList<Game>();
	
	/**
	 * Este construtor, cria toda a parte visual do Servidor bem como todos os eventos gerados pelo
	 * chat, como comandos inseridos para saber estados actuais de Jogos em curso.
	 */	
	public Server() {
		
		int x = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400);
		int y = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 30);
		janela.setAlwaysOnTop(true);
		janela.setLocation(x,0);
		janela.setSize(400,y);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		contentor.setLayout(new BorderLayout());
		text.setBackground(Color.black);
		text.setForeground(Color.green);
		text.setEditable(false);
		text.setLineWrap(true);
		text.setFont(new Font("Arial", Font.BOLD, 13));
		
		consola.setText("WRITE COMMANDS HERE!");
		consola.setFont(new Font("Arial", Font.BOLD, 15));
		contentor.add(text, BorderLayout.CENTER);
		contentor.add(consola, BorderLayout.SOUTH);
		contentor.add(Stop, BorderLayout.NORTH);
		
		Stop.addActionListener(new ActionListener() {
			/**
			 * este Listener vai interromper todos os jogos em curso quando c pressionado o Stop Button do
			 * servidor.
			 */
			public void actionPerformed(ActionEvent arg0) {
				try{
					for(Game a : games){
						a.interrupt();
					}
					socketServer.close();
					for(PlayerCliente c : Players){
						c.getIn().interrupt();
						c.getIn().join();
					}
					gestor.interrupt();
					gestor.join();
					
					janela.dispose();
				}catch(InterruptedException e){
					text("ERRO: INTERROMPER (JOIN)");
				} catch (IOException e) {
					text("ERRO: INTERROMPER (IO)");
				}
			}
		});
		
		consola.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
			}
			/**
			 * Quando c pressionado Enter na Consola do Servidor, este vai ler o comando inserido.
			 * Dependendo do comando inserido este vai efectuar algo:
			 * a) CMD - Lista todos os comandos posscveis
			 * b) NumPlayer/ListPlayers - Lista os jogadores online no Servidor
			 * c) gameOn - Lista todos os jogos activos
			 * d) game1/2 - Activa o Observador no Servidor do jogo ncmero 1 ou 2...
			 */
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER){
					String comando = consola.getText();
					consola.setText("");
					// CMD: 
					// 	player(nc de ligacoes existentes)
					// 	list(lista as ligacoes)
					if(comando.equals("CMD")){
						text("\n############## " + comando + " ##############");
						text("- \"CMD\" : List all Commands possible");
						text("- \"NumPlayer\" : Number of Players online");
						text("- \"ListPlayers\" : Number of Players online");
						text("- \"gameOn\" : List all Games Online");
						text("- \"game1\" : Game 1 set Visible On (if is OFF)");
						text("-           : Game 1 set Visible OFF (if is ON)");
						text("- \"game2\" : Game 2 set Visible On (if is OFF)");
						text("-           : Game 2 set Visible OFF (if is ON)");
						text("- (...)");
						text("######################################\n");
					}
					if(comando.equals("NumPlayer")){
						text("> " + comando+ ":");
						text("> " + Players.size());
					}
					if(comando.equals("ListPlayers")){
						text("> " + Players.size());
						for(PlayerCliente x : Players){
							text(">" + x);
						}
					}
					if(comando.equals("gameOn")){
						text("> Active Game (" + games.size()+ ")...");
						for(int i = 1; i <= games.size(); i++)
							text("> Game" + i );	
					}
					if(comando.equals("game1")){
						if(Players.size() > 0){
							if(games.get(0).getFrame().isVisible()){
								text("> Game 1 set Visible OFF");
								games.get(0).getFrame().setVisible(false);
							}else{
								text("> Game 1 set Visible ON");
								games.get(0).getFrame().setVisible(true);
							}
						}
					}
					
					if(comando.equals("game2")){
						if(Players.size() > 1){
							if(games.get(1).getFrame().isVisible()){
								text("> Game 2 set Visible OFF");
								games.get(1).getFrame().setVisible(false);
							}else{
								text("> Game 2 set Visible ON");
								games.get(1).getFrame().setVisible(true);
							}
						}
					}
					//...
				}
			}
		});
	}
	
	/**
	 * Este mctodo envia para o ecrc o texto Escrito na consola do Servidor.
	 * @param s c a varicvel que representa o que foi escrito pelo utilizador.
	 */
	public void text(String s){
		text.setText(text.getText() + "\n" + s);
	}
	/**
	 * Inicia a janela do Servidor, com a socket pretendida e inicia uma Thread GestorLigacao. 
	 * @throws IOException
	 */
	public void inicia() throws IOException{
		janela.setVisible(true);
		text("A Ligar...");
		socketServer = new ServerSocket(PORTO);
		gestor = new GestorLigacao();
		gestor.start();
		text("Ligado! A espera de Ligacces...");
		text("> For Help type \"CMD\"(List All Commands)");
	}
	/**
	 * Main da classe Servidor. Inicia um novo Servidor.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		new Server().inicia();
	}
	
	public class GestorLigacao extends Thread{
		/**
		 * Cria um gestor de ligacco que vai gerar o ObjectOutputStream, o InputStream e um Cliente que 
		 * se vai ligar ao Servidor. Sempre que c criado um Cliente este c adicionado a uma lista de clientes e 
		 * caso esta seja diferente de 0 e estejam activos um ncmero par de clientes, c iniciado um novo jogo. Caso 
		 * contrcrio, c criada uma Lista de espera de jogadores atc que se encontrem jogadores necesscrios
		 * para a criacco de um jogo.
		 */
		public void run(){
			try{
				while(!interrupted()){
					
					NLigacoes++;
					Socket socket = socketServer.accept();
					text("Nova Ligacco > [Ligacco Nc:" + NLigacoes + "]");
					ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
					ListenerPlayer in = new ListenerPlayer(new ObjectInputStream(socket.getInputStream()), NLigacoes);
					PlayerCliente a = new PlayerCliente(NLigacoes ,out, in);
					in.start();	
					
					Players.add(a);
					if(Players.size()%2 == 0 && Players.size() != 0){
						JFrame novoGame = new JFrame("Game " + NLigacoes/2);
						Game game = new Game(novoGame, PlayerEspera, Players.get(NLigacoes-1));
						PlayerEspera = null;
						games.add(game);
						game.start();
						text("New Game Started: Player" + game.getP1().getID() 
								+ " vs Player" + game.getP2().getID() );
					}else{
						PlayerEspera = a;	
						text("Player" + PlayerEspera.getID() + " waiting for opponent!");
					}
				}
			}catch(IOException e){
				text("ERRO: NO GESTOR DE NOVAS LIGAcOES");
			}
		}	
	}
	
	public class ListenerPlayer extends Thread{
		
		private int IdPlayer;
		private ObjectInputStream in;
		/**
		 * Construtor que recebe um ObjectInputStream e um Player ID.
		 * 
		 * @param in ObjectInputStream que contem ordens do jogador com o ID recebido.
		 * @param ID ID do Jogador que envia ordem para o Servidor.
		 */
		public ListenerPlayer(ObjectInputStream in, int ID) {
			this.in = in;
			this.IdPlayer = ID;
		}
		/**
		 * Neste mctodo run, existe um tratamento da informacco chegada do cliente. Caso seja
		 * do Tipo 2(CHAT), este envia para o TextArea do chat a mensagem. Para o ecrc do Jogador
		 * que enviou a mensagem, aparece YOU + mensagem. Caso seja no ecrc Enimigo, aparece ENEMY + mensagem.
		 * Caso algum jogador feche a sessco do Jogo, c  reduzido o ncmero de ligacces, guarda o ID do player que
		 * se desconectou do jogo e vai adicionar o Jogador que ainda se encontra conectado c lista de Espera.
		 * Interrompendo o jogo.
		 */
		public void run(){
			try{
				while(!interrupted()){
					// TRATAMENTO DE INFORMAccO CHEGADA DO CLIENTE
					Informacao ordem = (Informacao)in.readObject();
					// CHAT
					if(ordem.getTipo() == 2){
						String frase = "" + ordem.getMensagem();
						text("Player(" + IdPlayer + ") Mensagem: " + frase);
						frase = "ENEMY: " + ordem.getMensagem();
						for(int i = 0; i < games.size(); i++){
							if(games.get(i).getP1().getID() == IdPlayer){
								games.get(i).getP2().getOut().writeObject(new Informacao(frase));
							}
							if(games.get(i).getP2().getID() == IdPlayer){
								games.get(i).getP1().getOut().writeObject(new Informacao(frase));
							}
							
						}
					}
					// MOVE PEcA
					if(ordem.getTipo() == 3){
						System.out.println("> CHEGOU ORDEM DE MOVIMENTO");
						
						for(int i = 0; i < games.size(); i++){
							if(games.get(i).getP1().getID() == IdPlayer || games.get(i).getP2().getID() == IdPlayer){
								games.get(i).Operacco(ordem);
							}
						}
						
					}
						
				}
			}catch(IOException e){
				NLigacoes--;
				PlayerCliente PlayerSai = null;
				for(PlayerCliente a : Players)
					if(a.getID() == IdPlayer){
						PlayerSai = a;
					}
				Game GameFim = null;
				if(PlayerEspera != null && PlayerSai.getID() == PlayerEspera.getID()){
						PlayerEspera = null;
				}else{
					for(Game a : games){
						if(a.getP1().getID() == PlayerSai.getID() || a.getP2().getID() == PlayerSai.getID()){
							GameFim = a;
							if(PlayerSai.getID() == a.getP1().getID()){
								PlayerEspera = a.getP2();
							}else{
								PlayerEspera = a.getP1();
							}
						}
					}
					GameFim.interrupt();
					games.remove(GameFim);
					try {
						PlayerEspera.getOut().writeObject(new Informacao("ENEMY LEFT!"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					text("Player" + PlayerEspera.getID() + " waiting for opponent!");
				}
				text("> PLAYER(" + PlayerSai.getID() + ") LEFT THE GAME");
				Players.remove(PlayerSai);
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
