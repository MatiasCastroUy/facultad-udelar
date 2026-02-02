package excepciones;

@SuppressWarnings("serial")
public class EventoRepetido extends Exception {
	public EventoRepetido(String string) {
		super(string);
	}
}
