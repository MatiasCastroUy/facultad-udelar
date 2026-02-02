#ifndef USUARIO_OBSERVADOR_H
#define USUARIO_OBSERVADOR_H

#include "Usuario.h"
#include "IObserver.h"
#include "Publicacion.h"
#include <list>
#include <vector>
#include <set>
class Suscripcion;
class Inmobiliaria;

class UsuarioObservador : public Usuario, public IObserver {
protected:
    std::list<Suscripcion*> suscripciones;

public:
    UsuarioObservador(std::string nickname, std::string contrasena, std::string nombre, std::string email);

    std::set<Publicacion*> consultarNotificacionesRecibidas();
    std::list<Suscripcion*>& getSuscripciones();

    // Implementación de IObserver
    void notificar(Publicacion* pub, Inmobiliaria* inmo);
    virtual ~UsuarioObservador();

    //suscribirse
    void suscribir(Inmobiliaria* inmo, DTFecha* fecha);
    void quitarSuscripcion(Inmobiliaria* inmo); 
};

#endif
