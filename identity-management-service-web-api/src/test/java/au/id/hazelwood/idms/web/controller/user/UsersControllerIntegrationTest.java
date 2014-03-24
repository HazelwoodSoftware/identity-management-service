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
import au.id.hazelwood.idms.web.dto.user.UserCreateDto;
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
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

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$").value(hasSize(2)));
        result.andExpect(jsonPath("$[0].id").value(one.getId().intValue()));
        result.andExpect(jsonPath("$[1].id").value(two.getId().intValue()));
        verify(userService).findAllUsers();
    }

    @Test
    public void shouldCreateUser() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJSON(new UserCreateDto()));

        ResultActions result = perform(request, false);

        result.andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestOnInvalidCreateUserBody() throws Exception
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
        when(userService.getUserById(admin.getId())).thenReturn(admin);
        RequestBuilder request = MockMvcRequestBuilders.get("/api/users/{id}", admin.getId());

        ResultActions result = perform(request);

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").value(admin.getId().intValue()));
        result.andExpect(jsonPath("$.email").value(admin.getEmail()));
        verify(userService).getUserById(admin.getId());
    }

    @Test
    public void shouldReturnNotFoundOnUnknownFindUserId() throws Exception
    {
        when(userService.getUserById(1000L)).thenThrow(new EntityNotFoundException("User not found."));
        RequestBuilder request = MockMvcRequestBuilders.get("/api/users/{id}", 1000L);

        ResultActions result = perform(request);

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.message").value("Entity not found."));
        result.andExpect(jsonPath("$.errors").value(empty()));
        verify(userService).getUserById(1000L);
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        UserModel model = UserModelFixture.standard();
        when(userService.getUserById(model.getId())).thenReturn(model);
        when(userService.saveUser(any(UserModel.class))).thenAnswer(new AnswerWithFirstArgument());
        RequestBuilder request = MockMvcRequestBuilders
            .put("/api/users/{id}", model.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJSON(createUserDetailDto(model.getId(), "new@hazelwood.id.au", "new first", "new last")));

        ResultActions result = perform(request, false);

        result.andExpect(status().isNoContent());
        ArgumentCaptor<UserModel> argument = ArgumentCaptor.forClass(UserModel.class);
        verify(userService).getUserById(model.getId());
        verify(userService).saveUser(argument.capture());
        assertThat(argument.getValue().getId(), is(model.getId()));
        assertThat(argument.getValue().getEmail(), is("new@hazelwood.id.au"));
        assertThat(argument.getValue().getFirstName(), is("new first"));
        assertThat(argument.getValue().getLastName(), is("new last"));
    }

    @Test
    public void shouldValidationErrorsForInvalidUpdateUserBody() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .put("/api/users/{id}", 1000L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJSON(createUserDetailDto(1000L, "invalid email", StringUtils.repeat("a", 21), StringUtils.repeat("a", 21))));

        ResultActions result = perform(request);

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.message").value("Validation error."));
        result.andExpect(jsonPath("$.errors").value(hasSize(3)));
        result.andExpect(jsonPath("$.errors[0].field").value("email"));
        result.andExpect(jsonPath("$.errors[0].message").value("not a well-formed email address"));
        result.andExpect(jsonPath("$.errors[1].field").value("firstName"));
        result.andExpect(jsonPath("$.errors[1].message").value("size must be between 0 and 20"));
        result.andExpect(jsonPath("$.errors[2].field").value("lastName"));
        result.andExpect(jsonPath("$.errors[2].message").value("size must be between 0 and 20"));
    }

    @Test
    public void shouldReturnBadRequestOnIdNotMatchingUpdateUserBody() throws Exception
    {
        RequestBuilder request = MockMvcRequestBuilders
            .put("/api/users/{id}", 1000L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJSON(createUserDetailDto(1L, "email@hazelwood.id.au", "first", "last")));

        ResultActions result = perform(request);

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.message").value("Invalid request."));
        result.andExpect(jsonPath("$.errors").value(hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].field").value(nullValue()));
        result.andExpect(jsonPath("$.errors[0].message").value("Id in the url doesn't match id in the body."));
    }

    private UserDetailDto createUserDetailDto(Long id, String email, String first, String last)
    {
        UserDetailDto dto = new UserDetailDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setFirstName(first);
        dto.setLastName(last);
        return dto;
    }
}
