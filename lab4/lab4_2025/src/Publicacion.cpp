#include "../include/Publicacion.h"
#include <iostream>

//Constructor
Publicacion::Publicacion (int codigo, DTFecha* fecha, TipoPublicacion tipo,
                std::string texto, float precio, bool activa): 
                    codigo(codigo), fecha(fecha), tipo(tipo), texto(texto), precio(precio), activa(activa){}

//Destructor
Publicacion::~Publicacion(){
    delete fecha;
}

//Getters
int Publicacion::getCodigo(){
    return codigo;
}

DTFecha* Publicacion:: getFechaAlta(){
    return fecha;
}

TipoPublicacion Publicacion::getTipo(){
     return tipo;
}
std::string Publicacion::getTexto(){
    return texto;
}
float Publicacion::getPrecio(){
    return precio;
}
bool Publicacion::getActiva(){
    return activa;
}

void Publicacion::setActiva(bool activa) {
    this->activa = activa;
}