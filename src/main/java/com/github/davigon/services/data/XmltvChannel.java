package com.github.davigon.services.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public record XmltvChannel(
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        String id,

        @JacksonXmlProperty(localName = "display-name")
        String displayName) {
}
