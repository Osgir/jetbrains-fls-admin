<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${data.size() > 0 }">
        <div>
            <table   >
                <tr>
                    <td>name:</td>
                    <td><c:out value="${employee.name}"/></td>
                </tr>
                <tr>
                    <td>identificationname:</td>
                    <td><c:out value="${employee.identificationname}"/></td>
                </tr>
            </table>

        </div>
        <table id="myTable" class=" table   table-bordered ">
            <tr>
                <th style="width:20%">Name</th>
                <th style="width:15%">Last seen Utilization</th>
                <th style="width:15%">Last seen Errorlog</th>
                <th style="width:25%">Utilization</th>
                <th style="width:10%">Error log</th>
                <th style="width:10%">Blacklist</th>
            </tr>
            <c:forEach var="record" items="${data}">
                <tr align="center">
                    <td>
                        <c:if test="${record.identified == false && isHasIdentifiers }">
                        <span style="color:#dc3545; font-weight:bold">!</c:if>
                            <c:if test="${record.identified == true}"><span> </c:if>
                                        ${record.identificationname}
                                </span>

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
                            <%--                                <fmt:parseDate value="${record.lastseenerrordate}" pattern="yyyy-MM-dd'T'HH:mm:sss" var="parsedDateTime" type="both"/>--%>
                            <%--                                <fmt:formatDate pattern="dd.MM.yy HH:mm:ss" value="${ parsedDateTime }"/>--%>
                    </td>
                    <td>
                        <c:url var="delLink" value="/blacklist/addcopmuter">
                            <c:param name="computerid" value="${record.id}"/>
                            <c:param name="productid" value="${allid}"/>
                        </c:url>
                        <a href="${delLink}" class="btn btn-danger" title="Add to Blacklist"
                           onclick=" if (!(confirm('Are you sure you want to delete from blacklist?'))) return false">ALL </a>
                        <c:forEach var="bl" items="${record.uniqUtilProduct}">
                            <c:url var="addLink" value="/blacklist/addcopmuter">
                                <c:param name="computerid" value="${record.id}"/>
                                <c:param name="productid" value="${bl.id}"/>
                            </c:url>
                            <a href="${addLink}" class="btn btn-success" title="Add to Blacklist"
                               onclick=" if (!(confirm('Are you sure you want to delete from blacklist?'))) return false">${bl.valueP} </a>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bl" items="${record.uniqErrorProduct}">
                            <c:url var="addLink" value="/blacklist/deletecomputer">
                                <c:param name="computerid" value="${record.id}"/>
                                <c:param name="productid" value="${bl.id}"/>
                            </c:url>
                            <a href="${addLink}" class="btn btn-secondary"
                               title="Delete from Blacklist">${bl.valueP} </a>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="bl" items="${record.jbServerBlacklistProductSet}">
                            <c:url var="addLink" value="/blacklist/deletecomputer">
                                <c:param name="computerid" value="${record.id}"/>
                                <c:param name="productid" value="${bl.jbServerProductBlacklist.id}"/>
                            </c:url>
                            <a href="${addLink}" class="btn btn-danger"
                               title="Delete from Blacklist">${bl.jbServerProductBlacklist.valueP} </a>
                        </c:forEach>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <div class="panel-footer">
            Showing ${number+1} - ${size} of ${totalElements}
            <ul class="pagination">

                <c:set var="p" value="${param.pageNumber}"/> <%-- current page (1-based) --%>
                <c:set var="l" value="10"/> <%-- amount of page links to be displayed --%>
                <c:set var="r" value="${l / 2}"/> <%-- minimum link range ahead/behind --%>
                <c:set var="t" value="${totalPages}"/> <%-- total amount of pages --%>


                <c:set var="begin"
                       value="${((p - r) > 0 ? ((p - r) < (t - l + 1) ? (p - r) : (t - l)) : 0) + 1}"/>
                <c:set var="end" value="${(p + r) < t ? ((p + r) > l ? (p + r) : l) : t}"/>


                <c:forEach begin="${begin}" end="${end}" var="page">

                    <li class="page-item">

                        <c:url var="myURL" value="list?">
                            <%--                                    <c:param name="username" value="${username}"/>--%>
                            <%--                                    <c:param name="sort" value="${sort}"/>--%>
                            <c:param name="page" value="${page-1}"/>
                            <c:param name="size" value="${size}"/>
                        </c:url>
                            <%--                                <a href="${myURL}" class="page-link">${page}</a>--%>
                            ${page}
                    </li>

                </c:forEach>
            </ul>
        </div>


    </c:when>
    <c:otherwise>
        <tr align="center">
            <td colspan="5">No Computers available</td>
        </tr>
    </c:otherwise>
</c:choose>