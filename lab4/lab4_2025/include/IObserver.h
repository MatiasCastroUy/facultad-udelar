#ifndef IOBSERVER_H
#define IOBSERVER_H

class Publicacion;  // Forward declaration para evitar incluir todo si no hace falta
class Inmobiliaria;

class IObserver {
public:
    virtual void notificar(Publicacion* pub, Inmobiliaria* inmo) = 0;
    virtual ~IObserver() {}
};

#endif