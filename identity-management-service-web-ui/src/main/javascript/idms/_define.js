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
define([
    'angular',
    'angular_route',
    'idms/constants/constants',
    'idms/services/services',
    'idms/directives/directives',
    'idms/filters/filters',
    'idms/controllers/controllers'
], function (angular)
{
    'use strict';

    // Declare app level module which depends on filters, and services
    return angular.module('idms', ['ngRoute', 'idms.constants', 'idms.services', 'idms.directives', 'idms.filters', 'idms.controllers'])
        .factory('Page', function ()
        {
            return {
                title: '',
                menu: '',
                errors: []
            };
        })
        .run(['$rootScope', 'Page', function ($rootScope, Page)
        {
            $rootScope.page = Page;
        }]);
});
