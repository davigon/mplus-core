package com.github.davigon.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.davigon.exceptions.XmlDeserializeException;
import com.github.davigon.exceptions.XmlSerializeException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class XmlMapper {
    private final com.fasterxml.jackson.dataformat.xml.XmlMapper xmlMapper = new com.fasterxml.jackson.dataformat.xml.XmlMapper();

    public XmlMapper() {
        xmlMapper.registerModule(new JavaTimeModule());
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
    }

    public Path objectToXmlFile(Object o, Path out) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            xmlMapper.writeValue(writer, o);
        } catch (JsonProcessingException e) {
            throw new XmlSerializeException();
        }

        return out;
    }

    public <T> T xmlFileToObject(Path in, Class<T> clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            return (T) jaxbUnmarshaller.unmarshal(in.toFile());
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new XmlDeserializeException();
        }
    }

    public String normalizeXmlString(String xml) {
        return xml.replace("''", "\"")
                .replace("&", "&amp;")
                .replace("<.<", ".<");
    }

    public Path normalizeXmlFile(Path in, Path out) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
             BufferedReader reader = Files.newBufferedReader(in, StandardCharsets.UTF_8)
        ) {
            String line;
            while ((line = reader.readLine()) != null)
                writer.write(this.normalizeXmlString(line));

            return out;
        }
    }
}
