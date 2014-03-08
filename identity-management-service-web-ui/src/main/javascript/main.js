/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the 'License'); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
require.config({
    paths: {
        'require_text': [
            '//cdnjs.cloudflare.com/ajax/libs/require-text/2.0.10/text.min',
            'lib/requirejs/require-text'
        ],
        'jquery': [
            '//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.0/jquery.min',
            'lib/jquery/jquery'
        ],
        'bootstrap': [
            '//cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.1.1/js/bootstrap.min',
            'lib/bootstrap/bootstrap'
        ],
        'angular': [
            'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular.min',
            'lib/angularjs/angular'
        ],
        'angular_route': [
            'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-route.min',
            'lib/angularjs/angular-route'
        ],
        'angular_resource': [
            'https://ajax.googleapis.com/ajax/libs/angularjs/1.2.14/angular-resource.min',
            'lib/angularjs/angular-resource'
        ]
    },
    shim: {
        'bootstrap': ['jquery'],
        'angular': {exports: 'angular'},
        'angular_route': ['angular'],
        'angular_resource': ['angular']
    },
    priority: [
        'jquery',
        'bootstrap',
        'angular'
    ]
});

//http://code.angularjs.org/1.2.1/docs/guide/bootstrap#overview_deferred-bootstrap
window.name = 'NG_DEFER_BOOTSTRAP!';

require(['angular', 'idms/app'], function (angular, app)
{
    'use strict';

    angular.element().ready(function ()
    {
        angular.resumeBootstrap([app.name]);
    });
});