package excepciones;

@SuppressWarnings("serial")
public class CategoriaRepetidaExcepcion extends Exception {
	public CategoriaRepetidaExcepcion(String mensaje) {
		super(mensaje);
	}
}
