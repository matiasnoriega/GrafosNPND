	package ar.edu.uno.poo2.modelo

	import java.util.*;

	public class Grafo{
		private Integer dimension;
		private Integer probabilidad;
		private Integer porcentaje;
		private Integer cantidadAristas;
		private Integer cantidadColores = 0;
		private Boolean[][] matrizAdyacencia;
		private Vertice[] vectorVertices;
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
						matrizAuxiliar[j][i]=true;
					}
					else{
						matrizAuxiliar[i][j]=false;
						matrizAuxiliar[j][i]=false;
					}
				}
			}
			this.matrizAdyacencia = matrizAuxiliar;

		}

		public Grafo(Integer dimension, Integer porcentaje){
			setDimension(dimension);
			setPorcentaje(porcentaje);
			setCantidadAristas(dimension);

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
		public Integer acotarCantidadAristas(Integer cantidad, Integer porcentaje){
			 return (this.cantidadAristas*porcentaje)/100;

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

	}