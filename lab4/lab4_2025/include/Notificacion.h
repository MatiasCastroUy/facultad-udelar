#ifndef NOTIFICACION_H
#define NOTIFICACION_H

#include "Publicacion.h"
#include "DTFecha.h"

class Notificacion {
private:
    int codigoPublicacion;   
    DTFecha fecha; 

public:
    Notificacion(Publicacion* pub);
    DTFecha getFecha();
    int getCodigoPublicacion();
};

#endif