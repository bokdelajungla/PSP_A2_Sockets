package requerimiento2;
/**
 * Clase que simula un libro
 * 
 * @author Adrian Rodriguez
 * @version 1.0
 *
 */
public class Libro {

	/**
	 * ISBN del libro
	 */
	private String isbn;
	/**
	 * Titulo del libro
	 */
	private String titulo;
	/**
	 * Autor del libro
	 */
	private String autor;
	/**
	 * Precio del libro en €
	 */
	private float precio;
	
	/**
	 * Cra un objeto de clase libro
	 * @param isbn ISBN del libro
	 * @param titulo Titulo del libro
	 * @param autor Autor del libro
	 * @param precio Precio del libro en €
	 */
	public Libro(String isbn, String titulo, String autor, float precio) {
		this.isbn = isbn;
		this.titulo =  titulo;
		this.autor= autor;
		this.precio = precio;
	}

	/**
	 * getter del ISBN
	 * @return ISBN del libro
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * setter del ISBN
	 * @param isbn ISBN del libro
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * getter del titulo
	 * @return Titulo del libro
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * setter del titulo
	 * @param titulo Titulo del libro
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * getter del autor
	 * @return Autor del libro
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * setter del autor
	 * @param autor Autor del libro
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}

	/**
	 * getter del precio
	 * @return Precio del libro
	 */
	public float getPrecio() {
		return precio;
	}

	/**
	 * setter del precio
	 * @param precio Precio del libro
	 */
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	
	/**
	 * Método que devuelve los datos del libro con un aseparador de :: necesario para el futuro split que hará el cliente de la información
	 * @return cadena con la información completa del libro (incluye un separador de ::)
	 */
	public String dataLibro() {
		return "isbn: " + isbn + "::titulo: " + titulo + "::autor: " + autor + "::precio: " + precio + "€";
	}
	
	
}
