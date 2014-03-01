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
package au.id.hazelwood.idms.service.user.impl;

import au.id.hazelwood.idms.entity.user.UserEntity;
import au.id.hazelwood.idms.model.user.UserModel;
import au.id.hazelwood.idms.repository.user.UserRepository;
import au.id.hazelwood.idms.service.user.UserService;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplUnitTest
{
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserEntityToModelConverter userEntityToModelConverter;
    private UserService userService;

    @Before
    public void setUp() throws Exception
    {
        userService = new UserServiceImpl(userRepository, userEntityToModelConverter);
    }

    @Test
    public void shouldFindAllDelegatesToUserRepository()
    {
        when(userRepository.findAll()).thenReturn(Collections.<UserEntity>emptyList());

        List<UserModel> models = userService.findAllUsers();

        assertThat(models.isEmpty(), is(true));
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
        verifyZeroInteractions(userEntityToModelConverter);
    }

    @Test
    public void shouldFindAll()
    {
        UserEntity entityOne = mock(UserEntity.class);
        UserEntity entityTwo = mock(UserEntity.class);
        UserModel modelOne = mock(UserModel.class);
        UserModel modelTwo = mock(UserModel.class);

        when(userRepository.findAll()).thenReturn(Arrays.asList(entityOne, entityTwo));
        when(userEntityToModelConverter.apply(entityOne)).thenReturn(modelOne);
        when(userEntityToModelConverter.apply(entityTwo)).thenReturn(modelTwo);

        List<UserModel> models = userService.findAllUsers();

        assertThat(models.size(), is(2));
        assertThat(models, contains(modelOne, modelTwo));
        verify(userRepository).findAll();
        verify(userEntityToModelConverter).apply(entityOne);
        verify(userEntityToModelConverter).apply(entityTwo);
        verifyNoMoreInteractions(userRepository, userEntityToModelConverter);
    }

    @Test
    public void shouldFindUserByIdDelegatesToUserRepository()
    {
        Long id = 1L;
        when(userRepository.findOne(id)).thenReturn(null);

        UserModel model = userService.findUserById(id);

        assertThat(model, nullValue());
        verify(userRepository).findOne(id);
        verifyNoMoreInteractions(userRepository);
        verifyZeroInteractions(userEntityToModelConverter);
    }

    @Test
    public void shouldFindUserById()
    {
        Long id = 1L;
        UserEntity entity = mock(UserEntity.class);
        UserModel model = mock(UserModel.class);

        when(userRepository.findOne(id)).thenReturn(entity);
        when(userEntityToModelConverter.apply(entity)).thenReturn(model);

        assertThat(userService.findUserById(id), is(model));

        verify(userRepository).findOne(id);
        verify(userEntityToModelConverter).apply(entity);
        verifyNoMoreInteractions(userRepository, userEntityToModelConverter);
    }

    @Test
    public void shouldFindUserByEmailDelegatesToUserRepository()
    {
        String email = "test@mail.com";
        when(userRepository.findOneByEmail(email)).thenReturn(null);

        assertThat(userService.findUserByEmail(email), nullValue());

        verify(userRepository).findOneByEmail(email);
        verifyNoMoreInteractions(userRepository);
        verifyZeroInteractions(userEntityToModelConverter);
    }

    @Test
    public void shouldFindUserByEmail()
    {
        String email = "test@mail.com";
        UserEntity entity = mock(UserEntity.class);
        UserModel model = mock(UserModel.class);

        when(userRepository.findOneByEmail(email)).thenReturn(entity);
        when(userEntityToModelConverter.apply(entity)).thenReturn(model);

        assertThat(userService.findUserByEmail(email), is(model));

        verify(userRepository).findOneByEmail(email);
        verify(userEntityToModelConverter).apply(entity);
        verifyNoMoreInteractions(userRepository, userEntityToModelConverter);
    }
}
