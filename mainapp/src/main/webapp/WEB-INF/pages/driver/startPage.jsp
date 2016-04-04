
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <!-- Main Header -->
  <header class="main-header">

    <!-- Logo -->
    <a href="${pageContext.request.contextPath}/home" class="logo">
      <!-- mini logo for sidebar mini 50x50 pixels -->
      <span class="logo-mini"><b>LW</b></span>
      <!-- logo for regular state and mobile devices -->
      <span class="logo-lg"><b>LogiWeb</b></span>
    </a>

    <!-- Header Navbar -->
    <nav class="navbar navbar-static-top" role="navigation">
      <!-- Sidebar toggle button-->
      <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>
      <!-- Navbar Right Menu -->
      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li>
            <%--<a href="${pageContext.request.contextPath}/logout">Sign out</a>--%>
              <form role="form" action="${pageContext.request.contextPath}/logout" method="post" class="margin">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" name="action" value="completeOrder" class="btn btn-primary btn-sm">Sign out</button>
              </form>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  <!-- Left side column. contains the logo and sidebar -->
  <aside class="main-sidebar">

    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
      <!-- Sidebar user panel -->
      <div class="user-panel">
        <div class="pull-left image">
          <img src="${pageContext.request.contextPath}/resources/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
        </div>
        <div class="pull-left info">
          <p><sec:authentication property="principal.employee.firstName" /> <sec:authentication property="principal.employee.secondName" /></p>
        </div>
      </div>

      <!-- Sidebar Menu -->
      <ul class="sidebar-menu">

      </ul><!-- /.sidebar-menu -->
    </section>
    <!-- /.sidebar -->
  </aside>

<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper">
  <!-- Content Header (Page header) -->
  <section class="content-header">
    <h1>
      Welcome
    </h1>
  </section>

  <!-- Main content -->
  <section class="content">

    <div class="row">
      <div class="col-md-6">
        <div class="box">
          <div class="box-header with-border">
            <h3 class="box-title">Enter your serial number</h3>
          </div><!-- /.box-header -->
          <form role="form" action="${pageContext.request.contextPath}/driver/order" method="get">
            <div class="box-body">
              <div class="form-group">
                <label for="inputSerial">Serial number</label>
                <input type="number" min="0" id="inputSerial" name="serialNumber" required>
                <p class="text-danger">${error}</p>
              </div>
            </div><!-- /.box-body -->
            <div class="box-footer">
              <button type="submit" class="btn btn-primary">Submit</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-12">
        <div class="box box-solid">
          <div class="box-header with-border">
            <i class="fa fa-info"></i>
            <h3 class="box-title">Order information</h3>
          </div><!-- /.box-header -->
          <div class="box-body">
            <c:choose>
              <c:when test="${type eq 'order'}">
                <dl class="dl-horizontal">
                  <dt>Your serial number</dt>
                  <dd>${serialNumber}</dd>
                  <dt>All drivers in order</dt>
                  <dd>
                  <ul>
                    <c:forEach var="driver" items="${codriverList}">
                      <li>number: ${driver.serialNumber}<br>
                        first name: ${driver.employee.firstName}<br>
                        secondName: ${driver.employee.secondName}
                      </li>
                    </c:forEach>
                  </ul>
                  </dd>
                  <dt>Truck's registration number</dt>
                  <dd>${truck.regNumber}</dd>
                  <dt>Order's number</dt>
                  <dd>${order.id}</dd>
                  <dt>Order's start time</dt>
                  <dd>${order.startTime}</dd>
                  <dt>Order's duration</dt>
                  <dd>${order.duration}</dd>
                  <dt>The list of waypoints</dt>
                  <dd>
                    <table class="table table-striped">
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
                            <td>Cargo: id:${item.cargo.id}; name:${item.cargo.name}; weight:${item.cargo.weight}; status:${item.cargo.status}</td>
                            <td>${item.type}</td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </dd>
                </dl>
              </c:when>
              <c:when test="${type eq 'noorder'}">
                <p>
                  Now you do not have any job.
                </p>
              </c:when>
              <c:otherwise>
              </c:otherwise>
            </c:choose>
          </div><!-- /.box-body -->
        </div><!-- /.box -->
      </div><!-- ./col -->
  </div><!-- /.row -->

  </section><!-- /.content -->
</div><!-- /.content-wrapper -->

<%@ include file="/WEB-INF/pages/manager/footer.jsp" %>

</body>
</html>