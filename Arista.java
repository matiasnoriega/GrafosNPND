package ar.edu.uno.poo2.modelo

import java.util.*;

public class Arista{
	private Vertice origen;
	private Vertice destino;

	public Arista(Vertice origen, Vertice destino){
		setOrigen(origen);
		setDestino(destino);
	}

	//Getters y Setters
	public void setOrigen(Vertice o){
		this.origen=o;
	}
	public Vertice getOrigen(){
		return this.origen;
	}
	public void setDestino(Vertice d){
		this.destino=d;
	}
	public Vertice getDestino(){
		return this.destino;
	}
}