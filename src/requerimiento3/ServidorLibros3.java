package requerimiento3;

import java.util.HashSet;
import java.net.ServerSocket;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <p>Clase que implementa un servidor concurrente cuya direccion es {@link #IP_SERVER} y acepta peticiones de
 * los clientes en el puerto {@link #PUERTO}
 * <p>Cada vez que recibe una petición de conexión crea un hilo que se encargará de comunicarse con el
 * cliente y vuelve a esperar conexiones de nuevos clientes.
 * 
 * @author Adrián, Antonio, Jorge.
 *
 */

public class ServidorLibros3 {
	
	public static void main(String[] args) {
		
		//Creamos la coleccion
		//Usamos un HashSet para controlar la unicidad de los elementos
		HashSet<Libro> Biblioteca = new HashSet<Libro>();
		//Creamos 5 Libros:
		//El ISBN debe cumplir unas reglas, como empezar por 978 o 979, tener varios
		//grupos de digitos para grupo de registro, titular, publicacion y un digito de control
		//pero para nuestro ejemplo vamos a obviarlo.
		Libro libro001 = new Libro("978001", "Titulo1", "Autor1", "11");
		Libro libro002 = new Libro("978002", "Titulo2", "Autor2", "12");
		Libro libro003 = new Libro("978003", "Titulo3", "Autor2", "13");
		Libro libro004 = new Libro("978004", "Titulo4", "Autor4", "14");
		Libro libro005 = new Libro("978005", "Titulo5", "Autor5", "15");
		//Añadimos los libros a la coleccion
		Biblioteca.add(libro001);
		Biblioteca.add(libro002);
		Biblioteca.add(libro003);
		Biblioteca.add(libro004);
		Biblioteca.add(libro005);
		
		//Creamos el socket de escucha:
		try (ServerSocket servidor = new ServerSocket();){
			System.out.println("Iniciando el Servidor...");
			InetSocketAddress direccion = new InetSocketAddress("localhost",9999);
			servidor.bind(direccion);
			
			//Bucle de escucha:
			while(true) {
				System.out.println("Esperando peticiones entrantes");
				Socket socketEscucha = servidor.accept();
				System.out.println("Peticion de cliente recibida. Lanzando Hilo");
				new HiloBuscar3(socketEscucha, Biblioteca);
			}
			
		// Excepción de E/S	
		}catch (IOException e) {
			System.out.println("SERVIDOR: Error de E/S");
			e.printStackTrace();  

		// Captura de otros tipos de excepciones no especificadas
		} catch (Exception e) {
			System.out.println("SERVIDOR: Error -> " + e);
			e.printStackTrace();
		}
	}
}
