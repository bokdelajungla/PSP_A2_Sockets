package requerimiento1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

/**
 * <p>Implementa un hilo que se encarga de gestionar la peticion de un cliente
 * <p>Establece los flujos de datos de subida y bajada y se encarga de interpretar
 * la cadena peticion recibida por el ClienteLibros.
 * <p>La cadena que se recibe está formada por el número de la opción concatenada al
 * término de búsqueda.
 * <p>El proceso separa el primer caracter y dependiendo del valor, realiza una
 * búsqueda u otra iterando sobre la colección.
 * <p>Si encuentra UNA coincidencia, le envía al cliente la cadena de texto resultante de invocar
 * {@link requerimiento1.Libro#toString()}
 * 
 * @author Jorge SALor
 *
 */
public class HiloBuscar implements Runnable{
	
	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketCliente;
	private HashSet<Libro> biblioteca;
	
	public HiloBuscar(Socket cliente, HashSet<Libro> biblioteca) {
		numCliente++;
		hilo = new Thread(this, "Cliente"+numCliente);
		this.socketCliente = cliente;
		this.biblioteca = biblioteca;
		hilo.start();
	}
	
	@Override
	public void run() {
		System.out.println("Estableciendo comunicación con " + hilo.getName());
		try {
			//InputStream y OutputStream son flujos de bytes
			//Se escriben y se leen bytes -> Hay que transformarlos
			InputStream entrada = socketCliente.getInputStream();
			OutputStream salida = socketCliente.getOutputStream();
			String peticion = new String();
			byte[] mensaje = new byte[100];
			entrada.read(mensaje);
			peticion = new String(mensaje).trim(); //Hay que eliminar los elementos vacíos
			boolean encontrado = false;
			switch(peticion.charAt(0)) {
				case '1':
					String isbn = peticion.substring(1);
					for(Libro l: biblioteca) {
						if(l.getIsbn().equals(isbn)) {
							salida.write(l.toString().getBytes());
							encontrado = true;
							break;
						}
					}
					if(!encontrado) {
						salida.write("El libro que busca no se encuentra en la Biblioteca".getBytes());
					}
					break;
					
				case '2':
					String titulo = peticion.substring(1);
					for(Libro l: biblioteca) {
						if(l.getTitulo().equals(titulo)) {
							salida.write(l.toString().getBytes());
							encontrado = true;
							break;
						}
					}
					if(!encontrado) {
						salida.write("El libro que busca no se encuentra en la Biblioteca".getBytes());
					}
					break;	
			}
			entrada.close();
			salida.close();
			socketCliente.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
}



