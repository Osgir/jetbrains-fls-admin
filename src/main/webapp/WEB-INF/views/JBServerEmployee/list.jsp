<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    function searchLogin() {
        var input, filter;
        input = document.getElementById("myInput");
        filter = input.value.toUpperCase();
        window.location.replace("list?searchname=" + filter);
    }

    function hideBlocked() {
        window.location.replace("list?hideBlock=1&size=100");
    }

    function openComputer(id) {

        // $('#divComputer').load('computer/list?empid=' + id);

    }

    function toblackList(empid, productid) {
        $('#loadingmessage').show();
        $.ajax({
            type: 'GET',
            cache: false,
            url: '/blacklist/addemployee?employeeid=' + empid + '&productid=' + productid,
            success: function () {
                $('#loadingmessage').hide();
                location.reload();
            }
        });
    }

    function fromblackList(empid, productid) {
        $('#loadingmessage').show();
        $.ajax({
            type: 'GET',
            cache: false,
            url: '/blacklist/deleteemployee?employeeid=' + empid + '&productid=' + productid,
            success: function () {
                $('#loadingmessage').hide();
                location.reload();
            }
        });
    }



</script>

<c:set var="searchname" value="${param.searchname}"/>
<c:set var="sort" value="${param.sort}"/>
<c:set var="empid" value="${param.empid}"/>

<c:url var="myURL0" value="list?">
    <c:param name="searchname" value="${searchname}"/>
    <c:param name="sort" value="0"/>
    <c:param name="page" value="0"/>
    <c:param name="size" value="${size}"/>
</c:url>

<c:url var="myURL1" value="list?">
    <c:param name="searchname" value="${searchname}"/>
    <c:param name="sort" value="1"/>
    <c:param name="page" value="0"/>
    <c:param name="size" value="${size}"/>
</c:url>

<!DOCTYPE html>
<html>
<head>
    <title>JB Admin</title>
</head>
<body>
<%@include file="/WEB-INF/views/template/header.jsp" %>
<div class=" m-2 mt-2 panel-body w-100">
    <div class="panel-heading">
        <div class="panel-title">Search
            <input type="text" id="myInput" onchange="searchLogin()" placeholder="Search by login..">
            <c:if test="${ isHasIdentifiers}">
            <button type="submit" value="Hide blocked" onclick="hideBlocked()"
                    class="archivebutton btn btn-outline-dark btn-sm">
                Hide blocked
            </button>
            </c:if>

        </div>
    </div>
    <c:choose>
        <c:when test="${data.size() > 0 }">
            <table class="tg">
                <tr>
                    <td class="tg-0pky w-50" rowspan="2" style="vertical-align:top;">

                        <table id="myTable" class=" table table-striped table-bordered">
                            <tr>
                                <th>User login</th>
                                <th><a href="${myURL0}">Last seen Utilization</a></th>
                                <th><a href="${myURL1}">Last seen Errorlog</a></th>
                                <th>Utilization</th>
                                <th>Error log</th>
                                <th>Blacklist</th>
                            </tr>
                            <c:forEach var="record" items="${data}">
                                <tr>
                                    <td>

                                        <c:url var="empURL" value="list?">
                                            <c:param name="searchname" value="${searchname}"/>
                                            <c:param name="sort" value="${sort}"/>
                                            <c:param name="page" value="${page}"/>
                                            <c:param name="size" value="${size}"/>
                                            <c:param name="empid" value="${record.id}"/>
                                        </c:url>


<%--                                        <a href="#" onclick="openComputer(${record.id});">--%>

                                        <a href="${empURL}">

                                            <c:if test="${record.isIdentified == false && isHasIdentifiers}">
                                            <span style="color:#dc3545; font-weight:bold"> </c:if>
                                                <c:if test="${record.isIdentified == true}"><span> </c:if>
                                        ${record.identificationname}
                                </span>
                                        </a>

                                    </td>
                                    <td>
                                        <fmt:parseDate value="${record.lastseenutildate}" pattern="yyyy-MM-dd'T'HH:mm"
                                                       var="parsedDateTime" type="both"/>
                                        <fmt:formatDate pattern="dd.MM.yy HH:mm" value="${ parsedDateTime }"/>
                                            <%--                                <fmt:parseDate value="${record.lastseenutildate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both"/>--%>
                                            <%--                                <fmt:formatDate pattern="dd.MM.yy HH:mm:ss" value="${ parsedDateTime }"/>--%>
                                    </td>
                                    <td>
                                        <fmt:parseDate value="${record.lastseenerrordate}" pattern="yyyy-MM-dd'T'HH:mm"
                                                       var="parsedDateTime" type="both"/>
                                        <fmt:formatDate pattern="dd.MM.yy HH:mm" value="${ parsedDateTime }"/>

                                            <%--                                <fmt:parseDate value="${record.lastseenerrordate}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="parsedDateTime" type="both"/>--%>
                                            <%--                                <fmt:formatDate pattern="dd.MM.yy HH:mm:ss" value="${ parsedDateTime }"/>--%>
                                    </td>

                                    <td>
                                            <%--                                <c:url var="delLink" value="/blacklist/addemployee">--%>
                                            <%--                                    <c:param name="employeeid" value="${record.id}"/>--%>
                                            <%--                                    <c:param name="productid" value="${allid}"/>--%>
                                            <%--                                </c:url>--%>
                                            <%--                                <a href="${delLink}" class="btn btn-danger" title="Add to Blacklist"--%>
                                            <%--                                   onclick=" if (!(confirm('Are you sure you want to delete from blacklist?'))) return false">ALL </a>--%>
                                        <a href="#" class="btn btn-success" title="Add to Blacklist"
                                           onclick="toblackList(${record.id}, ${allid});">ALL</a>
                                        <c:forEach var="bl" items="${record.uniqUtilProduct}">
                                            <%--                                    <c:url var="addLink" value="/blacklist/addemployee">--%>
                                            <%--                                        <c:param name="employeeid" value="${record.id}"/>--%>
                                            <%--                                        <c:param name="productid" value="${bl.id}"/>--%>
                                            <%--                                    </c:url>--%>
                                            <%--                                    <a href="${addLink}" class="btn btn-success" title="Add to Blacklist"--%>
                                            <%--                                       onclick=" if (!(confirm('Are you sure you want to delete from blacklist?'))) return false">${bl.value} </a>--%>
                                            <a href="#" class="btn btn-success" title="Add to Blacklist"
                                               onclick="toblackList(${record.id}, ${bl.id});">${bl.valueP}</a>

                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="bl" items="${record.uniqErrorProduct}">
                                            <%--                                    <c:url var="addLink" value="/blacklist/deleteemployee">--%>
                                            <%--                                        <c:param name="employeeid" value="${record.id}"/>--%>
                                            <%--                                        <c:param name="productid" value="${bl.id}"/>--%>
                                            <%--                                    </c:url>--%>
                                            <a class="btn btn-secondary disabled"
                                               title="Delete from Blacklist">${bl.valueP} </a>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="bl" items="${record.jbServerBlacklistProductSet}">
                                            <%--                                    <c:url var="addLink" value="/blacklist/deleteemployee">--%>
                                            <%--                                        <c:param name="employeeid" value="${record.id}"/>--%>
                                            <%--                                        <c:param name="productid" value="${bl.jbServerProductBlacklist.id}"/>--%>
                                            <%--                                    </c:url>--%>
                                            <%--                                    <a href="${addLink}" class="btn btn-danger"--%>
                                            <%--                                       title="Delete from Blacklist">${bl.jbServerProductBlacklist.value} </a>--%>
                                            <a href="#" class="btn btn-danger" title="Delete from Blacklist"
                                               onclick="fromblackList(${record.id}, ${bl.jbServerProductBlacklist.id});">${bl.jbServerProductBlacklist.valueP}</a>

                                        </c:forEach>

                                            <%--                                                                                    <a class="btn btn-primary" data-toggle="collapse" href="#h${record.id}" role="button" aria-expanded="false" aria-controls="collapseExample">--%>
                                            <%--                                                                                        Link with href--%>
                                            <%--                                                                                    </a>--%>


                                    </td>

                                </tr>
                                <%--                                <tr>--%>
                                <%--                                    <td colspan="6" class="collapse">--%>
                                <%--                                        <div id="h${record.id}"--%>
                                <%--                                             style="width: 100%; height: 400px;">${record.id}</div>--%>
                                <%--                                    </td>--%>
                                <%--                                </tr>--%>


                            </c:forEach>
                        </table>

                        <div class="panel-footer">
                            Showing ${number+1} - ${size} of ${totalElements}
                            <ul class="pagination">

                                <c:set var="p" value="${param.pageNumber}"/> <%-- current page (1-based) --%>
                                <c:set var="l" value="25"/> <%-- amount of page links to be displayed --%>
                                <c:set var="r" value="${l / 2}"/> <%-- minimum link range ahead/behind --%>
                                <c:set var="t" value="${totalPages}"/> <%-- total amount of pages --%>


                                <c:set var="begin"
                                       value="${((p - r) > 0 ? ((p - r) < (t - l + 1) ? (p - r) : (t - l)) : 0) + 1}"/>
                                <c:set var="end" value="${(p + r) < t ? ((p + r) > l ? (p + r) : l) : t}"/>


                                <c:forEach begin="${begin}" end="${end}" var="page">

                                    <li class="page-item">

                                        <c:url var="myURL" value="list?">
                                            <c:param name="searchname" value="${searchname}"/>
                                            <c:param name="sort" value="${sort}"/>
                                            <c:param name="page" value="${page-1}"/>
                                            <c:param name="size" value="${size}"/>
                                        </c:url>
                                        <a href="${myURL}" class="page-link">${page}</a>
                                    </li>

                                </c:forEach>
                            </ul>
                        </div>

                    </td>
                </tr>
                <tr>
                    <td class="tg-0lax w-50 align-top ">
                        <div id='loadingmessage' style='display:none'>
                            <h2> LOADING! </h2>
                        </div>
                        <div id="divComputer" class="m-2 mt-4"/>
                        <script>
                        $('#divComputer').load("computer/list?empid=${empid}");
                        </script>
                        <br>
                    </td>
                </tr>
            </table>

        </c:when>
        <c:otherwise>
            <tr align="center">
                <td colspan="5">No Users available</td>
            </tr>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>