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

import au.id.hazelwood.idms.exception.EmailAddressInUseException;
import au.id.hazelwood.idms.model.user.UserModel;
import au.id.hazelwood.idms.service.user.UserService;
import au.id.hazelwood.idms.web.dto.error.ErrorType;
import au.id.hazelwood.idms.web.dto.user.UserCreateDto;
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;
import au.id.hazelwood.idms.web.dto.user.UserSummaryDto;
import au.id.hazelwood.idms.web.exception.BadRequestException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(MockitoJUnitRunner.class)
public class UsersControllerUnitTest
{
    @Mock
    private UserService userService;
    @Mock
    private UserModelToSummaryDtoConverter userModelToSummaryDtoConverter;
    @Mock
    private UserModelToDetailDtoConverter userModelToDetailDtoConverter;
    @Mock
    private UserDetailDtoToModelConverter userDetailDtoToModelConverter;
    private ExpectedException expectedException = ExpectedException.none();
    private UsersController controller;

    @Rule
    public ExpectedException getExpectedException()
    {
        return expectedException;
    }

    @Before
    public void before() throws Exception
    {
        controller = new UsersController(userService, userModelToSummaryDtoConverter, userModelToDetailDtoConverter, userDetailDtoToModelConverter);
    }

    @After
    public void after() throws Exception
    {
        verifyNoMoreInteractions(userService, userModelToSummaryDtoConverter, userModelToDetailDtoConverter, userDetailDtoToModelConverter);
    }

    @Test
    public void shouldFindAllUsersByUsingUserServiceAndSummaryDtoConverter() throws Exception
    {
        UserModel model = mock(UserModel.class);
        UserSummaryDto dto = mock(UserSummaryDto.class);
        when(userService.findAllUsers()).thenReturn(Arrays.asList(model));
        when(userModelToSummaryDtoConverter.apply(model)).thenReturn(dto);

        List<UserSummaryDto> dtos = controller.findAllUsers();

        assertThat(dtos, hasSize(1));
        assertThat(dtos, contains(dto));
        verify(userService).findAllUsers();
        verify(userModelToSummaryDtoConverter).apply(model);
        verifyNoMoreInteractions(model, dto);
    }

    @Test
    public void shouldCreateUser() throws Exception
    {
        UserCreateDto dto = mock(UserCreateDto.class);

        controller.createUser(dto);

        verifyNoMoreInteractions(dto);
    }

    @Test
    public void shouldFindUserByUsingUserService() throws Exception
    {
        Long userId = 10L;
        UserModel model = mock(UserModel.class);
        UserDetailDto dto = mock(UserDetailDto.class);

        when(userService.getUserById(userId)).thenReturn(model);
        when(userModelToDetailDtoConverter.apply(model)).thenReturn(dto);

        UserDetailDto actual = controller.findUser(userId);

        assertThat(actual, is(dto));
        verify(userService).getUserById(userId);
        verify(userModelToDetailDtoConverter).apply(model);
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        UserDetailDto dto = mock(UserDetailDto.class);
        UserModel model = mock(UserModel.class);

        when(dto.getId()).thenReturn(10L);
        when(userDetailDtoToModelConverter.apply(dto)).thenReturn(model);
        when(userService.saveUser(model)).thenReturn(model);

        controller.updateUser(10L, dto);

        verify(dto).getId();
        verify(userDetailDtoToModelConverter).apply(dto);
        verify(userService).saveUser(model);
        verifyNoMoreInteractions(dto, model);
    }

    @Test
    public void shouldHandleEmailAddressInUseExceptionOnUpdate() throws Exception
    {
        UserDetailDto dto = mock(UserDetailDto.class);
        UserModel model = mock(UserModel.class);

        when(dto.getId()).thenReturn(10L);
        when(userDetailDtoToModelConverter.apply(dto)).thenReturn(model);
        EmailAddressInUseException ex = new EmailAddressInUseException("test@mail.com");
        when(userService.saveUser(model)).thenThrow(ex);

        expectedException.expect(BadRequestException.class);
        expectedException.expect(hasProperty("errorDto", hasProperty("code", is(ErrorType.VALIDATION_ERROR.getCode()))));
        expectedException.expect(hasProperty("errorDto", hasProperty("message", is(ErrorType.VALIDATION_ERROR.getMessage()))));
        expectedException.expect(hasProperty("errorDto", hasProperty("errors", hasItem(hasProperty("field", is("email"))))));
        expectedException.expect(hasProperty("errorDto", hasProperty("errors", hasItem(hasProperty("message", is(ex.getMessage()))))));
        try
        {
            controller.updateUser(10L, dto);
        }
        finally
        {
            verify(dto).getId();
            verify(userDetailDtoToModelConverter).apply(dto);
            verify(userService).saveUser(model);
            verifyNoMoreInteractions(dto);
        }
    }

    @Test
    public void shouldNotUpdateUserIdDoesNotMatchBody() throws Exception
    {
        UserDetailDto dto = mock(UserDetailDto.class);

        when(dto.getId()).thenReturn(1L);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Id in the url doesn't match id in the body.");
        try
        {
            controller.updateUser(10L, dto);
        }
        finally
        {
            verify(dto).getId();
            verifyNoMoreInteractions(dto);
        }
    }
}
