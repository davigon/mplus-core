package com.github.davigon.exceptions;

public class XmlSerializeException extends RuntimeException {

    public XmlSerializeException() {
        super("Error al serializar el objeto a XML.");
    }
}
