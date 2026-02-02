#include "../include/Apartamento.h"
#include "../include/DTInmueble.h"
#include "../include/DTApartamento.h"
#include "../include/TipoTecho.h"
#include <string>


Apartamento::Apartamento(int codigo, std::string direccion, int numeroPuerta,
    int superficie, int anioConstruccion,
    int piso, bool tieneAscensor, float gastosComunes)
: Inmueble(codigo, direccion, numeroPuerta, superficie, anioConstruccion),
piso(piso), tieneAscensor(tieneAscensor), gastosComunes(gastosComunes) {}

DTInmueble * Apartamento::getDTInmueble() {
    DTInmueble *ret = new DTApartamento(getCodigo(), getDireccion(), getNumeroPuerta(),
    getSuperficie(), getAnioConstruccion(), piso, tieneAscensor, gastosComunes);

    return ret;
}
DTInmuebleListado* Apartamento::getDTInmuebleListado() {
    return new DTInmuebleListado(getCodigo(), getDireccion(), getNicknamePropietario());
}
Apartamento::~Apartamento() {} 

///