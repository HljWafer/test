<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Proxy Webapp</title>
</head>
<body>
<form action="getResult">
    ${msg}
    <table>
        <tr>
            <td>语句</td>
            <td>
                <textarea name="query" cols=100 rows=6></textarea>
            </td>
        </tr>
        <tr>
            <td></td>
            <td>
                <input type="submit" value="提交">
            </td>
        </tr>
    </table>
    ${msg1}
</form>

</body>
</html>
