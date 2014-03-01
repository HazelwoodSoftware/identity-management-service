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
import au.id.hazelwood.idms.web.dto.user.UserCreateDto;
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;
import au.id.hazelwood.idms.web.dto.user.UserSummaryDto;
import au.id.hazelwood.idms.web.exception.NotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
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
    private UsersController controller;

    @Before
    public void before() throws Exception
    {
        controller = new UsersController(userService, userModelToSummaryDtoConverter, userModelToDetailDtoConverter);
    }

    @After
    public void after() throws Exception
    {
        verifyNoMoreInteractions(userService, userModelToSummaryDtoConverter, userModelToDetailDtoConverter);
    }

    @Test
    public void shouldFindAllUsersDelegatesToUserServiceAndSummaryDtoConverter() throws Exception
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
    public void shouldFindUserDelegatesToUserService() throws Exception
    {
        Long userId = 10L;
        UserModel model = mock(UserModel.class);
        UserDetailDto dto = mock(UserDetailDto.class);

        when(userService.findUserById(userId)).thenReturn(model);
        when(userModelToDetailDtoConverter.apply(model)).thenReturn(dto);

        UserDetailDto actual = controller.findUser(userId);

        assertThat(actual, is(dto));
        verify(userService).findUserById(userId);
        verify(userModelToDetailDtoConverter).apply(model);
    }

    @Test(expected = NotFoundException.class)
    public void shouldFindUserThrowsNotFoundExceptionWhenUserMissing() throws Exception
    {
        Long userId = 10L;

        when(userService.findUserById(userId)).thenReturn(null);

        try
        {
            controller.findUser(userId);
        }
        finally
        {
            verify(userService).findUserById(userId);
        }
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        UserDetailDto dto = mock(UserDetailDto.class);

        controller.updateUser(10L, dto);

        verifyNoMoreInteractions(dto);
    }
}
