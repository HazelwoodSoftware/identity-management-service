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
define(['angular', 'angular_mocks', 'idms/app'], function (angular, mocks)
{
    'use strict';

    describe('idms', function ()
    {
        beforeEach(mocks.module('idms'));

        describe('routes', function ()
        {
            it('should map to controllers with title, menu and template config', mocks.inject(function ($route)
            {
                expect($route.routes['/home'].title).toEqual('Home');
                expect($route.routes['/home'].menu).toEqual('Home');
                expect($route.routes['/home'].controller).toBe('HomeController');
                expect($route.routes['/home'].templateUrl).toEqual('views/home/home.html');

                expect($route.routes['/users'].title).toEqual('Users');
                expect($route.routes['/users'].menu).toEqual('Users');
                expect($route.routes['/users'].controller).toEqual('UserListController');
                expect($route.routes['/users'].templateUrl).toEqual('views/users/user-list.html');

                expect($route.routes['/users/:userId'].title).toEqual('Edit user');
                expect($route.routes['/users/:userId'].menu).toEqual('Users');
                expect($route.routes['/users/:userId'].controller).toEqual('UserEditController');
                expect($route.routes['/users/:userId'].templateUrl).toEqual('views/users/user-edit.html');

                // otherwise redirect to
                expect($route.routes[null].redirectTo).toEqual('/home');
            }));
        });

        describe('page', function ()
        {
            it('should set default tile and menu', mocks.inject(function ($rootScope, Page)
            {
                expect($rootScope.page).toEqual(Page);
                expect(Page.title).toEqual('');
                expect(Page.menu).toEqual('');
            }));
            it('should set tile and menu on $routeChangeSuccess', mocks.inject(function ($rootScope, Page)
            {
                $rootScope.$broadcast("$routeChangeSuccess", {title:"title-test",menu:"menu-test"});
                expect(Page.title).toEqual('title-test');
                expect(Page.menu).toEqual('menu-test');
            }));
        });
    });
});