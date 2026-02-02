#include "../include/Casa.h"
#include "../include/DTInmueble.h"
#include "../include/DTCasa.h"
#include "../include/TipoTecho.h"
#include <string>


Casa::Casa(int codigo, std::string direccion, int numeroPuerta,
    int superficie, int anioConstruccion, bool esPH, TipoTecho techo)
: Inmueble(codigo, direccion, numeroPuerta, superficie, anioConstruccion),
esPH(esPH), techo(techo) {}


DTInmueble * Casa::getDTInmueble() {
    DTInmueble *ret = new DTCasa(getCodigo(), getDireccion(), getNumeroPuerta(),
    getSuperficie(), getAnioConstruccion(), esPH, techo);

    return ret;
}
DTInmuebleListado* Casa::getDTInmuebleListado() {
    return new DTInmuebleListado(getCodigo(), getDireccion(), getNicknamePropietario());
}

Casa::~Casa() {}
