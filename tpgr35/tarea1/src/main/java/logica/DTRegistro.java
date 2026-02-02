package logica;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DTRegistro {
    private String evento;   // nombre del evento
    private String edicion;  // nombre de la edición
    private LocalDate fecha; // fecha del registro
    private String tipo;     // nombre del tipo de registro
    private float costo;     // costo del tipo de registro
    private boolean asistio; // indica si el asistente confirmó su asistencia


    public DTRegistro(String evento, String edicion, LocalDate fecha, String tipo, float costo) {
        this.setEvento(evento);
        this.setEdicion(edicion);
        this.setFecha(fecha);
        this.setTipo(tipo);
        this.setCosto(costo);
        this.setAsistio(false);
    }

    public DTRegistro(String evento, String edicion, LocalDate fecha, String tipo, float costo, boolean asistio) {
        this.setEvento(evento);
        this.setEdicion(edicion);
        this.setFecha(fecha);
        this.setTipo(tipo);
        this.setCosto(costo);
        this.setAsistio(asistio);
    }

    public DTRegistro() {
        this.setEvento(null);
        this.setEdicion(null);
        this.setFecha(null);
        this.setTipo(null);
        this.setCosto(-1);
        this.setAsistio(false);
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public boolean isAsistio() {
        return asistio;
    }

    public void setAsistio(boolean asistio) {
        this.asistio = asistio;
    }

    public String toString() {
        return "Registro a \"" + edicion + "\" de \"" + evento + "\"";
    }
}
