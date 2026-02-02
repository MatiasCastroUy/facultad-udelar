#ifndef ICONTROLADORUSUARIO_H
#define ICONTROLADORUSUARIO_H

#include <vector>
#include <set>
#include <string>
#include "DTUsuario.h"
#include "_DTInmobiliariaListado.h"
#include "Publicacion.h"

// Forward declarations
class Inmobiliaria;
class Usuario;
class Propietario;
class Publicacion;

class IControladorUsuario {
public:
    virtual ~IControladorUsuario() {}

    virtual bool altaCliente(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string apellido, std::string documento) = 0;
    virtual bool altaPropietario(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string cuentaBancaria, std::string telefono) = 0;
    virtual bool altaInmobiliaria(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string direccion, std::string url, std::string telefono) = 0;
    virtual std::vector<DTUsuario *> listarPropietarios() = 0;
    virtual std::set<Inmobiliaria *> getInmobiliarias() = 0;
    virtual Inmobiliaria *getInmobiliaria(std::string nicknameInmobiliaria) = 0;
    virtual std::set<DTUsuario *> listarInmobiliarias() = 0;
    virtual std::set<Publicacion *> consultarNotificacionesRecibidas(std::string nickname) = 0;
    virtual std::set<DTInmobiliariaListado> listar_suscripciones(std::string nickname) = 0;
    virtual bool cancelar_suscripcion(std::string nicknameProp, std::string nicknameInmo) = 0;
    virtual std::set<DTInmobiliariaListado *> listar_disponibles(std::string nickname) = 0;
    virtual bool suscribirUsuario(const std::string nicknameUsuario, const std::string nicknameInmobiliaria) = 0;
    virtual bool representarPropietario(std::string nicknamePropietario, std::string nombreInmobiliaria) = 0;
    virtual Usuario* getUsuario(std::string nickname) = 0;
};

#endif
