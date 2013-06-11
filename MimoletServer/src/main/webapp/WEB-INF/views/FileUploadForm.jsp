<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page session="true" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>File Upload with Spring 3 MVC</title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1251" >
    </head>
    <body>
        <h1>File Upload Form</h1><br />
        <form:form commandName="FORM" enctype="multipart/form-data" method="POST">
        <table>
         <tr><td colspan="2" style="color: red;"><form:errors path="*" cssStyle="color : red;"/>
         ${errors}
         </td></tr>
         <tr><td>Name : </td><td><form:input type="file" path="file" /></td></tr>
         <tr><td colspan="2"><input type="submit" value="Upload File" /></td></tr>
        </table>
        </form:form>
    </body>
</html>
