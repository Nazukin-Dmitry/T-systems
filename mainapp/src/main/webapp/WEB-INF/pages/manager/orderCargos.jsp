<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 15.02.2016
  Time: 23:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>LogiWeb</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.5 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://code.ionicframework.com/ionicons/2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/plugins/datatables/dataTables.bootstrap.css">

    <!-- Theme style -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
          page. However, you can choose any other skin. Make sure you
          apply the skin class to the body tag so the changes take effect.
    -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/dist/css/skins/skin-blue.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<%@ include file="/WEB-INF/pages/manager/header.jsp" %>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
        <h1>
            Order
        </h1>
    </section>

    <!-- Main content -->
    <section class="content">

        <div class="row">
            <div class="col-md-6">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Common information</h3>
                    </div><!-- /.box-header -->

                    <div class="box-body">
                        <c:choose>
                            <c:when test="${order.status eq 'DONE'}">
                                <h3>This order has been done.</h3>
                            </c:when>
                            <c:otherwise>
                                <dl class="dl-horizontal">
                                    <dt>Truck's registration number</dt>
                                    <dd>${truck.regNumber}</dd>
                                    <dt>Drivers</dt>
                                    <dd>
                                        <ul>
                                            <c:forEach var="driver" items="${driverList}">
                                                <li>serial number: ${driver.serialNumber}<br>
                                                    first name: ${driver.employee.firstName}<br>
                                                    secondName: ${driver.employee.secondName}<br>
                                                    status: ${driver.status}
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </dd>
                                </dl>
                            </c:otherwise>
                        </c:choose>
                    </div><!-- /.box-body -->
                    <div class="box-footer">
                        <p class="text-danger">${error}</p>
                        <form role="form" method="post" action="${pageContext.request.contextPath}/manager/orders/delete">
                            <input type="hidden" name="id" value="${order.id}">
                            <input type="hidden" name="cityId" value="${order.startCity.id}">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="btn btn-primary">Delete order</button>
                        </form>
                        <c:if test="${order.status eq 'NEW'}">
                            <br>
                            <a href="${pageContext.request.contextPath}/manager/orders/editOrder?orderId=${order.id}"><button type="submit" class="btn btn-primary">Edit order</button></a>
                        </c:if>
                    </div>
                </div><!-- /.box -->
            </div><!-- /.col -->

            <div class="col-md-6">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Cargos information</h3>
                    </div><!-- /.box-header -->

                    <div class="box-body">
                        <table id="example1" class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Weight</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="cargo" items="${cargoList}">
                                <tr>
                                    <td>${cargo.id}</td>
                                    <td>${cargo.name}</td>
                                    <td>${cargo.weight}</td>
                                    <td>${cargo.status}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div><!-- /.box-body -->
                </div><!-- /.box -->

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Order points</h3>
                    </div><!-- /.box-header -->

                    <div class="box-body">
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th>Num</th>
                                <th>City</th>
                                <th>Cargo</th>
                                <th>Type</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${orderItemList}">
                                <tr>
                                    <td>${item.sequenceNumber+1}</td>
                                    <td>${item.city.name}</td>
                                    <td><c:if test="${item.type ne 'TRANSIT'}"> Cargo: id:${item.cargo.id}; name:${item.cargo.name}; weight:${item.cargo.weight};</c:if></td>
                                    <td>${item.type}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div><!-- /.box-body -->
                </div><!-- /.box -->

            </div><!-- /.col -->

        </div><!-- /.row -->

    </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<%@ include file="/WEB-INF/pages/manager/footer.jsp" %>
<!-- DataTables -->
<script src="${pageContext.request.contextPath}/resources/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/datatables/dataTables.bootstrap.min.js"></script>

<script type="text/javascript">
    $(function () {
        $("#example1").DataTable({
            "paging": false,
            "lengthChange": false,
            "searching": false,
            "ordering": false,
            "info": false,
            "autoWidth": false
        });
    });
</script>


</body>
</html>