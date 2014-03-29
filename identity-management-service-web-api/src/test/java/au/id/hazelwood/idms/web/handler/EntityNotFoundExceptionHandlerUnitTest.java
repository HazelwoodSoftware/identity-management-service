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
package au.id.hazelwood.idms.web.handler;

import au.id.hazelwood.idms.web.dto.error.ErrorType;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static au.id.hazelwood.idms.web.handler.ErrorResponseEntityAssert.assertResponseBody;
import static au.id.hazelwood.idms.web.handler.ErrorResponseEntityAssert.assertResponseStatus;

@FixMethodOrder(MethodSorters.JVM)
public class EntityNotFoundExceptionHandlerUnitTest
{
    private EntityNotFoundExceptionHandler handler;

    @Before
    public void before() throws Exception
    {
        handler = new EntityNotFoundExceptionHandler();
    }

    @Test
    public void shouldHandleEntityNotFound() throws Exception
    {
        ResponseEntity<Object> responseEntity = handler.handle();
        assertResponseStatus(responseEntity, HttpStatus.NOT_FOUND);
        assertResponseBody(responseEntity, ErrorType.ENTITY_MISSING, 0);
    }
}
