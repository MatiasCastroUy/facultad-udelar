#ifndef INMOBILIARIA_H
#define INMOBILIARIA_H

#include <string>
#include <set>
#include "Propietario.h"
#include "DTUsuario.h"
#include "DTInmuebleListado.h"
#include "AdministraPropiedad.h"
#include "Inmueble.h"

class AdministraPropiedad; // para compilar

#include "ISubject.h"
class Inmobiliaria : public Usuario, public ISubject {
private:
    std::set<Propietario *> propietarios;
    std::set<AdministraPropiedad *> administradores;
    std::string direccion;
    std::string url;
    std::string telefono;

    // Observadores (clientes suscriptos a esta inmobiliaria)
    std::set<IObserver*> observadores;
public:
    Inmobiliaria(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string direccion, std::string url, std::string telefono);
    DTUsuario *getDTUsuario();
    void altaAdministracionPropiedad(Inmueble *i, DTFecha *fechaActual);
    std::set<DTInmuebleListado *> getInmueblesNoAdmin();
    bool operator==(const Inmobiliaria &otra) const;
    std::set<AdministraPropiedad*> getAdministradores();
    AdministraPropiedad* RelacionInmoIn(Inmueble* inmueble);
    UsuarioObservador* getSuscriptor(const std::string& nickname);


    // Implementación de ISubject
    void agregarObservador(IObserver* obs);
    void quitarObservador(IObserver* obs);
    void notificarObservadores(Publicacion* pub);

    void agregarPropietario(Propietario* propietario);
   //destructor
    ~Inmobiliaria();
};

#endif
