#ifndef ICONTROLADORINMUEBLE_H
#define ICONTROLADORINMUEBLE_H

#include <list>
#include <set>
#include "DTInmuebleListado.h"
#include "DTInmueble.h"
#include "DTInmuebleAdministrado.h"
#include "TipoTecho.h"

class Inmueble;
class Propietario;

class IControladorInmueble {
public:
    virtual std::list<DTInmuebleListado *> listarInmuebles() = 0;
    virtual DTInmueble *detalleInmueble(int codigoInmueble) = 0;
    virtual Inmueble *getInmueble(int codigoInmueble) = 0;
    virtual std::set<DTInmuebleAdministrado *> listarInmueblesAdministrados(std::string nicknameInmobiliaria) = 0;
    virtual std::set<DTInmuebleListado *> listarInmueblesNoAdministradosInmobiliaria(std::string nicknameInmobiliaria) = 0;
    virtual void eliminarInmueble(int codigoInmueble) = 0;
    virtual void altaAdministraPropiedad(std::string nicknameInmobiliaria, int codigoInmueble) = 0;
    virtual void altaCasa(std::string direccion, int numeroPuerta, int superficie, int anioConstruccion, bool esPH, TipoTecho techo) = 0;
    virtual void setPropietarioRecordado(Propietario* p) = 0;
    virtual void altaApartamento(std::string direccion, int numeroPuerta, int superficie, int anioConstruccion, int piso, bool tieneAscensor, float gastosComunes) = 0;
    
    virtual ~IControladorInmueble() {};
};

#endif
