package h2s.studio.rental.schedule.worcle;

import nablarch.common.code.CodeUtil;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * worcleのスケジュールを保持する。
 * 空き状況の問い合わせにも回答する。
 */
public class WorcleSchedule {
    private static Pattern PATTERN = Pattern.compile("<input type=\"hidden\".*?>");

    private String year;
    private String month;
    private int startHour;
    private Map<String, boolean[][]> schedule;
    private String[] rooms;
    private String[] roomIDs;
    private WorcleSite site;

    public WorcleSchedule(String content) {
        String hiddenStr = extractHidden(content);
        setFrom(hiddenStr);
    }
    private int offset(int index) {
        int offset = Integer.parseInt(CodeUtil.getShortName("C0000001", "0"));
        int h = offset / 100;
        int m = (offset % 100) == 0 ? 0 : 1;
        return index + (h - startHour) * 2 + m;
    }

    private void setFrom(String hidden) {
        List nodes = null;
        Element root = null;
        try {
            Document doc = new SAXReader().read(new ByteArrayInputStream(hidden.getBytes()));
            root = doc.getRootElement();
            nodes = doc.selectNodes("//input[@type=\"hidden\"]");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Iterator iterator = nodes.iterator();
        while (iterator.hasNext()) {
            Element e = (Element)iterator.next();
            Attribute attr = e.attribute("name");
            if (attr != null) {
//                System.out.println("set : " + attr.getValue() + " = " + e.attributeValue("value"));
                setFrom(e, attr.getValue());
            }
        }
        this.schedule = new LinkedHashMap<>();
        for (int roomIndex=0; roomIndex<roomIDs.length; roomIndex++) {
            String roomId = roomIDs[roomIndex];
            Element e = (Element) root.selectSingleNode(String.format("//input[@id=\"D_%s\"]", roomId));
            String[] scheduleStr = e.attributeValue("value").split(",");
            boolean[][] schedule = new boolean[scheduleStr.length][scheduleStr[0].length()];
            for (int i=0; i<scheduleStr.length; i++) {
                for (int j=0; j<scheduleStr[i].length(); j++) {
                    schedule[i][j] = toBoolean(scheduleStr[i].charAt(j));
                }
            }
            this.schedule.put(rooms[roomIndex], schedule);
        }
    }
    private boolean toBoolean(char bit) {
        if (bit == '1') {
            return true;
        }
        return false;
    }
    private void setFrom(Element e, String name) {
        if ("y".equals(name)) {
            year = e.attributeValue("value");
        } else if ("m".equals(name)) {
            month = e.attributeValue("value");
        } else if ("start_hour".equals(name)) {
            startHour = Integer.parseInt(e.attributeValue("value"));
        } else if ("room_ids".equals(name)) {
            roomIDs = e.attributeValue("value").split(",");
        } else if ("room_names".equals(name)) {
            rooms = e.attributeValue("value").split(",");
        }
    }
    private String extractHidden(String content) {
        Matcher m = PATTERN.matcher(content);
        StringBuilder builder = new StringBuilder();
        builder.append("<root>").append(System.lineSeparator());
        while (m.find()) {
            String str = m.group();
            if (!str.endsWith("/>")) {
                str = str.replace(">", "/>");
            }
            builder.append(str).append(System.lineSeparator());
        }
        builder.append("</root>");
        return builder.toString();
    }

    public List<WorcleResult> availableRoom(int day, int start, int end) {
        List<WorcleResult> availableRooms = new ArrayList<>();
        for (Map.Entry<String, boolean[][]> entry : schedule.entrySet()) {
            boolean[] resavation = entry.getValue()[day - 1];
            boolean avail = true;
            for (int i = offset(start); i < offset(end); i++) {
                if (resavation[i]) {
                    avail = false;
                    break;
                }
            }
            if (avail) {
                availableRooms.add(new WorcleResult(site, entry.getKey()));
            }
        }
        return availableRooms;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(year + "/" + month + ":" + startHour).append(System.lineSeparator());
        Arrays.stream(rooms).forEach(
                room -> {
                    builder.append(room + ":");
                    boolean[][] roomSchedule = schedule.get(room);
                    for (int i=0; i<roomSchedule.length; i++) {
                        for (boolean reserve : roomSchedule[i]) {
                            if (reserve) {
                                builder.append("-");
                            } else {
                                builder.append(" ");
                            }
                        }
                        builder.append(",");
                    }
                    builder.append(System.lineSeparator());
                }
        );
        return builder.toString();
    }

    public void setSite(WorcleSite site) {
        this.site = site;
    }
    public static class WorcleResult {
        private WorcleSite site;
        private String room;

        public WorcleResult(WorcleSite site, String room) {
            this.site = site;
            this.room = room;
        }

        public WorcleSite getSite() {
            return site;
        }

        public void setSite(WorcleSite site) {
            this.site = site;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }
}
