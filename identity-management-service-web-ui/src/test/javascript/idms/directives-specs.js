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
define([ 'angular', 'angular-mocks', 'idms/app' ], function (angular, mocks)
{
    'use strict';

    describe('directives', function ()
    {
        beforeEach(mocks.module('idms.directives'));

        describe('idms-app-version', function ()
        {
            it('should print current version', function ()
            {
                mocks.module(function ($provide)
                {
                    $provide.value('version', 'TEST_VER');
                });
                mocks.inject(function ($compile, $rootScope)
                {
                    var element = $compile('<span data-idms-app-version></span>')($rootScope);
                    expect(element.text()).toEqual('TEST_VER');
                });
            });
        });
    });
});