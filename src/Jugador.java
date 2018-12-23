import java.util.ArrayList;
import java.util.Collections;


public abstract class Jugador {

	protected String color;
	protected int fichasEnCasa; //Fichas en casa
	protected ArrayList<Ficha> fichas; //Fichas en juego
	protected int pI, pE; //Casilla inicial y final
	protected Vista v;
	
	public int fichasEnCasa(){
		return fichasEnCasa;
	}
	
	public Jugador(String c, int posicionInicial ,int posicionEspecial, Vista v){
		this.color = c;
		fichasEnCasa = 4;
		fichas = new ArrayList<>();
		pI = posicionInicial;
		pE = posicionEspecial;
		this.v = v;
	}
	
	public abstract void jugada(int dado);

	//Funcion que es llamada cuando le comen una ficha
	public void quitarFicha(int pos) {
		for(int i = 0; i < fichas.size();i++){
			if(fichas.get(i).getPosicion() == pos){
				fichas.remove(i);
				v.quitarFichaCasa(color);
				fichasEnCasa++;
			}
		}
	}
	
}
