
#ifndef CONTROLADORPUBLICACIONES_H
#define CONTROLADORPUBLICACIONES_H

#include <list>
#include <map>
#include <string>
#include "Publicacion.h"
#include "ControladorUsuario.h"
#include "DTPublicacion.h"
#include "TipoInmueble.h"
#include "IControladorPublicacion.h"

class ControladorPublicacion: public IControladorPublicacion
{
private:
    static int ultimoCodigopu; 

    std::map<int, Publicacion *> publicaciones; // mapea cada publicacion a su codigo
    static ControladorPublicacion *ControladorP; 
    ControladorPublicacion(); 
    ~ControladorPublicacion();
public:
    static ControladorPublicacion *getInstanciaP();
    bool altaPublicacion(std::string nicknameInmobiliaria,int codigoInmueble,TipoPublicacion tipo,std::string texto,float precio);
    void quitarPublicacion(int codigoPublicacion); // quita una pub de la coleccion
    bool existePublicacion(int codigo);
    Publicacion* obtenerPublicacion(int codigo);
    DTPublicacion* getDTPublicacion(Publicacion* pub, Inmobiliaria* inmo);
    std::set<DTPublicacion*> listarPublicacion(TipoPublicacion tipo, float min, float max,TipoInmueble tipoInmuebleFiltro);
    DTInmueble* detalleInmueblePublicacion(int codigoPublicacion);
    static void liberarInstancia() ;
};

#endif
