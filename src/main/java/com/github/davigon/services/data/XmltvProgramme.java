package com.github.davigon.services.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.time.OffsetDateTime;

public record XmltvProgramme(
        @JacksonXmlProperty(isAttribute = true, localName = "start")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss Z", timezone = "Europe/Madrid")
        OffsetDateTime start,

        @JacksonXmlProperty(isAttribute = true, localName = "stop")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss Z", timezone = "Europe/Madrid")
        OffsetDateTime stop,

        @JacksonXmlProperty(isAttribute = true, localName = "channel")
        String channel,

        @JacksonXmlProperty(localName = "title")
        String title,

        @JacksonXmlProperty(localName = "desc")
        String desc,

        @JacksonXmlProperty(localName = "category")
        String category
) {
}
