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
package au.id.hazelwood.idms.web.dto.error;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ErrorTypeUnitTest
{
    @Test
    public void shouldHaveCodeAndMessage() throws Exception
    {
        assertErrorType(ErrorType.UNEXPECTED_ERROR, 1000, "Unexpected system error.");
        assertErrorType(ErrorType.INVALID_REQUEST, 1010, "Invalid request.");
        assertErrorType(ErrorType.VALIDATION_ERROR, 1020, "Validation error.");
        assertErrorType(ErrorType.CONSTRAINT_VIOLATION, 1030, "Constraint violation.");
        assertErrorType(ErrorType.INTEGRITY_VIOLATION, 1040, "Data integrity violation.");
        assertErrorType(ErrorType.ENTITY_MISSING, 1050, "Entity not found.");
    }

    private void assertErrorType(ErrorType type, int code, String message)
    {
        assertThat(type.getCode(), is(code));
        assertThat(type.getMessage(), is(message));
    }
}
