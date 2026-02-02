#include "../include/Suscripcion.h"
#include "../include/Publicacion.h"
#include "../include/Notificacion.h"
#include "../include/Inmobiliaria.h"

Suscripcion::Suscripcion(Inmobiliaria* inmo, DTFecha* fecha)
    : inmo(inmo), fecha(fecha) {}

Suscripcion::~Suscripcion() {
    delete fecha;
    for (auto n : notificaciones)
        delete n;
    notificaciones.clear();
}

std::list<Notificacion*> Suscripcion::getNotificaciones() {
    return notificaciones;
}

void Suscripcion::agregarNotificacion(Publicacion* pub) {
    Notificacion* notif = new Notificacion(pub);
    notificaciones.push_back(notif);
}

void Suscripcion::MarcarVistas() {
    for (Notificacion* n : notificaciones) {
        delete n;
    }
    notificaciones.clear();
}

Inmobiliaria* Suscripcion::getInmobiliaria() {
    return inmo;
}

DTFecha* Suscripcion::getFechaSuscripcion() {
    return fecha;
}