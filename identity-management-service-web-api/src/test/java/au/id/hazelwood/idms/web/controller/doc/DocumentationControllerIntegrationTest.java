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

import au.id.hazelwood.idms.web.controller.framework.BaseIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DocumentationControllerIntegrationTest extends BaseIntegrationTest
{
    @Value("${idms.api.version}")
    private String idmsApiVersion;

    @Test
    public void shouldGetResources() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders.get("/");

        ResultActions result = perform(request);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.apiVersion").value(idmsApiVersion));
        result.andExpect(jsonPath("$.info.title").value("Hazelwood IDMS"));
        result.andExpect(jsonPath("$.info.description").value("Basic identity management system for restful applications."));
        result.andExpect(jsonPath("$.apis").value(hasSize(1)));
        result.andExpect(jsonPath("$.apis[0].path").value("/doc/api/users"));
    }

    @Test
    public void shouldGetUsersApiDocumentation() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders.get("/doc/api/users");

        ResultActions result = perform(request);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.apiVersion").value(idmsApiVersion));
        result.andExpect(jsonPath("$.resourcePath").value("/api/users"));
    }
}
