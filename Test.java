package ar.edu.uno.poo2.grafos.modelo;

import java.io.*;
import java.util.*;

public class Test {

	//metodo que me dice si lo que esta leyendo del archivo .in es un int o no

	public static boolean esInteger(String cadena){
		try{
			Integer.parseInt(cadena);
			return true;
		}
		catch(NumberFormatException nfe){
			return false;
		}
	}

	public static String analisisEstadistico(Integer dimension, Integer porcentaje){
		String string = "\n-------------------------------------------------\nAnalisis Estadistico para 1000 Grafos\n-------------------------------------------------\n";
	
		
		int[] vectorContadores = new int[20];
		int contadorAuxiliar = 0;
		for(int i=0;i<1000;i++){
			Grafo grafo = new Grafo(dimension, porcentaje);
			grafo.coloreoSecuencialAleatorio();
			switch(grafo.getCantidadColores()){
			case 1:
				vectorContadores[1]++;
			case 2:
				vectorContadores[2]++;
			case 3:
				vectorContadores[3]++;
			case 4:
				vectorContadores[4]++;
			case 5:
				vectorContadores[5]++;
			case 6:
				vectorContadores[6]++;
			case 7:
				vectorContadores[7]++;
			case 8:
				vectorContadores[8]++;
			case 9:
				vectorContadores[9]++;
			case 10:
				vectorContadores[10]++;

			}

		}

		double porcentajeMinimo = 0.00;

		
		while(vectorContadores[contadorAuxiliar]==0){
			contadorAuxiliar++;	
		}

		porcentajeMinimo= ((double)vectorContadores[contadorAuxiliar]*100.00)/1000.00;
		
		string+="La menor cantidad de colores para el análisis fue de \n"+contadorAuxiliar+" en "+vectorContadores[contadorAuxiliar]+" grafos del total de 1000";
		string+="\nSiendo la menor cantidad con un "+porcentajeMinimo+"% del total.\n\n";
	

		string+="-------------------------------------------------\n";
		return string;

	}

	public static void main(String[] args) {
		Grafo grafo=null;
		File file= new File("grafo.in");
		FileReader fr = null;
		BufferedReader br;


		try{
			fr= new FileReader(file);
			br= new BufferedReader(fr);
			StringTokenizer linea= new StringTokenizer(br.readLine());
			int vertices= Integer.parseInt(linea.nextToken());
			String lineaTest = linea.nextToken();
			if(esInteger(lineaTest)==true){
				int porcentajeAdyacencia= Integer.parseInt(lineaTest);
				grafo= new Grafo(vertices, porcentajeAdyacencia);
			}
			else{
				double probabilidad= Double.parseDouble(lineaTest);
				grafo= new Grafo(vertices, probabilidad);
			}
		}catch (FileNotFoundException e){
			System.out.println(file.getAbsolutePath());
			System.out.println("Error Al abrir el archivo");
		}catch (Exception e1){
			System.out.println(e1.getStackTrace());
		}
		finally{try{                    
			if( null != fr ){   
				fr.close();     
			}                  
		}catch (Exception e2){ 
			System.out.println(e2.getStackTrace());
		}	
		}

		System.out.println(analisisEstadistico(600, 75));
		grafo.coloreoSecuencialAleatorio();
		FileWriter salida= null;
		PrintWriter pw = null;
		try{
			salida= new FileWriter("grafo.out");
			pw= new PrintWriter(salida);
			pw.println(grafo.toString());
		}catch(Exception e){
			System.out.println(e.getStackTrace());
		}finally{
			if (null!=pw)
				pw.close();
		}
	}

}
