package requerimiento2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

/**
 * <p>Implementa un hilo que se encarga de gestionar la peticion de un cliente
 * <p>Establece los flujos de datos de subida y bajada y se encarga de interpretar
 * la cadena peticion recibida por el ClienteLibros.
 * <p>La cadena que se recibe est� formada por el n�mero de la opci�n concatenada al
 * t�rmino de b�squeda.
 * <p>El proceso separa el primer caracter y dependiendo del valor, realiza una
 * b�squeda u otra iterando sobre la colecci�n.
 * <p>Si encuentra una coincidencia, le env�a la cadena de texto resultante de invocar
 * {@link requerimiento1.Libro#toString()}
 * <p>Cuando termina env�a un mensaje con la palabra FIN para indicar al cliente que ya
 * ha terminado.
 * 
 * @author Jorge SALor
 *
 */
public class HiloBuscar2 implements Runnable{
	
	private Thread hilo;
	private static int numCliente = 0;
	private Socket socketCliente;
	private HashSet<Libro> biblioteca;
	
	public HiloBuscar2(Socket cliente, HashSet<Libro> biblioteca) {
		numCliente++;
		hilo = new Thread(this, "Cliente"+numCliente);
		this.socketCliente = cliente;
		this.biblioteca = biblioteca;
		hilo.start();
	}
	
	@Override
	public void run() {
		System.out.println("Estableciendo comunicaci�n con " + hilo.getName());
		try {
			//InputStream y OutputStream son flujos de bytes
			//Se escriben y se leen bytes -> Hay que transformarlos
			InputStream entrada = socketCliente.getInputStream();
			OutputStream salida = socketCliente.getOutputStream();
			String peticion = new String();
			byte[] mensaje = new byte[100];
			entrada.read(mensaje);
			peticion = new String(mensaje).trim(); //Hay que eliminar los elementos vac�os
			boolean encontrado = false;

			switch(peticion.charAt(0)) {
				case '1':
					String isbn = peticion.substring(1);
					for(Libro l: biblioteca) {
						if(l.getIsbn().equals(isbn)) {
							salida.write(l.toString().getBytes());
							encontrado = true;
							break; //Solo puede devolver un libro as� que salimos del bucle si lo encuentra
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
							break; //Solo puede devolver un libro as� que salimos del bucle si lo encuentra
						}
					}
					if(!encontrado) {
						salida.write("El libro que busca no se encuentra en la Biblioteca".getBytes());
					}
					break;
					
				case '3':
					String autor = peticion.substring(1);
					for(Libro l: biblioteca) {
						if(l.getAutor().equals(autor)) {
							salida.write(l.toString().getBytes());
							encontrado = true;
							//En este caso no hay break porque puede haber varios
						}
					}
					if(!encontrado) {
						salida.write("El libro que busca no se encuentra en la Biblioteca".getBytes());
					}
					break;

			}
			//Enviamos la palabra FIN para indicar que hemos terminado
			salida.write("FIN".getBytes());
			//Al cerrar el socket se cierran tambien los flujos asociados a �l
			socketCliente.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
}



