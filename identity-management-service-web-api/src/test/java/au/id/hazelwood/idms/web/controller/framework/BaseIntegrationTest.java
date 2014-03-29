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
package au.id.hazelwood.idms.web.controller.framework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({@ContextConfiguration("classpath:au/id/hazelwood/idms/application-context-mock.xml"),
                   @ContextConfiguration("classpath:au/id/hazelwood/idms/web-context.xml")})
public abstract class BaseIntegrationTest
{
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private List<Object> beans;

    @Before
    public void resetAllMocks()
    {
        reset(getAllMocks());
    }

    @After
    public void verifyNoMoreInteractionsOnAllMocks()
    {
        verifyNoMoreInteractions(getAllMocks());
    }

    protected String toJSON(Object obj) throws Exception
    {
        return new ObjectMapper().writeValueAsString(obj);
    }

    protected ResultActions perform(RequestBuilder request) throws Exception
    {
        return perform(request, true);
    }

    protected ResultActions perform(RequestBuilder request, boolean hasResponseBody) throws Exception
    {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                                         .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
                                         .alwaysDo(MockMvcResultHandlers.print())
                                         .build();
        ResultActions result = mockMvc.perform(request);
        if (hasResponseBody)
        {
            result.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        }
        else
        {
            result.andExpect(content().string(StringUtils.EMPTY));
        }
        return result;
    }

    private Object[] getAllMocks()
    {
        List<?> mocks = Lists.newArrayList(Iterables.filter(beans, new MockObjectPredicate()));
        return mocks.toArray(new Object[mocks.size()]);
    }

    public static class AnswerWithFirstArgument implements Answer<Object>
    {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable
        {
            return invocation.getArguments()[0];
        }
    }

    private static class MockObjectPredicate implements Predicate<Object>
    {
        @Override
        public boolean apply(Object input)
        {
            return mockingDetails(input).isMock();
        }
    }
}
