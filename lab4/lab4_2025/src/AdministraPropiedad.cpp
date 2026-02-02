#include "../include/AdministraPropiedad.h"
#include "../include/Inmueble.h"
#include "../include/Inmobiliaria.h"
#include "../include/Publicacion.h"
#include "../include/Factory.h"
#include "../include/ControladorPublicacion.h"

#include <list>


// Constructor y Destructor
AdministraPropiedad::AdministraPropiedad(DTFecha* fecha, Inmueble* inmuebleAsociado, Inmobiliaria* inmoAsociada) {
    this->fecha = fecha;
    this->inmuebleAsociado = inmuebleAsociado;
    this->inmoAsociada = inmoAsociada;
}

AdministraPropiedad::~AdministraPropiedad() {
    delete fecha;
    eliminarPublicaciones(); // destructor se asegura de liberar memoria
}

// Getters

DTFecha* AdministraPropiedad::getFecha() {
    return fecha;
}

Inmueble* AdministraPropiedad::getInmuebleAsociado() {
    return inmuebleAsociado;
}

std::list<Publicacion*> AdministraPropiedad::getPublicaciones() {
    return publicaciones;
}

Publicacion* AdministraPropiedad::ultimaPub() {
    if (publicaciones.empty())
        return NULL;
    return publicaciones.back();
}

// Modificadores

void AdministraPropiedad::agregarPublicacion(Publicacion *p) {
    publicaciones.push_back(p);
}

bool AdministraPropiedad::ActualizarPubActiva(Publicacion* Pub) {
    for (auto it = publicaciones.begin(); it != publicaciones.end(); ++it) {
        Publicacion* pubaux = *it;
        if (pubaux->getTipo() == Pub->getTipo()) {
            if (pubaux->getFechaAlta() == (Pub->getFechaAlta())) {
                delete Pub;  // descartar la nueva publicación
                return false;     
            } else if (pubaux->getActiva() && pubaux->getFechaAlta() < (Pub->getFechaAlta())) {
                pubaux->setActiva(false);
            } else if (pubaux->getActiva()) {
                Pub->setActiva(false);  // nueva es más vieja, no la activamos
            }
        }
    }
    return true; // se puede agregar la publicación
}

// Eliminación

// DESTRUYE TODOS LOS OBJETOS PUBLICACION ASOCIADOS Y LOS QUITA DE LA COLECCION GENERAL DEL CONTROLADOR
void AdministraPropiedad::eliminarPublicaciones() {

    IControladorPublicacion * conPub = Factory::getInstance()->getControladorPublicacion();

    while (!publicaciones.empty()) {
        Publicacion* ultima = publicaciones.back();
        conPub->quitarPublicacion(ultima->getCodigo());
        delete ultima;
        publicaciones.pop_back();
    }  
}

std::string AdministraPropiedad::getNicknameInmobiliaria() {
  return inmoAsociada->getNickname();
}

// quita la referencia a este admpropiedad de la inmoAsociada
void AdministraPropiedad::quitarInmobiliaria() {
    this->inmoAsociada = nullptr;
}
