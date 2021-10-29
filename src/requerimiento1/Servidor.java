package requerimiento1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Servidor de la aplicacion
 * 
 * @author Adrian Rodriguez
 * @version 1.0
 *
 */
public class Servidor {
	
	/**
	 * biblioteca de libros
	 */
	public static Queue<Libro> biblioteca;
	/**
	 * Puerto de conexión
	 */
	public static final int PUERTO = 2017;
	
	/**
	 * hilo principal de ejecución
	 * @param args argumentos de entrada por consola
	 * @throws InterruptedException excepción lanzada desde el método main
	 */
	public static void main(String[] args) throws InterruptedException {
		// creamos el objeto biblioteca
		biblioteca = new LinkedList<>();
		
		// añadimos 5 libros a la biblioteca
		biblioteca.add(new Libro("8921-23423-586-6", "Don Quijote de la Mancha", "Miguel de Cervantes", 27.99f));
		biblioteca.add(new Libro("232-0-23892-386-5", "El Hobbit", "J.R.R. Tolkien", 12.99f));
		biblioteca.add(new Libro("789-1-23893-854-3", "EL Gran Gatsby", "F. Scott Fitzgerald", 10.99f));
		biblioteca.add(new Libro("234-3-11029-345-1", "El nio del pijama de rayas", "John Boyne", 18.99f));
		biblioteca.add(new Libro("983-0-39429-348-4", "Harry Potter y la Piedra Filosofal", "J.K. Rowling", 22.99f));
		
		System.out.println("      APLICACION SERVIDOR      ");
		System.out.println("-------------------------------");
		
		/**
		 * Stream de entrada del servidor
		 */
		InputStreamReader entrada = null;
		
		/**
		 * Stream de salida del servidor
		 */
		PrintStream salida = null;
		
		/**
		 * Socket al cliente
		 */
		Socket socketAlCliente = null;
		
		// try with resources (de esta forma no es necesario cerrar los flujos o streams tras su uso)
		try(ServerSocket serverSocket = new ServerSocket()) {
			
			// no es necesario pasarle la IP, el servidor ya se levanta en la máquina
			/**
			 * objeto que encapsula la Dir_IP y el puerto
			 */
			InetSocketAddress direccion = new InetSocketAddress(PUERTO);
			
			// establecemos que el serverSocket escuche peticiones desde el puerto establecido
			serverSocket.bind(direccion);
			
			/**
			 * contador del número de peticiones (número de clientes)
			 */
			int num_peticion = 0;
			
			// Es servidor esta a la espera de una petición de cliente
			System.out.println("SERVIDOR: Esperando peticion por el puerto " + PUERTO);
			
			// el servidor siempre tiene que estar escuchando (así el hilo nunca se para y el programa servidor siempre está operativo)
			while(true) {
				
				// entra la petición de un cliente (con connect) y se crea un objeto socketAlCliente, a partir de aqui se crea el flujo de datos
				socketAlCliente = serverSocket.accept();
				// incrementamos el número de petición ya que se ha recibido (aceptado) una peticion
				System.out.println("SERVIDOR: Peticion del número " + ++num_peticion + " recibida");
				
				// creamos el stream de entrada del servidor
				entrada =  new InputStreamReader(socketAlCliente.getInputStream());
				// creamos un buffer de entrada que me permita leer los datos de una línea entera y no caracter a caracter como lo hace el InputStreamReader
				BufferedReader bf = new BufferedReader(entrada);
				// me quedo esperando a que el usuario introduzca un printLine
				String dataRecibido = bf.readLine();
				
				// comunicamos la cadena llegada del cliente
				System.out.println("SERVIDOR: Me ha llegado del cliente: " + dataRecibido);
				// realizamos la separación de los datos recibidos, recordemos que el separador o regrex es : 
				String[] datos = dataRecibido.trim().split(":");
				// opción seleccionada por el cliente puesta como cadena para evitar un cast
				String opc = datos[0];
				// isbn o autor a buscar
				String buscar = datos[1];
				
				// opcion 1 seleccionada por el cliente
				if(opc.equals("1")) {
					// buscamos el libro por ISBN haciendo llamada a la función correspondiente a tal tarea
					/**
					 * Datos del libro que serán devueltos al cliente
					 */
					String datosLibro = buscarPorISBN(buscar);
					if(!datosLibro.equals(""))
						System.out.println("SERVIDOR: Se ha encontrado el libro, se procede a enviar " + datosLibro + " al cliente");
					else 
						System.out.println("SERVIDOR: No se ha encontrado el libro, se procede a enviar la cadena noLibro al cliente al cliente");
					
					// creamos el stream de salida del servidor al cliente
					salida = new PrintStream(socketAlCliente.getOutputStream());
					// mandamos la información al cliente
					salida.println(datosLibro);
					
				// opcion 2 seleccionada por el cliente
				} else if(opc.equals("2")) {
					// buscamos el libro por Autor haciendo llamada a la función correspondiente a tal tarea
					/**
					 * Datos del libro que serán devueltos al cliente
					 */
					String datosLibro = buscarPorTitulo(buscar);
					if(!datosLibro.equals(""))
						System.out.println("SERVIDOR: Se ha encontrado el libro, se procede a enviar " + datosLibro + " al cliente");
					else 
						System.out.println("SERVIDOR: No se ha encontrado el libro, se procede a enviar la cadena noLibro al cliente al cliente");
					
					// creamos el stream de salida del servidor al cliente
					salida = new PrintStream(socketAlCliente.getOutputStream());
					// mandamos la información al cliente
					salida.println(datosLibro);
				}
				
				// cerramos el socket al cliente ya que estamos esperando peticiones de mas clienets
				socketAlCliente.close();
			}
			
		// captura de excepciones de I/O (entrada/salida)
		} catch (IOException e) {
			
			System.out.println("SERVIDOR: Error de E/S");
			e.printStackTrace();  

		// captura de otros tipos de excepciones no especificadas
		} catch (Exception e) {
			
			System.out.println("SERVIDOR: Error -> " + e);
			e.printStackTrace();  
		
		} 
		
	}
	
	/**
	 * método que busca un libro en la biblioteca mediante un ISBN introducido por el usuario
	 * @param isbn ISBN por el cual se buscará el libro en el servidor
	 * @return cadena con la información completa del libro o noLibro si no se encuentra el libro
	 */
	public static String buscarPorISBN(String isbn) {
		for(Libro libro : biblioteca) {
			if(libro.getIsbn().equals(isbn))
				return libro.dataLibro();
		}
		return "noLibro";
	}
	
	/**
	 * método que busca un libro en la biblioteca mediante un titulo introducido por el usuario
	 * @param titulo Titulo por el cual se buscará el libro en el servidor
	 * @return cadena con la información completa del libro o noLibro si no se encuentra el libro
	 */
	public static String buscarPorTitulo(String titulo) {
		for(Libro libro : biblioteca) {
			if(libro.getTitulo().equals(titulo))
				return libro.dataLibro();
		}
		return "noLibro";
	}
	
}



