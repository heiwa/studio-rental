package h2s.studio.rental.schedule.worcle;

import java.util.Arrays;

/**
 * worcle の 店舗
 */
public enum WorcleSite {
    IKEBUKURO("ikebukuro", "池袋"),
    BABA("baba", "高田馬場"),
    SHIBUYA("shibuya", "渋谷"),
    HARAJUKU("harajuku", "原宿"),
    GYOEN("gyoen", "新宿御苑"),
    YOYOGI("yoyogi", "代々木");

    private String value;
    private String label;
    private WorcleSite(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return "https://www.studioworcle.com/"+ value +"/schedule/";
    }

    public static WorcleSite getSite(String value) {
        for (WorcleSite site : WorcleSite.values()) {
            if (site.getValue().equals(value)) {
                return site;
            }
        }
        return null;
    }
}
