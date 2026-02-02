#ifndef ADMINISTRAPROPIEDAD_H
#define ADMINISTRAPROPIEDAD_H
#include "DTFecha.h"
#include <list>
#include <string>

class Inmobiliaria; // declaración hacia adelante
class Inmueble;
class Publicacion;

class AdministraPropiedad
{
private:
    DTFecha *fecha;
    Inmueble *inmuebleAsociado;
    Inmobiliaria *inmoAsociada;
    std::list<Publicacion*> publicaciones;

public:
     // Constructor y Destructor
    AdministraPropiedad(DTFecha* fecha, Inmueble* inmuebleAsociado, Inmobiliaria* inmoAsociada);
    ~AdministraPropiedad();

    // Getters
    DTFecha* getFecha();
    Inmueble* getInmuebleAsociado();
    std::list<Publicacion*> getPublicaciones();
    Publicacion* ultimaPub();

    // Modificadores
    void agregarPublicacion(Publicacion* p);
    bool ActualizarPubActiva(Publicacion* Pub);

    // Eliminación
    void eliminarPublicaciones();

    // Relaciones
    std::string getNicknameInmobiliaria();
    void quitarInmobiliaria(); // quita la referencia a este admpropiedad de la inmoAsociada
};

#endif
