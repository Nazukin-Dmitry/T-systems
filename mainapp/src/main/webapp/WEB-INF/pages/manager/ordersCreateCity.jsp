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
                            <button type="submit" name="action" value="completeOrder" class="btn btn-sm btn-primary">Sign out</button>
                        </form>
                    </li>
                    <li>
                        <a href="#" data-toggle="control-sidebar"><i class="fa fa-angle-double-left"></i></a>
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
                <li class="header">MENU</li>
                <!-- Optionally, you can add icons to the links -->
                <li><a href="${pageContext.request.contextPath}/manager/map"><i class="fa fa-building"></i> <span>Map</span></a></li>
                <li class="treeview">
                    <a href="#"><i class="fa fa-users"></i> <span>Users</span> <i class="fa fa-angle-left pull-right"></i></a>
                    <ul class="treeview-menu">
                        <li><a href="${pageContext.request.contextPath}/manager/drivers"><i class="fa fa-circle-o"></i>Drivers</a></li>
                        <li><a href="${pageContext.request.contextPath}/manager/newUsers"><i class="fa fa-circle-o"></i>New users</a></li>
                    </ul>
                </li>
                <li class="treeview">
                    <a href="#"><i class="fa fa-truck"></i> <span>Trucks</span> <i class="fa fa-angle-left pull-right"></i></a>
                    <ul class="treeview-menu">
                        <li><a href="${pageContext.request.contextPath}/manager/trucks"><i class="fa fa-circle-o"></i> View all</a></li>
                        <li><a href="${pageContext.request.contextPath}/manager/trucks/create"><i class="fa fa-circle-o"></i> Create</a></li>
                    </ul>
                </li>
                <li class="treeview">
                    <a href="#"><i class="fa fa-reorder"></i> <span>Orders</span> <i class="fa fa-angle-left pull-right"></i></a>
                    <ul class="treeview-menu">
                        <li><a href="${pageContext.request.contextPath}/manager/orders"><i class="fa fa-circle-o"></i> View all</a></li>
                        <li><a href="${pageContext.request.contextPath}/manager/orders/createStart"><i class="fa fa-circle-o"></i> Create</a></li>
                    </ul>
                </li>
            </ul><!-- /.sidebar-menu -->
        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                ${number+1} item <small>Select or create city</small>
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-6">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">Select city</h3>
                        </div><!-- /.box-header -->
                        <form role="form" action="${pageContext.request.contextPath}/manager/orders/create" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="city0">From the city:</label>
                                <input type="hidden" class="form-control" value="${cityFrom.id}" name="cityFrom">
                                <input id="city0" type="text" class="form-control" value="${cityFrom.name}"  readonly>
                            </div>
                            <div class="form-group">
                                <label for="inputCity">To the city:</label>
                                <select id="inputCity" class="form-control" name="cityTo">
                                    <c:forEach var="city" items="${cityList}">
                                        <option value="${city.id}"}>${city.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="box-footer">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="number" value="${number}">
                            <button type="submit" name="action" value="chooseCity" class="btn btn-primary">Submit</button>
                        </div>
                        </form>
                    </div>
                    <div class="box" id="interval" ${visible eq true ? '': 'hidden'}>
                        <div class="box-header with-border">
                            <h5 class="box-title">It is not known the distance between cities, enter it or select a different city.</h5>
                        </div><!-- /.box-header -->
                        <form role="form" action="${pageContext.request.contextPath}/manager/orders/create" method="post">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="city1">From the city:</label>
                                    <input type="hidden" class="form-control" value="${cityFrom.id}" name="cityFrom">
                                    <input id="city1" type="text" class="form-control" value="${cityFrom.name}"  readonly>
                                </div>
                                <div class="form-group">
                                    <label for="city2">To the city:</label>
                                    <input type="hidden" class="form-control" value="${cityTo.id}" name="cityTo">
                                    <input id="city2" type="text" class="form-control" value="${cityTo.name}" readonly>
                                </div>
                                <div class="form-group">
                                    <label for="interval1">Interval(hours):</label>
                                    <input id="interval1" name="interval" type="number" min="1" class="form-control" required>
                                </div>
                            </div>
                            <div class="box-footer">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="number" value="${number}">
                                <button type="submit" name="action" value="createInterval" class="btn btn-primary">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">Create city</h3>
                        </div><!-- /.box-header -->
                        <form role="form" action="${pageContext.request.contextPath}/manager/orders/create" method="post">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="city3">From the city:</label>
                                    <input type="hidden" class="form-control" value="${cityFrom.id}" name="cityFrom">
                                    <input id="city3" type="text" class="form-control" value="${cityFrom.name}"  readonly>
                                </div>
                                <div class="form-group">
                                    <label for="cityName1">To the city:</label>
                                    <input id="cityName1" type="text" class="form-control"  name="cityName" required>
                                </div>
                                <div class="form-group">
                                    <label for="interval2">Interval(hours):</label>
                                    <input id="interval2" type="number" min="1" class="form-control" name="interval" required>
                                </div>
                                <p class="text-danger">${errorCreateCity}</p>
                            </div>
                            <div class="box-footer">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input type="hidden" name="number" value="${number}">
                                <button type="submit" name="action" value="createCity" class="btn btn-primary">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-2">
                    <form role="form" action="${pageContext.request.contextPath}/manager/orders/create" method="post">
                        <p class="text-danger">${errorWithOrder}</p>
                        <input type="hidden" name="number" value="${number}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <button type="submit" name="action" value="completeOrder" class="btn btn-primary">Complete order</button>
                    </form>
                </div>
            </div>



            <!-- Your Page Content Here -->

        </section><!-- /.content -->
    </div><!-- /.content-wrapper -->

    <%@ include file="/WEB-INF/pages/manager/slider.jsp" %>
    <%@ include file="/WEB-INF/pages/manager/footer.jsp" %>

    <script type="text/javascript">
        function showDiv(elem){
            if(elem.value == "TRANSIT"){
                $("#TRANSIT").show("slow");
                $("#LOADING").hide("slow");
                $("#UNLOADING").hide("slow");
            }
            if(elem.value == "LOADING"){
                $("#TRANSIT").hide("slow");
                $("#LOADING").show("slow");
                $("#UNLOADING").hide("slow");
            }
            if(elem.value == "UNLOADING"){
                $("#TRANSIT").hide("slow");
                $("#LOADING").hide("slow");
                $("#UNLOADING").show("slow");
            }
        }
        $(document).ready(function(){
            $("#TRANSIT").show("fast");
            $("#LOADING").hide("fast");
            $("#UNLOADING").hide("fast");
        });
    </script>

    </body>
</html>