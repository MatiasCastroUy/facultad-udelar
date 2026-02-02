#include "../include/Usuario.h"
#include "../include/Suscripcion.h"
#include "../include/Notificacion.h"
#include "../include/Inmobiliaria.h"
#include "../include/ControladorPublicacion.h"
#include <string>
#include <iostream>
#include <vector>
#include <list>
using namespace std;

// Constructor
Usuario::Usuario(string nickname, string contrasenia, string nombre, string email)
    : nickname(nickname), contrasena(contrasenia), nombre(nombre), email(email) {}

// Destructor
Usuario::~Usuario()
{}

std::string Usuario::getNickname()
{
    return this->nickname;
}

std::string Usuario::getNombre()
{
    return this->nombre;
}
