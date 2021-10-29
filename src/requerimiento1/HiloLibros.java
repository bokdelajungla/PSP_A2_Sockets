package requerimiento1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import entidad.Libro;

public class HiloLibros implements Runnable {
	
	ArrayList<Libro> libros = new ArrayList();
	private Integer index;
	Integer id;

	public Integer getIndex() { return index; }

	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketAlCliente;	
	
	public HiloLibros(Socket cliente, ArrayList<Libro> libros) {
		numCliente++;
		hilo = new Thread(this, "Cliente_"+numCliente);
		this.socketAlCliente = cliente;
		this.libros = libros;
		hilo.start();
	}
	

	@Override
	public void run() {
		System.out.println("Estableciendo comunicacion con " + hilo.getName());
		PrintStream salida = null;
		InputStreamReader entrada = null;
		BufferedReader entradaBuffer = null;
		
		try {
			//Entrada del servidor al cliente
			entrada = new InputStreamReader(socketAlCliente.getInputStream());
			entradaBuffer = new BufferedReader(entrada);
			salida = new PrintStream(socketAlCliente.getOutputStream());
			
			String texto = "";
			boolean continuar = true;
			//Procesaremos entradas hasta que el texto del cliente sea FIN
			while (continuar) {
				texto = entradaBuffer.readLine();
				System.out.println(texto);
				String[] operadores = texto.split("/");
				System.out.println(operadores);
				String numero = (operadores[0]);//3
				String dato = (operadores[1]);//4
				System.out.println("el numero es " +numero);
				System.out.println("el dato es " + dato);
				
				Vector librosLista = new Vector();
				for (int i = 0; i < libros.size(); i++) {
//					if (numero.trim().equalsIgnoreCase("3")) {
//							if (dato.trim().equalsIgnoreCase("si")) {
//									salida.println("Cerró");
//									System.out.println(hilo.getName() + " ha cerrado la comunicacion");
//									continuar = false;
//									break;	
//							}
//					}
					switch (numero) {
					case "1":
						if (dato.trim().contentEquals(libros.get(i).getIsbn())){
							index = i;
						}
						System.out.println(libros.get(i).getIsbn());
						System.out.println("He llegado al case 1");
						System.out.println(librosLista);
						break;
					case "2":
						if (dato.trim().equalsIgnoreCase(libros.get(i).getTitulo())){
							index = i;
						}
						System.out.println(libros.get(i).getTitulo());
						System.out.println(dato);
						System.out.println("Hellegado al case 2");
						System.out.println(librosLista);
	
					case "3":
						//Mandamos la señal de "0" para que el cliente sepa que vamos a cortar
						//la comunicacion
						if (dato.trim().equalsIgnoreCase("si")) {
							salida.println("Cerró");
							System.out.println(hilo.getName() + " ha cerrado la comunicacion");
							continuar = false;
							break;
						}
					}
				}
				
////			//Salida del servidor al cliente
				if (getIndex() != null) {
					salida.println(libros.get(index).getIsbn() + "/" + libros.get(index).getAutor() + "/" + libros.get(index).getTitulo() + "/" + libros.get(index).getPrecio());
				} else {
					salida.println("No/Hay/Libro)");
				}
				

			}
			//Cerramos el socket
			socketAlCliente.close();
		} catch (IOException e) {
			System.err.println("HiloContadorLetras: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("HiloContadorLetras: Error");
			e.printStackTrace();
		}
	}
}
