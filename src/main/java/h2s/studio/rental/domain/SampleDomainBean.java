package h2s.studio.rental.domain;

import nablarch.core.validation.ee.Length;
import nablarch.core.validation.ee.Required;
import nablarch.core.validation.ee.SystemChar;

/**
 * ドメイン定義。
 */
public class SampleDomainBean {

    /**
     * ドメイン定義の例。
     */
    @SystemChar(charsetDef = "システム許容文字")
    @Length(min = 3, max = 5)
    public String exampleDomain;

    /**
     * 年
     */
    @SystemChar(charsetDef = "半角数字")
    @Length(min = 4, max = 4)
    @Required
    public String year;

    /**
     * 月
     */
    @SystemChar(charsetDef = "半角数字")
    @Length(min = 2, max = 2)
    @Required
    public String month;

    /**
     * 日
     */
    @SystemChar(charsetDef = "半角数字")
    @Length(min = 1, max = 2)
    @Required
    public String day;

}
