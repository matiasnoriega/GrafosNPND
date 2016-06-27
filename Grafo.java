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
		String prueba = "";
		for(int i=0;i<dimension;i++){
			for(int j=i+1;j<dimension;j++){
				vectorAristasAuxiliar[contador] = new Arista(new Vertice(i), new Vertice(j));
				prueba+=vectorAristasAuxiliar[contador].getOrigen().getNumero()+" "+vectorAristasAuxiliar[contador].getDestino().getNumero()+"\n";
				System.out.println(contador);
				contador++;
			}
		}
		System.out.println(prueba);
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
		System.out.println("Dimension "+dimension);
		//creo un vector de numeros aleatorios del mismo tamaño que el de aristas
		for(int i=0; i<dimension; i++){
			n = r.nextInt(dimension); 
			vectorAuxiliarDesorden[i] = n; 
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
		String prueba ="";
		for(int i=0;i<nuevoTamano;i++){
			vectorTemporal[i]=vectorAuxiliarArista[i];
			prueba+=vectorTemporal[i].getOrigen().getNumero()+" "+vectorTemporal[i].getDestino().getNumero()+"\n";
		}
		System.out.println(prueba);
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
		String prueba ="";
		for(int i=0;i<vectorAristas.length;i++){
			matrizAuxiliar[vectorAristasAuxiliar[i].getOrigen().getNumero()][vectorAristasAuxiliar[i].getDestino().getNumero()] = true;			
		}
		for(int i=0;i<dimension;i++){
			for(int j=i+1;j<dimension;j++){
				prueba+=matrizAuxiliar[i][j]+" ";
			}
			prueba+="\n";
		}
		System.out.println(prueba);
		
		return matrizAuxiliar;
	}

	public Integer acotarCantidadAristas(Integer cantidad, Integer porcentaje){
		return (cantidad*porcentaje)/100;
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
					if(colores[i]>this.cantidadColores)
						this.cantidadColores=colores[i];					
					
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
		String string = this.dimension+" "+this.cantidadColores+" "+getCantidadAristas()+" "+this.porcentaje+"\n";
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
