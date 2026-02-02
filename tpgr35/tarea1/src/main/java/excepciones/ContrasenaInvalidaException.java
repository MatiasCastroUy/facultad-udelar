package excepciones;

@SuppressWarnings("serial")
public class ContrasenaInvalidaException extends Exception {

	public ContrasenaInvalidaException(String mensaje) {
		super(mensaje);
	}
}
