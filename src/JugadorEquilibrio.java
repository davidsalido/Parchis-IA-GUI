import java.util.Collections;

import javax.swing.JOptionPane;


public class JugadorEquilibrio extends Jugador{

	public JugadorEquilibrio(String c, int posicionInicial, int posicionEspecial,Vista v) {
		super(c, posicionInicial, posicionEspecial, v);
	}

	public void jugada(int dado){
		
		if(dado == 5){
			if(fichasEnCasa > 0 && v.fichasEnCasilla(pI) < 2){ //Se saca ficha siempre que se pueda
				fichas.add(new Ficha(pI,color,pE));
				v.colocarFicha(color, pI);
				fichasEnCasa--;
				v.sacarFichaCasa(color);
			}
			else{
				decidirFichaJugar(dado);
			}
		}
		else{
			decidirFichaJugar(dado);
		}
	}
	
	private void decidirFichaJugar(int dado){

		fichas.sort(Collections.reverseOrder()); //Al ser jugador equilibrado, se selecciona las mas cerca al origen para que todas vayan a la par
		for(Ficha f:fichas){
			if(f.getPosicion() != pE && v.esPosibleMoverFicha(f.getPosicion(), v.suma(f.getPosicion(), dado, color),color,pE)){
				int posInicial = f.getPosicion();
				v.quitarFicha(f.getPosicion(),color);
				f.setPosicion(v.suma(f.getPosicion(), dado, color));
				v.colocarFichaDeslizar(color, posInicial,v.suma(posInicial, dado, color));
				break;
			}
		}
	}
	
}
