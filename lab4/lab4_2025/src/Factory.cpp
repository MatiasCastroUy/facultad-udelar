#include "../include/Factory.h"
#include "../include/ControladorUsuario.h"
#include "../include/ControladorInmueble.h"
#include "../include/ControladorPublicacion.h"
#include "../include/ControladorFechaActual.h"
#include <cstddef>

Factory* Factory::instance = NULL;

Factory::Factory() {}

Factory* Factory::getInstance() {
    if (instance == NULL)
        instance = new Factory();
    return instance;
}

IControladorUsuario* Factory::getControladorUsuario() {
    return ControladorUsuario::getInstanciaU();
}

IControladorInmueble* Factory::getControladorInmueble() {
    return ControladorInmueble::getInstanciaI();
}

IControladorPublicacion* Factory::getControladorPublicacion() {
    return ControladorPublicacion::getInstanciaP();
}

IControladorFechaActual* Factory::getControladorFechaActual() {
    return ControladorFechaActual::getInstanciaF();
}


void Factory::liberarControladorFechaActual() {
  ControladorFechaActual::liberarInstancia();
}

void Factory::liberarControladorUsuario() {
  ControladorUsuario::liberarInstancia();
}

void Factory::liberarControladorInmueble() {
  ControladorInmueble::liberarInstancia();
}

void Factory::liberarControladorPublicacion() {
  ControladorPublicacion::liberarInstancia();
}
