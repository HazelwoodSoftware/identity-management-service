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
package au.id.hazelwood.idms.web.controller.user;

import au.id.hazelwood.idms.model.user.UserModel;
import au.id.hazelwood.idms.service.user.UserService;
import au.id.hazelwood.idms.web.controller.framework.BaseIntegrationTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsersControllerIntegrationTest extends BaseIntegrationTest
{
    @Autowired
    private UserService userService;

    @Test
    public void shouldGetAllUsers() throws Exception
    {
        UserModel one = UserModelFixture.random();
        UserModel two = UserModelFixture.random();
        when(userService.findAllUsers()).thenReturn(Arrays.asList(one, two));
        RequestBuilder request = MockMvcRequestBuilders.get("/api/users");

        ResultActions result = perform(request);

        verify(userService).findAllUsers();
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").value(hasSize(2)));
        result.andExpect(jsonPath("$[0].id").value(one.getId().intValue()));
        result.andExpect(jsonPath("$[1].id").value(two.getId().intValue()));
    }

    @Test
    public void shouldCreateUser() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}");

        ResultActions result = perform(request);

        result.andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadeRequestOnInvalidCreateUserBody() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/api/users")
            .contentType(MediaType.APPLICATION_JSON);

        ResultActions result = perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldFindUser() throws Exception
    {
        UserModel admin = UserModelFixture.admin();
        when(userService.findUserById(admin.getId())).thenReturn(admin);
        RequestBuilder request = MockMvcRequestBuilders.get("/api/users/{id}", admin.getId());

        ResultActions result = perform(request);

        verify(userService).findUserById(admin.getId());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(admin.getId().intValue()));
        result.andExpect(jsonPath("$.email").value(admin.getEmail()));
    }

    @Test
    public void shouldReturnNotFoundOnUnknownFindUserId() throws Exception
    {
        when(userService.findUserById(1000L)).thenReturn(null);
        RequestBuilder request = MockMvcRequestBuilders.get("/api/users/{id}", 1000L);

        ResultActions result = perform(request);

        verify(userService).findUserById(1000L);
        result.andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestOnInvalidUpdateUserBody() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .put("/api/users/{id}", 1000L)
            .contentType(MediaType.APPLICATION_JSON);

        ResultActions result = perform(request);

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .put("/api/users/{id}", 1000L)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}");

        ResultActions result = perform(request, false);

        result.andExpect(status().isOk());
    }
}
