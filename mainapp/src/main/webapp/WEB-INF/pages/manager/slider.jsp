<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 30.03.2016
  Time: 17:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="control-sidebar control-sidebar-dark">
    <!-- Tab panes -->
    <div class="tab-content">
        <!-- Settings tab content -->
        <div class="tab-pane active" id="control-sidebar-settings-tab">
            <h3 class="control-sidebar-heading">Order Points</h3>
                <c:forEach var="orderItem" items="${sessionScope.listOrderItems}">
                    <form method="post" action="${pageContext.request.contextPath}/manager/orders/back">
                    <div class="form-group">
                        <label class="control-sidebar-subheading">
                            Point № ${orderItem.sequenceNumber+1}
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" value="${orderItem.sequenceNumber}" name="itemNum">
                            <c:if test="${orderItem.sequenceNumber != sessionScope.listOrderItems.size()-1}">
                            <button type="submit" name="action" value="delete" class="btn btn-primary btn-xs pull-right">Delete all next</button>
                            </c:if>
                            <c:if test="${orderItem.cargo != null && orderItem.type == 'LOADING'}">
                            <button type="submit" name="action" value="update" class="btn btn-primary btn-xs pull-right">Update</button>
                            </c:if>
                        </label>
                        <p>
                            City: ${orderItem.city.name}<br>
                            Type: ${orderItem.type}<br>
                            <c:if test="${orderItem.cargo != null && orderItem.type == 'LOADING'}">
                            Cargo:<ul>
                                    <li>name: <input style="color: #222d32;" type="text" name="name" value="${orderItem.cargo.name}" required></li>
                                    <li>weight(kg): <input style="color: #222d32;" type="number" min="1" max="${sessionScope.maxWeight + orderItem.cargo.weight}" name="weight" value="${orderItem.cargo.weight}" required></li>
                                    </ul>
                            </c:if>
                            <c:if test="${orderItem.cargo != null && orderItem.type == 'UNLOADING'}">
                                Cargo:<ul>
                                <li>name: ${orderItem.cargo.name}</li>
                                <li>weight(max = ${sessionScope.maxWeight} kg):${orderItem.cargo.weight}</li>
                                </ul>
                            </c:if>

                        </p>
                    </div><!-- /.form-group -->

                    </form>
                </c:forEach>
        </div><!-- /.tab-pane -->
    </div>
</aside><!-- /.control-sidebar -->      <!-- Add the sidebar's background. This div must be placed
immediately after the control sidebar -->
<div class="control-sidebar-bg"></div>
