#ifndef USUARIO_H
#define USUARIO_H
#include <vector>
#include <string>
#include <list>
#include "IObserver.h" 
class Usuario {
private:
    std::string nickname;
    std::string contrasena;
    std::string nombre;
    std::string email;
public:
    std::string getNombre();
    std::string getNickname();
    Usuario(std::string nickname, std::string contrasena, std::string nombre, std::string email);
    virtual ~Usuario();
};

#endif