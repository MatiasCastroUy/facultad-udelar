#ifndef ISUBJECT_H
#define ISUBJECT_H

#include "IObserver.h"
class Publicacion;  // Forward declaration

class ISubject {
public:
    virtual void agregarObservador(IObserver* obs) = 0;
    virtual void quitarObservador(IObserver* obs) = 0;
    virtual void notificarObservadores(Publicacion* pub) = 0;
    virtual ~ISubject() {}
};
#endif 