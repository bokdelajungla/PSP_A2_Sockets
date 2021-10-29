package requerimiento3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import entidad.Libro;

public class ServidorHilo {
	
	public static final int PUERTO = 2018;
	public static final String IP_SERVER = "localhost";
	static ArrayList<Libro> libros = new ArrayList();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("      APLICACIÓN DE SERVIDOR CON HILOS     ");
		System.out.println("-------------------------------------------");
		Libro libro1 = new Libro("12345", "bayana", "elsa", 13.20);
		Libro libro2 = new Libro("54321", "frozen", "elsa", 13.00);
		Libro libro3 = new Libro("678910", "programacion", "alberto", 1.20);
		Libro libro4 = new Libro("109876", "nintendo", "joaquin", 3.20);
		Libro libro5 = new Libro("1112131415", "bayana", "rowling", 10.20);
		int peticion = 0;
		
		try (ServerSocket servidor = new ServerSocket()){	
			libros.add(libro1);
			libros.add(libro2);
			libros.add(libro3);
			libros.add(libro4);
			libros.add(libro5);
			InetSocketAddress direccion = new InetSocketAddress(IP_SERVER,PUERTO);
			servidor.bind(direccion);

			System.out.println("SERVIDOR: Esperando peticion por el puerto " + PUERTO);
			
			while (true) {
				Socket socketAlCliente = servidor.accept();
				System.out.println("SERVIDOR: peticion numero " + ++peticion + " recibida");
				//Abrimos un hilo nuevo y liberamos el hilo principal para que pueda
				//recibir peticiones de otros clientes
				new HiloLibros(socketAlCliente, libros);
			}			
		} catch (IOException e) {
			System.err.println("SERVIDOR: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("SERVIDOR: Error");
			e.printStackTrace();
		}
	}

}
