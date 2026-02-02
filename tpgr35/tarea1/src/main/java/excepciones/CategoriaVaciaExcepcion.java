package excepciones;

@SuppressWarnings("serial")
public class CategoriaVaciaExcepcion extends Exception {
	public CategoriaVaciaExcepcion(String mensaje) {
		super(mensaje);
	}
}
