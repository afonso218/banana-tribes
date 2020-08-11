package SinglePlayer;

import java.awt.event.MouseEvent;

public interface Level{

	void MouseClicada(MouseEvent e);
	void MouseMove(MouseEvent e) ;
	boolean getComplete();
	boolean getLoss();
	
}
