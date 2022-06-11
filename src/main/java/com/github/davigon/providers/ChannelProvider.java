package com.github.davigon.providers;

import com.github.davigon.domain.Channel;
import com.microsoft.playwright.*;

import java.util.ArrayList;
import java.util.List;

public class ChannelProvider {
    private static final String SOURCE_URL = "https://www.movistarplus.es/diales";

    public List<Channel> get() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.firefox().launch(
                     new BrowserType.LaunchOptions().setHeadless(true));
             BrowserContext browserContext = browser.newContext();
             Page page = browserContext.newPage();
        ) {
            page.waitForNavigation(() -> {
                page.navigate(SOURCE_URL);
            });

            return getChannels(page);
        }
    }

    private List<Channel> getChannels(Page page) {
        List<Channel> channels = new ArrayList<>();

        Locator j_dial = page.locator(".j_dial");
        for (int i = 0; i < j_dial.count(); i++) {
            Locator j_dial_i = j_dial.nth(i);

            String href = j_dial_i.locator("a").getAttribute("href");
            int idPos = href.lastIndexOf('=');

            String id = href.substring(idPos + 1);
            String name = j_dial_i.locator(".canal-title").textContent();
            int number = Integer.parseInt(j_dial_i.locator(".box-pixel").textContent());
            String logoUrl = "https://www.movistarplus.es/recorte/m-NEO/canal/" + id + ".png";

            channels.add(new Channel(id, name, number, logoUrl));
        }

        return channels;
    }
}
