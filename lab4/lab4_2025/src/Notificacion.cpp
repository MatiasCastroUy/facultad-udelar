#include "../include/Notificacion.h"
#include "../include/Publicacion.h"
#include <vector>

Notificacion::Notificacion(Publicacion* pub) :  
    fecha(pub->getFechaAlta()),
    codigoPublicacion(pub->getCodigo()) {}

int Notificacion::getCodigoPublicacion() {
    return codigoPublicacion;
}

DTFecha Notificacion::getFecha() {
    return fecha;
}

