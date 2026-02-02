package logica;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/*
 * Adaptador para usar LocalDate en WebServices
 * Se envía como String
 * */

public class AdaptadorLocalDate extends XmlAdapter<String, LocalDate> {
	@Override
    public LocalDate unmarshal(String v) {
        return (v == null) ? null : LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) {
        return (v == null) ? null : v.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

