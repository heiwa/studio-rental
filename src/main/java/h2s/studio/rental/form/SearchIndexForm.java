package h2s.studio.rental.form;

import h2s.studio.rental.schedule.worcle.WorcleSite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 検索初期表示用のform。
 */
public class SearchIndexForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SearchIndexYear> yearList;
    private List<SearchIndexMonth> monthList;
    private List<WorcleSite> siteList;

    public List<SearchIndexYear> getYearList() {
        return yearList;
    }

    public void setYearList(List<SearchIndexYear> yearList) {
        this.yearList = yearList;
    }

    public List<SearchIndexMonth> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<SearchIndexMonth> monthList) {
        this.monthList = monthList;
    }

    public List<WorcleSite> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<WorcleSite> siteList) {
        this.siteList = siteList;
    }

    public void setPeriodFrom(int year, int month, int during) {
        List<SearchIndexYear> yearList = new ArrayList<>();
        List<SearchIndexMonth> monthList = new ArrayList<>();

        SearchIndexYear y = new SearchIndexYear();
        y.setYear(String.format("%04d", year));
        yearList.add(y);

        int yp = 1;
        for (int i=0, j=0; i<during; i++, j++) {
            SearchIndexMonth m = new SearchIndexMonth();
            if (month + j > 12) {
                j = j - 12;
                y = new SearchIndexYear();
                y.setYear(String.format("%04d", year + yp));
                yearList.add(y);
                yp++;
            }
            m.setMonth(String.format("%02d", month + j));
            monthList.add(m);
        }
        this.yearList = yearList;
        this.monthList = monthList;
    }

    public class SearchIndexMonth {
        private String month;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

    }
    public class SearchIndexYear {
        private String year;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
