#include "../include/ControladorInmueble.h"
#include "../include/Usuario.h"
#include "../include/ControladorUsuario.h"
#include "../include/Casa.h"
#include "../include/Apartamento.h"
#include "../include/DTInmueble.h"
#include "../include/Inmueble.h"
#include "../include/DTInmuebleListado.h"
#include "../include/Factory.h"
#include "../include/IControladorFechaActual.h"

#include <list>
#include <map>
#include "../include/DTInmuebleListado.h"

#include <list>
#include <map>
#include <set>

// falta la implementacion del singleton acá

// lu singleton
ControladorInmueble *ControladorInmueble::instancia = NULL;
ControladorInmueble::ControladorInmueble() {}
ControladorInmueble::~ControladorInmueble() {
  while(!col_inmuebles.empty()) {
    std::map<int,Inmueble*>::iterator primero = col_inmuebles.begin();
    int cod = primero->first;
    eliminarInmueble(cod);
  }
}

ControladorInmueble *ControladorInmueble::getInstanciaI()
{
    if (instancia == NULL)
        instancia = new ControladorInmueble();
    return instancia;
}

void ControladorInmueble::liberarInstancia() {
    if (instancia!= nullptr) {
        delete instancia;
        instancia = nullptr;
    }
}
// operaciones:

std::set<DTInmuebleListado*> ControladorInmueble::listarInmueblesNoAdministradosInmobiliaria(std::string nicknameInmobiliaria) {
  IControladorUsuario *c_usuario = Factory::getInstance()->getControladorUsuario();
  Inmobiliaria *inmo = c_usuario->getInmobiliaria(nicknameInmobiliaria);
  std::set<DTInmuebleListado*> lista = inmo->getInmueblesNoAdmin();

  return lista;
}


int ControladorInmueble::ultimoCodigoInmueble = 0; // <- inicialización estática
void ControladorInmueble::setPropietarioRecordado(Propietario* p) {
    propietarioRecordado = p;
}

void ControladorInmueble::altaCasa(std::string direccion, int numeroPuerta, int superficie, int anioConstruccion, bool esPH, TipoTecho techo)
{
     // Generar nuevo código autoincremental
    int nuevoCodigo = ++ultimoCodigoInmueble;

    Casa* nuevaCasa = new Casa(nuevoCodigo, direccion, numeroPuerta, superficie, anioConstruccion, esPH, techo);
 if (propietarioRecordado != nullptr) {
       nuevaCasa->setPropietario(propietarioRecordado);      
      propietarioRecordado->agregarInmueble(nuevaCasa);  
      col_inmuebles.insert({nuevoCodigo,nuevaCasa});

   }
if (propietarioRecordado == nullptr) {
    delete nuevaCasa;
}

}

void ControladorInmueble::altaApartamento(std::string direccion, int numeroPuerta, int superficie,
                                          int anioConstruccion, int piso, bool tieneAscensor, float gastosComunes)
{

    // Generar nuevo código autoincremental
    int nuevoCodigo = ++ultimoCodigoInmueble;

    // Crear el nuevo Apartamento
    Apartamento* nuevoApartamento = new Apartamento(nuevoCodigo, direccion, numeroPuerta,
                                                    superficie, anioConstruccion, piso,
                                                    tieneAscensor, gastosComunes);

    if (propietarioRecordado != nullptr) 
    {
        propietarioRecordado->agregarInmueble(nuevoApartamento);  // ← vínculo: propietario → inmueble
        nuevoApartamento->setPropietario(propietarioRecordado);    // ← inmueble → propietario DOBLE VISIBILIDAD ?
        col_inmuebles.insert({nuevoCodigo, nuevoApartamento});

    }
    if (propietarioRecordado == nullptr) {
    delete nuevoApartamento;
    }                      
}


DTInmueble *ControladorInmueble::detalleInmueble(int codigoInmueble)
{

    return col_inmuebles[codigoInmueble]->getDTInmueble();
}

void ControladorInmueble::eliminarInmueble(int codigoInmueble)
{
    Inmueble *elim = col_inmuebles[codigoInmueble];
    col_inmuebles.erase(codigoInmueble);
    elim->eliminarReferencias();

    delete elim;
}

std::list<DTInmuebleListado *> ControladorInmueble::listarInmuebles()
{

    std::list<DTInmuebleListado *> lista;

    for (std::map<int, Inmueble *>::iterator it = col_inmuebles.begin(); it != col_inmuebles.end(); it++)
    {
        Inmueble *i = it->second;
        lista.push_back(i->getDTInmuebleListado()); // corregi esto porq tenia un insert
    }

    return lista;
}

void ControladorInmueble::altaAdministraPropiedad(std::string nicknameInmobiliaria, int codigoInmueble)
{
    Factory *factory = Factory::getInstance();
    IControladorFechaActual *fechaActual = factory->getControladorFechaActual();
  

    IControladorUsuario *c_usuario = Factory::getInstance()->getControladorUsuario();

    Inmobiliaria *ci = c_usuario->getInmobiliaria(nicknameInmobiliaria);

    Inmueble *cin = getInmueble(codigoInmueble);

    ci->altaAdministracionPropiedad(cin, fechaActual->getFechaActual());
}

Inmueble *ControladorInmueble::getInmueble(int codigoInmueble)
{
    return this->col_inmuebles[codigoInmueble];
}
std::set<DTInmuebleAdministrado*> ControladorInmueble::listarInmueblesAdministrados(std::string nickname) {
    IControladorUsuario* cU = Factory::getInstance()->getControladorUsuario();
    Inmobiliaria* inmo = cU->getInmobiliaria(nickname);
    std::set<DTInmuebleAdministrado*> resultado;

    if (inmo != nullptr) {
        std::set<AdministraPropiedad*> administraciones = inmo->getAdministradores();

        for (std::set<AdministraPropiedad*>::iterator it = administraciones.begin(); it != administraciones.end(); ++it) {
            AdministraPropiedad* ap = *it;
            Inmueble* inmueble = ap->getInmuebleAsociado();
            int codigo = inmueble->getCodigo();
            std::string direccion = inmueble->getDireccion();
            DTFecha* fecha = ap->getFecha();

            DTInmuebleAdministrado* dtia = new DTInmuebleAdministrado(codigo, direccion, fecha);
            resultado.insert(dtia);
        }
    }

    return resultado;
}
