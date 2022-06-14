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
import picocli.CommandLine;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

@CommandLine.Command(name = "mplus", mixinStandardHelpOptions = true, version = "mplus 1.0.0")
public class Main implements Callable<Integer> {
    private final static Logger LOGGER = Logger.getLogger(Main.class.getName());

    @CommandLine.Option(names = {"--xmltv"}, description = "Generar xmltv.")
    private boolean xmltv;

    @CommandLine.Option(names = {"--m3u"}, description = "Generar M3U.")
    private boolean m3u;

    @CommandLine.Option(names = {"-u", "--upload"}, description = "Subir ficheros a la nube.")
    private boolean upload;

    public static void main(String[] args) {
        int exitCode = new picocli.CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        if (!xmltv && !m3u)
            return 2;

        try {
            List<Channel> channels;
            List<Programme> programmes;
            Path mepg3Path = Location.JAR_PATH.resolve("mepg3.xml");
            Path mepg3GzPath = Location.JAR_PATH.resolve("mepg3.xml.gz");
            Path channelsM3uPath = Location.JAR_PATH.resolve("channels.m3u8");

            LOGGER.log(Level.INFO, "Obteniendo canales...");
            ChannelProvider cp = new ChannelProvider();
            channels = cp.get();
            LOGGER.log(Level.INFO, "Canales obtenidos");

            if (xmltv) {
                LOGGER.log(Level.INFO, "Obteniendo programación...");
                ProgrammeProvider pp = new ProgrammeProvider();
                programmes = pp.get(LocalDate.now(), 3);
                LOGGER.log(Level.INFO, "Programación obtenida");

                LOGGER.log(Level.INFO, "Obteniendo fichero xmltv...");
                XmltvService xmltvService = new XmltvService();
                xmltvService.getFile(channels, programmes, mepg3Path);
                LOGGER.log(Level.INFO, "Fichero xmltv obtenido");

                LOGGER.log(Level.INFO, "Obteniendo fichero xmltv.gz...");
                GzipService gzipService = new GzipService();
                gzipService.compress(mepg3Path);
                LOGGER.log(Level.INFO, "Fichero xmltv.gz obtenido");
            }

            if (m3u) {
                LOGGER.log(Level.INFO, "Obteniendo fichero M3U...");
                M3uService m3uService = new M3uService();
                m3uService.getFile(channels, channelsM3uPath);
                LOGGER.log(Level.INFO, "Fichero M3U obtenido");
            }

            if (upload) {
                LOGGER.log(Level.INFO, "Subiendo ficheros a la nube...");
                Storage storage = new Storage();
                if (xmltv) {
                    storage.uploadFile(mepg3Path, "application/xml", "epg/mepg3.xml");
                    storage.uploadFile(mepg3GzPath, "application/gzip", "epg/mepg3.xml.gz");
                }
                if (m3u) {
                    storage.uploadFile(channelsM3uPath, "vnd.apple.mpegURL", "ch/channels.m3u8");
                }
                LOGGER.log(Level.INFO, "Ficheros subidos a la nube");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }


}
