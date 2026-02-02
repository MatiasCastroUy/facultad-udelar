#ifndef PROPIETARIO_H
#define PROPIETARIO_H
#include "Usuario.h"
#include "UsuarioObservador.h"
#include "DTInmuebleListado.h"
#include <string>
#include <set>

class Inmueble;
class Inmobiliaria;

class Propietario : public UsuarioObservador {
private:
    std::set<Inmueble *> inmuebles;
    // Inmobiliaria* inmorepresentante;
    std::string cuentaBancaria;
    std::string telefono;

public:
    Propietario(std::string nickname, std::string contrasena, std::string nombre, std::string email,
    std::string cuentaBancaria, std::string telefono);
    ~Propietario();
    std::set<DTInmuebleListado *> getInmueblesNoAdminInmobiliaria(std::string nicknameInmobiliaria);
    // Inmobiliaria *getInmoRepr();
    void agregarInmueble(Inmueble* i);
    void quitarInmueble(int codigoInmueble);
    void quitarSuscripcion(Inmobiliaria* inmobiliaria);
    void agregarInmobiliariaRepresentante(Inmobiliaria* inmo);
};

#endif
