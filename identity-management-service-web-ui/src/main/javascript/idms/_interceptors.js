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
define(['angular', 'idms/_define'], function (angular, module)
{
    'use strict';

    module.config(['$httpProvider', function ($httpProvider)
    {
        $httpProvider.interceptors.push(['$q', '$log', 'Page', function ($q, $log, Page)
        {
            return {
                'request': function (config)
                {
                    $log.debug('Sending request : ' + angular.toJson(config));
                    return config;
                },

                'requestError': function (request)
                {
                    $log.warn('Request error : ' + angular.toJson(request));
                    return $q.reject(request);
                },

                'response': function (response)
                {
                    $log.debug('Received response : ' + angular.toJson(response));
                    return response;
                },

                'responseError': function (response)
                {
                    $log.warn('Response error : ' + angular.toJson(response));
                    if (response.status === 404)
                    {
                        Page.errors.push({title: "Not found", data: response.data});
                    }
                    else if (response.status === 500)
                    {
                        Page.errors.push({title: "Unexpected system error", data: response.data});
                    }
                    return $q.reject(response);
                }
            };
        }]);
    }]);

    module.run(['$rootScope', 'Page', function ($rootScope, Page)
    {
        $rootScope.$on('$routeChangeSuccess', function (event, current)
        {
            Page.errors = [];
        });
    }]);
});
