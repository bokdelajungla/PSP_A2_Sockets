package requerimiento2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Aplicaci�n del cliente de la Actividad 2 de PSP de Sockets
 * Presenta al usuario un men� y le solicita que seleccione una opcion
 * entre las mostradas.
 * 
 * @author Jorge SAlor
 *
 */
public class ClienteLibros2 {
	
	public static void main (String[] args) {
		
		//Variables
		Scanner sc = new Scanner(System.in);
		boolean salir = false;
		int opcion = 0;
		String peticion = new String();
		
		//Bucle principal de funcionamiento
		System.out.println("Bienvenido a la Biblioteca:");
		while(!salir){
			System.out.println("Elija una opci�n:");
			System.out.println("1 - Consultar libro por ISBN");
			System.out.println("2 - Consultar libro por Titulo");
			System.out.println("3 - Consultar libro por Autor");
			System.out.println("4 - Salir de la aplicaci�n");
			
			//Try-Catch para el Scanner
			try {
				opcion=sc.nextInt();
			}
			catch(InputMismatchException e) {
				//Tratamos el error
				opcion = 0; //Inicializamos la opci�n
				sc.next(); //Para limpiar el buffer
			}
			finally {
				//Comprobamos la opcion seleccionada
				switch(opcion) {
					case 1: //B�squeda por ISBN
						System.out.println("Esciba el ISBN:");
						String isbn = sc.next();
						peticion = String.valueOf(opcion)+isbn; 
						
						enviarPeticion(peticion,"localhost",9999);  
						break;
						
					case 2: //B�squeda por T�tulo
						System.out.println("Esciba el T�tulo:");
						String titulo = sc.next();
						peticion = String.valueOf(opcion)+titulo; 
						
						enviarPeticion(peticion,"localhost",9999);
						break;
						
					case 3:
						System.out.println("Esciba el Autor:");
						String autor = sc.next();
						peticion = String.valueOf(opcion)+autor; 
						
						enviarPeticion(peticion,"localhost",9999);
						break;
						
					case 4:
						System.out.println("...Saliendo");
						salir=true;
						break;
						
					default: //Opci�n no v�lida
						System.out.println("Opci�n no v�lida. Introduzca un n�mero entre 1 y 3");
				}
			}
		}
		sc.close(); //Cerramos el Scanner
	}

	/**
	 * Crea una nueva conexi�n hacia el servidor en el puerto indicado y le env�a el String peticion.
	 * <p>Despu�s se queda a la espera de las respuestas por parte del servidor hasta que recive un mensaje 
	 * con la palabra FIN, en ese momento muestra lo que ha recibido por pantalla y cierra la conexi�n</p>
	 *  
	 * @param peticion - La cadena que se desea enviar que contiene los datos de la petici�n
	 * @param servidor - La direcci�n del servidor  
	 * @param puerto - El n�mero de puerto en el que escucha el servidor
	 */
	public static void enviarPeticion(String peticion, String servidor, int puerto) {
		try {
			Socket cliente = new Socket();
			InetSocketAddress direccionServidor = new InetSocketAddress(servidor,puerto);
			System.out.println("Esperando a que el servidor acepte la conexi�n");

			cliente.connect(direccionServidor);
			System.out.println("Comunicaci�n establecida con el servidor");
			
			/*
			 * Aunque PrintStream e InputStreamReader + BufferedReader pueden simplificar
			 * el proceso de leer-escribir cadenas de texto en un flujo de un socket, 
			 * en nuestro caso hacen que el programa no funcione, debido a c�mo se defini�
			 * el m�todo Libro.toString que hace uso de \n para dar formato.
			 * En vez de usar InputStream y OutputStream como en el requerimiento1
			 * se van a utilizar
			 * Se deja a continuaci�n el c�digo como referencia para el estudio:
			 * 
			 * 	//Creamos el objeto que nos permite mandar informacion al servidor
			 *	PrintStream salida = new PrintStream(cliente.getOutputStream());
			 *	//Creamos el objeto que nos va a permitir leer la salida del servidor
			 *	InputStreamReader entrada = new InputStreamReader(cliente.getInputStream());
			 *	//Esta clase nos ayuda a leer datos del servidor linea a linea en vez de 
			 *	//caracter a caracter como la clase InputStreamReader
			 *	BufferedReader bf = new BufferedReader(entrada);
			 *  //Enviamos
			 *  salida.println(peticion);
			 *  //Leemos
			 *  String mensaje = bf.readLine();
			 *  
			 */ 
			InputStream entrada = cliente.getInputStream();
			OutputStream salida = cliente.getOutputStream();
			
			//Enviamos la petici�n:
			salida.write(peticion.getBytes());
						
			//Esperamos la respuesta del Server
			System.out.println("Esperando respuesta ...... ");
			String respuesta = new String();
			boolean continua = true;
			while(continua) {
				byte[] mensaje = new byte[100];
				entrada.read(mensaje);
				String aux = new String(mensaje).trim();
				if(aux.equals("FIN")) {
					continua = false;
				}
				else {
					respuesta = respuesta.concat("\n"+aux);
				}
			}
			System.out.println("Servidor responde: " + respuesta);

			entrada.close();
			salida.close();
			cliente.close();
			
			System.out.println("Comunicaci�n cerrada");
			
		} catch (UnknownHostException e) {
			System.out.println("No se puede establecer comunicaci�n con el servidor");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de E/S");
			System.out.println(e.getMessage());
		}  
	}
}
