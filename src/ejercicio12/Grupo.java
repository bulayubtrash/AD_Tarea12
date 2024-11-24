package ejercicio12;

import java.io.Serializable;

public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String grupo;

	public Grupo() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	@Override
	public String toString() {
		return "Grupo [id=" + id + ", grupo=" + grupo + "]";
	}

}
