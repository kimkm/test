<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/header.jsp"%>
<h3>Audio file upload</h3>
<form id="fileUploadForm" method="post" action="fileUpload.do"
	enctype="multipart/form-data">
	<table>
		<tr>
			<th>장치명</th>
			<td><input type='text' name='i'></td>
		</tr>
		<tr>
			<th>일 시</th>
			<td><input type='text' name='d' maxlength='14'>
				YYYYMMDDHHmmSS</td>
		</tr>
		<tr>
			<th>WAV 파일</th>
			<td><input type="file" id="uploadFile" name="awfile"></td>
		</tr>

		<tr>
			<td colspan="2" align="center"><input type="submit" value="업로드"></td>
		</tr>
	</table>
</form>
<br>
<br>
<table>
	<tr>
		<th>Method</th>
		<td>POST</td>
	</tr>
	<tr>
		<th>Host</th>
		<td>192.168.219.106:8080/test/api/fileUpload.do</td>
	</tr>
	<tr>
		<th>Header</th>
		<td>Content-Type: multipart/form-data</td>
	</tr>
	<tr>
		<th>Body</th>
		<td>i = "[deviceID]" ,<br>d = "[YYYYMMDDHHmmSS]" ,<br>awfile = "[FilePath]"
			
		</td>
	</tr>
	<tr>
		<th>Result</th>
		<td>"OK"</td>
	</tr>
</table>
※i:장치명(문자), d:등록일시(문자), awfile:wav파일
</center>
</body>
</html>