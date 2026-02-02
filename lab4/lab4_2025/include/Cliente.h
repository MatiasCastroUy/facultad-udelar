#ifndef CLIENTE_H
#define CLIENTE_H
#include "UsuarioObservador.h"
#include "Suscripcion.h"
#include "Publicacion.h"
#include <set>
#include <string>
#include <vector>

//Cliente hereda todo lo que tenía Usuario a través de UsuarioObservador
class Cliente : public UsuarioObservador {
    private:
        std::string apellido;
        std::string documento;
    public:
        Cliente(std::string nickname, std::string contrasena, std::string nombre, std::string email, std::string apellido, std::string documento);
        ~Cliente();
};

#endif