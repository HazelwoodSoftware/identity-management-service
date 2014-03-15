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

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class ErrorResponseBodyUnitTest
{
    @Test
    public void shouldDefaultErrors() throws Exception
    {
        ErrorResponseBody body = new ErrorResponseBody("message");
        assertThat(body.getMessage(), is("message"));
        assertThat(body.getErrors(), empty());
    }

    @Test
    public void shouldHaveValidGetters() throws Exception
    {
        ErrorDetail detail = new ErrorDetail("property-1", "message-1");
        ErrorResponseBody body = new ErrorResponseBody("message-value", Arrays.asList(detail));
        assertThat(body.getMessage(), is("message-value"));
        assertThat(body.getErrors(), contains(detail));
    }
}
