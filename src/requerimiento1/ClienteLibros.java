package requerimiento1;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * <p>Aplicaci�n del cliente de la Actividad 2 de PSP de Sockets
 * <p>Presenta al usuario un men� y le solicita que seleccione una opcion
 * entre las mostradas.
 * <p>Dependiendo de la opci�n seleccionada env�a al servidor una cadena de texto diferente
 * 
 * @author Adrian. Antonio, Jorge.
 *
 */
public class ClienteLibros {
	
	public static final int PUERTO = 9999;
	public static final String IP_SERVER = "localhost";
	
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
			System.out.println("3 - Salir de la aplicaci�n");
			
			//Try-Catch para el Scanner
			try {
				opcion=sc.nextInt();
			}
			catch(InputMismatchException e) {
				//Tratamos el error
				opcion = 0; //Inicializamos la opci�n
			}
			finally {
				sc.nextLine();//Limpiamos el buffer
				//Comprobamos la opcion seleccionada
				switch(opcion) {
					case 1: //B�squeda por ISBN
						System.out.println("Esciba el ISBN:");
						String isbn = sc.nextLine();
						peticion = String.valueOf(opcion)+isbn; 
						
						enviarPeticion(peticion, IP_SERVER, PUERTO);  
						break;
						
					case 2: //B�squeda por T�tulo
						System.out.println("Esciba el T�tulo:");
						String titulo = sc.nextLine();
						peticion = String.valueOf(opcion)+titulo; 
						
						enviarPeticion(peticion, IP_SERVER, PUERTO);
						break;
						
					case 3: //Salir
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
	 * <p>Despu�s se queda a la espera de una respuesta por parte del servidor y cuando llega, la muestra
	 * por pantalla.</p>
	 *  
	 * @param peticion - La cadena que se desea enviar que contiene los datos de la petici�n
	 * @param servidor - La direcci�n del servidor  
	 * @param puerto - El n�mero de puerto en el que escucha el servidor
	 */
	public static void enviarPeticion(String peticion, String servidor, int puerto) {
		try (Socket cliente = new Socket();){
			
			InetSocketAddress direccionServidor = new InetSocketAddress(servidor,puerto);
			System.out.println("Esperando a que el servidor acepte la conexi�n");

			cliente.connect(direccionServidor);
			System.out.println("Comunicaci�n establecida con el servidor");
			
			//InputStream y OutputStream son flujos de bytes
			//Se escriben y se leen bytes -> Hay que transformarlos
			InputStream entrada = cliente.getInputStream();
			OutputStream salida = cliente.getOutputStream();
			
			//Enviamos la petici�n:
			salida.write(peticion.getBytes());
			
			//Esperamos la respuesta del Server
			byte[] mensaje = new byte[100];
			System.out.println("Esperando respuesta ...... ");
			entrada.read(mensaje);
			
			//Mostramos la respuesta
			System.out.println("Servidor responde: " + new String(mensaje));
			System.out.println("Comunicaci�n cerrada");
		//Excepciones	
		} catch (UnknownHostException e) {
			System.out.println("No se puede establecer comunicaci�n con el servidor");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de E/S");
			System.out.println(e.getMessage());
		}  
	}
}
