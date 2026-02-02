#ifndef SUSCRIPCION_H
#define SUSCRIPCION_H

#include "DTFecha.h"
#include "Notificacion.h"
#include <list>

class Inmobiliaria;       // Forward declaration

class Suscripcion {
private:
    DTFecha* fecha;
    std::list<Notificacion*> notificaciones;
    Inmobiliaria* inmo;    // solo si la necesitás

public:
    Suscripcion(Inmobiliaria* inmo, DTFecha* fecha);
    ~Suscripcion();
    Inmobiliaria* getInmobiliaria();
    DTFecha* getFechaSuscripcion() const;
    void MarcarVistas();
    std::list<Notificacion*> getNotificaciones();
    void agregarNotificacion(Publicacion* pub);
    DTFecha* getFechaSuscripcion();
};

#endif