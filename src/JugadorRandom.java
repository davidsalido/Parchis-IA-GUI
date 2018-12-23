import java.util.Collections;


public class JugadorRandom extends Jugador{

	public JugadorRandom(String c, int posicionInicial, int posicionEspecial,Vista v) {
		super(c, posicionInicial, posicionEspecial, v);
	}

	public void jugada(int dado){
		if(dado == 5){
			if(fichasEnCasa > 0 && v.fichasEnCasilla(pI) < 2){ //Saca ficha de casa siempre que puede
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
		Collections.shuffle(fichas); //Al ser jugador random, se barajean las fichas para elegir una cualquiera
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
