package MultiPlayer;

/**
 * @author Ricardo Afonso
 * @author Diogo Pedreira 
 * 
 * Multiplayer Starter para Cliente.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Mapa.Mapa;
import Mapa.Player;

public class MultiPlayerStart {

	private Player player;
	private boolean start;
	private Mapa mapa;
	private PintarMapa pinta;
	private Thread pintor;
	
	private boolean ready;
	private ScrollPane consola;
	private JTextArea text;
	private JTextField chat;
	private JPanel painel;
	private Container contentor;

	private ObjectOutputStream out; // envia ordens de pedido
	
	/**
	 * Este Construtor recebe o Contentor, que é vindo da classe Engine, para criar em cada 
	 * cliente a janela de jogo e todo o espaço onde os Panels e o Chat vão ser inseridos, bem como 
	 * todos os eventos no chat.
	 * É também efectuada a tentativa de ligação ao Servidor por parte do Cliente através da port 8080.
	 * 
	 * @param contentor
	 * @see Engine.Engine
	 */
	public MultiPlayerStart(Container contentor) {
		
		this.ready = false;
		this.start = true;
		this.contentor = contentor;
		
		text = new JTextArea();
		text.setFont(new Font("Arial", Font.BOLD, 15));
		text.setForeground(Color.white);
		text.setBackground(Color.black);
		text.setEditable(false);
		painel = new JPanel();
		painel.setLayout(new BorderLayout());
		painel.setBackground(Color.black);
		
		consola = new ScrollPane();
		consola.add(text);
		consola.setPreferredSize(new Dimension(800,100));
		chat = new JTextField(25);
		JPanel painelBaixo = new JPanel();
		JPanel a = new JPanel();
		a.setLayout(new BorderLayout());
		a.add(new JLabel("Chat:  "), BorderLayout.WEST);
		a.add(chat, BorderLayout.CENTER);
		chat.addKeyListener(new KeyListener() {
		
			public void keyTyped(KeyEvent arg0) {
			}
			public void keyReleased(KeyEvent arg0) {
			}
			/**
			 * Caso a Tecla pressionada seja o ENTER, todo o corpo de texto escrito, é enviado para
			 * o TextArea tanto no cliente actual como no cliente enimigo.
			 */
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(chat.getText() != null){
						text("YOU: " + chat.getText());
						try {
							out.writeObject(new Informacao(chat.getText()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						chat.setText("");
					}
				}
			}
		});
		painelBaixo.setLayout(new BorderLayout());
		painelBaixo.add(consola, BorderLayout.CENTER);
		painelBaixo.add(a, BorderLayout.SOUTH);
		this.contentor.add(painelBaixo, BorderLayout.SOUTH);
		this.contentor.add(painel, BorderLayout.CENTER);

		// LISTENERS
		painel.addMouseMotionListener(new MouseMotionListener() {
		/**
		 * Painel de jogo fica à espera de uma acção por parte do User, para mover o mapa para a Esquerda, 
		 * Direita, Cima ou Baixo pintando o Mapa no sentido pretendido.	
		 * 
		 * @see PintarMapa.MouseMove
		 */
			public void mouseMoved(MouseEvent e) {
				if(ready)
					pinta.MouseMove(e);
			}
			
			public void mouseDragged(MouseEvent e) {
			}
		});
		
		painel.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
			}
			/**
			 * Quando é pressionado o botão do rato esquerdo ou direito, este redirecciona 
			 * os dados do click. 
			 */
			public void mousePressed(MouseEvent e) {
				if(ready)
					pinta.MouseClicada(e);
			}

			public void mouseClicked(MouseEvent e) {
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
		});
		
		try {
			text("> Connecting to Server...");
			InetAddress endereco = InetAddress.getByName(null);
			Socket socket = new Socket(endereco, 8080); // ...ServerSocket

			ListenerServidor in = new ListenerServidor(new ObjectInputStream(
					socket.getInputStream()));
			out = new ObjectOutputStream(socket.getOutputStream());
			text("> Connected! ");
			text("> Waiting for new players online...");
			in.start();
		} catch (IOException e) {
			text("[ ERRO: CONNECTING TO SERVER... TRY AGAIN LATER... SERVER OFFLINE ]");
		}
		
		this.pintor = new Thread(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					while(!Thread.interrupted()){
						pinta.repaint();
						Thread.sleep(25);
					}
				} catch (InterruptedException e) {
					System.out.println("> Pintor interrompido!");
				}
			}
		});
	}
	
	/**
	 * Este método, restringe o tamanho do texto(x) que é enviado para o ecrã a 1000 caractéres.
	 * Caso seja superior a 1000 esta mensagem não é enviada para o ecrã.
	 * 
	 * @param x esta variável é o texto recebido pelo método
	 */
	public void text(String x) {
		// LIMITE DE PALAVRAS
		if(text.getText().length() > 1000)
			text.setText("");
		text.setText(text.getText() + "\n" + x );
		// SCROLL
		text.setCaretPosition( text.getText().length() );
	}

	public void executa() {
	}
	
	/**
	 * Nested Class que extends uma Thread, que vai efectuar o controlo dos dados recebidos
	 * através do Servidor, e através das ordens(Informacao) recebidas realizar um certo tipo de Operação.
	 * 
	 */
	public class ListenerServidor extends Thread {

		private ObjectInputStream in;
		/**
		 * Este construtor da classe vai receber um ObjectInputStream, que será a ligação do servidor
		 * ao cliente (dados recebidos do servidor) .
		 * 
		 * @param in variável associada ao dados recebidos.
		 */
		public ListenerServidor(ObjectInputStream in) {
			this.in = in;
		}
		
		/**
		 * Este método vai estar constantemente, até ser interrompido, a receber uma nova informacao.
		 * Processando toda a informação recebida, dentro dos tipo de informacao pré-definidas.
		 * 
		 * */
		public void run() {
			try {
				while (!interrupted()) {

					Informacao ordem = (Informacao)in.readObject();
					
					if(ordem.getTipo() == 5){
						player = ordem.getPlayer();
						pinta = new PintarMapa(out, player);
					}
					// MAPA
					if(ordem.getTipo() == 1){
						mapa = ordem.getMapa();
						if (start) {
							text("> GAME STARTED !");
							painel.add(pinta, BorderLayout.CENTER);
							pinta.setMapa(mapa);
							pintor.start();
							ready = true;
							start = false;
						}
						pinta.setMapa(mapa);
						pinta.repaint();
					}
					//MENSAGEM
					if(ordem.getTipo() == 2){
						text(ordem.getMensagem());
					}
				}
			} catch (IOException e) {
				text("ERRO: I/O REFRESH LISTENER");
			} catch (ClassNotFoundException e) {
				text("ERRO: CLASS REFRESH LISTENER");
			}
		}
	}
}
