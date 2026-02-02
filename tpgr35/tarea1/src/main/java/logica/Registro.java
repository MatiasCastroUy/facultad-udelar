package logica;

import java.time.LocalDate;

public class Registro {
    private final Asistente asistente;
    private final TipoRegistro tipo;
    private final Edicion edicion;
    private final LocalDate fechaRegistro;
    private final String codPatrocinio;

    private boolean asistio = false;

    public Registro(Asistente asistente, TipoRegistro tipo, Edicion edicion, LocalDate fechaRegistro) {
        this.asistente = asistente;
        this.tipo = tipo;
        this.edicion = edicion;
        this.fechaRegistro = fechaRegistro;
        this.codPatrocinio= "";
    }
    public Registro(Asistente asistente, TipoRegistro tipo, Edicion edicion, LocalDate fechaRegistro, String CodPatrocinio) {
        this.asistente = asistente;
        this.tipo = tipo;
        this.edicion = edicion;
        this.fechaRegistro = fechaRegistro;
        this.codPatrocinio= CodPatrocinio;
    }

    public Asistente getAsistente() {
        return asistente;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }

    public Edicion getEdicion() {
        return edicion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public float getCosto() {
        return tipo.getCosto();
    }
    
    public String getCodPatrocinio() {
    	return this.codPatrocinio;
    }

    public boolean isAsistio() {
        return asistio;
    }

    public void confirmarAsistencia() {
        if (!this.asistio) {
            this.asistio = true;
        }
    }
}
