package cz.xelfi.anna.poznamky;

import cz.xelfi.anna.poznamky.js.PlatformServices;

public class BrowserMain {
    private BrowserMain() {
    }

    public static void main(String... args) throws Exception {
        UIModel.onPageLoad(new WebServices());
    }

    private static final class WebServices extends PlatformServices {
    }
}
