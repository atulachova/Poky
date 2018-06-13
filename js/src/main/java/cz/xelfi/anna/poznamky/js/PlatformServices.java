package cz.xelfi.anna.poznamky.js;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import net.java.html.BrwsrCtx;
import net.java.html.js.JavaScriptBody;
import net.java.html.json.Models;

public class PlatformServices {
    public final void storeList(String prefix, List<?> arr) {
        final int count = arr.size();
        setPreferences(prefix + ".count", "" + count);
        for (int i = 0; i < count; i++) {
            setPreferences(prefix + "." + i, arr.get(i).toString());
        }
    }

    public final <T> void readList(String prefix, Class<T> type, List<T> arr) {
        BrwsrCtx ctx = BrwsrCtx.findDefault(type);
        int count;
        try {
            count = Integer.parseInt(getPreferences(prefix + ".count"));
        } catch (NumberFormatException ex) {
            count = 0;
        }
        arr.clear();
        for (int i = 0; i < count; i++) {
            String value = getPreferences(prefix + "." + i);
            try (ByteArrayInputStream is = new ByteArrayInputStream(value.getBytes("UTF-8"))) {
                T obj = Models.parse(ctx, type, is);
                if (obj != null) {
                    arr.add(obj);
                }
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    /**
     * Reads a value from a persistent storage.
     * @param key the identification for the value
     * @return the value or <code>null</code> if not found
     */
    public String getPreferences(String key) {
        return getPreferencesImpl(key);
    }

    /**
     * Puts a value into the persitent storage.
     * @param key the identification for the value
     * @param value the value to store
     */
    public void setPreferences(String key, String value) {
        setPreferencesImpl(key, value);
    }

    @JavaScriptBody(args = { "key" }, body =
        "if (!window.localStorage) return null;\n" +
        "return window.localStorage.getItem(key);\n"
    )
    private static native String getPreferencesImpl(String key);

    @JavaScriptBody(args = { "key", "value" }, body =
        "if (!window.localStorage) return;\n" +
        "window.localStorage.setItem(key, value);\n"
    )
    private static native void setPreferencesImpl(String key, String value);

}
