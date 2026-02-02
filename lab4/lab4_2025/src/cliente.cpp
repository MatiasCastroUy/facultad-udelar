#include "../include/Cliente.h"
// Constructor
Cliente::Cliente(std::string nickname, std::string contrasena, std::string nombre, std::string email,
                 std::string apellido, std::string documento)
    : UsuarioObservador(nickname, contrasena, nombre, email),
      apellido(apellido),
      documento(documento)
{}

Cliente::~Cliente(){}
