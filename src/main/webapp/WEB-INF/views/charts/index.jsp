<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="th" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>JB Admin</title>
</head>
<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<script src="/resources/js/Chart.min.js"></script>

<div class=" m-2 mt-2 panel-body w-100">
    <select id="comboA" onchange="getComboA(this)">
        <c:forEach var="record" items="${productlist}">
            <c:choose>
                <c:when test="${record eq product}">
                    <option value="${record}" selected>${record}</option>
                </c:when>
                <c:otherwise>
                    <option value="${record}">${record}</option>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </select>

    <c:choose>
        <c:when test="${data.length() > 0 }">
            <div>
                <div th:replace="fragments/navbar :: top"></div>
                <div class="chart-container" style="margin: 0 auto;   width:60vw">
                    <canvas id="myChart" height="110"></canvas>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <tr align="center">
                <td colspan="5">No data</td>
            </tr>
        </c:otherwise>
    </c:choose>

    <c:choose>
        <c:when test="${dataDay.length() > 0 }">
            <div>
                <div th:replace="fragments/navbar :: top"></div>
                <div class="chart-container" style="margin: 0 auto;   width:60vw">
                    <canvas id="myChartDay" height="110"></canvas>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="5">No data</td>
            </tr>
        </c:otherwise>
    </c:choose>
</div>

<script>
    function getComboA(selectObject) {
        var value = selectObject.value;
        location.replace("index?licensename=" + value);
    }

    var htmldata = [${data}];
    var labels = htmldata.map(function (e) {
        return e.x;
    });
    var ctx1 = document.getElementById('myChart').getContext('2d');
    const config1 = {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: "${product}",
                data: Object.keys(htmldata).map(function (key) {
                    return htmldata[key];
                }),
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                tension: 0.1
            }]
        },
    }

    var htmldataDay = [${dataDay}];
    var labelsDay = htmldataDay.map(function (e) {
            return e.x;
        }),
        dataDay = htmldataDay.map(function (e) {
            return e.y;
        });


    var ctxDay = document.getElementById('myChartDay').getContext('2d');
    const configDay = {
        type: 'line',
        data: {
            labels: labelsDay,
            datasets: [{
                label: "${product}",
                data: dataDay,
                fill: false,
                borderColor: 'rgb(128, 128, 0)',
                tension: 0.1
            }]
        },
    }

    var myChart = new Chart(ctx1, config1);
    var myChartDay = new Chart(ctxDay, configDay);
</script>
</html>
