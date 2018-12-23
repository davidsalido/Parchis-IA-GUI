import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MouseClick implements MouseListener{

	private int pos;
	private Vista v;
	private Thread t;
	
	public MouseClick(int i, Vista v, Thread t){
		this.pos = i;
		this.v = v;
		this.t = t;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		t.interrupt();
		v.posicionSeleccionada = pos;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
