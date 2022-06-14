package com.github.davigon;

import com.github.davigon.domain.Channel;
import com.github.davigon.domain.Programme;
import com.github.davigon.providers.ChannelProvider;
import com.github.davigon.providers.ProgrammeProvider;
import com.github.davigon.services.GzipService;
import com.github.davigon.services.M3uService;
import com.github.davigon.services.XmltvService;
import com.github.davigon.storage.Storage;
import com.github.davigon.utils.Location;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        LOGGER.log(Level.INFO, "Obteniendo canales...");
        ChannelProvider cp = new ChannelProvider();
        List<Channel> channels = cp.get();
        LOGGER.log(Level.INFO, "Canales obtenidos");

        LOGGER.log(Level.INFO, "Obteniendo programación...");
        ProgrammeProvider pp = new ProgrammeProvider();
        List<Programme> programmes = pp.get(LocalDate.now(), 3);
        LOGGER.log(Level.INFO, "Programación obtenida");

        LOGGER.log(Level.INFO, "Obteniendo fichero xmltv...");
        XmltvService xmltvService = new XmltvService();
        Path mepg3Path = Location.JAR_PATH.resolve("mepg3.xml");
        xmltvService.getFile(channels, programmes, mepg3Path);
        LOGGER.log(Level.INFO, "Fichero xmltv obtenido");

        LOGGER.log(Level.INFO, "Obteniendo fichero xmltv.gz...");
        GzipService gzipService = new GzipService();
        Path mepg3GzPath = gzipService.compress(mepg3Path);
        LOGGER.log(Level.INFO, "Fichero xmltv.gz obtenido");

        LOGGER.log(Level.INFO, "Obteniendo fichero M3U...");
        M3uService m3uService = new M3uService();
        Path channelsM3uPath = Location.JAR_PATH.resolve("channels.m3u8");
        m3uService.getFile(channels, channelsM3uPath);
        LOGGER.log(Level.INFO, "Fichero M3U obtenido");

        LOGGER.log(Level.INFO, "Subiendo ficheros a la nube...");
        Storage storage = new Storage();
        storage.uploadFile(mepg3Path, "application/xml", "epg/mepg3.xml");
        storage.uploadFile(mepg3GzPath, "application/gzip", "epg/mepg3.xml.gz");
        storage.uploadFile(channelsM3uPath, "vnd.apple.mpegURL", "ch/channels.m3u8");
        LOGGER.log(Level.INFO, "Ficheros subidos a la nube");
    }
}
