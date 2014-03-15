/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
define(['idms/_define'], function (module)
{
    'use strict';

    // Declare app level module which depends on filters, and services
    return module
        .config(['$routeProvider', function ($routeProvider)
        {
            $routeProvider.when('/home', {
                title: 'Home',
                menu: 'Home',
                templateUrl: 'views/home/home.html',
                controller: 'HomeController'
            });
            $routeProvider.when('/users', {
                title: 'Users',
                menu: 'Users',
                templateUrl: 'views/users/user-list.html',
                controller: 'UserListController'
            });
            $routeProvider.when('/users/:userId', {
                title: 'Edit user',
                menu: 'Users',
                templateUrl: 'views/users/user-edit.html',
                controller: 'UserEditController'
            });
            $routeProvider.otherwise({redirectTo: '/home'});
        }])
        .run(['$rootScope', function ($rootScope)
        {
            $rootScope.page = {
                title: 'Home',
                menu: 'Home'
            };
            $rootScope.$on('$routeChangeSuccess', function (event, current)
            {
                $rootScope.page.title = current.title;
                $rootScope.page.menu = current.menu;
            });
        }]);
});
