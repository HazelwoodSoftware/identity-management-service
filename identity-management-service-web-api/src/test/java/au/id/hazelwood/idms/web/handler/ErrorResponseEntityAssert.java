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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public final class ErrorResponseEntityAssert
{
    private ErrorResponseEntityAssert()
    {
    }

    public static void assertResponseStatus(ResponseEntity<Object> responseEntity, HttpStatus status)
    {
        assertThat(responseEntity.getStatusCode(), is(status));
    }

    public static void assertResponseHeader(ResponseEntity<Object> responseEntity, int headers)
    {
        assertThat(responseEntity.getHeaders().size(), is(headers));
    }

    public static void assertResponseBody(ResponseEntity<Object> responseEntity, ErrorType type, int errors)
    {
        assertThat(responseEntity.getBody(), hasProperty("code", is(type.getCode())));
        assertThat(responseEntity.getBody(), hasProperty("message", is(type.getMessage())));
        assertThat(responseEntity.getBody(), hasProperty("errors", hasSize(errors)));
    }

    public static void assertResponseErrors(ResponseEntity<Object> responseEntity, String property, String message)
    {
        assertThat(responseEntity.getBody(), hasProperty("errors", hasItem(hasProperty("field", is(property)))));
        assertThat(responseEntity.getBody(), hasProperty("errors", hasItem(hasProperty("message", is(message)))));
    }
}
