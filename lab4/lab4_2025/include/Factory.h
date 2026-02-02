#ifndef FACTORY_H
#define FACTORY_H

#include "IControladorUsuario.h"
#include "IControladorInmueble.h"
#include "IControladorPublicacion.h"
#include "IControladorFechaActual.h"

class Factory {
private:
    static Factory* instance;
    Factory(); // constructor privado

public:
    static Factory* getInstance();

    IControladorUsuario* getControladorUsuario();
    IControladorInmueble* getControladorInmueble();
    IControladorPublicacion* getControladorPublicacion();
    IControladorFechaActual* getControladorFechaActual();
    void liberarControladorFechaActual();
    void liberarControladorUsuario();
    void liberarControladorInmueble();
    void liberarControladorPublicacion();
};

#endif

