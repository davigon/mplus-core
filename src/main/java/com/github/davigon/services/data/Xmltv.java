package com.github.davigon.services.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.time.LocalDate;
import java.util.List;

@JacksonXmlRootElement(localName = "tv")
public record Xmltv(
        @JacksonXmlProperty(isAttribute = true, localName = "date")
        LocalDate date,

        @JacksonXmlProperty(isAttribute = true, localName = "generator-info-name")
        String generatorInfoName,

        @JacksonXmlProperty(isAttribute = true, localName = "generator-info-url")
        String generatorInfoUrl,

        @JacksonXmlProperty(localName = "channel")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<XmltvChannel> channels,

        @JacksonXmlProperty(localName = "programme")
        @JacksonXmlElementWrapper(useWrapping = false)
        List<XmltvProgramme> programmes
) {
}
