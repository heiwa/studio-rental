package h2s.studio.rental.action;

import h2s.studio.rental.form.SearchForm;
import h2s.studio.rental.form.SearchIndexForm;
import h2s.studio.rental.schedule.worcle.WorcleRequestSender;
import h2s.studio.rental.schedule.worcle.WorcleSchedule;
import h2s.studio.rental.schedule.worcle.WorcleSite;
import nablarch.common.web.interceptor.InjectForm;
import nablarch.core.message.ApplicationException;
import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.interceptor.OnError;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 検索画面用アクション。
 */
public class SearchAction {

    /**
     * 検索画面初期表示
     * @param request　リクエスト
     * @param context 実行コンテキスト
     * @return 検索画面
     */
    public HttpResponse index(HttpRequest request, ExecutionContext context) {
        SearchIndexForm form = createInitialForm();
        context.setRequestScopedVar("initialForm", form);

        return new HttpResponse("/search/index.jsp");
    }

    private SearchIndexForm createInitialForm() {
        SearchIndexForm form = new SearchIndexForm();
        Calendar calendar = Calendar.getInstance();
        form.setPeriodFrom(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 3);
        form.setSiteList(Arrays.asList(WorcleSite.values()));
        return form;
    }

    /**
     * 検索結果画面表示
     * @param request　リクエスト
     * @param context 実行コンテキスト
     * @return 検索画面
     */
    @InjectForm(form = SearchForm.class, prefix = "form")
    @OnError(type = ApplicationException.class, path = "/errorPages/USER_ERROR.jsp")
    public HttpResponse search(HttpRequest request, ExecutionContext context) {
        final SearchForm form = context.getRequestScopedVar("form");
        List<WorcleSchedule.WorcleResult> result = Arrays.stream(form.getSites())
//                .map(SearchForm.SiteForm::getSite)
                .map(WorcleSite::getSite)
                .flatMap(s -> {
                    WorcleSchedule schedule = WorcleRequestSender.executeRequest(s, form.getYear(), form.getMonth());
                    List<WorcleSchedule.WorcleResult> resultList = schedule.availableRoom(Integer.valueOf(form.getDay()), Integer.valueOf(form.getHourFrom()), Integer.valueOf(form.getHourTo()));
                    return resultList.stream();
                })
                .collect(Collectors.toList());
        SearchIndexForm initialForm = createInitialForm();
        context.setRequestScopedVar("initialForm", initialForm);
        context.setRequestScopedVar("form", form);
        context.setRequestScopedVar("result", result);
        return new HttpResponse("/search/index.jsp");
    }
}
