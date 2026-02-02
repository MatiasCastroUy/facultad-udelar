#include "../include/UsuarioObservador.h"
#include "../include/ControladorPublicacion.h"
#include "../include/Notificacion.h"
#include "../include/Suscripcion.h"
#include "../include/Inmobiliaria.h" 
#include "../include/Factory.h"

UsuarioObservador::UsuarioObservador(std::string nickname, std::string contrasena, std::string nombre, std::string email)
    : Usuario(nickname, contrasena, nombre, email) {}

UsuarioObservador::~UsuarioObservador() {
    for (std::list<Suscripcion *>::iterator it = suscripciones.begin(); it != suscripciones.end(); ++it)
    {
        delete *it;
    }
    suscripciones.clear();
}

void UsuarioObservador::notificar(Publicacion* pub, Inmobiliaria* inmo) {
    std::list<Suscripcion*>::iterator it = suscripciones.begin();
    
    while (it != suscripciones.end() && (*it)->getInmobiliaria() != inmo) {
        ++it;
    }

    if (it != suscripciones.end()) {
        (*it)->agregarNotificacion(pub);
    }
}

std::list<Suscripcion*>& UsuarioObservador::getSuscripciones() {
    return suscripciones;
}

std::set<Publicacion*> UsuarioObservador::consultarNotificacionesRecibidas() {
    std::set<Publicacion *> resultado;

    // recorrer suscripciones del usuario
    for (std::list<Suscripcion *>::iterator it = suscripciones.begin(); it != suscripciones.end(); ++it)
    {
        Suscripcion *s = *it;
        // obtengo las notificaciones de la suscripcion
        const std::list<Notificacion *> &notifs = s->getNotificaciones();
        // para cada notificacion
        for (std::list<Notificacion *>::const_iterator itN = notifs.begin(); itN != notifs.end(); ++itN)
        {
            Notificacion *n = *itN;
            int codigo = n->getCodigoPublicacion();
            Factory *factory = Factory::getInstance();
            IControladorPublicacion *c_publicacion = factory->getControladorPublicacion();
           if (c_publicacion->existePublicacion(codigo)) {
                Publicacion *pub = c_publicacion->obtenerPublicacion(codigo);

                // suscripción debe ser anterior o igual al alta
                if (s->getFechaSuscripcion() <= pub->getFechaAlta() && pub->getActiva()) {
                    resultado.insert(pub);
                }
            }
        }
        s->MarcarVistas(); // Eliminar notificaciones luego de procesarlas
    }
    return resultado;
}

void UsuarioObservador::suscribir(Inmobiliaria* inmo, DTFecha* fecha) {
    // Crear la suscripción
    Suscripcion* nueva = new Suscripcion(inmo, fecha);

    // Agregar la suscripción a la lista del usuario
    suscripciones.push_back(nueva);

    // Agregar al usuario como observador de la inmobiliaria
    inmo->agregarObservador(this);
}

void UsuarioObservador::quitarSuscripcion(Inmobiliaria* inmo) {
    std::list<Suscripcion*>::iterator it = suscripciones.begin();

    while (it != suscripciones.end()) {
        if ((*it)->getInmobiliaria()->getNickname() == inmo->getNickname()) {
            delete *it; 
            it = suscripciones.erase(it);  
            return;  
        } else {
            ++it;
        }
    }
}