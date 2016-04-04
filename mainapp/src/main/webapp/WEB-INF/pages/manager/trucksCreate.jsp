<%--
  Created by IntelliJ IDEA.
  User: 1
  Date: 17.02.2016
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                Create truck
            </h1>
        </section>
        <!-- Main content -->
        <section class="content">
        <div class="row">
            <div class="col-md-6">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Create truck</h3>
                    </div><!-- /.box-header -->
                    <form:form role="form" action="${pageContext.request.contextPath}/manager/trucks/create" method="post" modelAttribute="truckEntity">
                        <div class="box-body">
                            <%--<div class="form-group">--%>
                                <%--<label for="inputId">Id</label>--%>
                                <%--<input id="inputId" type="text" class="form-control" value="${truck.id}" name="id" disabled>--%>
                            <%--</div>--%>
                            <div class="form-group">
                                <label for="inputReg">Registration Number(example:  AE23123)</label>
                                <form:errors path="regNumber" cssClass="text-danger" />
                                <form:input id="inputReg" type="text" pattern="[A-Z]{2}[0-9]{5}" class="form-control" value="" path="regNumber" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="inputState">State</label>
                                <form:errors path="state" cssClass="text-danger" />
                                <form:select id="inputState" class="form-control" path="state">
                                    <c:forEach var="state" items="${stateList}">
                                        <option value="${state}"}>${state}</option>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <div class="form-group">
                                <label for="inputCapacity">Capacity(kg)</label>
                                <form:errors path="capacity" cssClass="text-danger" />
                                <form:input id="inputCapacity" type="number" min="1" class="form-control" value="" path="capacity" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="inputCount">Driver count</label>
                                <form:errors path="driverCount" cssClass="text-danger" />
                                <form:input id="inputCount" type="number" min="1" class="form-control" value="" path="driverCount" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="inputCity">Current city</label>
                                <%--<form:errors path="currentCity" cssClass="text-danger" />--%>
                                <select id="inputCity" class="form-control" name="currentCityId">
                                    <c:forEach var="city" items="${cityList}">
                                        <option value="${city.id}"}>${city.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                                <p class="text-danger">${error}</p>
                        </div><!-- /.box-body -->
                        <div class="box-footer">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <form:button type="submit" name="action" value="create" class="btn btn-primary">Submit</form:button>
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="col-md-6">
            </div>
        </div>

            <!-- Your Page Content Here -->

        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->

    <%@ include file="/WEB-INF/pages/manager/footer.jsp" %>

    </body>
</html>
