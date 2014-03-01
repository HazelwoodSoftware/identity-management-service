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
package au.id.hazelwood.idms.web.filter;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@FixMethodOrder(MethodSorters.JVM)
public class CrossOriginRequestFilterUnitTest
{
    private CrossOriginRequestFilter filter;

    @Before
    public void before() throws Exception
    {
        filter = new CrossOriginRequestFilter();
    }

    @Test
    public void shouldAddHeaders() throws Exception
    {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertAccessControlHeaders(response, "Accept, Content-Type, Origin, X-Requested-With", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
        verify(chain).doFilter(request, response);

        verifyNoMoreInteractions(response, chain);
    }

    @Test
    public void shouldAddOverriddenHeaders() throws Exception
    {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.setAllowedHeaders("Accept, Content-Type");
        filter.setAllowedMethods("GET");
        filter.doFilter(request, response, chain);

        assertAccessControlHeaders(response, "Accept, Content-Type", "GET");
        verify(chain).doFilter(request, response);

        verifyNoMoreInteractions(response, chain);
    }

    private void assertAccessControlHeaders(HttpServletResponse response, String allowedHeaders, String allowedMethods)
    {
        verify(response).setHeader("Access-Control-Allow-Origin", "*");
        verify(response).setHeader("Access-Control-Allow-Headers", allowedHeaders);
        verify(response).setHeader("Access-Control-Allow-Methods", allowedMethods);
        verify(response).setHeader("Access-Control-Max-Age", "3600");
    }
}
