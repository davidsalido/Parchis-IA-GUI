import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.Position;


public class Vista extends JFrame{

	public static void main(String[] args){
		new Vista();
	}
	
	private long tiempoDado = 10;
	private long tiempoJugada = 50;
	private long tiempoDeslizamiento = 30;
	
	private HashMap<Integer,Rectangle> posiciones; //Coordenadas de las fichas centrales en el tablero
	private ArrayList<JLabel> fichas;
	private ArrayList<JLabel> fichasIzq; //Fichas desplazadas a la izquierda (dos fichas en la misma casilla)
	private ArrayList<JLabel> fichasDer; //Fichas desplazadas a la derecha (dos fichas en la misma casilla)
	private ArrayList<ArrayList<JLabel>> fichasEnCasa; //Fichas de los jugadores en sus respectivas casas
	private ArrayList<JLabel> cuartasFichas; //Cuarta ficha en las casillas ganadoras de cada jugador
	
	private ArrayList<JLabel> dados; //Dado de cada jugador
	private int turno = 0; //Turno
	
	private ArrayList<JLabel> textoJugadores;
	
	private ArrayList<Casilla> casillas; //Lista de casilla con la informacion de cuantas casillas hay
	private ArrayList<Jugador> jugadores; //Jugadores que juegan la partida
	
	private String[] colores = {"amarillo","azul","rojo","verde"};
	private String[] coloresTexto = {"Amarillo","Azul","Rojo","Verde"};
	private String[] tipos = {"(persona)","(IA lejano)","(IA equilibrado)","(IA random)"};
	private int[] posFinales = {75,83,91,99};
	
	//Lectura del fichero de coordenadas
	private void inicializarFichero(){
		posiciones = new HashMap<>();
		try {
			BufferedReader bd = new BufferedReader(new FileReader("fichero.txt"));
			for(int i = 1; i <= 100;i++){
				String l = bd.readLine();
				String[] s = l.split(" ");
				Rectangle r = new Rectangle(40,40);
				r.setLocation(Integer.parseInt(s[1]),Integer.parseInt(s[2]));
				posiciones.put(Integer.parseInt(s[0]), r);
			}
			bd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vista(){
		JPanel principal = new JPanel();
		principal.setLayout(null);
		

		Rectangle rr = new Rectangle(1032,50,150,30);
		textoJugadores = new ArrayList<>();
		for(int i = 0; i < 4;i++){
			JLabel d = new JLabel(coloresTexto[i] + " " + tipos[i]);
			d.setBackground(Color.BLACK);
			d.setBounds(rr);
			principal.add(d);
			d.setBackground(Color.red);
			textoJugadores.add(d);
			rr.y +=200;
		}
		
		//Inicializacion dados
		Rectangle r = new Rectangle(1032,100,136,136);
		dados = new ArrayList<>(4);
		for(int i = 0; i < 4;i++){
			JLabel d = new JLabel(new ImageIcon("img/1.jpg"));
			d.setBounds(r);
			principal.add(d);
			dados.add(d);
			r.y += 200;
		}

		//Lectura fichero coordenadas
		inicializarFichero();
		
		//Inicializacion de casillas y fichas (Invisibles, se haran visibles si hay ficha en casilla)
		casillas = new ArrayList<>();
		fichas = new ArrayList<>(100);
		fichasDer = new ArrayList<>(100);
		fichasIzq = new ArrayList<>(100);
		for(int i = 1; i <= 100;i++){
			JLabel jl = new JLabel(new ImageIcon("img/amarillo.png"));
			JLabel jIzquierda = new JLabel(new ImageIcon("img/amarillo.png"));
			JLabel jDerecha = new JLabel(new ImageIcon("img/amarillo.png"));
			
			jl.setBounds(coordenadas(i));
			jIzquierda.setBounds(coordenadasIzq(i));
			jDerecha.setBounds(coordenadasDer(i));
			
			principal.add(jl);
			principal.add(jIzquierda);
			principal.add(jDerecha);
			
			jl.setVisible(false);
			jIzquierda.setVisible(false);
			jDerecha.setVisible(false);
			
			fichas.add(jl);
			fichasDer.add(jDerecha);
			fichasIzq.add(jIzquierda);
			
			if(i == 76 || i == 84 || i == 92 || i == 100) casillas.add(new CasillaEspecial()); //Casilla ganadora
			else casillas.add(new Casilla());
		}
		
		int[] x = {759,759,116,116}; //Coordenadas por color de las fichas en casa
		int[] y = {759,116,116,759};
		
		fichasEnCasa = new ArrayList<>(4);
		for(int k = 0; k < 4;k++){
			for(int i = 0; i < 4;i++){
				if(i == 1) x[k]+= 85;
				if(i == 2) y[k]+= 85;
				if(i == 3) x[k]-=85;
				fichasEnCasa.add(new ArrayList<>());
				JLabel jl = new JLabel(new ImageIcon("img/" + colores[k] + ".png"));
				jl.setBounds(x[k],y[k],40,40);
				fichasEnCasa.get(k).add(jl);
				principal.add(jl);
	
			}
		}
		
	
		
		int[] xx = {0,-30,0,30}; //Desplazamiento por color de las fichas en casilla ganadora
		int[] yy = {-30,0,30,0};
		
		cuartasFichas = new ArrayList<>(4);
		for(int i = 0; i < 4;i++){
			JLabel j = new JLabel(new ImageIcon("img/" + colores[i] + ".png"));
			j.setBounds(posiciones.get(posFinales[i] + 1).x + xx[i], posiciones.get(posFinales[i] + 1).y + yy[i] ,posiciones.get(posFinales[i]+ 1).width ,posiciones.get(posFinales[i]+ 1).height);
			principal.add(j);
			cuartasFichas.add(j);
			j.setVisible(false);
		}
		
		JLabel fondo = new JLabel(new ImageIcon("img/fondo.jpg"));
		fondo.setBounds(0, 0, 1000, 1000);
		principal.add(fondo);
		
		principal.setSize(new Dimension(1000,1000));
		this.setContentPane(principal);
		this.setSize(new Dimension(1200,1030));
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				jugar();
				
			}
			
		});
		t.start();
	}
	
	private boolean partidaAcabada(){
		return ((CasillaEspecial) casillas.get(75)).haGanado() ||  ((CasillaEspecial) casillas.get(83)).haGanado()
				||  ((CasillaEspecial) casillas.get(91)).haGanado() ||  ((CasillaEspecial) casillas.get(99)).haGanado();
	}
	
	//Genera un numero semi-aleatorio y muveve graficamente el dado
	private int tirarDado(int turno) {
		
		Random r = new Random();
		int result = 0;
		for(int i = 0; i < 6;i++){
			int num = r.nextInt();
			num = (num % 6);
			num = Math.abs(num) + 1;
			try {
				Thread.sleep(tiempoDado);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			dados.get(turno).setIcon(new ImageIcon("img/" + num + ".jpg"));
			result = num;
		}
		return result;
	}
	
	//Llama a cada jugador para que efectue su turno hasta que acabe el juego
	private void jugar(){
		jugadores = new ArrayList<>(4);
		jugadores.add(new JugadorPersona("amarillo",4,75,this));
		jugadores.add(new JugadorLejano("azul",21,83,this));
		jugadores.add(new JugadorEquilibrio("rojo",38,91,this));
		jugadores.add(new JugadorRandom("verde",55,99,this));
		
		
		while(!partidaAcabada()){
			try {
				Thread.sleep(tiempoJugada );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			jugadores.get(turno).jugada(tirarDado(turno));
			if(!partidaAcabada()) turno = ++turno % 4;
		}
		
		JOptionPane.showMessageDialog(this, "Ha ganado el color " + colores[turno]);
	}
	
	//Funcion que detecta el color de una ficha dado la ruta de un icono. Ejemplo "amarillo.png"
	private String colorSubsecuencia(String s, int ini){
		String r = "";
		while(s.charAt(ini) != '.'){
			r += s.charAt(ini);
			ini++;
		}
		return r;
	}
	
	//Siguiente posicion de la casilla ya que segun el color cambia
		public static int sigPos(int pos, String color){
			if(color.equals("amarillo")){
				if(pos == 75){
					return 75;
				}
			}
			if(color.equals("verde")){
				if(pos == 99){
					return 99;
				}
				if(pos == 67){
					return 0;
				}
				if(pos == 50){
					return 92;
				}
			}
			else if(color.equals("rojo")){
				if(pos == 91){
					return 91;
				}
				if(pos == 67){
					return 0;
				}
				if(pos == 33){
					return 84;
				}
			}
			else if(color.equals("azul")){
				if(pos == 83){
					return 83;
				}
				if(pos == 67){
					return 0;
				}
				if(pos == 16){
					return 76;
				}
			}
			return pos + 1;
		}
	
	//Comprueba si es posible mover la ficha viendo que no haya barreras en su camino
	public boolean esPosibleMoverFicha(int ini,int fin,String color,int pE){
		boolean sePuede = true;
		for(int i = sigPos(ini,color); i != sigPos(fin,color)  && sePuede;i = sigPos(i,color)){
			if(i == pE){
				sePuede =  true;
			}
			else sePuede = !casillas.get(i).dosFichas();
			if(i == sigPos(i,color)) break; //Evita bucles infinitos cuando se llega al final del camino de cada jugador
		}
		return sePuede;
	}
	
	//Colocar una ficha de inicio a final pasando por cada ficha, esta no comprueba si es pobile, hay que comprobarlo antes
	public void colocarFichaDeslizar(String color, int ini,int fin){
		
		int i = ini;
		while(i != fin){
			colocarFicha(color,i);
			try {
				Thread.sleep(tiempoDeslizamiento);
			} catch (InterruptedException e) {
				
			}
			quitarFicha(i,color);
			i = sigPos(i,color);
		}
		//Se puede comar ficha
		if(casillas.get(fin).unaFicha() && !casa(fin)){
			String colorFicha = ((ImageIcon) fichas.get(fin).getIcon()).getDescription();
			boolean esMismoColor = colorFicha.indexOf(color) !=-1? true: false;
			//Mismo color no se come
			if(esMismoColor) colocarFicha(color,fin);
			else{ //Se coma ficha y se avanzan 20
				String colorAntiguo = colorSubsecuencia(colorFicha,4);
				quitarFicha(fin,colorAntiguo);
				colocarFicha(color,fin);
				jugadores.get(colorAPos(colorAntiguo)).quitarFicha(fin);
				jugadores.get(colorAPos(color)).jugada(20);
			}
		}
		else{
			colocarFicha(color,fin);
		}
	}
	
	//Funcion que quita la ficha graficamente de una casilla
	public void quitarFicha(int pos, String color){
		if(casillas.get(pos).dosFichas())
		{
			casillas.get(pos).quitarFicha();
			fichasIzq.get(pos).setVisible(false);
			fichasDer.get(pos).setVisible(false);
			
			String input = ((ImageIcon) fichasIzq.get(pos).getIcon()).getDescription();
			boolean izquierda = input.indexOf(color) !=-1? true: false;
			
			if(izquierda){
				fichas.get(pos).setIcon(fichasDer.get(pos).getIcon());
			}
			else{
				fichas.get(pos).setIcon(fichasIzq.get(pos).getIcon());
			}
			fichas.get(pos).setVisible(true);
		}
		else{
			casillas.get(pos).quitarFicha();
			fichas.get(pos).setVisible(false);
		}
	}
	
	//Funciona que coloca graficamente una ficha en la casilla
	public void colocarFicha(String color, int pos){
		if(casillas.get(pos) instanceof CasillaEspecial && casillas.get(pos).dosFichas()){
			if(((CasillaEspecial) casillas.get(pos)).ficha3){
				casillas.get(pos).colocarFicha();
				cuartasFichas.get(colorAPos(color)).setVisible(true);
			}
			else{
				casillas.get(pos).colocarFicha();
				fichas.get(pos).setVisible(true);
			}
		}
		else if(casillas.get(pos).unaFicha()){
			casillas.get(pos).colocarFicha();
			fichas.get(pos).setVisible(false);
			
			fichasIzq.get(pos).setIcon(fichas.get(pos).getIcon());
			fichasIzq.get(pos).setVisible(true);
			
			fichasDer.get(pos).setIcon(new ImageIcon("img/" + color + ".png"));
			fichasDer.get(pos).setVisible(true);
		}
		else{
			casillas.get(pos).colocarFicha();
			fichas.get(pos).setIcon(new ImageIcon("img/" + color + ".png"));
			fichas.get(pos).setVisible(true);
		}
		
	}
	
	//Numero de fichas en una casilla
	public int fichasEnCasilla(int pos){
		if(casillas.get(pos).dosFichas()){
			return 2;
		}
		else if(casillas.get(pos).unaFicha()){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	//Devuelve la ficha destino segun la suma
	public int suma(int pos, int suma,String color){
		for(int i = 0; i < suma;i++){
			pos = sigPos(pos,color);
		}
		return pos;
	}
	
	//Coordenaas da las fichas centrales
	private Rectangle coordenadas(int casilla){
		return posiciones.get(casilla);
	}
	
	//Coordenaas da las fichas derechas(centrales desplazando)
	private Rectangle coordenadasDer(int casilla){
		int x = 0; int y = 0;
		if((casilla >= 1 && casilla <= 8) || (casilla >= 60 && casilla <= 76)){
			x = 20;
		}
		else if((casilla >= 9 && casilla <= 25) || (casilla >= 77 && casilla <= 84)){
			y = -20;
		}
		else if((casilla >= 26 && casilla <= 42) || (casilla >= 85 && casilla <= 92)){
			x = 20;
		}
		else if((casilla >= 43 && casilla <= 59) || (casilla >= 93 && casilla <= 100)){
			y = 20;
		}
		return new Rectangle( posiciones.get(casilla).x + x, posiciones.get(casilla).y + y, posiciones.get(casilla).width, posiciones.get(casilla).height);
	}
	//Coordenaas da las fichas derechas(centrales desplazando)
	private Rectangle coordenadasIzq(int casilla){
		int x = 0; int y = 0;
		if((casilla >= 1 && casilla <= 8) || (casilla >= 60 && casilla <= 76)){
			x = -20;
		}
		else if((casilla >= 9 && casilla <= 25) || (casilla >= 77 && casilla <= 84)){
			y = 20;
		}
		else if((casilla >= 26 && casilla <= 42) || (casilla >= 85 && casilla <= 92)){
			x = -20;
		}
		else if((casilla >= 43 && casilla <= 59) || (casilla >= 93 && casilla <= 100)){
			y = -20;
		}
		
		return new Rectangle( posiciones.get(casilla).x + x, posiciones.get(casilla).y + y, posiciones.get(casilla).width, posiciones.get(casilla).height);
	}

	//Posiciones que son casa
	public boolean casa(int pos){
		return pos == 67 || pos == 4 || pos == 11 || pos == 16 || pos == 21 || pos == 28 || pos == 33 || pos == 38 || pos == 45 || pos == 50 || pos ==  55 || pos == 62;
	}

	//Posicion del jugador segun su color
	private int colorAPos(String color){
		int pos = 0;
		for(int i = 0; i < colores.length;i++){
			if(color.equals(colores[i])){
				pos = i;
			}
		}
		return pos;
	}
	
	//Sacar graficamente ficha de la casa del jugador
	public void sacarFichaCasa(String color) {
		int pos = colorAPos(color);
		fichasEnCasa.get(pos).get(jugadores.get(pos).fichasEnCasa()).setVisible(false);;
	}

	//Quitar graficamente ficha de la casa del jugador
	public void quitarFichaCasa(String color) {
		int pos = colorAPos(color);
		fichasEnCasa.get(pos).get(jugadores.get(pos).fichasEnCasa()).setVisible(true);;
	}
	
	//Distancia entre una casilla y otra segun el color del jugador
	public static int distancia(int ini, int fin,String color){
		int r = 0;
		while(ini != fin){
			ini = sigPos(ini,color);
			r++;
		}
		return r;
	}

	public int posicionSeleccionada;
	
	//Funciona para que el jugador usuario seleccione la ficha que desea mover
	public int seleccionarFicha(String color, ArrayList<Integer> posiciones,boolean cinco) {
		for(int i: posiciones){
			fichas.get(i).addMouseListener(new MouseClick(i,this, Thread.currentThread()));
			fichasIzq.get(i).addMouseListener(new MouseClick(i,this, Thread.currentThread()));
			fichasDer.get(i).addMouseListener(new MouseClick(i,this, Thread.currentThread()));
		}
		
		if(cinco){
			for(int i = 0;i < 4;i++){
				fichasEnCasa.get(colorAPos(color)).get(i).addMouseListener(new MouseClick(-1,this, Thread.currentThread()));
			}
		}
		
		while(!Thread.interrupted());
		
		for(int i: posiciones){
			fichas.get(i).removeMouseListener(fichas.get(i).getMouseListeners()[0]);
			fichasIzq.get(i).removeMouseListener(fichasIzq.get(i).getMouseListeners()[0]);
			fichasDer.get(i).removeMouseListener(fichasDer.get(i).getMouseListeners()[0]);
		}
		
		if(cinco){
			for(int i = 0;i < 4;i++){
				fichasEnCasa.get(colorAPos(color)).get(i).removeMouseListener(fichasEnCasa.get(colorAPos(color)).get(i).getMouseListeners()[0]);;
			}
		}
		
		return posicionSeleccionada;
		
	}
}
