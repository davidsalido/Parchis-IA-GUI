
public class CasillaEspecial extends Casilla{

	protected boolean ficha3 = false;
	private boolean ficha4 = false;
	
	public boolean haGanado(){
		return super.dosFichas() && ficha3 && ficha4;
	}

	public void colocarFicha(){
		if(super.dosFichas()){
			if(!ficha3) ficha3 = true;
			else ficha4 = true;
		}
		else super.colocarFicha();
	}
	
	
}
