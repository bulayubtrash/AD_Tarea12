package ejercicio12;

import java.util.Date;

public class Alumno {
	private int nia;
	private String nombre;
	private String apellidos;
	private char genero;
	private Date fechaNac;
	private String ciclo;
	private String curso;
	private String grupo;

	public Alumno() {
		super();
	}

	public Alumno(int nia, String nombre, String apellidos, char genero, Date fechaNac, String ciclo, String curso,
			String grupo) {
		super();
		this.nia = nia;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.fechaNac = fechaNac;
		this.ciclo = ciclo;
		this.curso = curso;
		this.grupo = grupo;
	}

	public int getNia() {
		return nia;
	}

	public void setNia(int nia) {
		this.nia = nia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public char getGenero() {
		return genero;
	}

	public void setGenero(char genero) {
		this.genero = genero;
	}

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getCiclo() {
		return ciclo;
	}

	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	@Override
	public String toString() {
		return "Alumno [nia=" + nia + ", nombre=" + nombre + ", apellidos=" + apellidos + ", genero=" + genero
				+ ", fechaNac=" + fechaNac + ", ciclo=" + ciclo + ", curso=" + curso + ", grupo=" + grupo + "]";
	}
	
	

}
