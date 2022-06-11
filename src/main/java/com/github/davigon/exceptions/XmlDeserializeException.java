package com.github.davigon.exceptions;

public class XmlDeserializeException extends RuntimeException {

    public XmlDeserializeException() {
        super("Error al deserializar el XML a objeto.");
    }
}
