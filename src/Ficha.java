
public class Ficha implements Comparable<Ficha>{

	private int posicion;
	private String color;
	private int fin;
	
	public Ficha(int p, String c, int fin){
		this.setPosicion(p);
		this.color = c;
		this.fin = fin;
	}

	public int getPosicion() {
		return posicion;
	}

	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	@Override
	public int compareTo(Ficha f) {
		return Vista.distancia(posicion, fin, color) - Vista.distancia(f.posicion, f.fin, f.color);
	}

	public String toString(){
		return color + " " + posicion;
	}
	
}
