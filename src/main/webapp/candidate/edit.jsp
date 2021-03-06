<%--
  Created by IntelliJ IDEA.
  User: FaizievRT
  Date: 015 15.12.2021
  Time: 0:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="ru.job4j.dream.store.DbStore" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.Date" %>
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
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script>
    $(document).ready(function () {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/dreamjob/cityJson.do',
            dataType: 'json'
        }).done(function (data) {
            let result = "";
            // result += "<option disabled selected>Выберите город</option>";
            for (var city of data) {
                result += "<option>" + city.name + "</option>";
            }
            $('#cityName').html(result);
        });
    });
</script>
<body>
<%
    String id = request.getParameter("id");
    Candidate cand = new Candidate(0, "", "",  new Date());
    if (id != null) {
        cand = DbStore.instOf().findByIdCandidate(Integer.valueOf(id));
    }
%>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат
                <% } else { %>
                Редактирование кандидата
                <% } %>
            </div>
            <div class="card-body">
                <form id="my_form" action="<%=request.getContextPath()%>/candidates.do?id=<%=cand.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" required class="form-control" name="name" value="<%=cand.getName()%>">
                        <label for="cityName">Город</label>
                        <select class="form-control" id="cityName" name="cityName">
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>