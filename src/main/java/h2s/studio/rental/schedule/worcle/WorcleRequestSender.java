package h2s.studio.rental.schedule.worcle;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * worcleのサイトからスケジュールを取得する。
 */
public class WorcleRequestSender {

    public static void main(String[] args) {
        Arrays.stream(WorcleSite.IKEBUKURO.getClass().getEnumConstants())
                .forEach(System.out::println);
//        WorcleSchedule worcleSchedule = executeRequest(WorcleSite.IKEBUKURO, "2017", "08");
//        System.out.println(worcleSchedule);
//        List<WorcleSchedule.WorcleResult> rooms = worcleSchedule.availableRoom(27, 0, 2);
//        rooms.forEach(System.out::println);
    }

    public static WorcleSchedule executeRequest(WorcleSite site, String year, String month) {
        Form form = Form.form().add("wcl_sch_year", year).add("wcl_sch_month", month);

        WorcleSchedule worcleSchedule = null;
        try {
            Response response = Request.Post(site.getUrl())
                    .bodyForm(form.build())
                    .execute();

            worcleSchedule = new WorcleSchedule(response.returnContent().asString());
            worcleSchedule.setSite(site);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return worcleSchedule;
    }
}
