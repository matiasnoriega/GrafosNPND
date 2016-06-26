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

		Boolean[][] matrizAuxiliar = new Boolean[dimension][dimension];
		Random r= new Random();
		for (int i=0; i<dimension; i++){
			matrizAuxiliar[i][i]=false;
			for (int j=i+1; j<dimension; j++){
				if (r.nextDouble()>probabilidad){
					matrizAuxiliar[i][j]=true;
					//matrizAuxiliar[j][i]=true;
				}
				else{
					matrizAuxiliar[i][j]=false;
					//matrizAuxiliar[j][i]=false;
				}
			}
		}
		this.matrizAdyacencia = matrizAuxiliar;

	}

	public Grafo(Integer dimension, Integer porcentaje){
		setDimension(dimension);
		setPorcentaje(porcentaje);
		setCantidadAristas(dimension);
		setMatrizAdyacencia(generarMatrizAdyacencia(dimension, acortarVectorAristas(llenarVectorAristas(dimension))));

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
		Integer[] vectorAuxiliarDesorden = new Integer[dimension];    
		Integer n =0;      
		Random r = new Random();

		//creo un vector de numeros aleatorios del mismo tamaño que el de aristas
		for(int i=0; i<dimension; i++){
			n = r.nextInt(dimension+1); 
			vectorAuxiliarDesorden[i] = n; 
			System.out.println(vectorAuxiliarDesorden[i]);
		}
		//ordeno ese vector de numeros aleatorios paralelamente al de aristas
		for (int i=1; i<dimension; i++){
			int index = vectorAuxiliarDesorden[i];
			int j = i;

			while (j > 0 && vectorAuxiliarDesorden[j-1] > index){
				vectorAuxiliarDesorden[j] = vectorAuxiliarDesorden[j-1];
				vectorAuxiliarArista[j] = vectorAuxiliarArista[j-1];
				j--;
			}
			vectorAuxiliarDesorden[j] = index;
		}

		Integer nuevoTamano = acotarCantidadAristas(dimension, this.getPorcentaje());
		Arista[] vectorTemporal = new Arista[nuevoTamano];
		for(int i=0;i<nuevoTamano;i++){
			vectorTemporal[i]=vectorAuxiliarArista[i];
		}
		
		return vectorTemporal;		
	}


	//Genera la matriz de adyacencia a partir del vector de Aristas generado previamente.
	//Genera una matriz inicializada toda en false, luego recorre el vector de Aristas
	//Por cada arista, pregunta el numero del vertice origen, y el Destino, y en esa posicion
	//De la matriz de adyacencia coloca un true.

	public Boolean[][] generarMatrizAdyacencia(Integer dimension, Arista[] vectorAristas){
		Boolean[][] matrizAuxiliar = generarMatrizAdyacenciaVacia(dimension);
		Arista[] vectorAristasAuxiliar = vectorAristas;
		for(int i=0;i<vectorAristas.length;i++){
			matrizAuxiliar[vectorAristasAuxiliar[i].getOrigen().getNumero()][vectorAristasAuxiliar[i].getDestino().getNumero()] = true;			
		}
		
		return matrizAuxiliar;
	}

	public Integer acotarCantidadAristas(Integer cantidad, Integer porcentaje){
		return (cantidad*porcentaje)/100;
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
		this.cantidadAristas=(cantidad*(cantidad-1)/2);
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

}
