#ifndef CONTROLADORINMUEBLE_H
#define CONTROLADORINMUEBLE_H

#include <list>
#include <map>
#include <set>
#include <string>
#include "Inmueble.h"
#include "DTInmueble.h"
#include "DTInmuebleAdministrado.h"
#include "DTInmuebleListado.h"
#include "TipoTecho.h"
#include "IControladorInmueble.h"

class ControladorInmueble: public IControladorInmueble
{
private:
    static ControladorInmueble *instancia;
    ControladorInmueble();
    ~ControladorInmueble();
    static int ultimoCodigoInmueble; 
    Propietario* propietarioRecordado;
    std::map<int, Inmueble *> col_inmuebles; // inmuebles organizados por codigo
public:
    static ControladorInmueble *getInstanciaI();
    static void liberarInstancia() ;
    std::list<DTInmuebleListado *> listarInmuebles();
    DTInmueble *detalleInmueble(int codigoInmueble);
    Inmueble *getInmueble(int codigoInmueble);
    std::set<DTInmuebleAdministrado *> listarInmueblesAdministrados(std::string nicknameInmobiliaria);
    std::set<DTInmuebleListado *> listarInmueblesNoAdministradosInmobiliaria(std::string nicknameInmobiliaria);
    void eliminarInmueble(int codigoInmueble);
    void altaAdministraPropiedad(std::string nicknameInmobiliaria, int codigoInmueble);
    void altaCasa(std::string direccion, int numeroPuerta, int superficie, int anioConstruccion, bool esPH, TipoTecho techo);
    void setPropietarioRecordado(Propietario* p);
    void altaApartamento(std::string direccion, int numeroPuerta, int superficie,int anioConstruccion, int piso, bool tieneAscensor, float gastosComunes);
};

#endif
