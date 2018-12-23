
public class Casilla {

	protected boolean ficha1 = false;
	protected boolean ficha2 = false;

	
	public boolean dosFichas(){
		return ficha1 && ficha2 ;
	}
	
	public void colocarFicha(){
		if(!ficha1) ficha1 = true;
		else ficha2 = true;
	}
	
	public boolean unaFicha(){
		return ficha1 || ficha2;
	}
	
	public void quitarFicha(){
		if(!ficha2) ficha1 = false;
		else ficha2 = false;
	}
	
	
	public String toString(){
		return ficha1 + " " + ficha2;
	}
	
}
