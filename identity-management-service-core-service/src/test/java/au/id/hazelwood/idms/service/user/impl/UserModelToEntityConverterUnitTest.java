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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(MockitoJUnitRunner.class)
public class UserModelToEntityConverterUnitTest
{
    @Mock
    private UserRepository userRepository;
    private UserModelToEntityConverter converter;

    @Before
    public void before() throws Exception
    {
        converter = new UserModelToEntityConverter(userRepository);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldConvertAllWithNoId() throws Exception
    {
        UserModel one = createUserModel(null, "test-1@mail.com", "first", "last");
        UserModel two = createUserModel(null, "test-2@mail.com", "second", "last");

        List<UserEntity> models = Lists.newArrayList(Iterables.transform(Arrays.asList(one, two), converter));

        assertUserEntity(models.get(0), null, "test-1@mail.com", "first", "last");
        assertUserEntity(models.get(1), null, "test-2@mail.com", "second", "last");
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldConvertAllWithId() throws Exception
    {
        UserModel one = createUserModel(1001L, "test-1@mail.com", "first", "last");
        UserModel two = createUserModel(1002L, "test-2@mail.com", "second", "last");

        when(userRepository.getOne(1001L)).thenReturn(createUserEntity(1001L));
        when(userRepository.getOne(1002L)).thenReturn(createUserEntity(1002L));

        List<UserEntity> models = Lists.newArrayList(Iterables.transform(Arrays.asList(one, two), converter));

        assertUserEntity(models.get(0), 1001L, "test-1@mail.com", "first", "last");
        assertUserEntity(models.get(1), 1002L, "test-2@mail.com", "second", "last");

        verify(userRepository).getOne(1001L);
        verify(userRepository).getOne(1002L);
    }

    @After
    public void verifyNoMoreInteractionsOnAllMocks()
    {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void shouldConvertNull() throws Exception
    {
        assertThat(converter.apply(null), nullValue());
    }

    private void assertUserEntity(UserEntity entity, Long id, String email, String first, String last)
    {
        assertThat(entity.getId(), is(id));
        assertThat(entity.getEmail(), is(email));
        assertThat(entity.getFirstName(), is(first));
        assertThat(entity.getLastName(), is(last));
    }

    private UserEntity createUserEntity(Long id)
    {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        return entity;
    }

    private UserModel createUserModel(Long id, String email, String first, String last)
    {
        UserModel model = new UserModel();
        model.setId(id);
        model.setEmail(email);
        model.setFirstName(first);
        model.setLastName(last);
        return model;
    }
}
