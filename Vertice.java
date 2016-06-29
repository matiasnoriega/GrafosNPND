package ar.edu.uno.poo2.grafos.modelo;

public class Vertice{
	private Integer numero;

	public Vertice(Integer numero){
		setNumero(numero);
	}

	//Setters y Getters
	public void setNumero(Integer num){
		this.numero=num;
	}
	public Integer getNumero(){
		return this.numero;
	}
	
}
