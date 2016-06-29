package ar.edu.uno.poo2.grafos.modelo;

import java.util.*;

public class Grafo{
	private Integer dimension;
	private Double probabilidad;
	private Integer porcentaje;
	private Integer cantidadAristas;
	private Integer cantidadColores = 0;
	private Boolean[][] matrizAdyacencia;
	private Arista[] vectorAristas;
	private Integer[] vectorColores;

	public Grafo(Integer dimension, Double probabilidad){
		setDimension(dimension);
		setProbabilidad(probabilidad);
		Boolean[][] matrizAuxiliar = generarMatrizAdyacenciaVacia(dimension);
		Random r= new Random();
		for (int i=0; i<dimension; i++){
			matrizAuxiliar[i][i]=false;
			for (int j=i+1; j<dimension; j++){
				if (r.nextDouble()>probabilidad){
					matrizAuxiliar[i][j]=true;
					matrizAuxiliar[j][i]=true;
				}
				else{
					matrizAuxiliar[i][j]=false;
					matrizAuxiliar[j][i]=false;
				}
			}
		}
		setCantidadAristas(contarAristas(matrizAuxiliar));
		setPorcentaje(calcularPorcentaje(getCantidadAristas()));
		setMatrizAdyacencia(matrizAuxiliar);

	}

	public Grafo(Integer dimension, Integer porcentaje){
		setDimension(dimension);
		setPorcentaje(porcentaje);
		setCantidadAristas(dimension*(dimension-1)/2);
		setMatrizAdyacencia(generarMatrizAdyacencia(dimension, acortarVectorAristas(llenarVectorAristas(dimension))));

	}
	
	//Cuenta las aristas recorriendo la mitad superior de la matriz de adyacencia, aumentando el contador cada vez que hay un true
	public Integer contarAristas(Boolean[][] matriz){
		Boolean[][] matrizAuxiliar = matriz;
		Integer contador = 0;
		for(int i=0;i<this.getDimension();i++){
			for(int j=i+1;j<this.getDimension();j++){
				if(matrizAuxiliar[i][j]==true){
					contador++;
				}
			}
		}
		return contador;
	}
	
	//Calcula el porcentaje de adyacencia en funcion de las aristas totales posibles para el grafo, y las que hay realmente.
	public Integer calcularPorcentaje(Integer cantAristas){
		Integer cant = cantAristas;
		Integer total = ((this.getDimension()*(this.getDimension()-1)/2));
		
		return (cant*100)/total;
		
		
	}

	public Boolean[][] generarMatrizAdyacenciaVacia(Integer dimension){
		Boolean[][] matrizAuxiliar = new Boolean[dimension][dimension];
		for(int i=0;i<dimension;i++){
			for(int j=0;j<dimension;j++){
				matrizAuxiliar[i][j]=false;
			}
		}


		return matrizAuxiliar;
	}

	//Metodo que llena originalmente el vector de Aristas con todas las aristas posibles,
	//creando también todos los Vertices posibles dada la dimension del Grafo.

	public Arista[] llenarVectorAristas(Integer dimension){
		Arista[] vectorAristasAuxiliar = new Arista[this.getCantidadAristas()];
		Integer contador=0;
		for(int i=0;i<dimension;i++){
			for(int j=i+1;j<dimension;j++){
				vectorAristasAuxiliar[contador] = new Arista(new Vertice(i), new Vertice(j));
				contador++;
			}
		}

		return vectorAristasAuxiliar;
	}

	//Metodo que toma el vector de Aristas completo y lo acorta dado el porcentaje pasado en el constructor.
	//Crea un vector de numeros aleatorios que mediante el insertion sorting ordena, parallemante ordenando los del vector de Aristas
	//Finalmente crea un vector temporal del tamaño acortado que necesitamos, y llena ese vector con todas las posiciones hasta su tamaño

	public Arista[] acortarVectorAristas(Arista[] vectorAristas){
		int dimension = vectorAristas.length;
		Arista[] vectorAuxiliarArista = vectorAristas;
		//ordeno ese vector de numeros aleatorios paralelamente al de aristas

		vectorAuxiliarArista = mezclar(vectorAuxiliarArista);

		int nuevoTamano = acotarCantidadAristas(dimension, this.getPorcentaje());
		Arista[] vectorTemporal = new Arista[nuevoTamano];
		for(int i=0;i<nuevoTamano;i++){
			vectorTemporal[i]=vectorAuxiliarArista[i];
		}
		setVectorAristas(vectorTemporal);
		return vectorTemporal;		
	}


	//Genera la matriz de adyacencia a partir del vector de Aristas generado previamente.
	//Genera una matriz inicializada toda en false, luego recorre el vector de Aristas
	//Por cada arista, pregunta el numero del vertice origen, y el Destino, y en esa posicion
	//De la matriz de adyacencia coloca un true.

	public Boolean[][] generarMatrizAdyacencia(Integer dimension, Arista[] vectorAristas){
		Boolean[][] matrizAuxiliar = generarMatrizAdyacenciaVacia(dimension);
		Arista[] vectorAristasAuxiliar = vectorAristas;
		int contador= 0;
		for(int i=0;i<vectorAristas.length;i++){
			matrizAuxiliar[vectorAristasAuxiliar[contador].getOrigen().getNumero()][vectorAristasAuxiliar[contador].getDestino().getNumero()] = true;
			matrizAuxiliar[vectorAristasAuxiliar[contador].getDestino().getNumero()][vectorAristasAuxiliar[contador].getOrigen().getNumero()] = true;
			contador++;
		}
		return matrizAuxiliar;
	}

	public Integer acotarCantidadAristas(Integer cantidad, Integer porcentaje){
		return (cantidad*porcentaje)/100;
	}

	public Arista[] mezclar(Arista[] vectorAristas){
		int m = vectorAristas.length-1;
		int alea= 0;
		Arista temp = null;
		for (int i=m;i>1;i--){ 
			alea=(int) Math.floor(i*Math.random()); 
			temp=vectorAristas[i]; 
			vectorAristas[i]=vectorAristas[alea]; 
			vectorAristas[alea]=temp; 
		}
		return(vectorAristas);
	}
	//metodos de coloreo

	public Integer[] coloreoSecuencialAleatorio(){
		Integer colores[]= new Integer[dimension];
		boolean iguales;
		boolean verticeNoColoreado;

		for (int i = 0 ; i < dimension; i++){
			//Al principio le asigno el color más  bajo posible
			// e inicializo las banderas para iniciar la comprobacion 
			// de vertices adyacentes con el mismo color
			colores[i]= 1;
			iguales= false;
			verticeNoColoreado= true;
			while (verticeNoColoreado){
				for (int j = i ; j >=0; j--){
					// Si son adyacentes compruebo los colores y de acuerdo a eso
					// cambio las banderas.
					if (matrizAdyacencia[j][i]){
						if (colores[i] == colores[j]){
							iguales=true;
						}
					}
					if (iguales){
						verticeNoColoreado=true;
						iguales= false;
						colores[i]++;
					}else{
						verticeNoColoreado=false;
					}
					if(colores[i]>getCantidadColores())
						setCantidadColores(colores[i]);					

				}
			}
		}
		this.vectorColores = colores;
		return colores;
	}

	public void coloreoWelshPowell(){
		int grado[]= new int[dimension];
		int contador;
		int aux;
		int k;
		boolean fila[]= new boolean[dimension];
		Grafo grafoAux= this;
		for (int i=0; i< dimension; i++){
			contador=0;
			for (int j=i+1; j < dimension; j++){
				if (matrizAdyacencia[i][j])
					contador++;
			}
			grado[i]=contador;
		}
		for (int i=1; i < dimension; i++){
			aux=grado[i];
			for (int l=0; l< dimension; l++)
				fila[i]=grafoAux.matrizAdyacencia[i][l];
			k= i-1;
			while ((k>=0) && (aux< grado[k])){
				grado[k + 1]= grado[k];
				for (int l=0; l< dimension; l++)
					fila[k+1]=grafoAux.matrizAdyacencia[k][l];
				k--;
			}
			grado[k+1]= aux;
			for (int l=0; l< dimension; l++)
				grafoAux.matrizAdyacencia[k+1][l]=fila[i];
		}
		this.vectorColores =  grafoAux.coloreoSecuencialAleatorio();
	}

	public void coloreoMatula(){

		int grado[]= new int[dimension];
		int contador;
		int aux;
		int k;
		boolean fila[]= new boolean[dimension];
		Grafo grafoAux= this;
		for (int i=0; i< dimension; i++){
			contador=0;
			for (int j=i+1; j < dimension; j++){
				if (matrizAdyacencia[i][j])
					contador++;
			}
			grado[i]=contador;
		}
		for (int i=1; i < dimension; i++){
			aux=grado[i];
			for (int l=0; l< dimension; l++)
				fila[i]=grafoAux.matrizAdyacencia[i][l];
			k= i-1;
			while ((k>=0) && (aux> grado[k])){
				grado[k + 1]= grado[k];
				for (int l=0; l< dimension; l++)
					fila[k+1]=grafoAux.matrizAdyacencia[k][l];
				k--;
			}
			grado[k+1]= aux;
			for (int l=0; l< dimension; l++)
				grafoAux.matrizAdyacencia[k+1][l]=fila[i];
		}
		this.vectorColores = grafoAux.coloreoSecuencialAleatorio();
	}


	public String toString(){
		String string = "Dimension: "+this.dimension+"\nCantidad de Colores: "+this.cantidadColores+"\nCantidad de Aristas: "+getCantidadAristas()+"\nPorcentaje: "+this.porcentaje+"%\n\n\n";
		for(int i =0; i<this.getDimension();i++){
			string += "Nodo: "+i+" Color: "+this.vectorColores[i]+"\n";

		}
		return string;
	}



	//Getters y Setters

	public void setDimension(Integer dim){
		this.dimension=dim;
	}
	public Integer getDimension(){
		return this.dimension;
	}
	public void setProbabilidad(Double prob){
		this.probabilidad=prob;
	}
	public Double getProbabilidad(){
		return this.probabilidad;
	}
	public void setCantidadAristas(Integer cantidad){
		this.cantidadAristas=(cantidad);
	}
	public Integer getCantidadAristas(){
		return this.cantidadAristas;
	}
	public void setPorcentaje(Integer porc){
		this.porcentaje=porc;
	}
	public Integer getPorcentaje(){
		return this.porcentaje;
	}
	public void setMatrizAdyacencia(Boolean[][] matriz){
		this.matrizAdyacencia=matriz;
	}
	public Boolean[][] getMatrizAdyacencia(){
		return this.matrizAdyacencia;
	}
	public void setVectorAristas(Arista[] vector){
		this.vectorAristas=vector;
	}
	public Arista[] getVectorAristas(){
		return this.vectorAristas;
	}
	public Integer getCantidadColores() {
		return cantidadColores;
	}
	public void setCantidadColores(Integer cantidadColores) {
		this.cantidadColores = cantidadColores;
	}
	

}package ar.edu.uno.poo2.grafos.modelo;

import java.util.*;

public class Grafo{
	private Integer dimension;
	private Double probabilidad;
	private Integer porcentaje;
	private Integer cantidadAristas;
	private Integer cantidadColores = 0;
	private Boolean[][] matrizAdyacencia;
	private Arista[] vectorAristas;
	private Integer[] vectorColores;

	public Grafo(Integer dimension, Double probabilidad){
		setDimension(dimension);
		setProbabilidad(probabilidad);
		Boolean[][] matrizAuxiliar = generarMatrizAdyacenciaVacia(dimension);
		Random r= new Random();
		for (int i=0; i<dimension; i++){
			matrizAuxiliar[i][i]=false;
			for (int j=i+1; j<dimension; j++){
				if (r.nextDouble()>probabilidad){
					matrizAuxiliar[i][j]=true;
					matrizAuxiliar[j][i]=true;
				}
				else{
					matrizAuxiliar[i][j]=false;
					matrizAuxiliar[j][i]=false;
				}
			}
		}
		setCantidadAristas(contarAristas(matrizAuxiliar));
		setPorcentaje(calcularPorcentaje(getCantidadAristas()));
		setMatrizAdyacencia(matrizAuxiliar);

	}

	public Grafo(Integer dimension, Integer porcentaje){
		setDimension(dimension);
		setPorcentaje(porcentaje);
		setCantidadAristas(dimension*(dimension-1)/2);
		setMatrizAdyacencia(generarMatrizAdyacencia(dimension, acortarVectorAristas(llenarVectorAristas(dimension))));

	}
	
	//Cuenta las aristas recorriendo la mitad superior de la matriz de adyacencia, aumentando el contador cada vez que hay un true
	public Integer contarAristas(Boolean[][] matriz){
		Boolean[][] matrizAuxiliar = matriz;
		Integer contador = 0;
		for(int i=0;i<this.getDimension();i++){
			for(int j=i+1;j<this.getDimension();j++){
				if(matrizAuxiliar[i][j]==true){
					contador++;
				}
			}
		}
		return contador;
	}
	
	//Calcula el porcentaje de adyacencia en funcion de las aristas totales posibles para el grafo, y las que hay realmente.
	public Integer calcularPorcentaje(Integer cantAristas){
		Integer cant = cantAristas;
		Integer total = ((this.getDimension()*(this.getDimension()-1)/2));
		
		return (cant*100)/total;
		
		
	}

	public Boolean[][] generarMatrizAdyacenciaVacia(Integer dimension){
		Boolean[][] matrizAuxiliar = new Boolean[dimension][dimension];
		for(int i=0;i<dimension;i++){
			for(int j=0;j<dimension;j++){
				matrizAuxiliar[i][j]=false;
			}
		}


		return matrizAuxiliar;
	}

	//Metodo que llena originalmente el vector de Aristas con todas las aristas posibles,
	//creando también todos los Vertices posibles dada la dimension del Grafo.

	public Arista[] llenarVectorAristas(Integer dimension){
		Arista[] vectorAristasAuxiliar = new Arista[this.getCantidadAristas()];
		Integer contador=0;
		for(int i=0;i<dimension;i++){
			for(int j=i+1;j<dimension;j++){
				vectorAristasAuxiliar[contador] = new Arista(new Vertice(i), new Vertice(j));
				contador++;
			}
		}

		return vectorAristasAuxiliar;
	}

	//Metodo que toma el vector de Aristas completo y lo acorta dado el porcentaje pasado en el constructor.
	//Crea un vector de numeros aleatorios que mediante el insertion sorting ordena, parallemante ordenando los del vector de Aristas
	//Finalmente crea un vector temporal del tamaño acortado que necesitamos, y llena ese vector con todas las posiciones hasta su tamaño

	public Arista[] acortarVectorAristas(Arista[] vectorAristas){
		int dimension = vectorAristas.length;
		Arista[] vectorAuxiliarArista = vectorAristas;
		//ordeno ese vector de numeros aleatorios paralelamente al de aristas

		vectorAuxiliarArista = mezclar(vectorAuxiliarArista);

		int nuevoTamano = acotarCantidadAristas(dimension, this.getPorcentaje());
		Arista[] vectorTemporal = new Arista[nuevoTamano];
		for(int i=0;i<nuevoTamano;i++){
			vectorTemporal[i]=vectorAuxiliarArista[i];
		}
		setVectorAristas(vectorTemporal);
		return vectorTemporal;		
	}


	//Genera la matriz de adyacencia a partir del vector de Aristas generado previamente.
	//Genera una matriz inicializada toda en false, luego recorre el vector de Aristas
	//Por cada arista, pregunta el numero del vertice origen, y el Destino, y en esa posicion
	//De la matriz de adyacencia coloca un true.

	public Boolean[][] generarMatrizAdyacencia(Integer dimension, Arista[] vectorAristas){
		Boolean[][] matrizAuxiliar = generarMatrizAdyacenciaVacia(dimension);
		Arista[] vectorAristasAuxiliar = vectorAristas;
		int contador= 0;
		for(int i=0;i<vectorAristas.length;i++){
			matrizAuxiliar[vectorAristasAuxiliar[contador].getOrigen().getNumero()][vectorAristasAuxiliar[contador].getDestino().getNumero()] = true;
			matrizAuxiliar[vectorAristasAuxiliar[contador].getDestino().getNumero()][vectorAristasAuxiliar[contador].getOrigen().getNumero()] = true;
			contador++;
		}
		return matrizAuxiliar;
	}

	public Integer acotarCantidadAristas(Integer cantidad, Integer porcentaje){
		return (cantidad*porcentaje)/100;
	}

	public Arista[] mezclar(Arista[] vectorAristas){
		int m = vectorAristas.length-1;
		int alea= 0;
		Arista temp = null;
		for (int i=m;i>1;i--){ 
			alea=(int) Math.floor(i*Math.random()); 
			temp=vectorAristas[i]; 
			vectorAristas[i]=vectorAristas[alea]; 
			vectorAristas[alea]=temp; 
		}
		return(vectorAristas);
	}
	//metodos de coloreo

	public Integer[] coloreoSecuencialAleatorio(){
		Integer colores[]= new Integer[dimension];
		boolean iguales;
		boolean verticeNoColoreado;

		for (int i = 0 ; i < dimension; i++){
			//Al principio le asigno el color más  bajo posible
			// e inicializo las banderas para iniciar la comprobacion 
			// de vertices adyacentes con el mismo color
			colores[i]= 1;
			iguales= false;
			verticeNoColoreado= true;
			while (verticeNoColoreado){
				for (int j = i ; j >=0; j--){
					// Si son adyacentes compruebo los colores y de acuerdo a eso
					// cambio las banderas.
					if (matrizAdyacencia[j][i]){
						if (colores[i] == colores[j]){
							iguales=true;
						}
					}
					if (iguales){
						verticeNoColoreado=true;
						iguales= false;
						colores[i]++;
					}else{
						verticeNoColoreado=false;
					}
					if(colores[i]>getCantidadColores())
						setCantidadColores(colores[i]);					

				}
			}
		}
		this.vectorColores = colores;
		return colores;
	}

	public void coloreoWelshPowell(){
		int grado[]= new int[dimension];
		int contador;
		int aux;
		int k;
		boolean fila[]= new boolean[dimension];
		Grafo grafoAux= this;
		for (int i=0; i< dimension; i++){
			contador=0;
			for (int j=i+1; j < dimension; j++){
				if (matrizAdyacencia[i][j])
					contador++;
			}
			grado[i]=contador;
		}
		for (int i=1; i < dimension; i++){
			aux=grado[i];
			for (int l=0; l< dimension; l++)
				fila[i]=grafoAux.matrizAdyacencia[i][l];
			k= i-1;
			while ((k>=0) && (aux< grado[k])){
				grado[k + 1]= grado[k];
				for (int l=0; l< dimension; l++)
					fila[k+1]=grafoAux.matrizAdyacencia[k][l];
				k--;
			}
			grado[k+1]= aux;
			for (int l=0; l< dimension; l++)
				grafoAux.matrizAdyacencia[k+1][l]=fila[i];
		}
		this.vectorColores =  grafoAux.coloreoSecuencialAleatorio();
	}

	public void coloreoMatula(){

		int grado[]= new int[dimension];
		int contador;
		int aux;
		int k;
		boolean fila[]= new boolean[dimension];
		Grafo grafoAux= this;
		for (int i=0; i< dimension; i++){
			contador=0;
			for (int j=i+1; j < dimension; j++){
				if (matrizAdyacencia[i][j])
					contador++;
			}
			grado[i]=contador;
		}
		for (int i=1; i < dimension; i++){
			aux=grado[i];
			for (int l=0; l< dimension; l++)
				fila[i]=grafoAux.matrizAdyacencia[i][l];
			k= i-1;
			while ((k>=0) && (aux> grado[k])){
				grado[k + 1]= grado[k];
				for (int l=0; l< dimension; l++)
					fila[k+1]=grafoAux.matrizAdyacencia[k][l];
				k--;
			}
			grado[k+1]= aux;
			for (int l=0; l< dimension; l++)
				grafoAux.matrizAdyacencia[k+1][l]=fila[i];
		}
		this.vectorColores = grafoAux.coloreoSecuencialAleatorio();
	}


	public String toString(){
		String string = "Dimension: "+this.dimension+"\nCantidad de Colores: "+this.cantidadColores+"\nCantidad de Aristas: "+getCantidadAristas()+"\nPorcentaje: "+this.porcentaje+"%\n\n\n";
		for(int i =0; i<this.getDimension();i++){
			string += "Nodo: "+i+" Color: "+this.vectorColores[i]+"\n";

		}
		return string;
	}



	//Getters y Setters

	public void setDimension(Integer dim){
		this.dimension=dim;
	}
	public Integer getDimension(){
		return this.dimension;
	}
	public void setProbabilidad(Double prob){
		this.probabilidad=prob;
	}
	public Double getProbabilidad(){
		return this.probabilidad;
	}
	public void setCantidadAristas(Integer cantidad){
		this.cantidadAristas=(cantidad);
	}
	public Integer getCantidadAristas(){
		return this.cantidadAristas;
	}
	public void setPorcentaje(Integer porc){
		this.porcentaje=porc;
	}
	public Integer getPorcentaje(){
		return this.porcentaje;
	}
	public void setMatrizAdyacencia(Boolean[][] matriz){
		this.matrizAdyacencia=matriz;
	}
	public Boolean[][] getMatrizAdyacencia(){
		return this.matrizAdyacencia;
	}
	public void setVectorAristas(Arista[] vector){
		this.vectorAristas=vector;
	}
	public Arista[] getVectorAristas(){
		return this.vectorAristas;
	}
	public Integer getCantidadColores() {
		return cantidadColores;
	}
	public void setCantidadColores(Integer cantidadColores) {
		this.cantidadColores = cantidadColores;
	}
	

}
