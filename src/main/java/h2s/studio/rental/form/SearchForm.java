package h2s.studio.rental.form;

import h2s.studio.rental.schedule.worcle.WorcleSite;
import nablarch.common.code.validator.ee.CodeValue;
import nablarch.core.validation.ee.Length;
import nablarch.core.validation.ee.NumberRange;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 検索条件用Form。
 */
public class SearchForm implements Serializable {

    private static final long serialVersionUID = 1L;

    // バリデーションができとうすぎる
    @NumberRange(min = 2017, max = 2025)
    private String year;
    @NumberRange(min = 1, max = 12)
    private String month;
    @CodeValue(codeId = "C0000002")
    private String day;
    @CodeValue(codeId = "C0000001")
    private String hourFrom;
    @CodeValue(codeId = "C0000001")
    private String hourTo;
    private String[] sites;

    public SearchForm() {
//        sites = new String[];
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHourFrom() {
        return hourFrom;
    }

    public void setHourFrom(String hourFrom) {
        this.hourFrom = hourFrom;
    }

    public String getHourTo() {
        return hourTo;
    }

    public void setHourTo(String hourTo) {
        this.hourTo = hourTo;
    }

    public String[] getSites() {
        return sites;
    }

    public void setSites(String[] sites) {
        this.sites = sites;
    }

    @AssertTrue
    public boolean isValidSite() {
        if (sites == null) {
            return false;
        }
        return Arrays.stream(sites)
                .allMatch(s ->
                        Arrays.stream(WorcleSite.values())
                                .anyMatch(t -> t.getValue().equals(s)));
    }
}
