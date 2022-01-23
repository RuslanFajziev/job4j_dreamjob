<%--
  Created by IntelliJ IDEA.
  User: FaizievRT
  Date: 001 01.01.2022
  Time: 17:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <title>Работа мечты</title>
</head>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
        integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
        crossorigin="anonymous"></script>
<script>
    function senderAuth() {
        if (checkDate()) {
            $('#my_form').submit();
        }
    }

    function checkDate() {
        let email = $('#email').val();
        let password = $('#password').val();
        if (email == '') {
            alert($('#email').attr('title'));
            return false;
        }
        if (password == '') {
            alert($('#password').attr('title'));
            return false;
        }
        return true;
    }
</script>
<body>
<div class="container pt-3">

    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                Авторизация
            </div>
            <div class="card-body">
                <form id="my_form" action="<%=request.getContextPath()%>/auth.do" method="post">
                    <div class="form-group">
                        <label>Почта</label>
                        <input type="text" class="form-control" title="поле Почта не заполнено" name="email"
                               id="email">
                    </div>
                    <div class="form-group">
                        <label>Пароль</label>
                        <input type="password" class="form-control" title="поле Пароль не заполнено" name="password"
                               id="password">
                    </div>
                    <button type="button" class="btn btn-primary" onclick="senderAuth();">Войти</button>
                </form>
                <c:if test="${not empty error}">
                    <div style="color:red; font-weight: bold; margin: 30px 0;">
                        <c:out value="${error}"/>
                    </div>
                    <form action="<%=request.getContextPath()%>/reg.jsp" method="post">
                        <button type="submit" class="btn btn-primary">Регистрация</button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>