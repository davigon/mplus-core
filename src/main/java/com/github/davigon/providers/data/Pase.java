package com.github.davigon.providers.data;

import com.github.davigon.domain.Programme;
import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pase")
public class Pase {

    @XmlAttribute(name = "cadena", required = true)
    private String cadena;

    @XmlAttribute(name = "fecha", required = true)
    private String fecha;

    @XmlElement(name = "hora", required = true)
    private String hora;

    @XmlElement(name = "descripcion_corta", required = true)
    private String descripcionCorta;

    @XmlElement(name = "titulo", required = true)
    private String titulo;

    @XmlElement(name = "tipo_ficha", required = true)
    private String tipoFicha;

    @XmlElement(name = "sinopsis_corta", required = true)
    private String sinopsisCorta;

    @XmlElement(name = "sinopsis_larga", required = true)
    private String sinopsisLarga;

    public Programme toProgramme(Programme prev) {
        OffsetDateTime pCurrentStart = toOffsetDateTime(this.getFecha().substring(1), this.getHora());
        if (prev != null) {
            if (prev.getStart().isAfter(pCurrentStart))
                pCurrentStart = pCurrentStart.plusDays(1);

            prev.setStop(pCurrentStart);
        }

        return new Programme(
                pCurrentStart,
                null,
                this.getCadena(),
                this.getDescripcionCorta(),
                this.getSinopsisLarga(),
                this.getTipoFicha()
        );
    }

    private OffsetDateTime toOffsetDateTime(String providerDate, String providerTime) {
        int year = Integer.parseInt(providerDate.substring(0, 4));
        int month = Integer.parseInt(providerDate.substring(5, 7));
        int day = Integer.parseInt(providerDate.substring(8, 10));
        int hour = Integer.parseInt(providerTime.substring(0, 2));
        int minutes = Integer.parseInt(providerTime.substring(3, 5));

        LocalDateTime dt = LocalDateTime.of(year, month, day, hour, minutes);
        return OffsetDateTime.of(dt, ZoneId.of("Europe/Madrid").getRules().getOffset(dt));
    }
}
