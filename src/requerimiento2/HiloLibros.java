package requerimiento2;

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
					switch (numero) {
					case "1":
						if (dato.trim().contentEquals(libros.get(i).getIsbn())){
							librosLista.add(libros.get(i));
						}
						break;
					case "2":
						if (dato.trim().equalsIgnoreCase(libros.get(i).getTitulo())){
							librosLista.add(libros.get(i));
						}
					case "3":
						//Mandamos la señal de "0" para que el cliente sepa que vamos a cortar
						//la comunicacion
						if (dato.trim().equalsIgnoreCase("si")) {
							salida.println("Cerró");
							System.out.println(hilo.getName() + " ha cerrado la comunicacion");
							continuar = false;
							break;
						}
					case "4":
						if (dato.trim().equalsIgnoreCase(libros.get(i).getAutor())){
							librosLista.add(libros.get(i));
						}
					}
				}
				
				Libro [] libroArray = new Libro[librosLista.size()];
				librosLista.copyInto(libroArray);
////				//Salida del servidor al cliente
				System.out.println("EL ARRAY QUE SE ENVIA ES " + libroArray.toString());
				salida.println(libroArray.toString());
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
