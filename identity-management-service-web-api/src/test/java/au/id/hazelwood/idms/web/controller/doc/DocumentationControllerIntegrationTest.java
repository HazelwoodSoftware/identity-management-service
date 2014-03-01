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
package au.id.hazelwood.idms.web.controller.doc;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:/au/id/hazelwood/idms/application-context.xml", "classpath:/au/id/hazelwood/idms/web-context.xml"})
public class DocumentationControllerIntegrationTest
{
    @Autowired
    private WebApplicationContext wac;
    @Value("${idms.api.version}")
    private String idmsApiVersion;
    private MockMvc mockMvc;

    @Before
    public void createMockMvcWithDefaults()
    {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(this.wac)
            .defaultRequest(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
            .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
            .alwaysExpect(jsonPath("$.apiVersion").value(idmsApiVersion))
            .build();
    }

    @Test
    public void shouldGetResources() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders.get("/");

        ResultActions result = this.mockMvc.perform(request);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.info.title").value("Hazelwood IDMS"));
        result.andExpect(jsonPath("$.info.description").value("Basic identity management system for restful applications."));
        result.andExpect(jsonPath("$.apis").value(hasSize(1)));
        result.andExpect(jsonPath("$.apis[0].path").value("/doc/api/users"));
    }

    @Test
    public void shouldGetUsersApiDocumentation() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders.get("/doc/api/users");

        ResultActions result = this.mockMvc.perform(request);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.resourcePath").value("/api/users"));
    }
}
