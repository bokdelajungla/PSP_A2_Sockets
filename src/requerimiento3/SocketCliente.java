package requerimiento3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Interfaz del cliente de la aplicación
 * 
 * @author Adrian Rodriguez
 * @version 1.0
 *
 */
public class SocketCliente {
	
	/**
	 * Puerto de conexión
	 */
	public static final int PUERTO = 2017;
	/**
	 * Dir IP del servidor
	 */
	public static final String IP_SERVER = "localhost";
	
	/**
	 * hilo principal de ejecución
	 * @param args argumentos de entrada por linea de comandos
	 */
	public static void main(String[] args) {
			
		/**
		 * objeto que encapsula la Dir_IP y el puerto
		 */
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);
	
		
		// try with resources (de esta forma no es necesario cerrar los flujos o streams tras su uso)
		try(Scanner sc = new Scanner(System.in);) {
			
			/**
			 * opción del menu de la aplicación
			 */
			int opc = 0;
			/**
			 * Datos libro recopilados por el interface cliente
			 */
			String info = "";
			/**
			 * Opcion de confirmacion de salida de la aplicacion
			 */
			String opcSalir = "";
			/**
			 * Variable de escape del bucle de la aplicacion
			 */
			boolean salir = false;
			
			System.out.println("      APLICACION CLIENTE      ");
			System.out.println("------------------------------");
			do {
				// Imprimimos el menú
				System.out.println("\n**********  MENU  **********");
				System.out.println("1 - Consultar libro por ISBN");
				System.out.println("2 - Consultar libro por título");
				System.out.println("3 - Consultar libro por autor");
				System.out.println("4 - Añadir libro");
				System.out.println("5 - Salir");
				System.out.println("****************************");
				System.out.print("\nIntroduzca una opcion: ");
				
				// leemos la opción (envueldo en un try-catch para que el programa siga ejecutándose)
				try {
					opc = sc.nextInt();
				} catch (InputMismatchException e) {
					System.out.println("CLIENTE: Se debe introducir un número");
					// si la opcion introducida no es número damos por defecto un 0 (opcion no valida) para que vuelva al menú
					opc = 0;
				} 
				// Limpiamos el buffer de entrada tras haber leído un entero
				sc.nextLine();
				
				/**
				 * Datos enviados por el cliente
				 */
				String data = "";
				
				// opción de consulta de libros por ISBN
				if(opc == 1) {
					System.out.println("****** Consulta de libros por ISBN *****");
					// pedimos el ISBN por pantalla
					System.out.print("\nIntroduzca un ISBN: ");
					// leemos el ISBN por pantalla
					data = sc.nextLine();
					// utilizamos esto para poder mandar como dato la opción 2, es decir que el servidor busque libros por ISBN. : será el separador
					data = "1:" + data;
					
					// creamos el socket al servidor
					Socket socketAlServidor = new Socket();
					// la conexión tarda un tiempo en establecerse
					System.out.println("CLIENTE: Esperando a aque el servidor acepte la conexión");
					// conectamos con el servidor
					socketAlServidor.connect(direccionServidor);
					System.out.println("CLIENTE: Conexión establecida... a " + IP_SERVER + " por el puerto " + PUERTO);
					
					/**
					 * Stream (canal) de salida del cliente
					 */
					PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
					// mandamos la información al servidor
					salida.println(data);
					
					/**
					 * Stream (canal) de entrada del cliente
					 */
					InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
					// mientras el servidor busca y envía los datos del libro
					System.out.println("CLIENTE: Esperando respuesta del servidor ...");
					/**
					 * Buffer de entrada que me permita leer los datos de una línea entera y no caracter a caracter como lo hace el InputStreamReader
					 */					
					BufferedReader bf = new BufferedReader(entrada);
					// me quedo esperando a que el servidor me devuelva el libro
					/**
					 * String de datos recibidos del servidor
					 */
					String dataRecibido = bf.readLine();
					
					// Imprimimos los resultados obtenidos
					System.out.println("CLIENTE: recibido los resultados del libro:");
					System.out.println(stringLibro(dataRecibido));
					
					// cerramos el socket al servidor
					socketAlServidor.close();
					

				// opción de consulta de libros por titulo
				} else if (opc == 2) {
					System.out.println("****** Consulta de libros por Titulo *****");
					// pedimos el titulo por pantalla
					System.out.print("\nCLIENTE: Introduzca un titulo: ");
					// leemos el titulo por pantalla
					data = sc.nextLine();
					// utilizamos esto para poder mandar como dato la opción 2, es decir que el servidor busque libros por Autor. : será el separador
					data = "2:" + data;
					
					// creamos el socket al servidor
					Socket socketAlServidor = new Socket();
					// la conexión tarda un tiempo en establecerse
					System.out.println("CLIENTE: Esperando a aque el servidor acepte la conexión");
					// conectamos con el servidor
					socketAlServidor.connect(direccionServidor);
					System.out.println("CLIENTE: Conexión establecida... a " + IP_SERVER + " por el puerto " + PUERTO);
					
					/**
					 * Stream de salida del cliente
					 */
					PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
					// mandamos la información al servidor
					salida.println(data);
					
					/**
					 * Stream de entrada del cliente
					 */
					InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
					// mientras el servidor busca y envía los datos del libro
					System.out.println("CLIENTE: Esperando respuesta del servidor ...");
					/**
					 * Buffer de entrada que me permita leer los datos de una línea entera y no caracter a caracter como lo hace el InputStreamReader
					 */
					BufferedReader bf = new BufferedReader(entrada);
					// me quedo esperando a que el servidor me devuelva el libro
					/**
					 * String de datos recibidos del servidor
					 */
					String dataRecibido = bf.readLine();
					
					// Imprimimos los resultados obtenidos
					System.out.println("CLIENTE: Se han recibido los resultados del libro:");
					System.out.println(stringLibro(dataRecibido));
					
					// cerramos el socket al servidor
					socketAlServidor.close();
					
				// opción de consulta de libros por titulo
				} else if (opc == 3) {
					System.out.println("****** Consulta de libros por Autor *****");
					// pedimos el titulo por pantalla
					System.out.print("\nCLIENTE: Introduzca un autor: ");
					// leemos el titulo por pantalla
					data = sc.nextLine();
					// utilizamos esto para poder mandar como dato la opción 2, es decir que el servidor busque libros por Autor. : será el separador
					data = "3:" + data;
					
					// creamos el socket al servidor
					Socket socketAlServidor = new Socket();
					// la conexión tarda un tiempo en establecerse
					System.out.println("CLIENTE: Esperando a aque el servidor acepte la conexión");
					// conectamos con el servidor
					socketAlServidor.connect(direccionServidor);
					System.out.println("CLIENTE: Conexión establecida... a " + IP_SERVER + " por el puerto " + PUERTO);
					
					/**
					 * Stream de salida del cliente
					 */
					PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
					// mandamos la información al servidor
					salida.println(data);
					
					/**
					 * Stream de entrada del cliente
					 */
					InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
					// mientras el servidor busca y envía los datos del libro
					System.out.println("CLIENTE: Esperando respuesta del servidor ...");
					/**
					 * Buffer de entrada que me permita leer los datos de una línea entera y no caracter a caracter como lo hace el InputStreamReader
					 */
					BufferedReader bf = new BufferedReader(entrada);
					// me quedo esperando a que el servidor me devuelva el libro
					/**
					 * String de datos recibidos del servidor
					 */
					String dataRecibido = bf.readLine();
					
					// Imprimimos los resultados obtenidos
					System.out.println("CLIENTE: Se han recibido los resultados del autor:");
					printLibros(dataRecibido);
					
					// cerramos el socket al servidor
					socketAlServidor.close();
					
				// opción añadir un libro a la biblioteca
				} else if (opc == 4) {
					// el separador o regrex de los distintos datos de la cadena de datos enviada al servidor será /
					System.out.println("****** Añadir un libro a la biblioteca *****");
					// pedimos y leemos el ISBN por pantalla
					System.out.print("\nCLIENTE: Introduzca un ISBN: ");
					info = sc.nextLine();
					// asigmanos el valor leido a la cadena de datos con un sepradador o regrex ::
					data += info;
					// pedimos y leemos el titulo por pantalla
					System.out.print("CLIENTE: Introduzca un titulo: ");
					info = sc.nextLine();
					// asigmanos el valor leido a la cadena de datos con un sepradador o regrex ::
					data += "/" + info;
					// pedimos y leemos el autor por pantalla
					System.out.print("CLIENTE: Introduzca un autor: ");
					info = sc.nextLine();
					// asigmanos el valor leido a la cadena de datos con un sepradador o regrex ::
					data += "/" + info;
					// pedimos y leemos el precio por pantalla
					System.out.print("CLIENTE: Introduzca un precio: ");
					info = sc.nextLine();
					// asigmanos el valor leido a la cadena de datos con un sepradador o regrex ::
					data += "/" + info;
					
					// utilizamos esto para poder mandar como dato la opción 4, es decir que el servidor añada un libro : el separador de la opción con los datos
					data = "4:" + data;
					
					// creamos el socket al servidor
					Socket socketAlServidor = new Socket();
					// la conexión tarda un tiempo en establecerse
					System.out.println("CLIENTE: Esperando a aque el servidor acepte la conexión");
					// conectamos con el servidor
					socketAlServidor.connect(direccionServidor);
					System.out.println("CLIENTE: Conexión establecida... a " + IP_SERVER + " por el puerto " + PUERTO);
					
					/**
					 * Stream de salida del cliente
					 */
					PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
					// mandamos la información al servidor
					salida.println(data);
					
					/**
					 * Stream de entrada del cliente
					 */
					InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
					// mientras el servidor busca y envía los datos del libro
					System.out.println("CLIENTE: Esperando respuesta del servidor ...");
					/**
					 * Buffer de entrada que me permita leer los datos de una línea entera y no caracter a caracter como lo hace el InputStreamReader
					 */
					BufferedReader bf = new BufferedReader(entrada);
					// me quedo esperando a que el servidor me devuelva el libro
					/**
					 * String de datos recibidos del servidor
					 */
					String dataRecibido = bf.readLine();
					
					// Imprimimos los resultados obtenidos, el servidor ha añadido el libro a la biblioteca
					if("OK".equalsIgnoreCase(dataRecibido))
						System.out.println("CLIENTE: Se ha añadido el libro a la biblioteca");
					// Imprimimos los resultados obtenidos, el servidor no ha podido añadir el libro a la biblioteca
					else
						System.out.println("CLIENTE: No se ha podido añadir el libro a la biblioteca");
					
					// cerramos el socket al servidor
					socketAlServidor.close();
					
				// salir de la aplicacion
				} else if (opc == 5) {
					// preguntamos si de verdad se desea salir
					System.out.println("CLIENTE: ¿Seguro que desea salir [S/N]?");
					opcSalir = sc.nextLine();
					// gestión de la salida del programa
					if(opcSalir.equalsIgnoreCase("s"))
						salir = true;
					else if(opcSalir.equalsIgnoreCase("n"))
						System.out.println("CLIENTE: No se ha salido del programa");
					else
						System.out.println("CLIENTE: Opcion incorrecta, No se ha salido del programa");
				// opción no valida
				} else
					System.out.println("CLIENTE: Por favor, introduzca una opcion valida");
			} while (!salir);
			System.out.println("CLIENTE: Se ha salido del servicio de bibliotca, gracias por su visita.");
			
		// captura de excepciones en las que no se encuentra el servidor
		} catch (UnknownHostException e) {
			
			System.out.println("CLIENTE: No se encuentra el servidor en la direccion " + IP_SERVER);
			e.printStackTrace(); 
		
		// captura de excepciones de I/O (entrada/salida)
		} catch (IOException e) {
			
			System.out.println("CLIENTE: Error de E/S");
			e.printStackTrace();  
		
		// captura de otros tipos de excepciones no especificadas
		} catch (Exception e) {
			
			System.out.println("CLIENTE: Error -> " + e);
			e.printStackTrace();  
		}
	}
	
	/**
	 * método que devuelve una cadena adecuada para su impresión en la aplicación cliente. En caso de recibirse la cadena noLibro devolverá un mensaje diciendo que el libro no se ha encontrado 
	 * @param cadena cadena recibida por el servidor (no propicia para su impresión)
	 * @return cadena propicia para la visualización del cliente
	 */
	public static String stringLibro(String cadena) {
		if(!cadena.equals("noLibro")) {
			String data[] = cadena.split("::"); 
			String dataPrint = "CLIENTE: Se ha encontrado el libro buscado\n" + "**** datos del libro ***";
			for(int i = 0; i < data.length; i++)
				dataPrint += "\n" + data[i];
			return dataPrint;
		} else
			return "CLIENTE: El libro buscado no se encuentra en la biblioteca";	
	}
	
	/**
	 * método imprime una cadena con varios libroscadecuada para la visualización en la aplicación cliente. En caso de recibirse la cadena noLibro devolverá un mensaje diciendo que no se han encontrado libros de dicho autor
	 * @param cadena cadena recibida por el servidor (no propicia para su impresión)
	 */
	public static void printLibros(String cadena) {
		if(!cadena.equals("noLibro")) {
			System.out.print("**** libros encontrados del autor ***");
			String libros[] = cadena.split(":::");
			for(int i = 0; i < libros.length; i++) {
				String data[] = libros[i].split("::"); 
				for(int j = 0; j < data.length; j++)
					System.out.println(data[j]);
				System.out.println();
			}
		} else
			System.out.println("CLIENTE: No se encuentran libros del autor en la biblioteca");	
	}

}