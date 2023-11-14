<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ include file="/WEB-INF/jsp/header.jsp" %>
		<table>
			<caption>
				<div>Audio List</div>
				<br />
			</caption>
			<colgroup>
				<col width='10%'>
				<col width='10%'>
				<col width='45%'>
				<col width='25%'>
				<col width='10%'>
			</colgroup>
			<tr>
				<th>번 호</th>
				<th>장치명</th>
				<th>파 일</th>
				<th>등록일시</th>
				<th>삭 제</th>
			</tr>

			<c:forEach var="result" items="${resultList}" varStatus="status">
				<tr align='center'>
					<td><c:out value="${result.id}" /></td>
					<td><c:out value="${result.i}" /></td>
					<td><audio id="audioPlayer" controls>
							<source src="fileDownload.do?fileName=${result.filename}"
								type="audio/wav">
							브라우저가 오디오를 지원하지 않습니다.
						</audio></td>
					<td>${result.d}</td>
					<td><a href="fileDelete.do?id=${result.id}&filename=${result.filename}">[삭제]</a></td>
				</tr>
			</c:forEach>
		</table>
		<br>
		<c:forEach var="i" begin="1" end="${total}">
			<a href="fileList.do?page=${i}">${i}</a> &nbsp;&nbsp;
		</c:forEach>
	</center>
</body>
</html>