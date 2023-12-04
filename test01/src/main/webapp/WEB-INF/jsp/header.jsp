<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>test</title>
<style>
table {
	width: 800px;
	border-collapse: collapse;
}

th, td {
	border: 1px solid #cccccc;
	padding: 5px;

}

th {
	background-color: #ffffca;
}
audio {
	height: 30px;
}

        body {
            display: flex;
            justify-content: center;

            min-height: 100vh;
            margin: 0;
            font-family: 'Roboto', sans-serif;
        }
        .overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 9998;
            display: none;
        }

        .loader {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            z-index: 9999;
            width: 200px;
            height: 200px;
            border-radius: 50%;
            background: linear-gradient(to right, #f09, #3023AE, #cddc39, #cd0000, #FFDD00);
            background-size: 200% 200%;
            animation: progress 2s linear infinite;
            display: none;
            justify-content: center;
            align-items: center;
        }

        .loader::before {
            content: "Filtering...!";
            color: white;
            font-size: 30px;
        }

        @keyframes progress {
            0% {
                background-position: 100% 0;
            }
            100% {
                background-position: 0 100%;
            }
        }

</style>
</head>
<body>
	<center>
		<br>

		<table>
			<tr>
				<td align='center'><a href="list.do">[Data_List]</a> | <a
					href="write.do">[Data_Input]</a> &nbsp; &nbsp;
					<img src="ci.png" width="70"> 
					 &nbsp; &nbsp; <a	href="fileList.do">[Audio_List]</a> | <a href="fileWrite.do">[Audio_Input]</a>
				</td>
			</tr>
		</table>

		<br>
		<hr>
		<br>