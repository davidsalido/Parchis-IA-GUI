import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;


public class JugadorPersona extends Jugador{

	public JugadorPersona(String c, int posicionInicial, int posicionEspecial,Vista v) {
		super(c, posicionInicial, posicionEspecial, v);
	}

	private boolean sePuedeJugar(int dado){
		for(Ficha f:fichas){
			if(f.getPosicion() != pE && v.esPosibleMoverFicha(f.getPosicion(), v.suma(f.getPosicion(), dado, color),color,pE)){
				return true;
			}
		}
		return false;
	}
	
	public void jugada(int dado){
		
		if(dado == 5){
			if(fichasEnCasa > 0 && v.fichasEnCasilla(pI) < 2){ 
				decidirFichaJugar(dado);
			}
			else{
				if(sePuedeJugar(dado))decidirFichaJugar(dado);
				else JOptionPane.showMessageDialog(v, "No se puede jugar nada :(");
			}
		}
		else{
			if(sePuedeJugar(dado)) decidirFichaJugar(dado);
			else JOptionPane.showMessageDialog(v, "No se puede jugar nada :(");
		}
	}
	
	private void decidirFichaJugar(int dado){
		ArrayList<Integer> posiciones = new ArrayList<>(); //Posiciones de las fichas que se pueden jugar
		for(Ficha f:fichas){
			if(f.getPosicion() != pE && v.esPosibleMoverFicha(f.getPosicion(), v.suma(f.getPosicion(), dado, color),color,pE)){
				posiciones.add(f.getPosicion());
			}
		}
		
		boolean cinco = false;
		if(dado == 5 && fichasEnCasa > 0 && v.fichasEnCasilla(pI) < 2 ) cinco = true;
		int pos = v.seleccionarFicha(color,posiciones,cinco);
		
		if(pos == -1){
			fichas.add(new Ficha(pI,color,pE));
			v.colocarFicha(color, pI);
			fichasEnCasa--;
			v.sacarFichaCasa(color);
		}
		else{
			Ficha f = null;
			for(int i = 0; i < fichas.size();i++){
				if(fichas.get(i).getPosicion() == pos){
					f = fichas.get(i);
					break;
				}
			}
			
			int posInicial = f.getPosicion();
			v.quitarFicha(f.getPosicion(),color);
			f.setPosicion(v.suma(f.getPosicion(), dado, color));
			v.colocarFichaDeslizar(color, posInicial,v.suma(posInicial, dado, color));
		}
	}
	

}
