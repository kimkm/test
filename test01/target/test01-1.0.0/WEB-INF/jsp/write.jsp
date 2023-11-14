<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ include file="/WEB-INF/jsp/header.jsp" %>
<h3>Data upload</h3>
		<form name='frm' method="post" action="datainput.do">

			<table>
				<tr>
					<th>장치명</th>
					<td><input type='text' name='i'></td>
				</tr>
				<tr>
					<th>일 시</th>
					<td><input type='text' name='d' maxlength='14'> YYYYMMDDHHmmSS</td>
				</tr>
				<tr>
					<th>온 도</th>
					<td><input type='text' name='t'> ℃</td>
				</tr>
				<tr>
					<th>습 도</th>
					<td><input type='text' name='h'> %</td>
				</tr>
				<tr>
					<th>C O₂</th>
					<td><input type='text' name='c'> ppm</td>
				</tr>
				<tr>
					<td colspan=2 align='center'><input type='button'
						onclick='fn_submit()' value=' 저 장 '></td>
				</tr>
			</table>
			
			<br> <br> 
		<table>
		<tr><th> Method </th><td> POST</td></tr>  
		<tr><th> Host </th><td> 192.168.219.106:8080/test/api/datainput.do</td></tr>
		<tr><th> Header</th><td> Content-Type: application/json</td></tr>
		<tr><th> Body </th><td>{ "t":[float], "h":[float], "c":[float], "i":[String], "d":[String] }</td></tr>
		<tr><th> Result </th><td> { "result":"OK" }</td></tr>
		</table>
		<br><br>
		<table>
		<tr><th> Method </th><td> GET</td></tr>  
		<tr><th> Host </th><td> 192.168.219.106:8080/test/api/datainput.do?i=dev02&d=20231122112233&t=25.2&h=80.5&c=455.6</td></tr>
		<tr><th> Result </th><td> "OK" </td></tr>
		</table>
※i:장치명(문자), t:온도(숫자), h:습도(숫자), c:CO₂(숫자), d:등록일시(문자)
		</form>
		<script type="text/javascript">
			function fn_submit() {
				if (document.frm.t.value == ""
						|| document.frm.c.value == ""
						|| document.frm.h.value == "") {
					alert("내용을 입력하세요");
					document.frm.i.focus();
					return false;
				}
				document.frm.submit();
			}
		</script>
	</center>
</body>
</html>