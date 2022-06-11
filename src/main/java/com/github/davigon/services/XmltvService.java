package com.github.davigon.services;

import com.github.davigon.domain.Channel;
import com.github.davigon.domain.Programme;
import com.github.davigon.mappers.XmlMapper;
import com.github.davigon.services.data.Xmltv;
import com.github.davigon.services.data.XmltvChannel;
import com.github.davigon.services.data.XmltvProgramme;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class XmltvService {
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public Path getFile(List<Channel> channels, List<Programme> programmes, Path out) throws IOException {
        List<XmltvChannel> xmltvChannels = channels.stream()
                .map(c -> new XmltvChannel(
                        c.getName(),
                        c.getName()))
                .collect(Collectors.toList());

        List<XmltvProgramme> xmltvProgrammes = programmes.stream()
                .map(p -> new XmltvProgramme(
                        p.getStart(),
                        p.getStop(),
                        p.getChannel(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getCategory()
                ))
                .collect(Collectors.toList());

        Xmltv xmltv = new Xmltv(
                LocalDate.now(),
                "",
                "",
                xmltvChannels,
                xmltvProgrammes
        );

        return XML_MAPPER.objectToXmlFile(xmltv, out);
    }
}
