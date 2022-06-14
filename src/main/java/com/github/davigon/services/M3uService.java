package com.github.davigon.services;

import com.github.davigon.domain.Channel;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class M3uService {

    public Path getFile(List<Channel> channels, Path out) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        ) {
            writer.write("#EXTM3U");
            writer.newLine();
            writer.write("#EXTVLCOPT:http-reconnect=true");
            writer.newLine();
            writer.newLine();

            for (Channel c : channels) {
                writer.write(channelToM3uString(c));
                writer.newLine();
                writer.write("URL");
                writer.newLine();
            }

            return out;
        }
    }

    private String channelToM3uString(Channel c) {
        return "#EXTINF:-1 " +
                "tvg-logo=\"" + c.getLogoUrl() + "\" " +
                "tvg-id=\"" + c.getId() + "\" " +
                "tvg-name=\"" + c.getName() + "\", " +
                "" + String.format("%03d", c.getNumber()) + " " +
                c.getName();
    }
}
