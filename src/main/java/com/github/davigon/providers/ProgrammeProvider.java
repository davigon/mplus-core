package com.github.davigon.providers;

import com.github.davigon.domain.Programme;
import com.github.davigon.mappers.XmlMapper;
import com.github.davigon.providers.data.Xml;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgrammeProvider {
    private final static Logger LOGGER = Logger.getLogger(ProgrammeProvider.class.getName());

    private static final String SOURCE_URL = "https://comunicacion.movistarplus.es/programacion/";
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    public List<Programme> get(LocalDate from, int days) {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(
                     new BrowserType.LaunchOptions().setHeadless(true));
             BrowserContext browserContext = browser.newContext();
             Page page = browserContext.newPage();
        ) {
            page.waitForNavigation(() -> {
                page.navigate(SOURCE_URL);
            });

            return getProgrammes(page, from, days);
        } catch (TimeoutError | IOException te) {
            LOGGER.log(Level.WARNING, "Descarga fallida. Reintentando descarga...");
            return get(from, days);
        }
    }

    private List<Programme> getProgrammes(Page page, LocalDate from, int days) throws IOException {
        page.click("text=\"Exportar programación\"");
        page.waitForSelector("#exportProgramation > div > div > div.modal-header");

        selectDays(page, from, days);
        selectChannels(page);
        selectFormat(page);

        Path dowloadPath = this.download(page);
        Xml xml = XML_MAPPER.xmlFileToObject(dowloadPath, Xml.class);
        return xml.toProgrammeList();
    }

    private void selectDays(Page page, LocalDate from, int days) {
        page.fill("#export-date-from",
                from.toString());
        page.fill("#export-date-to",
                from.plusDays(days - 1).toString());
    }

    private void selectChannels(Page page) {
        page.waitForSelector("#container-checkbox-categories-export > div:nth-child(1) > small.show");
        int n = page.locator("#container-checkbox-categories-export > div").count();
        for (int i = 1; i < n; i = i + 2) {
            page.dispatchEvent(
                    "#container-checkbox-categories-export > div:nth-child(" + i + ") > label",
                    "click");
        }
    }

    private void selectFormat(Page page) {
        page.selectOption("#export-format",
                new SelectOption().setLabel("Xml"));
    }

    private Path download(Page page) throws IOException {
        Download download = page.waitForDownload(() -> {
            page.click("#btn-export-programation");
        });

        Path downloadPath = download.path();
        Path tempPath = Files.createTempFile("epg", ".xml");

        return XML_MAPPER.normalizeXmlFile(downloadPath, tempPath);
    }
}
