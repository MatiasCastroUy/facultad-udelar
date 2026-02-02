package excepciones;

@SuppressWarnings("serial")
public class CargaDatosRepetidaExcepcion extends Exception {
	public CargaDatosRepetidaExcepcion(String mensaje) {
		super(mensaje);
	}
}
