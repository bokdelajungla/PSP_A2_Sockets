package entidad;

import java.io.Serializable;

public class Libro implements Serializable {
	
	private String isbn;
	private String titulo;
	private String autor;
	private Double precio;
	
	public Libro() {
		super();
	}
	
	public Libro(String isbn, String titulo, String autor, Double precio) {
		super();
		this.isbn = isbn;
		this.titulo = titulo;
		this.autor = autor;
		this.precio = precio;
	}
	
    @Override
    public boolean equals(Object obj) {
          
        // typecast obj to Student so that we can compare students
        Libro s = (Libro) obj;
          
        return this.isbn.equals(s.isbn) || this.titulo.equals(s.titulo);
    }


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getAutor() {
		return autor;
	}


	public void setAutor(String autor) {
		this.autor = autor;
	}


	public Double getPrecio() {
		return precio;
	}


	public void setPrecio(Double precio) {
		this.precio = precio;
	}


	@Override
	public String toString() {
		return "Libro [isbn=" + isbn + ", titulo=" + titulo + ", autor=" + autor + ", precio=" + precio + "]";
	}
	
	
	
	

}
