
#ifndef DTINMOBILIARIALISTADO_H
#define DTINMOBILIARIALISTADO_H

#include <string>

class DTInmobiliariaListado {
private:
    std::string nickname;
    std::string nombre;

public:
    DTInmobiliariaListado(std::string nickname, std::string nombre);
    
    std::string getNickname() const;
    std::string getNombre() const;

    bool operator<(const DTInmobiliariaListado& otro) const;
};

#endif
