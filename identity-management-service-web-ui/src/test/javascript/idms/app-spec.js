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

    describe('idms.app', function ()
    {
        beforeEach(mocks.module('idms'));

        describe('routes', function ()
        {
            it('should map to controllers with correct title and template', mocks.inject(function ($route)
            {
                expect($route.routes['/home'].controller).toBe('HomeController');
                expect($route.routes['/home'].templateUrl).toEqual('views/home/home.html');

                expect($route.routes['/users'].controller).toEqual('UsersController');
                expect($route.routes['/users'].templateUrl).toEqual('views/users/users.html');

                // otherwise redirect to
                expect($route.routes[null].redirectTo).toEqual('/home');
            }));
        });
    });
});