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
 * <p>Aplicación del cliente de la Actividad 2 de PSP de Sockets
 * <p>Presenta al usuario un menú y le solicita que seleccione una opcion
 * entre las mostradas.
 * <p>Dependiendo de la opción seleccionada envía al servidor una cadena de texto diferente
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
			System.out.println("Elija una opción:");
			System.out.println("1 - Consultar libro por ISBN");
			System.out.println("2 - Consultar libro por Titulo");
			System.out.println("3 - Salir de la aplicación");
			
			//Try-Catch para el Scanner
			try {
				opcion=sc.nextInt();
			}
			catch(InputMismatchException e) {
				//Tratamos el error
				opcion = 0; //Inicializamos la opción
			}
			finally {
				sc.nextLine();//Limpiamos el buffer
				//Comprobamos la opcion seleccionada
				switch(opcion) {
					case 1: //Búsqueda por ISBN
						System.out.println("Esciba el ISBN:");
						String isbn = sc.nextLine();
						peticion = String.valueOf(opcion)+isbn; 
						
						enviarPeticion(peticion, IP_SERVER, PUERTO);  
						break;
						
					case 2: //Búsqueda por Título
						System.out.println("Esciba el Título:");
						String titulo = sc.nextLine();
						peticion = String.valueOf(opcion)+titulo; 
						
						enviarPeticion(peticion, IP_SERVER, PUERTO);
						break;
						
					case 3: //Salir
						System.out.println("...Saliendo");
						salir=true;
						break;
						
					default: //Opción no válida
						System.out.println("Opción no válida. Introduzca un número entre 1 y 3");
				}
			}
		}
		sc.close(); //Cerramos el Scanner
	}

	/**
	 * Crea una nueva conexión hacia el servidor en el puerto indicado y le envía el String peticion.
	 * <p>Después se queda a la espera de una respuesta por parte del servidor y cuando llega, la muestra
	 * por pantalla.</p>
	 *  
	 * @param peticion - La cadena que se desea enviar que contiene los datos de la petición
	 * @param servidor - La dirección del servidor  
	 * @param puerto - El número de puerto en el que escucha el servidor
	 */
	public static void enviarPeticion(String peticion, String servidor, int puerto) {
		try (Socket cliente = new Socket();){
			
			InetSocketAddress direccionServidor = new InetSocketAddress(servidor,puerto);
			System.out.println("Esperando a que el servidor acepte la conexión");

			cliente.connect(direccionServidor);
			System.out.println("Comunicación establecida con el servidor");
			
			//InputStream y OutputStream son flujos de bytes
			//Se escriben y se leen bytes -> Hay que transformarlos
			InputStream entrada = cliente.getInputStream();
			OutputStream salida = cliente.getOutputStream();
			
			//Enviamos la petición:
			salida.write(peticion.getBytes());
			
			//Esperamos la respuesta del Server
			byte[] mensaje = new byte[100];
			System.out.println("Esperando respuesta ...... ");
			entrada.read(mensaje);
			
			//Mostramos la respuesta
			System.out.println("Servidor responde: " + new String(mensaje));
			System.out.println("Comunicación cerrada");
		//Excepciones	
		} catch (UnknownHostException e) {
			System.out.println("No se puede establecer comunicación con el servidor");
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de E/S");
			System.out.println(e.getMessage());
		}  
	}
}
