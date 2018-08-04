<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="n" uri="http://tis.co.jp/nablarch" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>スタジオ空室検索</title>
  <link rel="stylesheet" href="/css/material-components-web.min.css">
  <n:include path="/common/header.jsp"/>
</head>
<body>
<div class="container">
  <n:form>
    <h4>日時</h4>
    <div class="row">
    <n:select cssClass="col-xs-3" name="form.year" listName="initialForm.yearList" elementValueProperty="year" elementLabelProperty="year" elementLabelPattern="$LABEL$" />
    /<n:select cssClass="col-xs-3" name="form.month" listName="initialForm.monthList" elementValueProperty="month" elementLabelProperty="month" elementLabelPattern="$LABEL$"/>
    /<n:codeSelect cssClass="col-xs-3" name="form.day" codeId="C0000002" />
    <br />
    <n:codeSelect cssClass="col-xs-5" name="form.hourFrom" codeId="C0000001" />
    〜
    <n:codeSelect cssClass="col-xs-5" name="form.hourTo" codeId="C0000001" />
    <br />
    <br />
    worcle店舗：
    <n:select cssClass="col-xs-5" name="form.sites" listName="initialForm.siteList" elementValueProperty="value" elementLabelProperty="label" elementLabelPattern="$LABEL$" multiple="true"/>
    <br />
    <n:submit cssClass="btn btn-primary" type="submit" uri="/action/Search/search" value="検索"/>
    </div>
  </n:form>

  <c:if test="${!empty result}">
    <h4>検索結果</h4>
    <table class="table">
      <tr>
        <td>スタジオ</td>
        <td>部屋</td>
      </tr>
    <c:forEach items="${result}" var="item">
      <tr>
        <td>
          <n:write name="item.site.label" />
        </td>
        <td>
          <a href="<n:write name="item.site.url" />">
            <n:write name="item.room" />
          </a>
        </td>
      </tr>
    </c:forEach>
    </table>
  </c:if>
</div>
</body>
</html>