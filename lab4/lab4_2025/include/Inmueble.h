#ifndef INMUEBLE_H
#define INMUEBLE_H
#include <string>
#include <list>
#include "DTInmueble.h"
#include "DTInmuebleListado.h"

// declaraciones hacia adelante
class Inmobiliaria;
class AdministraPropiedad;
class Propietario;
class Propietario;

class Inmueble
{
private:
    int codigo;
    std::string direccion;
    int numeroPuerta;
    int superficie;
    int anioConstruccion;
    std::list<AdministraPropiedad *> administradores;
 std::string nicknamePropietario;

    Propietario *propietario; // DOBLE VISIBILIDAD ?

public:
    Inmueble(int codigo, std::string direccion, int numeroPuerta, int superficie, int anioConstruccion);
    virtual ~Inmueble();

    int getCodigo() const;
    std::string getDireccion() const;
    int getNumeroPuerta() const;
    int getSuperficie() const;
    int getAnioConstruccion() const;
    std::string getNicknamePropietario() const;
    void asociarAdministracionPropiedad(AdministraPropiedad *ap);

    bool esAdministrado(std::string nicknameInmobiliaria);
   void setNicknamePropietario(std::string nick);
    virtual DTInmueble* getDTInmueble() = 0;
        virtual DTInmuebleListado* getDTInmuebleListado() = 0;
void setPropietario(Propietario* p);

    void eliminarReferencias();
    //void setNicknamePropietario(const std::string &nickname); // no aplica si tenemos propietario por referencia

};

#endif
