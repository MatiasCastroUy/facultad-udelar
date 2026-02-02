#include "../include/Inmueble.h"
#include "../include/AdministraPropiedad.h"
#include "../include/Propietario.h"
#include <string>
#include <list>

Inmueble::Inmueble(int codigo, std::string direccion, int numeroPuerta, int superficie, int anioConstruccion)
    : codigo(codigo), direccion(direccion), numeroPuerta(numeroPuerta),
      superficie(superficie), anioConstruccion(anioConstruccion) {}


bool Inmueble::esAdministrado(std::string nicknameInmobiliaria) {
  for(std::list<AdministraPropiedad*>::iterator it = administradores.begin(); it != administradores.end(); ++it) {
    AdministraPropiedad *adm = *it;
    if(adm->getNicknameInmobiliaria() == nicknameInmobiliaria) return true;
  }

  return false;
}

int Inmueble::getCodigo() const
{
    return codigo;
}

std::string Inmueble::getDireccion() const
{
    return direccion;
}

int Inmueble::getNumeroPuerta() const
{
    return numeroPuerta;
}

int Inmueble::getSuperficie() const
{
    return superficie;
}

int Inmueble::getAnioConstruccion() const
{
    return anioConstruccion;
}

std::string Inmueble::getNicknamePropietario() const
{
    return this->propietario->getNickname();
}

void Inmueble::eliminarReferencias()
{

    // quito la referencia a este inmueble en el propietario
    propietario->quitarInmueble(codigo);

    // quitar los AdministraPropiedad
    while (!administradores.empty())
    {
        AdministraPropiedad *adm = administradores.front();
        administradores.pop_front();
        adm->eliminarPublicaciones();
        adm->quitarInmobiliaria();

        delete adm;
    }
}

void Inmueble::asociarAdministracionPropiedad(AdministraPropiedad *ap)
{
    this->administradores.push_back(ap);
}

Inmueble::~Inmueble(){}

void Inmueble::setNicknamePropietario(std::string nick) {
   nicknamePropietario = nick;
}

void Inmueble::setPropietario(Propietario* p) {
   this->propietario = p;
}
