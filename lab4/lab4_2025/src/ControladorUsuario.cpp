#include "../include/ControladorUsuario.h"
#include "../include/Propietario.h"
#include "../include/DTUsuario.h"
#include <vector>
#include "../include/_DTInmobiliariaListado.h"
#include "../include/ControladorFechaActual.h"

#include <iostream> // DEBUG
#include "../include/Factory.h"
// implementacion singleton

ControladorUsuario *ControladorUsuario::ControladorU = NULL;
ControladorUsuario::ControladorUsuario() {}
ControladorUsuario::~ControladorUsuario() {
  while(!usuarios.empty()) {
    std::map<std::string,Usuario*>::iterator it_user = usuarios.begin();
    Usuario *user = it_user->second;
    delete user;
    usuarios.erase(it_user);
  } 
}

ControladorUsuario *ControladorUsuario::getInstanciaU()
{
    if (ControladorU == NULL)
        ControladorU = new ControladorUsuario();
    return ControladorU;
}

// el resto de operaciones

bool ControladorUsuario::esContrasenaValida(const std::string &contrasena)
{
    return contrasena.length() >= 6;
}

bool ControladorUsuario::existeNickname(const std::string &nickname)
{
    return usuarios.find(nickname) != usuarios.end();
}

bool ControladorUsuario::altaCliente(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string apellido, std::string documento)
{
    if (!esContrasenaValida(contrasena) || existeNickname(nickname))
    {
        return false;
    }

    // Crear nuevo cliente y agregar al mapa
    Cliente *nuevoCliente = new Cliente(nickname, contrasena, nombre, email, apellido, documento);
    usuarios[nickname] = nuevoCliente;

    return true;
}
bool ControladorUsuario::altaPropietario(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string cuentaBancaria, std::string telefono)
{
    if (!esContrasenaValida(contrasena) || existeNickname(nickname))
    {
        return false;
    }
    
    Propietario* nuevoProp = new Propietario(nickname, contrasena, nombre, email, cuentaBancaria, telefono);

    usuarios[nickname] = nuevoProp;

    return true;
}
bool ControladorUsuario::altaInmobiliaria(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string direccion, std::string url, std::string telefono)
{
    if (!esContrasenaValida(contrasena) || existeNickname(nickname))
    {
        return false;
    }

    // Crear nueva inmobiliaria y agregarla al mapa
    Inmobiliaria *nuevaInmobiliaria = new Inmobiliaria(nickname, contrasena, nombre, email, direccion, url, telefono);
    usuarios[nickname] = nuevaInmobiliaria;

    return true;
}

std::vector<DTUsuario *> ControladorUsuario::listarPropietarios()
{
    std::vector<DTUsuario *> resultado;

    for (std::map<std::string, Usuario *>::iterator it = usuarios.begin(); it != usuarios.end(); ++it)
    {
        Propietario *prop = dynamic_cast<Propietario *>(it->second);
        if (prop != nullptr)
        {
            resultado.push_back(new DTUsuario(prop->getNickname(), prop->getNombre()));
        }
    }

    return resultado;
}



std::set<Inmobiliaria *> ControladorUsuario::getInmobiliarias()
{
    std::set<Inmobiliaria *> resultado;

    for (std::map<std::string, Usuario *>::iterator it = usuarios.begin(); it != usuarios.end(); ++it)
    {
        Usuario *u = it->second;
        Inmobiliaria *i = dynamic_cast<Inmobiliaria *>(u);
        if (i != nullptr)
        {
            resultado.insert(i);
        }
    }
    return resultado;
}

std::set<DTUsuario *> ControladorUsuario::listarInmobiliarias()
{
    std::set<DTUsuario *> resultado;

    // primero: pedirle una instacia a la Clase controladorusuario
    IControladorUsuario *m = Factory::getInstance()->getControladorUsuario();

    // segundo: a user le pido la coleccion de inmobiliarias y las guardo en un set
    std::set<Inmobiliaria *> li = m->getInmobiliarias();

    // tercero: iterar sobre el set de inmobiliarias y obtener el nickname y nombre de cada inmobiliaria
    for (std::set<Inmobiliaria *>::iterator it = li.begin(); it != li.end(); ++it)
    {
        Inmobiliaria *i = *it;
        DTUsuario *res = i->getDTUsuario();
        resultado.insert(res);
    }
    return resultado;
}

Inmobiliaria *ControladorUsuario::getInmobiliaria(std::string nicknameInmobiliaria)
{
    std::map<std::string, Usuario *>::iterator it = usuarios.find(nicknameInmobiliaria);
    return static_cast<Inmobiliaria *>(it->second);
}

std::set<Publicacion*> ControladorUsuario::consultarNotificacionesRecibidas(std::string nickname) {
    UsuarioObservador* uo = dynamic_cast<UsuarioObservador*>(usuarios[nickname]);
    return uo->consultarNotificacionesRecibidas();
}

std::set<DTInmobiliariaListado> ControladorUsuario::listar_suscripciones(std::string nickname) {
    std::set<DTInmobiliariaListado> resultado;

    // Buscar el usuario por nickname
    std::map<std::string, Usuario*>::iterator itU = usuarios.find(nickname);
    if (itU == usuarios.end())
        return resultado;

    // Intentar convertirlo a UsuarioObservador
    UsuarioObservador* obs = dynamic_cast<UsuarioObservador*>(itU->second);
    if (!obs)
        return resultado;

    // Obtener la lista de suscripciones
    std::list<Suscripcion*>& suscripciones = obs->getSuscripciones();

    std::list<Suscripcion*>::iterator it;
    for (it = suscripciones.begin(); it != suscripciones.end(); ++it) {
        Inmobiliaria* inmo = (*it)->getInmobiliaria();
        if (inmo != NULL) {
            DTInmobiliariaListado dto(inmo->getNickname(), inmo->getNombre());
            resultado.insert(dto);
        }
    }

    return resultado;
}

bool ControladorUsuario::cancelar_suscripcion(std::string nicknameProp, std::string nicknameInmo)
{
    auto itUsuario = usuarios.find(nicknameProp);
    if (itUsuario == usuarios.end())
        return false;

    UsuarioObservador* observador = dynamic_cast<UsuarioObservador*>(itUsuario->second);
    if (!observador)
        return false;

    auto itInmo = usuarios.find(nicknameInmo);
    if (itInmo == usuarios.end())
        return false;

    Inmobiliaria* inmo = dynamic_cast<Inmobiliaria*>(itInmo->second);
    if (!inmo)
        return false;

    observador->quitarSuscripcion(inmo);
    inmo->quitarObservador(observador);
    return true;
}


std::set<DTInmobiliariaListado *> ControladorUsuario::listar_disponibles(std::string nickname) {
    std::set<DTInmobiliariaListado *> resultado;

    // Buscar el usuario por nickname
    std::map<std::string, Usuario*>::iterator itU = usuarios.find(nickname);
    if (itU == usuarios.end())
        return resultado;

    UsuarioObservador* obs = dynamic_cast<UsuarioObservador*>(itU->second);
    if (!obs)
        return resultado;

    // Obtener la lista de suscripciones del usuario
    std::list<Suscripcion*>& suscripciones = obs->getSuscripciones();

    // Recorrer todos los usuarios para encontrar inmobiliarias
    std::map<std::string, Usuario*>::iterator it;
    for (it = usuarios.begin(); it != usuarios.end(); ++it) {
        Inmobiliaria* inmo = dynamic_cast<Inmobiliaria*>(it->second);
        if (!inmo)
            continue;

        // Verificar si ya está suscripto
        bool yaSuscripto = false;
        std::list<Suscripcion*>::iterator itS;
        for (itS = suscripciones.begin(); itS != suscripciones.end(); ++itS) {
            if ((*itS)->getInmobiliaria() == inmo) {
                yaSuscripto = true;
                break;
            }
        }

        // Si no está suscripto, agregarla al resultado
        if (!yaSuscripto) {
            DTInmobiliariaListado* dto = new DTInmobiliariaListado(inmo->getNickname(), inmo->getNombre());
            resultado.insert(dto);
        }
    }

    return resultado;
}


bool ControladorUsuario::suscribirUsuario(const std::string nicknameUsuario, const std::string nicknameInmobiliaria) {

    UsuarioObservador* usuario = dynamic_cast<UsuarioObservador*>(usuarios[nicknameUsuario]);
    DTFecha* fechaActual = Factory::getInstance()->getControladorFechaActual()->getFechaActual();
    Inmobiliaria* inmo = getInmobiliaria(nicknameInmobiliaria);
    if(inmo != nullptr) {
      usuario->suscribir(inmo, fechaActual);
    }else {
      delete fechaActual;
      return false;
    }

    return true;
}

bool ControladorUsuario::representarPropietario(std::string nicknamePropietario, std::string nombreInmobiliaria) {
    // Acceder al propietario desde el map
    Propietario* prop = dynamic_cast<Propietario*>(usuarios[nicknamePropietario]);

    // Acceder a la inmobiliaria por nombre
    Inmobiliaria* inmo = getInmobiliaria(nombreInmobiliaria);

    // Agregar el usuario a la lista de propietarios representados por la inmobiliaria 
    if(prop != nullptr && inmo != nullptr)inmo->agregarPropietario(prop);

    return true;
}

Usuario* ControladorUsuario::getUsuario(std::string nickname) {
    auto it = usuarios.find(nickname);
    if (it != usuarios.end()) {
        return it->second;
    }
    return nullptr;
}

void ControladorUsuario::liberarInstancia() {
    if (ControladorU != nullptr) {
      delete ControladorU;
      ControladorU = nullptr;
    }
}
