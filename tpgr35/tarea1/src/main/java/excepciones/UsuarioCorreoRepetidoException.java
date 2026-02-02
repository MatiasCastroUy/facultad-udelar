package excepciones;

@SuppressWarnings("serial")
public class UsuarioCorreoRepetidoException extends Exception{
	public UsuarioCorreoRepetidoException(String string) {
		super(string);
	}
}
