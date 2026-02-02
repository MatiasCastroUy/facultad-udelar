#ifndef ICONTROLADORPUBLICACION_H
#define ICONTROLADORPUBLICACION_H

#include <set>
#include <string>
#include "DTPublicacion.h"
#include "DTInmueble.h"
#include "TipoPublicacion.h"
#include "TipoInmueble.h"

class IControladorPublicacion {
public:
    virtual bool altaPublicacion(std::string nicknameInmobiliaria, int codigoInmueble, TipoPublicacion tipo, std::string texto, float precio) = 0;
    virtual void quitarPublicacion(int codigoPublicacion) = 0;
    virtual bool existePublicacion(int codigo) = 0;
    virtual DTPublicacion* getDTPublicacion(Publicacion* pub, Inmobiliaria* inmo) = 0;
    virtual std::set<DTPublicacion*> listarPublicacion(TipoPublicacion tipo, float min, float max, TipoInmueble tipoInmuebleFiltro) = 0;
    virtual DTInmueble* detalleInmueblePublicacion(int codigoPublicacion) = 0;
    virtual Publicacion* obtenerPublicacion(int codigo) = 0;
    virtual ~IControladorPublicacion() {};
};

#endif
