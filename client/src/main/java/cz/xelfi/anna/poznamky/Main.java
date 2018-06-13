package cz.xelfi.anna.poznamky;

import cz.xelfi.anna.poznamky.js.PlatformServices;
import java.util.prefs.Preferences;
import net.java.html.boot.BrowserBuilder;

public final class Main {
    private Main() {
    }

    public static void main(String... args) throws Exception {
        BrowserBuilder.newBrowser().
            loadPage("pages/index.html").
            loadClass(Main.class).
            invoke("onPageLoad", args).
            showAndWait();
        System.exit(0);
    }

    /**
     * Called when the page is ready.
     */
    public static void onPageLoad() throws Exception {
        UIModel.onPageLoad(new DesktopServices());
    }

    private static final class DesktopServices extends PlatformServices {
        @Override
        public String getPreferences(String key) {
            return Preferences.userNodeForPackage(Main.class).get(key, null);
        }

        @Override
        public void setPreferences(String key, String value) {
            Preferences.userNodeForPackage(Main.class).put(key, value);
        }
    }

}
