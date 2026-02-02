#include "../include/Propietario.h"
#include "../include/Inmueble.h"
#include "../include/Suscripcion.h"

Propietario::Propietario(std::string nickname, std::string contrasena, std::string nombre, std::string email,
                         std::string cuentaBancaria, std::string telefono)
    : UsuarioObservador(nickname, contrasena, nombre, email),
      cuentaBancaria(cuentaBancaria),
      telefono(telefono){

      }

Propietario::~Propietario() {}

std::set<DTInmuebleListado*> Propietario::getInmueblesNoAdminInmobiliaria(std::string nicknameInmobiliaria) {
  std::set<DTInmuebleListado*> lista;

  for(std::set<Inmueble*>::iterator it = this->inmuebles.begin(); it!= this->inmuebles.end(); ++it) {
    Inmueble *in = *it;
    if(!in->esAdministrado(nicknameInmobiliaria)) {
      int cod = in->getCodigo();
      std::string direccion = in->getDireccion();
      DTInmuebleListado *nuevoDT = new DTInmuebleListado(cod,direccion,this->getNickname());
      lista.insert(nuevoDT);
    }
  }
  return lista;
}

// PRECOND: EXISTE INMUEBLE CON CODIGO codigoInmueble
void Propietario::quitarInmueble(int codigoInmueble) {
  std::set<Inmueble *>::iterator it = inmuebles.begin();
  while((*it)->getCodigo() != codigoInmueble) {it++;}; // por precondicion lo tiene que encontrar
  inmuebles.erase(it); 
}

void Propietario::quitarSuscripcion(Inmobiliaria* inmo) {
   for (auto it = suscripciones.begin(); it != suscripciones.end(); ++it) {
       if ((*it)->getInmobiliaria() == inmo) {
           delete *it; // si manejás memoria manual
           suscripciones.erase(it);
           break;
       }
   }
}

void Propietario::agregarInmueble(Inmueble* i) {
   inmuebles.insert(i);
}
