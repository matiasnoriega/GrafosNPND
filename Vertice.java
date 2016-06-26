package ar.edu.uno.poo2.grafos.modelo;

import java.util.*;

public class Vertice{
	private Integer numero;
	private Arista[] aristasSalientes;
	private Integer grado;

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
