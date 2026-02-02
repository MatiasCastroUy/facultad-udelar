#include "../include/Inmobiliaria.h"
#include "../include/UsuarioObservador.h"
#include "../include/Publicacion.h"

Inmobiliaria::Inmobiliaria(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string direccion, std::string url, std::string telefono) : Usuario(nickname, contrasena, nombre, email)
{
    this->direccion = direccion;
    this->url = url;
    this->telefono = telefono;
}

Inmobiliaria::~Inmobiliaria() {}

DTUsuario *Inmobiliaria::getDTUsuario()
{
    std::string nick = getNickname();
    std::string nom = getNombre();
    DTUsuario *asd = new DTUsuario(nick, nom);
    return asd;
}

std::set<DTInmuebleListado *> Inmobiliaria::getInmueblesNoAdmin()
{
    std::set<DTInmuebleListado *> resultado;

    for (std::set<Propietario *>::iterator it = propietarios.begin(); it != this->propietarios.end(); ++it)
    {
        Propietario *p = *it;
        std::set<DTInmuebleListado *> listInmueblesPropietario = p->getInmueblesNoAdminInmobiliaria(this->getNickname());
        resultado.insert(listInmueblesPropietario.begin(), listInmueblesPropietario.end()); // se supone que con esto fusiono todos los set's de inmuebles no administrados asociados a un solo propietario en un set mayor con multiples propietarios
    }

    return resultado;
}

// sobrecarga de "=="

bool Inmobiliaria::operator==(const Inmobiliaria &otra) const
{
    return this->direccion == otra.direccion && this->url == otra.url && this->telefono == otra.telefono;
}

void Inmobiliaria::altaAdministracionPropiedad(Inmueble *i, DTFecha *fechaActual)
{
    AdministraPropiedad *ap = new AdministraPropiedad(fechaActual, i, this);
    this->administradores.insert(ap);
    i->asociarAdministracionPropiedad(ap);
}
std::set<AdministraPropiedad *> Inmobiliaria::getAdministradores()
{
    return this->administradores;
}

AdministraPropiedad *Inmobiliaria::RelacionInmoIn(Inmueble *inmueble)
{
    for (auto ap : administradores)
    {
        if (ap->getInmuebleAsociado() == inmueble)
            return ap;
    }
    return nullptr;
}

// Para el patron observer

void Inmobiliaria::agregarObservador(IObserver *obs)
{
    observadores.insert(obs);
}

void Inmobiliaria::quitarObservador(IObserver *obs)
{
    observadores.erase(obs);
}

void Inmobiliaria::notificarObservadores(Publicacion *pub)
{
   for (auto it = observadores.begin(); it != observadores.end(); ++it) {
        (*it)->notificar(pub, this);
    }
}
UsuarioObservador* Inmobiliaria::getSuscriptor(const std::string& nickname) {
    for (auto it = observadores.begin(); it != observadores.end(); ++it) {
        UsuarioObservador* uo = dynamic_cast<UsuarioObservador*>(*it);
        if (uo && uo->getNickname() == nickname) {
            return uo;
        }
    }
    return nullptr;
}

void Inmobiliaria::agregarPropietario(Propietario *propietario) {
  this->propietarios.insert(propietario);
}
