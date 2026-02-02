#include "../include/ControladorPublicacion.h"
#include "../include/ControladorUsuario.h"  
#include "../include/DTPublicacion.h"
#include "../include/ControladorInmueble.h"
#include "../include/ControladorFechaActual.h"
#include "../include/AdministraPropiedad.h"
#include "../include/IObserver.h"
#include "../include/TipoInmueble.h"
#include "../include/Casa.h"
#include "../include/Apartamento.h"
#include <vector>
#include <set>
#include <map>
#include "../include/Factory.h"

// implementacion singleton

ControladorPublicacion *ControladorPublicacion::ControladorP = NULL;
ControladorPublicacion::ControladorPublicacion() {}
ControladorPublicacion::~ControladorPublicacion() {
  while(!publicaciones.empty()) {
    std::map<int,Publicacion*>::iterator it_pub = publicaciones.begin();
    Publicacion *pub = it_pub->second;
    delete pub;
    publicaciones.erase(it_pub);
  } 
}

ControladorPublicacion *ControladorPublicacion::getInstanciaP()
{
    if (ControladorP == NULL)
        ControladorP = new ControladorPublicacion();
    return ControladorP;
}
void ControladorPublicacion::liberarInstancia() {
    if (ControladorP!= nullptr) {
        delete ControladorP;
        ControladorP = nullptr;
    }
}

//Funciones
int ControladorPublicacion::ultimoCodigopu= 0;
bool ControladorPublicacion::altaPublicacion(std::string nicknameInmobiliaria,int codigoInmueble,TipoPublicacion tipo,std::string texto,float precio){
    
    //Checkeo si existe la inmobiliaria
    IControladorUsuario *CUsuario = Factory::getInstance()->getControladorUsuario();
    Inmobiliaria *inmo=CUsuario->getInmobiliaria(nicknameInmobiliaria);
    if (inmo==NULL)
        return false;
    
    //Checkeo si existe el inmueble
    IControladorInmueble *CInmueble = Factory::getInstance()->getControladorInmueble();
    Inmueble *inmueble=CInmueble->getInmueble(codigoInmueble);
    if (inmueble==NULL)
        return false;
    ultimoCodigopu++;
    //Verifico si la inmobiliaria administra al inmueble
    if (!inmueble->esAdministrado(nicknameInmobiliaria))
        return false;

    //Obtengo la fecha actual mediante el controlador
    DTFecha *fechaActual = Factory::getInstance()->getControladorFechaActual()->getFechaActual();

    //Buscar AdministraPropiedad
    AdministraPropiedad *adm= inmo->RelacionInmoIn(inmueble);
    if (adm == nullptr)
        return false;

    //Obtengo la ultima publicacion para el codigo de la nueva publicacion
    Publicacion* ultPub=adm->ultimaPub();
    Publicacion *nuevaPub=NULL;
    //Despues de checkear todo creo la publicacion
   
    nuevaPub = new Publicacion(ultimoCodigopu, fechaActual, tipo, texto, precio, true);
    

    bool actualizo= adm->ActualizarPubActiva(nuevaPub);
    if (!actualizo)
        return false;
    
    inmo->notificarObservadores(nuevaPub);

    // Asociar publicación a la administración de propiedad
    adm->agregarPublicacion(nuevaPub);

    // Agrego la referencia de la publicacion al map

    this->publicaciones[nuevaPub->getCodigo()] = nuevaPub;

    return true;
}   
void ControladorPublicacion::quitarPublicacion(int codigoPublicacion) {
    publicaciones.erase(codigoPublicacion);
}

bool ControladorPublicacion::existePublicacion(int codigo) {
    return publicaciones.find(codigo) != publicaciones.end();
}

Publicacion* ControladorPublicacion::obtenerPublicacion(int codigo) {
    if (existePublicacion(codigo)) {
        return publicaciones[codigo];
    }
    return nullptr;
}
DTPublicacion* ControladorPublicacion::getDTPublicacion(Publicacion* pub, Inmobiliaria* inmo) {
    int codigo = pub->getCodigo();
    DTFecha* fecha = pub->getFechaAlta();
    std::string texto = pub->getTexto();
    float precio = pub->getPrecio();
    std::string precioStr = std::to_string(precio);
    std::string nombreInmo = inmo->getNombre();
    
    return new DTPublicacion(codigo, fecha, texto, precioStr, nombreInmo);
}
std::set<DTPublicacion*> ControladorPublicacion::listarPublicacion(TipoPublicacion tipo, float min, float max,TipoInmueble tipoInmuebleFiltro) {
    std::set<DTPublicacion*> resultado;

    // Obtener el controlador de usuario
    IControladorUsuario* ctrlUsuario = Factory::getInstance()->getControladorUsuario();
    // Obtener todas las inmobiliarias registradas
    std::set<Inmobiliaria*> inmobiliarias = ctrlUsuario->getInmobiliarias();

    // Recorrer cada inmobiliaria
    for (std::set<Inmobiliaria*>::iterator itInmo = inmobiliarias.begin(); itInmo != inmobiliarias.end(); ++itInmo) {
        Inmobiliaria* inmo = *itInmo;
        std::set<AdministraPropiedad*> admins = inmo->getAdministradores();

        // Recorrer las administraciones de esa inmobiliaria
        for (std::set<AdministraPropiedad*>::iterator itAdm = admins.begin(); itAdm != admins.end(); ++itAdm) {
            AdministraPropiedad* admin = *itAdm;
            std::list<Publicacion*> pubs = admin->getPublicaciones();

            // Recorrer publicaciones
            for (std::list<Publicacion*>::iterator itPub = pubs.begin(); itPub != pubs.end(); ++itPub) {
                Publicacion* pub = *itPub;
              if (pub->getActiva()==false)
              {
                continue;
              }
              
                // Si no es del tipo pedido de inmueble, ignorar
                if (tipoInmuebleFiltro!=Todos){
                    Inmueble* inmueble = admin->getInmuebleAsociado();
                    if (dynamic_cast<Casa*>(inmueble) != nullptr){
                        if(tipoInmuebleFiltro!=CasaTipo){
                            continue;
                        }
                    }
                    else if(dynamic_cast<Apartamento*>(inmueble) != nullptr){
                        if(tipoInmuebleFiltro!=ApartamentoTipo){
                            continue;
                        }
                    }
                }

                // Si no es del tipo pedido de publicacion pedido, ignorar
                if (pub->getTipo() != tipo)
                    continue;

                // Si el precio no está en el rango, ignorar
                float precio = pub->getPrecio();
                if (precio < min || precio > max)
                    continue;

                // Crear el DTPublicacion y agregar al resultado
                DTPublicacion* dt = getDTPublicacion(pub, inmo);
                resultado.insert(dt);

            }
        }
    }

    return resultado;
    
}
DTInmueble* ControladorPublicacion::detalleInmueblePublicacion(int codigoPublicacion) {
    // Verifica que exista la publicación
    if (!existePublicacion(codigoPublicacion)) {
        return nullptr;
    }

    // Obtiene la publicación
    Publicacion* pub = obtenerPublicacion(codigoPublicacion);

    // Recorre las inmobiliarias
    std::set<Inmobiliaria*> inmos = Factory::getInstance()->getControladorUsuario()->getInmobiliarias();
    for (std::set<Inmobiliaria*>::iterator it = inmos.begin(); it != inmos.end(); ++it) {
        Inmobiliaria* inmo = *it;
        std::set<AdministraPropiedad*> admins = inmo->getAdministradores();

        for (std::set<AdministraPropiedad*>::iterator ait = admins.begin(); ait != admins.end(); ++ait) {
            AdministraPropiedad* admin = *ait;

            std::list<Publicacion*> pubs = admin->getPublicaciones();
            for (std::list<Publicacion*>::iterator pit = pubs.begin(); pit != pubs.end(); ++pit) {
                Publicacion* actual = *pit;

                if (actual->getCodigo() == codigoPublicacion) {
                    Inmueble* in = admin->getInmuebleAsociado();

                    return in->getDTInmueble();
                }
            }
        }
    }

    return nullptr; // Si no se encontró nada
}
