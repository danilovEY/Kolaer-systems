materialAdmin
    .config(function ($stateProvider, $urlRouterProvider){
        $urlRouterProvider.otherwise("/home");


        $stateProvider
            // Домашняя страница
            .state ('home', {
                url: '/home',
                templateUrl: 'pages/home.html',
            })

        // Тестовая
            .state ('testlink', {
                url: '/testpage',
                templateUrl: 'pages/test-page.html',
            })
    });
