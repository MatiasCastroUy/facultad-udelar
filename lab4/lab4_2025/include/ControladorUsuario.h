#ifndef CONTROLADORUSUARIO_H
#define CONTROLADORUSUARIO_H

#include <map>
#include <vector>
#include <set> //agregado
#include <string>
#include "Usuario.h"
#include "Cliente.h"
#include "Notificacion.h"
#include "Propietario.h"
#include "Inmobiliaria.h"
#include "DTUsuario.h"
#include "_DTInmobiliariaListado.h"
#include "IControladorUsuario.h"

class ControladorUsuario : public IControladorUsuario
{
private:
    std::map<std::string, Usuario *> usuarios;
    static ControladorUsuario *ControladorU;
    ControladorUsuario();

public:
    static ControladorUsuario *getInstanciaU();
    bool altaCliente(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string apellido, std::string documento);
    bool altaPropietario(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string cuentaBancaria, std::string telefono);
    bool altaInmobiliaria(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string direccion, std::string url, std::string telefono);
    bool esContrasenaValida(const std::string &contrasena);
    bool existeNickname(const std::string &nickname);
    std::set<DTInmobiliariaListado *> listar_disponibles(std::string nickname);
    std::vector<DTUsuario *> listarPropietarios();
    std::set<DTUsuario *> listarInmobiliarias();
    std::set<Inmobiliaria *> getInmobiliarias();
    Inmobiliaria *getInmobiliaria(std::string nicknameInmobiliaria);
    std::set<Publicacion *> consultarNotificacionesRecibidas(std::string nickname);
    std::set<DTInmobiliariaListado > listar_suscripciones(std::string nickname);
    bool cancelar_suscripcion(std::string nicknameProp, std::string nicknameInmo);
    bool suscribirUsuario(const std::string nicknameUsuario, const std::string nicknameInmobiliaria); 
    bool representarPropietario(std::string nicknamePropietario, std::string nombreInmobiliaria);
    Usuario* getUsuario(std::string nickname);
    static void liberarInstancia() ;
    ~ControladorUsuario();
};

#endif
