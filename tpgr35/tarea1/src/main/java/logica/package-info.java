@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(type = java.time.LocalDate.class, value = AdaptadorLocalDate.class)
})
// registra el adaptador para todos los campos de tipo LocalDate en el paquete Logica


package logica;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
