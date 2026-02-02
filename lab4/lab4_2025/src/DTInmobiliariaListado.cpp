#include "../include/_DTInmobiliariaListado.h"

DTInmobiliariaListado::DTInmobiliariaListado(std::string nickname, std::string nombre)
    : nickname(nickname), nombre(nombre) {}

std::string DTInmobiliariaListado::getNickname() const {
    return nickname;
}

std::string DTInmobiliariaListado::getNombre() const {
    return nombre;
}

bool DTInmobiliariaListado::operator<(const DTInmobiliariaListado& otro) const {
    return nickname < otro.nickname;
}
