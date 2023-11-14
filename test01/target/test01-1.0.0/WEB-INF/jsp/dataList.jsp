<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<table>
<caption>
<div>Data List</div><br/>
</caption>
<colgroup>
<col width='10%'>
<col width='10%'>
<col width='15%'>
<col width='15%'>
<col width='15%'>
<col width='25%'>
<col width='10%'>
</colgroup>
<tr>
<th>번호</th>
<th>장치</th>
<th>온도</th>
<th>습도</th>
<th>CO₂</th>
<th>등록일시</th>
<th>삭제</th>
</tr>

<c:forEach var="result" items="${resultList}" varStatus="status">
<tr align='center'>
<td><c:out value="${result.id}"/></td>
<td><c:out value="${result.i}"/></td>
<td><c:out value="${result.t}"/>℃</td>
<td>${result.h}%</td>
<td>${result.c}ppm</td>
<td>${result.d}</td>
<td><a href="dataDelete.do?id=${result.id}">[삭제]</a></td>
</tr>
</c:forEach>
</table>
<c:forEach var="i" begin="1" end="${total}">
	<a href="list.do?page=${i}">${i}</a> &nbsp; &nbsp;
</c:forEach>
</center>
</body>
</html>