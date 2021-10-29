package requerimiento2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

	public static final int PUERTO = 2018;
	public static final String IP_SERVER = "localhost";

	public static void main(String[] args) {
		System.out.println("        APLICACI�N CLIENTE         ");
		System.out.println("-----------------------------------");

		//En este objeto vamos a encapsular la IP y el puerto al que nos vamos a conectar
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

		try (Scanner sc = new Scanner(System.in);){

			boolean continuar = true;				

			do {
				System.out.println("MENU: Escriba el numero con la opci�n deseada");
				System.out.println("1 - Consultar libro por ISBN");
				System.out.println("2 - Consultar libro por t�tulo");
				System.out.println("3 - Salir de la aplicaci�n");
				System.out.println("4 - Consultar libro por autor");
				String numeroSeleccionado = sc.nextLine();//el numero del menu
				Socket socketAlServidor = new Socket();

				switch (numeroSeleccionado) {
				case "1":
					System.out.println("Escriba el ISBN");
					break;
				case "2":
					System.out.println("Escriba el t�tulo");
					break;
				case "3":
					System.out.println("�Est� seguro que quiere cerrar la aplicaci�n? Escriba si/no");
					break;
				case "4":
					System.out.println("Escriba el autor");
					break;
				default:
					System.out.println("N�mero invalido");
					main(null);
				}

				String datoRecogido = sc.nextLine();

				if (datoRecogido.equalsIgnoreCase("si")) {
					continuar = false;
				}
				//En este caso voy a establecer que mi comunicaci�n entre el cliente y el
				//servidor va a ser que le mando los dos numeros separados por un "-"
				//para luego en el servidor obtener ambmos numeros mediante el 
				//metodo split() de la clase String
				String dataParaElServidor = numeroSeleccionado + "/" + datoRecogido;//3-41

				//Observese como no conviertos los datos a enteros, ya que cuando
				//se mandan a traves de un socket se mandan SIEMPRE en formato cadena

				//Establecemos la conexi�n
				System.out.println("CLIENTE: Esperando a que el servidor acepte la conexi�n");
				socketAlServidor.connect(direccionServidor);			
				System.out.println("CLIENTE: Conexion establecida... a " + IP_SERVER 
						+ " por el puerto " + PUERTO);	

				//Creamos el objeto que nos permite mandar informacion al servidor
				PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
				//Mandamos la informaci�n por el Stream
				salida.println(dataParaElServidor);//3-4

				//Creamos el objeto que nos va a permitir leer la salida del servidor
				InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());

				//Esta clase nos ayuda a leer datos del servidor linea a linea en vez de 
				//caracter a caracter como la clase InputStreamReader
				BufferedReader bf = new BufferedReader(entrada);

				System.out.println("CLIENTE: Esperando al resultado del servidor...");
				//En la siguiente linea se va a quedar parado el hilo principal
				//de ejecuci�n hasta que el servidor responda, es decir haga un println
				String resultado = bf.readLine();//7

				System.out.println("CLIENTE: El resultado es: " + resultado);//7

			} while(continuar);	
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la direcci�n" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}

		System.out.println("CLIENTE: Fin del programa");
	}

}
