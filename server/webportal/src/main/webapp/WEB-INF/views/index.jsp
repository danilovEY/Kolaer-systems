<%--
  Created by IntelliJ IDEA.
  User: danilovey
  Date: 20.07.2016
  Time: 8:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Portal</title>

        <!-- Bootstrap -->
        <link href="resources/bootstrap-3.3.6/css/bootstrap.min.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body class="page-body">
        <div class="container">
            <form role="form" name="form" action="/j_spring_security_check" method="post" class="form-inline">
                <div class="form-group">
                    <label for="personalNumberInput">Введите персональный номер:</label>
                    <input id="personalNumberInput" class="form-control" name="j_username" />
                </div>
                <div class="form-group">
                    <label for="passwordInput">Введите пароль:</label>
                    <input id="passwordInput" type="password" class="form-control" name="j_password" />
                </div>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="remember" name="_spring_security_remember_me"/>Запомнить меня
                    </label>
                </div>
                <button type="submit" class="btn btn-default">Submit</button>
            </form>

            <!-- Marketing Icons Section -->
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">
                        AAAAAAA
                    </h1>
                </div>
                <div class="col-lg-12">
                    <h1 class="page-header">
                        BBBB
                    </h1>
                </div>
                <div class="col-lg-12">
                    <h1 class="page-header">
                        CCCC
                    </h1>
                </div>
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4><i class="fa fa-fw fa-check"></i> Bootstrap v3.2.0</h4>
                        </div>
                        <div class="panel-body">
                            <p>Предназначен для всех, во всем мире.
                                Bootstrap - интуитивно простой и в тоже время мощный интерфейсный фрейморк, повышающий скорость и облегчающий разработку web-приложений.
                            </p>
                            <a href="#" class="btn btn-default">Подробнее</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4><i class="fa fa-fw fa-gift"></i>AngularJS</h4>
                        </div>
                        <div class="panel-body">
                            <p>AngularJS — JavaScript-фреймворк с открытым исходным кодом. Предназначен для разработки одностраничных приложений. Его цель — расширение браузерных MVC приложений, упрощение тестирования и разработки.</p>
                            <a href="#" class="btn btn-default">Подробнее</a>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h4><i class="fa fa-fw fa-compass"></i>HTML5</h4>
                        </div>
                        <div class="panel-body">
                            <p>В HTML5 реализовано множество новых синтаксических особенностей. Например, элементы video, audio и canvas, а также возможность использования SVG и математических формул.</p>
                            <a href="#" class="btn btn-default">Подробнее</a>
                        </div>
                    </div>
                </div>
            </div>
            <!-- /.row -->
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="resources/bootstrap-3.3.6/js/bootstrap.min.js"></script>
        </body>
</html>