#include "../include/CargaDatos.h"
#include <cstddef>

#include "../include/TipoInmueble.h"
#include "../include/TipoTecho.h"
#include "../include/TipoPublicacion.h"
#include "../include/Factory.h"
#include "../include/ControladorUsuario.h"
#include "../include/ControladorPublicacion.h"
#include "../include/ControladorInmueble.h"
// #include "../include/ControladorFechaActual.h"

#include <string>
using namespace std;

CargaDatos* CargaDatos::instance = NULL;

CargaDatos::CargaDatos() {
    // -------- DEFINICIÓN DE VARIABLES ------- //
    // CLIENTES
    string cli1_nickname = "luisito23";
    string cli1_contra = "qweasd12";
    string cli1_nombre = "Luis";
    string cli1_email = "luisito23@gmail.com";
    string cli1_apellido = "Pérez";
    string cli1_documento = "46859342";

    string cli2_nickname = "anarojo88";
    string cli2_contra = "claveAna1";
    string cli2_nombre = "Ana";
    string cli2_email = "anarojo88@hotmail.com";
    string cli2_apellido = "Rojo";
    string cli2_documento = "31287465";

    // PROPIETARIOS Y SUS INMUEBLES
    string prop1_nickname = "marcelom";
    string prop1_contra = "banco123";
    string prop1_nombre = "Marcelo";
    string prop1_email = "marcelo.m@gmail.com";
    string prop1_telefono = "099876543";
    string prop1_cuenta = "123456789012";
    // inmueble 1
    TipoInmueble i1_tipo = CasaTipo;
    string i1_dire = "Av. Rivera";
    int i1_nro_puerta = 1011;
    int i1_superficie = 120;
    int i1_anio_constr = 1995;
    bool i1_esPH = true;
    TipoTecho i1_techo = Plano;
    // inmueble 2
    TipoInmueble i2_tipo = ApartamentoTipo;
    string i2_dire = "Av. Brasil";
    int i2_nro_puerta = 2031;
    int i2_superficie = 75;
    int i2_anio_constr = 1980;
    int i2_piso = 5;
    bool i2_tieneAscensor = true;
    int i2_gastosComunes = 3500;

    string prop2_nickname = "robertarce";
    string prop2_contra = "pass456";
    string prop2_nombre = "Roberto";
    string prop2_email = "roberto.a@yahoo.com";
    string prop2_telefono = "091234567";
    string prop2_cuenta = "987654321001";
    //inmueble 3
    TipoInmueble i3_tipo = CasaTipo;
    string i3_dire = "Camino Maldonado";
    int i3_nro_puerta = 1540;
    int i3_superficie = 95;
    int i3_anio_constr = 1988;
    bool i3_esPH = false;
    TipoTecho i3_techo = Plano;

    string prop3_nickname = "soledadf";
    string prop3_contra = "sole789";
    string prop3_nombre = "Soledad";
    string prop3_email = "soledad.f@gmail.com";
    string prop3_telefono = "092345678";
    string prop3_cuenta = "654321987654";
    // inmueble 4
    TipoInmueble i4_tipo = ApartamentoTipo;
    string i4_dire = "Colonia";
    int i4_nro_puerta = 1542;
    int i4_superficie = 60;
    int i4_anio_constr = 1978;
    int i4_piso = 12;
    bool i4_tieneAscensor = true;
    int i4_gastosComunes = 2800;

    string prop4_nickname = "martagal";
    string prop4_contra = "martA01";
    string prop4_nombre = "Marta";
    string prop4_email = "marta.galvez@outlook.com";
    string prop4_telefono = "098765432";
    string prop4_cuenta = "321098765432";

    // inmueble 5
    TipoInmueble i5_tipo = CasaTipo;
    string i5_dire = "Juan Paullier";
    int i5_nro_puerta = 801;
    int i5_superficie = 110;
    int i5_anio_constr = 2000;
    bool i5_esPH = true;
    TipoTecho i5_techo = Liviano;


    string prop5_nickname = "jorge88";
    string prop5_contra = "jorgepass88";
    string prop5_nombre = "Jorge";
    string prop5_email = "jorge.rivera@uy.com";
    string prop5_telefono = "097654321";
    string prop5_cuenta = "890123456789";
    // inmueble 6
    TipoInmueble i6_tipo = ApartamentoTipo;
    string i6_dire = "Bulevar Artigas";
    int i6_nro_puerta = 871;
    int i6_superficie = 68;
    int i6_anio_constr = 2002;
    int i6_piso = 3;
    bool i6_tieneAscensor = true;
    int i6_gastosComunes = 2200;

    // inmueble 7
    TipoInmueble i7_tipo = ApartamentoTipo;
    string i7_dire = "Sarmiento";
    int i7_nro_puerta = 1476;
    int i7_superficie = 80;
    int i7_anio_constr = 2008;
    int i7_piso = 6;
    bool i7_tieneAscensor = true;
    int i7_gastosComunes = 3100;

    // inmueble 8
    TipoInmueble i8_tipo = CasaTipo;
    string i8_dire = "Cno. Carrasco";
    int i8_nro_puerta = 1576;
    int i8_superficie = 140;
    int i8_anio_constr = 2007;
    bool i8_esPH = true;
    TipoTecho i8_techo = Plano;

    // INMOBILIARIAS
    string inmo1_nickname = "casasur123";
    string inmo1_contra = "casasur99";
    string inmo1_nombre = "Casa Sur";
    string inmo1_email = "contacto@casasur.com";
    string inmo1_dire = "Canelones 2345";
    string inmo1_url = "https://casasur.com.uy";
    string inmo1_telefono = "24012345";
        //REPRESENTA: marcelom
        //REPRESENTA: jorge88

    string inmo2_nickname = "idealhome";
    string inmo2_contra = "home2025";
    string inmo2_nombre = "IHome";
    string inmo2_email = "info@idealhome.uy";
    string inmo2_dire = "Av. Italia 4567";
    string inmo2_url = "https://idealhome.uy";
    string inmo2_telefono = "099123456";
        //REPRESENTA: marcelom
        //REPRESENTA: jorge88

    string inmo3_nickname = "vivaurbana";
    string inmo3_contra = "viva4567";
    string inmo3_nombre = "Viva Urbana";
    string inmo3_email = "contacto@vivaurbana.com";
    string inmo3_dire = "18 de Julio 7890";
    string inmo3_url = "https://vivaurbana.com";
    string inmo3_telefono = "29109876";
        //REPRESENTA: robertarce
        //REPRESENTA: soledadf
        //REPRESENTA: jorge88


    // ALTA ADMINISTRA PROPIEDAD

    string adm1_inmo = "casasur123";
    int adm1_inmueble = 2;

    string adm2_inmo = "idealhome";
    int adm2_inmueble = 1;

    string adm3_inmo = "vivaurbana";
    int adm3_inmueble = 3;

    string adm4_inmo = "vivaurbana";
    int adm4_inmueble = 4;

    string adm5_inmo = "idealhome";
    int adm5_inmueble = 5;

    string adm6_inmo = "casasur123";
    int adm6_inmueble = 6;

    string adm7_inmo = "vivaurbana";
    int adm7_inmueble = 6;

    string adm8_inmo = "idealhome";
    int adm8_inmueble = 6;

    string adm9_inmo = "idealhome";
    int adm9_inmueble = 7;

    string adm10_inmo = "vivaurbana";
    int adm10_inmueble = 8;

        //ORDEN CRONOLOGICO (creo):
        // adm 5, adm 1, adm 6, adm 7, adm 4, adm 10, adm 3, adm 9, adm 2. adm 8

    // PUBLICACIONES

    TipoPublicacion pub1_tipo = Venta;
    string pub1_texto = "Casa al fondo Juan Paullier con 110 m fondo y techo liviano.";
    float pub1_precio = 47000.0;
    bool pub1_activa = true;
    int pub1_codigo = 1;
    int pub1_inmueble = 5;
    string pub1_inmo = "idealhome";

    TipoPublicacion pub2_tipo = Alquiler;
    string pub2_texto = "Oportunidad en Av. Brasil: apartamento de 75 m piso 5 con ascensor.";
    float pub2_precio = 28000.0;
    bool pub2_activa = false;
    int pub2_codigo = 2;
    int pub2_inmueble = 2;
    string pub2_inmo = "casasur123";

    TipoPublicacion pub3_tipo = Alquiler;
    string pub3_texto = "Apartamento luminoso en Av. Brasil piso alto con excelentes servicios.";
    float pub3_precio = 29500.0;
    bool pub3_activa = true;
    int pub3_codigo = 3;
    int pub3_inmueble = 2;
    string pub3_inmo = "casasur123";

    TipoPublicacion pub4_tipo = Alquiler;
    string pub4_texto = "Casa ideal para familia en Juan Paullier barrio tranquilo.";
    float pub4_precio = 38500.0;
    bool pub4_activa = true;
    int pub4_codigo = 4;
    int pub4_inmueble = 5;
    string pub4_inmo = "idealhome";

    TipoPublicacion pub5_tipo = Venta;
    string pub5_texto = "Apartamento en Bulevar Artigas piso 3 muy luminoso y moderno.";
    float pub5_precio = 375000.0;
    bool pub5_activa = true;
    int pub5_codigo = 5;
    int pub5_inmueble = 6;
    string pub5_inmo = "casasur123";

    TipoPublicacion pub6_tipo = Venta;
    string pub6_texto = "Excelente apartamento en Av. Brasil con 75 m ideal para reforma.";
    float pub6_precio = 390000.0;
    bool pub6_activa = true;
    int pub6_codigo = 6;
    int pub6_inmueble = 2;
    string pub6_inmo = "casasur123";

    TipoPublicacion pub7_tipo = Alquiler;
    string pub7_texto = "Apartamento 68 m en Bulevar Artigas tercer piso sin ascensor.";
    float pub7_precio = 23000.0;
    bool pub7_activa = true;
    int pub7_codigo = 7;
    int pub7_inmueble = 6;
    string pub7_inmo = "vivaurbana";

    TipoPublicacion pub8_tipo = Alquiler;
    string pub8_texto = "Apartamento con ascensor en Colonia 1542 piso 12 excelente conectividad.";
    float pub8_precio = 26000.0;
    bool pub8_activa = true;
    int pub8_codigo = 8;
    int pub8_inmueble = 4;
    string pub8_inmo = "vivaurbana";

    TipoPublicacion pub9_tipo = Alquiler;
    string pub9_texto = "Casa excelente en Camino Maldonado de 95 m con patio al fondo.";
    float pub9_precio = 27000.0;
    bool pub9_activa = true;
    int pub9_codigo = 9;
    int pub9_inmueble = 3;
    string pub9_inmo = "vivaurbana";

    TipoPublicacion pub10_tipo = Venta;
    string pub10_texto = "Casa en Av. Rivera de 120 m con techo plano ideal para familia.";
    float pub10_precio = 520000.0;
    bool pub10_activa = true;
    int pub10_codigo = 10;
    int pub10_inmueble = 1;
    string pub10_inmo = "idealhome";

    TipoPublicacion pub11_tipo = Alquiler;
    string pub11_texto = "Apartamento amplio en Sarmiento 1476 piso 6 con ascensor.";
    float pub11_precio = 32000.0;
    bool pub11_activa = false;
    int pub11_codigo = 11;
    int pub11_inmueble = 7;
    string pub11_inmo = "idealhome";

    TipoPublicacion pub12_tipo = Venta;
    string pub12_texto = "Apartamento de 80 m en Sarmiento excelente estado y vista.";
    float pub12_precio = 455000.0;
    bool pub12_activa = false;
    int pub12_codigo = 12;
    int pub12_inmueble = 7;
    string pub12_inmo = "idealhome";

    TipoPublicacion pub13_tipo = Alquiler;
    string pub13_texto = "Apartamento con gran vista a la rambla";
    float pub13_precio = 31000.0;
    bool pub13_activa = false;
    int pub13_codigo = 13;
    int pub13_inmueble = 7;
    string pub13_inmo = "idealhome";

    TipoPublicacion pub14_tipo = Venta;
    string pub14_texto = "Apartamento en excelentes condiciones de 80 m";
    float pub14_precio = 450000.0;
    bool pub14_activa = true;
    int pub14_codigo = 14;
    int pub14_inmueble = 7;
    string pub14_inmo = "idealhome";

    TipoPublicacion pub15_tipo = Venta;
    string pub15_texto = "Venta de casa en Camino Maldonado 95 m bien distribuidos.";
    float pub15_precio = 430000.0;
    bool pub15_activa = true;
    int pub15_codigo = 15;
    int pub15_inmueble = 3;
    string pub15_inmo = "vivaurbana";

    TipoPublicacion pub16_tipo = Alquiler;
    string pub16_texto = "Alquiler en Sarmiento 80 m piso alto con excelentes terminaciones.";
    float pub16_precio = 33000.0;
    bool pub16_activa = true;
    int pub16_codigo = 16;
    int pub16_inmueble = 7;
    string pub16_inmo = "idealhome";

    TipoPublicacion pub17_tipo = Venta;
    string pub17_texto = "A estrenar en Bulevar Artigas 871 apartamento moderno.";
    float pub17_precio = 400000.0;
    bool pub17_activa = true;
    int pub17_codigo = 17;
    int pub17_inmueble = 6;
    string pub17_inmo = "idealhome";

    // SUSCRIPCIONES

    string sus1_usuario = "luisito23";
    string sus1_inmo = "casasur123";

    string sus2_usuario = "luisito23";
    string sus2_inmo = "idealhome";

    string sus3_usuario = "anarojo88";
    string sus3_inmo = "casasur123";

    string sus4_usuario = "anarojo88";
    string sus4_inmo = "idealhome";

    string sus5_usuario = "anarojo88";
    string sus5_inmo = "vivaurbana";

    string sus6_usuario = "marcelom";
    string sus6_inmo = "idealhome";

    string sus7_usuario = "robertarce";
    string sus7_inmo = "idealhome";

    string sus8_usuario = "soledadf";
    string sus8_inmo = "vivaurbana";

    string sus9_usuario = "martagal";
    string sus9_inmo = "vivaurbana";

    string sus10_usuario = "jorge88";
    string sus10_inmo = "casasur123";

    string sus11_usuario = "jorge88";
    string sus11_inmo = "idealhome";

    string sus12_usuario = "jorge88";
    string sus12_inmo = "vivaurbana";

    // ---------- CARGA DE DATOS ------------- //
    
    Factory *factory = Factory::getInstance();
    IControladorFechaActual *c_fecha = factory->getControladorFechaActual();
    
    IControladorUsuario *c_usuario = factory->getControladorUsuario();
    IControladorInmueble *c_inmueble = factory->getControladorInmueble();
    IControladorPublicacion *c_publicacion = factory->getControladorPublicacion();
    // ControladorFechaActual *c_fecha = ControladorFechaActual::getInstanciaF();


    // CLIENTES
    c_usuario->altaCliente(cli1_nickname,cli1_contra,cli1_nombre,cli1_email,cli1_apellido,cli1_documento);
    c_usuario->altaCliente(cli2_nickname,cli2_contra,cli2_nombre,cli2_email,cli2_apellido,cli2_documento);

    // INMOBILIARIAS

    c_usuario->altaInmobiliaria(inmo1_nickname,inmo1_contra,inmo1_nombre,inmo1_email,inmo1_dire,inmo1_url,inmo1_telefono);
    c_usuario->altaInmobiliaria(inmo2_nickname,inmo2_contra,inmo2_nombre,inmo2_email,inmo2_dire,inmo2_url,inmo2_telefono);
    c_usuario->altaInmobiliaria(inmo3_nickname,inmo3_contra,inmo3_nombre,inmo3_email,inmo3_dire,inmo3_url,inmo3_telefono);

    // PROPIETARIOS Y SUS INMUEBLES
    Propietario *nuevoProp;
    
    // PROP 1
    c_usuario->altaPropietario(prop1_nickname, prop1_contra, prop1_nombre, prop1_email, prop1_cuenta, prop1_telefono);
    nuevoProp = dynamic_cast<Propietario*>(c_usuario->getUsuario(prop1_nickname));
    c_inmueble->setPropietarioRecordado(nuevoProp);
    c_inmueble->altaCasa(i1_dire,i1_nro_puerta,i1_superficie,i1_anio_constr, i1_esPH, i1_techo); // inm 1
    c_inmueble->altaApartamento(i2_dire,i2_nro_puerta,i2_superficie,i2_anio_constr,i2_piso,i2_tieneAscensor,i2_gastosComunes); // inm 2

    // PROP 2
    c_usuario->altaPropietario(prop2_nickname, prop2_contra, prop2_nombre, prop2_email, prop2_cuenta, prop2_telefono);
    nuevoProp = dynamic_cast<Propietario*>(c_usuario->getUsuario(prop2_nickname));
    c_inmueble->setPropietarioRecordado(nuevoProp);
    c_inmueble->altaCasa(i3_dire,i3_nro_puerta,i3_superficie,i3_anio_constr,i3_esPH,i3_techo); // inm 3

    // PROP 3
    c_usuario->altaPropietario(prop3_nickname, prop3_contra, prop3_nombre, prop3_email, prop3_cuenta, prop3_telefono);
    nuevoProp = dynamic_cast<Propietario*>(c_usuario->getUsuario(prop3_nickname));
    c_inmueble->setPropietarioRecordado(nuevoProp);
    c_inmueble->altaApartamento(i4_dire,i4_nro_puerta,i4_superficie,i4_anio_constr,i4_piso,i4_tieneAscensor,i4_gastosComunes); // inm 4

    // PROP 4
    c_usuario->altaPropietario(prop4_nickname, prop4_contra, prop4_nombre, prop4_email, prop4_cuenta, prop4_telefono);
    nuevoProp = dynamic_cast<Propietario*>(c_usuario->getUsuario(prop4_nickname));
    c_inmueble->setPropietarioRecordado(nuevoProp);
    c_inmueble->altaCasa(i5_dire,i5_nro_puerta,i5_superficie,i5_anio_constr,i5_esPH,i5_techo); // inm 5

    // PROP 5
    c_usuario->altaPropietario(prop5_nickname, prop5_contra, prop5_nombre, prop5_email, prop5_cuenta, prop5_telefono);
    nuevoProp = dynamic_cast<Propietario*>(c_usuario->getUsuario(prop5_nickname));
    c_inmueble->setPropietarioRecordado(nuevoProp);
    c_inmueble->altaApartamento(i6_dire,i6_nro_puerta,i6_superficie,i6_anio_constr,i6_piso,i6_tieneAscensor,i6_gastosComunes); // inm 6
    c_inmueble->altaApartamento(i7_dire,i7_nro_puerta,i7_superficie,i7_anio_constr,i7_piso,i7_tieneAscensor,i7_gastosComunes); // inm 7
    c_inmueble->altaCasa(i8_dire,i8_nro_puerta,i8_superficie,i8_anio_constr,i8_esPH,i8_techo); // inm 8

    // LA CARGA DE PUBLICACIONES, SUSCRIPCIONES, Y ADM PROPIEDAD
    // SE TIENE QUE HACER EN ORDEN CRONOLOGICO.
    // Las suscripciones no incluyen fecha en los datos, así que podemos variarlo nosotros.
   
    c_usuario->suscribirUsuario(sus1_usuario,sus1_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus2_usuario,sus2_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus3_usuario,sus3_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus4_usuario,sus4_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus5_usuario,sus5_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus6_usuario,sus6_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus7_usuario,sus7_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus8_usuario,sus8_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus9_usuario,sus9_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus10_usuario,sus10_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus11_usuario,sus11_inmo); // SE PUEDE MOVER
    c_usuario->suscribirUsuario(sus12_usuario,sus12_inmo); // SE PUEDE MOVER

    c_fecha->setNewFechaActual(3,7,2010);
    c_inmueble->altaAdministraPropiedad(adm5_inmo,adm5_inmueble);

    c_fecha->setNewFechaActual(9,10,2011);
    c_publicacion->altaPublicacion(pub1_inmo,pub1_inmueble,pub1_tipo,pub1_texto,pub1_precio);

    c_fecha->setNewFechaActual(12,12,2015);
    c_inmueble->altaAdministraPropiedad(adm1_inmo,adm1_inmueble);

    c_fecha->setNewFechaActual(15,1,2016);
    c_publicacion->altaPublicacion(pub2_inmo,pub2_inmueble,pub2_tipo,pub2_texto,pub2_precio);

    c_fecha->setNewFechaActual(5,3,2019);
    c_publicacion->altaPublicacion(pub3_inmo,pub3_inmueble,pub3_tipo,pub3_texto,pub3_precio);

    c_fecha->setNewFechaActual(3,4,2019);
    c_publicacion->altaPublicacion(pub4_inmo,pub4_inmueble,pub4_tipo,pub4_texto,pub4_precio);

    c_fecha->setNewFechaActual(4,11,2019);
    c_inmueble->altaAdministraPropiedad(adm6_inmo,adm6_inmueble);

    c_fecha->setNewFechaActual(12,12,2019);
    c_publicacion->altaPublicacion(pub5_inmo,pub5_inmueble,pub5_tipo,pub5_texto,pub5_precio);

    c_fecha->setNewFechaActual(19,5,2020);
    c_inmueble->altaAdministraPropiedad(adm7_inmo,adm7_inmueble);

    c_fecha->setNewFechaActual(12,6,2020);
    c_publicacion->altaPublicacion(pub6_inmo,pub6_inmueble,pub6_tipo,pub6_texto,pub6_precio);

    c_fecha->setNewFechaActual(20,7,2020);
    c_publicacion->altaPublicacion(pub7_inmo,pub7_inmueble,pub7_tipo,pub7_texto,pub7_precio);

    c_fecha->setNewFechaActual(1,1,2022);
    c_inmueble->altaAdministraPropiedad(adm4_inmo,adm4_inmueble);

    c_fecha->setNewFechaActual(4,3,2022);
    c_publicacion->altaPublicacion(pub8_inmo,pub8_inmueble,pub8_tipo,pub8_texto,pub8_precio);

    c_fecha->setNewFechaActual(19,5,2022);
    c_inmueble->altaAdministraPropiedad(adm10_inmo,adm10_inmueble);

    c_fecha->setNewFechaActual(20,7,2022);
    c_inmueble->altaAdministraPropiedad(adm3_inmo,adm3_inmueble);

    c_fecha->setNewFechaActual(12,9,2022);
    c_publicacion->altaPublicacion(pub9_inmo,pub9_inmueble,pub9_tipo,pub9_texto,pub9_precio);

    c_fecha->setNewFechaActual(18,9,2023);
    c_inmueble->altaAdministraPropiedad(adm9_inmo,adm9_inmueble);

    c_fecha->setNewFechaActual(25,9,2023);
    c_inmueble->altaAdministraPropiedad(adm2_inmo,adm2_inmueble);

    c_fecha->setNewFechaActual(1,10,2023);
    c_publicacion->altaPublicacion(pub10_inmo,pub10_inmueble,pub10_tipo,pub10_texto,pub10_precio);

    c_fecha->setNewFechaActual(18,10,2023);
    c_publicacion->altaPublicacion(pub11_inmo,pub11_inmueble,pub11_tipo,pub11_texto,pub11_precio);

    c_fecha->setNewFechaActual(19,10,2023);
    c_publicacion->altaPublicacion(pub12_inmo,pub12_inmueble,pub12_tipo,pub12_texto,pub12_precio);

    c_fecha->setNewFechaActual(20,11,2023);
    c_publicacion->altaPublicacion(pub13_inmo,pub13_inmueble,pub13_tipo,pub13_texto,pub13_precio);

    c_fecha->setNewFechaActual(20,11,2023);
    c_publicacion->altaPublicacion(pub14_inmo,pub14_inmueble,pub14_tipo,pub14_texto,pub14_precio);

    c_fecha->setNewFechaActual(8,3,2024);
    c_publicacion->altaPublicacion(pub15_inmo,pub15_inmueble,pub15_tipo,pub15_texto,pub15_precio);

    c_fecha->setNewFechaActual(5,5,2024);
    c_publicacion->altaPublicacion(pub16_inmo,pub16_inmueble,pub16_tipo,pub16_texto,pub16_precio);

    c_fecha->setNewFechaActual(19,7,2024);
    c_inmueble->altaAdministraPropiedad(adm8_inmo,adm8_inmueble);

    c_fecha->setNewFechaActual(1,8,2024);
    c_publicacion->altaPublicacion(pub17_inmo,pub17_inmueble,pub17_tipo,pub17_texto,pub17_precio);



}

CargaDatos::~CargaDatos() {

}

void CargaDatos::deleteInstance() {
  delete instance;
}

CargaDatos* CargaDatos::getInstance() {
    if (instance == NULL) {
        instance = new CargaDatos();
    }
    return instance;
}
