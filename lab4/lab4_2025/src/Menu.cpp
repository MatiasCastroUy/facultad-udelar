#include <iostream>
#include "../include/Menu.h"
#include "../include/CargaDatos.h"
#include "../include/TipoInmueble.h"
#include "../include/TipoPublicacion.h"
#include "../include/TipoTecho.h"
#include "../include/Factory.h"
#include "../include/DTApartamento.h"
#include "../include/DTCasa.h"
#include "../include/DTFecha.h"
#include "../include/DTInmueble.h"
#include "../include/DTInmuebleAdministrado.h"
#include "../include/DTInmuebleListado.h"
#include "../include/DTPublicacion.h"
#include "../include/DTUsuario.h"
#include <string>
#include <set>

#include "../include/IControladorUsuario.h"
#include "../include/IControladorInmueble.h"
#include "../include/IControladorPublicacion.h"

#include "../include/Inmueble.h"
#include "../include/Propietario.h"

void mostrarMenu()
{
    std::cout << "=== Menu de Operaciones ===" << std::endl;
    std::cout << "1. Alta de Usuario" << std::endl;
    std::cout << "2. Alta de Publicacion" << std::endl;
    std::cout << "3. Consulta de Publicaciones" << std::endl;
    std::cout << "4. Eliminar Inmueble" << std::endl;
    std::cout << "5. Suscribirse a Notificaciones" << std::endl;
    std::cout << "6. Consulta de Notificaciones" << std::endl;
    std::cout << "7. Eliminar Suscripciones" << std::endl;
    std::cout << "8. Alta de Administracion de Propiedad" << std::endl;
    std::cout << "9. Cargar Datos" << std::endl;
    std::cout << "10. Ver fecha actual" << std::endl;
    std::cout << "11. Asignar fecha actual" << std::endl;
    std::cout << "0. Salir" << std::endl;
    std::cout << "Ingrese el codigo de operacion: ";
}

int obtenerOpcion()
{
    int opcion;
    std::cin >> opcion;
    std::cin.ignore(); // esto es para consumir el salto de linea (/n) luego de ingresar "opcion"
    return opcion;
}

void ejecutarOpcion(int opcion)
{
    switch (opcion)
    {
    case 1:
        std::cout << " - ALTA DE USUARIO - " << std::endl;
        altaUsuario();
        break;
    case 2:
        std::cout << " - ALTA DE PUBLICACION - " << std::endl;
        altaPublicacion();
        break;
    case 3:
        std::cout << " - CONSULTA DE PUBLICACIONES - " << std::endl;
        consultaPublicaciones();
        break;
    case 4:
        std::cout << " - ELIMINAR INMUEBLE - " << std::endl;
        eliminarInmueble();
        break;
    case 5:
        std::cout << " - SUSCRIBIRSE A NOTIFICACIONES - " << std::endl;
        suscribirseNotificaciones();
        break;
    case 6:
        std::cout << " - CONSLTAR NOTIFICACIONES - " << std::endl;
        consultaNotificaciones();
        break;
    case 7:
        std::cout << " - ELIMINAR SUSCRIPCIONES - " << std::endl;
        eliminarSuscripciones();
        break;
    case 8:
        std::cout << " - ALTA ADMINISTRACION DE PROPIEDAD - " << std::endl;
        altaAdministracionPropiedad();
        break;
    case 9:
        std::cout << " - CARGAR DATOS - " << std::endl;
        cargarDatos();
        break;
    case 10:
        std::cout << " - VER FECHA ACTUAL - " << std::endl;
        verFechaActual();
        break;
    case 11:
        std::cout << " - ASIGNAR FECHA ACTUAL - " << std::endl;
        asignarFechaActual();
        break;
    case 0:
        std::cout << "Saliendo del programa..." << std::endl;
        Salir();
    default:
        std::cout << "Opcion no valida. Intente de nuevo." << std::endl;
    }
}

void altaUsuario()
{
    Factory *factory = Factory::getInstance();
    IControladorUsuario *c_Usuario = factory->getControladorUsuario();

    IControladorInmueble *c_Inmueble = factory->getControladorInmueble();

    std::cout << "Ingrese el tipo de usuario (0: Cliente, 1: Inmobiliaria, 2: Propietario): ";
    int tipoUsuario;
    std::cin >> tipoUsuario;
    std::cin.ignore();
    if (tipoUsuario < 0 || tipoUsuario > 2)
    {
        std::cout << "Opcion no valida. Intente de nuevo." << std::endl;
        return;
    }

    bool usuarioOk = false;

    std::string nickname;
    std::string contrasena;
    std::string nombre;
    std::string email;
    std::string apellido;
    std::string documento;
    std::string url;
    std::string telefono;
    std::string direccion;
    std::string cuentaBancaria;

    std::cout << "Nickname: ";
    std::getline(std::cin, nickname);
    std::cout << "Contrasena: ";
    std::getline(std::cin, contrasena);
    std::cout << "Nombre: ";
    std::getline(std::cin, nombre);
    std::cout << "Email: ";
    std::getline(std::cin, email);

    if (tipoUsuario == 0)
    {
        std::cout << "Apellido: ";
        std::getline(std::cin, apellido);
        std::cout << "Documento: ";
        std::getline(std::cin, documento);
        usuarioOk = c_Usuario->altaCliente(nickname, contrasena, nombre, email, apellido, documento);
    }
    else if (tipoUsuario == 1)
    {
        std::cout << "Direccion: ";
        std::getline(std::cin, direccion);
        std::cout << "URL: ";
        std::getline(std::cin, url);
        std::cout << "Telefono: ";
        std::getline(std::cin, telefono);
        usuarioOk = c_Usuario->altaInmobiliaria(nickname, contrasena, nombre, email, direccion, url, telefono);
    }
    else if (tipoUsuario == 2)
    {

        std::cout << "Cuenta Bancaria: ";
        std::getline(std::cin, cuentaBancaria);
        std::cout << "Telefono: ";
        std::getline(std::cin, telefono);
        usuarioOk = c_Usuario->altaPropietario(nickname, contrasena, nombre, email, cuentaBancaria, telefono);
        c_Inmueble->setPropietarioRecordado(dynamic_cast<Propietario*>(c_Usuario->getUsuario(nickname)));
    }
    if (usuarioOk)
    {
        std::cout << "Usuario dado de alta con éxito.\n";
        if (tipoUsuario == 1 || tipoUsuario == 2)
        {
            int salir = 1;
            std::cout << "¿Quiere ingresar los datos relacionados? (1: Si, 0: No): ";
            std::cin >> salir;
            std::cin.ignore();
            std::string inmuebleDireccion;
            int numeroPuerta;
            int superficie;
            int anoConstruccion;
            while (salir != 0)
            {
                if (tipoUsuario == 1)
                {
                    std::cout << "Lista de Propietarios:\n";
                    std::vector<DTUsuario *> propietarios = c_Usuario->listarPropietarios();
                    for (std::vector<DTUsuario *>::iterator it = propietarios.begin(); it != propietarios.end(); ++it)
                    {
                    std::cout << "- Nickname: " << (*it)->getNickname() << ", Nombre: " << (*it)->getNombre() << std::endl;
                    }
                    std::cout << "Nickname propietario a representar: ";
                    std::string nicknamePropietario;
                    std::getline(std::cin, nicknamePropietario);
                    std::string nombreInmobiliaria = nickname;  // usamos el nickname ingresado al crear la inmobiliaria
                    bool ok = c_Usuario->representarPropietario(nicknamePropietario, nombreInmobiliaria);
                    if (ok)
                    {
                    std::cout << "Se representará al propietario " << nicknamePropietario << " correctamente.\n";
                    }
                     else
                    {
                    std::cout << "No se pudo representar al propietario.\n";
                    }

                // Liberar memoria de los DTUsuario*
                     for (std::vector<DTUsuario *>::iterator it = propietarios.begin(); it != propietarios.end(); ++it)
                    {
                     delete *it;
                    }
                }
                else if (tipoUsuario == 2)
                {
                    
                    int tipoInmueble;
                    std::cout << "Indique el tipo de inmueble (1: Casa, 0: Apartamento): ";
                    std::cin >> tipoInmueble;
                    std::cin.ignore();
                    std::cout << "Direccion: ";
                    std::getline(std::cin, inmuebleDireccion);
                    std::cout << "Numero de Puerta: ";
                    std::cin >> numeroPuerta;
                    std::cin.ignore();
                    std::cout << "Superficie: ";
                    std::cin >> superficie;
                    std::cin.ignore();
                    std::cout << "Ano de Construccion: ";
                    std::cin >> anoConstruccion;
                    std::cin.ignore();

                    if (tipoInmueble == 1)
                    {
                        std::cout << "Es PH (1 para si, 0 para no): ";
                        int inEsPH;
                        std::cin >> inEsPH;
                        bool esPH = (inEsPH == 1);
                        std::cin.ignore();
                        std::cout << "Tipo de Techo (0: Liviano 1: A dos aguas, 2: Plano): ";
                        int inTipoTecho;
                        std::cin >> inTipoTecho;
                        std::cin.ignore();
                        TipoTecho techo = Liviano;
                        if (inTipoTecho == 1)
                        {
                            techo = A_dos_aguas;
                        }
                        else if (inTipoTecho == 2)
                        {
                            techo = Plano;
                        }
                     
                         c_Inmueble->altaCasa(inmuebleDireccion, numeroPuerta, superficie, anoConstruccion, esPH, techo);
                    }
                    else
                    {
                        int piso;
                        std::cout << "Piso: ";
                        std::cin >> piso;
                        std::cin.ignore();
                        std::cout << "Tiene Ascensor (1 para si, 0 para no): ";
                        int inTieneAscensor;
                        std::cin >> inTieneAscensor;
                        bool tieneAscensor = (inTieneAscensor == 1);
                        std::cin.ignore();
                        std::cout << "Gastos Comunes: ";
                        float gastosComunes;
                        std::cin >> gastosComunes;
                        std::cin.ignore();
                        

                      c_Inmueble->altaApartamento(inmuebleDireccion, numeroPuerta, superficie, anoConstruccion, piso, tieneAscensor, gastosComunes);
                    }
                }
            std::cout << "¿Quiere seguir ingresando? (1: Si, 0: No): ";
            std::cin >> salir;
            std::cin.ignore();
            }
        }
        // TODO: controlador->finalizarAltaUsuario();
    }
    else
    {
        std::cout << "Error al crear el usuario" << std::endl;
    }
}

void altaPublicacion()
{
    Factory *factory = Factory::getInstance();
    IControladorUsuario *c_Usuario = factory->getControladorUsuario();
    IControladorInmueble *c_Inmueble = factory->getControladorInmueble();
    IControladorPublicacion *c_Publicacion = factory->getControladorPublicacion();

    std::cout << "Lista de Inmobiliarias:\n";
    std::set<DTUsuario*> inmobiliarias = c_Usuario->listarInmobiliarias();
    // Recorrer la coleccion Mostrar "- Nickname: xx, Nombre: zz";
     for(std::set<DTUsuario *>::iterator it = inmobiliarias.begin(); it != inmobiliarias.end(); ++it) {
        std::cout << "Nickname: " << (*it)->getNickname() << ", Nombre: " << (*it)->getNombre()  << std::endl;
        delete *it;
    }
    std::cout << "Nickname de la inmobiliaria: ";
    std::string nicknameInmobiliaria;
    std::getline(std::cin, nicknameInmobiliaria);
    std::set<DTInmuebleAdministrado *> inAdm= c_Inmueble->listarInmueblesAdministrados(nicknameInmobiliaria);
    // Recorrer la coleccion Mostrar "- Codigo: xx, Direccion: yy, Propietario: zzz"
    for(std::set<DTInmuebleAdministrado *>::iterator it = inAdm.begin(); it != inAdm.end(); ++it) {
        std::string propietario = "Desconocido";
        Inmueble* in = c_Inmueble->getInmueble((*it)->getCodigo());
        if (in != nullptr){
        propietario = in->getNicknamePropietario();
        }
        std::cout << "Codigo " << (*it)->getCodigo() << ", Direccion: " << (*it)->getDireccion()  << ", Propietario: " << propietario << std::endl;
        delete *it;
    }
    int codigoInmueble;
    std::cout << "Inmueble: ";
    std::cin >> codigoInmueble;
    std::cin.ignore();
    int inTipoPublicacion;
    std::cout << "Tipo de Publicacion: (1: Venta, 0: Alquiler)";
    std::cin >> inTipoPublicacion;
    TipoPublicacion tipoPublicacion = Alquiler;
    if (inTipoPublicacion == 1)
    {
        tipoPublicacion = Venta;
    }
    std::cin.ignore();
    std::cout << "Texto: ";
    std::string texto;
    std::getline(std::cin, texto);
    std::cout << "Precio: ";
    float precio;
    std::cin >> precio;
    std::cin.ignore();
    bool exito = c_Publicacion->altaPublicacion(nicknameInmobiliaria,codigoInmueble,tipoPublicacion,texto,precio);
    if (exito){
        std::cout << "Publicación dada de alta correctamente.\n";
    }else{
        std::cout << "Error: No se pudo dar de alta la publicación.\n";
    }
}

void consultaPublicaciones()
{

    Factory *factory = Factory::getInstance();
    IControladorPublicacion *c_Publicacion = factory->getControladorPublicacion();

    int inTipoPublicacion;
    std::cout << "Tipo de Publicacion: (1: Venta, 0: Alquiler)";
    std::cin >> inTipoPublicacion;
    TipoPublicacion tipoPublicacion = Alquiler;
    if (inTipoPublicacion == 1)
    {
        tipoPublicacion = Venta;
    }
    std::cin.ignore();
    std::cout << "Precio (Min): ";
    float precioMinimo;
    std::cin >> precioMinimo;
    std::cin.ignore();
    std::cout << "Precio (Max): ";
    float precioMaximo;
    std::cin >> precioMaximo;
    std::cin.ignore();
    int inTipoInmueble;
    std::cout << "Tipo de Inmueble: (1: Casa, 2: Apartamento, 0: Todos)";
    std::cin >> inTipoInmueble;
    std::cin.ignore();
    TipoInmueble tipoInmueble = Todos;
    if (inTipoInmueble == 1)
    {
        tipoInmueble = CasaTipo;
    }
    else if (inTipoInmueble == 2)
    {
        tipoInmueble = ApartamentoTipo;
    }
    std::cout << "Publicaciones encontradas:\n";
    // TODO: Coleccion de DTPublicacion = Controlador->listarPublicacion(tipoPublicacion, precionMinimo, precioMaximo, tipoInmueble);
    std::set<DTPublicacion *> publicaciones = c_Publicacion->listarPublicacion(tipoPublicacion, precioMinimo, precioMaximo, tipoInmueble);
    // Recorrer la coleccion Mostrar "- Codigo: xx, fecha: dd/mm/yyyy, texto: zzz, precio: aaa, inmobiliaria: bbb";
    for(std::set<DTPublicacion *>::iterator it = publicaciones.begin(); it != publicaciones.end(); ++it) {
        std::cout << "Codigo " << (*it)->getCodigo() << ", fecha: " << (*it)->getFecha()  << ", texto: " << (*it)->getTexto() << 
        ", precio: " << (*it)->getPrecio() << ", inmobiliaria: " << (*it)->getInmobiliaria() << std::endl;
        delete *it;
        }
    int verDetalle;
    std::cout << "Ver detalle de la publicacion: (1: Si, 0: No)";
    std::cin >> verDetalle;
    std::cin.ignore();
    if (verDetalle == 1)
    {
        std::cout << "Codigo de publicacion: ";
        int codigoPublicacion;
        std::cin >> codigoPublicacion;
        std::cin.ignore();
        std::cout << "Detalle del inmueble:\n";
        // TODO: DTInmueble = Controlador->detalleInmueblePublicacion(codigoPublicacion): DTInmueble
        DTInmueble* detInmueble= c_Publicacion->detalleInmueblePublicacion(codigoPublicacion);
        // Mostrarlo:
        DTCasa *detCasa = dynamic_cast<DTCasa*>(detInmueble);
        DTApartamento *detApart = dynamic_cast<DTApartamento*>(detInmueble);
        if(detCasa != nullptr) {
          //  Si es casa-> "Codigo: aaa, direccion: bbb, nro. puerta: ccc, superficie: xx m2, consturccion: dddd, PH: Si/No, Tipo de techo: Liviano/A dos aguas/Plano"
          std::string str_PH = detCasa->getEsPH() ? "Si" : "No";
          std::string str_techo;
          TipoTecho techo = detCasa->getTecho();
          if(techo == Liviano) str_techo = "Liviano";
          else if(techo == A_dos_aguas) str_techo = "A dos aguas";
          else if(techo == Plano) str_techo = "Plano";
          std::cout << "Codigo: " << detCasa->getCodigo() << ", direccion: " << detCasa->getDireccion() << ", nro. puerta: " << detCasa->getNumeroPuerta() << ", superficie: " << detCasa->getSuperficie() << " m2, construccion: " << detCasa->getAnioConstruccion() << ", PH: " << str_PH << ", Tipo de techo: " << str_techo << std::endl;
      } else if(detApart != nullptr) {
      //  Si es apartamento-> "Codigo: aaa, direccion: bbb, nro. puerta: ccc, superficie: xx m2, consturccion: dddd, piso: xx, ascensor: Si/No, gastos comunes: yyy"
          std::string str_ascensor = detApart->getTieneAscensor() ? "Si" : "No";

          std::cout << "Codigo: " << detApart->getCodigo() << ", direccion: " << detApart->getDireccion() << ", nro. puerta: " << detApart->getNumeroPuerta() << ", superficie: " << detApart->getSuperficie() << " m2, construccion: " << detApart->getAnioConstruccion() << ", piso: " << detApart->getPiso() << ", ascensor: " << str_ascensor << ", gastos comunes: " << detApart->getGastosComunes() << std::endl;
      }
    }
}

void eliminarInmueble()
{

  Factory *factory = Factory::getInstance();
  IControladorInmueble *c_Inmueble = factory->getControladorInmueble();

  std::list<DTInmuebleListado *> inmListados = c_Inmueble->listarInmuebles();
  if(inmListados.empty()) {
    std::cout << "No hay inmuebles en el sistema." << std::endl;
  } else {
    std::cout << "Listado de inmuebles:\n";
    // Recorrer la coleccion Mostrar "- Codigo: xx, direccion: xxxx, propietario: bbbbb";
    for(std::list<DTInmuebleListado *>::iterator it = inmListados.begin(); it != inmListados.end(); ++it) {
        std::cout << "Codigo: " << (*it)->getCodigo() << ", direccion: " << (*it)->getDireccion() << ", propietario: " << (*it)->getPropietario() << std::endl;
        delete *it;
    }

    std::cout << "Codigo del inmueble a eliminar: ";
    int codigoInmueble;
    std::cin >> codigoInmueble;
    std::cin.ignore();
    std::cout << "Detalle del inmueble:\n";
    DTInmueble *detInmueble = c_Inmueble->detalleInmueble(codigoInmueble);
    

    DTCasa *detCasa = dynamic_cast<DTCasa*>(detInmueble);
    DTApartamento *detApart = dynamic_cast<DTApartamento*>(detInmueble);
    if(detCasa != nullptr) {
        //  Si es casa-> "Codigo: aaa, direccion: bbb, nro. puerta: ccc, superficie: xx m2, consturccion: dddd, PH: Si/No, Tipo de techo: Liviano/A dos aguas/Plano"
        std::string str_PH = detCasa->getEsPH() ? "Si" : "No";
        std::string str_techo;
        TipoTecho techo = detCasa->getTecho();
        if(techo == Liviano) str_techo = "Liviano";
        else if(techo == A_dos_aguas) str_techo = "A dos aguas";
        else if(techo == Plano) str_techo = "Plano";
        std::cout << "Codigo: " << detCasa->getCodigo() << ", direccion: " << detCasa->getDireccion() << ", nro. puerta: " << detCasa->getNumeroPuerta() << ", superficie: " << detCasa->getSuperficie() << " m2, construccion: " << detCasa->getAnioConstruccion() << ", PH: " << str_PH << ", Tipo de techo: " << str_techo << std::endl;
    } else if(detApart != nullptr) {
    //  Si es apartamento-> "Codigo: aaa, direccion: bbb, nro. puerta: ccc, superficie: xx m2, consturccion: dddd, piso: xx, ascensor: Si/No, gastos comunes: yyy"
        std::string str_ascensor = detApart->getTieneAscensor() ? "Si" : "No";

        std::cout << "Codigo: " << detApart->getCodigo() << ", direccion: " << detApart->getDireccion() << ", nro. puerta: " << detApart->getNumeroPuerta() << ", superficie: " << detApart->getSuperficie() << " m2, construccion: " << detApart->getAnioConstruccion() << ", piso: " << detApart->getPiso() << ", ascensor: " << str_ascensor << ", gastos comunes: " << detApart->getGastosComunes() << std::endl;
    }
    int deseaEliminar;
    std::cout << "¿Desea eliminar?: (1: Si, 0: No)";
    std::cin >> deseaEliminar;
    std::cin.ignore();
    if (deseaEliminar == 1)
    {
        c_Inmueble->eliminarInmueble(codigoInmueble);
    }
  }
}

void suscribirseNotificaciones()
{
  Factory *factory = Factory::getInstance();
  IControladorUsuario *controlador = factory->getControladorUsuario();

  std::cout << "Ingrese el nickname del cliente o propietario: ";
  std::string nickname;
  std::getline(std::cin, nickname);

  // Mostrar inmobiliarias disponibles
  std::cout << "Lista de Inmobiliarias disponibles:\n";
  std::set<DTInmobiliariaListado *> disponibles = controlador->listar_disponibles(nickname);
  for (std::set<DTInmobiliariaListado *>::iterator it = disponibles.begin(); it != disponibles.end(); ++it)
  {
  std::cout << "- Nickname: " << (*it)->getNickname()
            << ", Nombre: " << (*it)->getNombre() << std::endl;
  }
  // Bucle para suscribirse
  int seguir = 1;
  while (seguir == 1)
  {
  std::cout << "Ingrese el nickname de la inmobiliaria a suscribirse: ";
  std::string nicknameInmo;
  std::getline(std::cin, nicknameInmo);

  bool ok = controlador->suscribirUsuario(nickname, nicknameInmo);
  if (ok)
      std::cout << "Suscripción a " << nicknameInmo << " realizada con éxito.\n";
  else
      std::cout << "No se pudo suscribir a " << nicknameInmo << ". Verifique el nickname.\n";

  std::cout << "¿Desea suscribirse a otra inmobiliaria? (1: Sí, 0: No): ";
  std::cin >> seguir;
  std::cin.ignore();  
  }
  // Liberar memoria
  for (std::set<DTInmobiliariaListado *>::iterator it = disponibles.begin(); it != disponibles.end(); ++it)
  {
  delete *it;
  }
}

void consultaNotificaciones()//shows Notificaciones recibidas de un usu ???
{
  Factory *factory = Factory::getInstance();
  IControladorUsuario *controlador = factory->getControladorUsuario();

  std::cout << "Ingrese su nickname: ";
  std::string nickname;
  std::getline(std::cin, nickname);

  std::set<Publicacion*> notificaciones = controlador->consultarNotificacionesRecibidas(nickname);

  if (notificaciones.empty())
  {
      std::cout << "No hay notificaciones disponibles." << std::endl;
  }
  else
  {
      std::cout << "Notificaciones recibidas:\n";
      for (Publicacion* pub : notificaciones)
      {
          std::cout << "- Inmueble código: " << pub->getCodigo()
                    << ", tipo: " << (pub->getTipo() == Alquiler ? "Alquiler" : "Venta")
                    << ", texto: " << pub->getTexto()
                    << ", precio: $" << pub->getPrecio() << std::endl;
      }
  }
}



void eliminarSuscripciones()// lu 
{
  Factory *factory = Factory::getInstance();
  IControladorUsuario *cu = factory->getControladorUsuario();
    
  std::cout << "Ingrese su nickname: ";
  std::string nickname;
  std::getline(std::cin, nickname);

  std::set<DTInmobiliariaListado> suscripciones = cu->listar_suscripciones(nickname);

  if (suscripciones.empty()) {
      std::cout << "No hay suscripciones registradas para este usuario.\n";
      return;
  }

  std::cout << "Inmobiliarias a las que está suscripto:\n";
  for (const DTInmobiliariaListado& dt : suscripciones) {
      std::cout << "- Nickname: " << dt.getNickname() << ", Nombre: " << dt.getNombre() << "\n";
  }

  std::cout << "Ingrese el nickname de la inmobiliaria a la que desea cancelar la suscripción: ";
  std::string nickInmo;
  std::getline(std::cin, nickInmo);

  if (cu->cancelar_suscripcion(nickname, nickInmo)) {
      std::cout << "Suscripción cancelada exitosamente.\n";
  } else {
      std::cout << "No se pudo cancelar la suscripción. Verifique los datos ingresados.\n";
  }
}



void altaAdministracionPropiedad()
{
    Factory *factory = Factory::getInstance();
    IControladorUsuario *ControladorU = factory->getControladorUsuario();
    IControladorInmueble *ControladorI = factory->getControladorInmueble();

    std::cout << "Lista de Inmobiliarias:\n";
    // TODO: Coleccion de DTUsuario = controlador->listarInmobiliarias();
    // Recorrer la coleccion Mostrar "- Nickname: xx, Nombre: zz";

    std::set<DTUsuario *> listaDeInmobiliarias = ControladorU->listarInmobiliarias();
    for (std::set<DTUsuario *>::iterator it = listaDeInmobiliarias.begin(); it != listaDeInmobiliarias.end(); ++it)
    {
        DTUsuario *usuario = *it;
        std::cout << "- Nickname: " << usuario->getNickname() << ", Nombre: " << usuario->getNombre() << std::endl;
        delete usuario;
    }

    std::cout << "Nickname de la inmobiliaria: ";
    std::string nicknameInmobiliaria;
    std::getline(std::cin, nicknameInmobiliaria);
    // TODO: Coleccion de DTInmuebleListado = Controlador->listarInmueblesNoAdministradosInmobiliaria(nicknameInmobiliaria);
    // Recorrer la coleccion Mostrar "- Codigo: xx, direccion: xxxx, propietario: bbbbb";

    std::set<DTInmuebleListado *> listaDeInmuebles = ControladorI->listarInmueblesNoAdministradosInmobiliaria(nicknameInmobiliaria);

    // imprimimos cada DTInmuebleListado, lo quitamos de la lista y lo eliminamos de memoria
    while(!listaDeInmuebles.empty()) {
      std::set<DTInmuebleListado*>::iterator primero = listaDeInmuebles.begin();
      DTInmuebleListado *inListado = *primero;

      std::cout << "- Codigo: " << inListado->getCodigo() << ", " << "direccion: " << inListado->getDireccion() << ", " << "propietario: " << inListado->getPropietario() << std::endl;

      listaDeInmuebles.erase(primero);
      delete inListado;

    }

    // for (std::set<DTInmuebleListado *>::iterator it = listaDeInmuebles.begin(); it != listaDeInmuebles.end(); ++it)
    // {
    //     DTInmuebleListado *asd = *it;
    //     std::cout << "- Codigo: " << asd->getCodigo() << ", " << "direccion: " << asd->getDireccion() << ", " << "propietario: " << asd->getPropietario() << std::endl;
    //
    //      delete asd;
    // }

    std::cout << "Codigo del inmueble a administrar: ";
    int codigoInmueble;
    std::cin >> codigoInmueble;
    std::cin.ignore();
    // TODO: Controlador->altaAdministraPropiedad(nicknameInmobiliaria, codigoInmueble);
    // aprovecho la instancia geteada arriba

    ControladorI->altaAdministraPropiedad(nicknameInmobiliaria, codigoInmueble);
}

void cargarDatos()
{
    CargaDatos *datos = CargaDatos::getInstance();
    CargaDatos::deleteInstance();
}

void verFechaActual()
{
    Factory *factory = Factory::getInstance();
    IControladorFechaActual *cfecha = factory->getControladorFechaActual();
    DTFecha *fechaActual = cfecha->getFechaActual();
    std::cout << "fecha actual: " << fechaActual;
    delete fechaActual;
}

void asignarFechaActual()
{
    Factory *factory = Factory::getInstance();
    IControladorFechaActual *cfecha = factory->getControladorFechaActual();
    std::cout << "dia: ";
    int dia;
    std::cin >> dia;
    std::cin.ignore();
    std::cout << "mes: ";
    int mes;
    std::cin >> mes;
    std::cin.ignore();
    std::cout << "ano: ";
    int ano;
    std::cin >> ano;
    std::cin.ignore();
    cfecha->setNewFechaActual(dia, mes, ano);
}
void Salir(){
    Factory *factory = Factory::getInstance();
    factory->liberarControladorFechaActual();
    factory->liberarControladorInmueble();
    factory->liberarControladorUsuario();
    factory->liberarControladorPublicacion();
    delete factory;

    exit(0);
}
