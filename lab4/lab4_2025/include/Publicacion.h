#ifndef PUBLICACION_H
#define PUBLICACION_H
#include "DTFecha.h"
#include "TipoPublicacion.h"
#include <string>

class Publicacion {
private:
    int codigo;
    DTFecha *fecha;
    TipoPublicacion tipo;
    std::string texto;
    float precio;
    bool activa;

public:
    // Constructor
    Publicacion(int codigo, DTFecha* fecha, TipoPublicacion tipo,
                std::string texto, float precio, bool activa);

    // Destructor
    ~Publicacion();

    // Getters
    int getCodigo();
    DTFecha* getFechaAlta() ;
    TipoPublicacion getTipo() ;
    std::string getTexto();
    float getPrecio() ;
    bool getActiva() ;

    // Setters
    void setCodigo(int codigo);
    void setFecha( DTFecha* fecha);
    void setTipo(TipoPublicacion tipo);
    void setTexto(std::string& texto);
    void setPrecio(float precio);
    void setActiva(bool activa);
};

#endif

