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
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;

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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(MockitoJUnitRunner.class)
public class UserDetailDtoToModelConverterUnitTest
{
    @Mock
    private UserService userService;
    private UserDetailDtoToModelConverter converter;

    @Before
    public void before() throws Exception
    {
        converter = new UserDetailDtoToModelConverter(userService);
    }

    @After
    public void after() throws Exception
    {
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void shouldConvertNull() throws Exception
    {
        assertThat(converter.apply(null), nullValue());
    }

    @Test
    public void shouldConvertAll() throws Exception
    {
        when(userService.getUserById(1001L)).thenReturn(UserModelFixture.create(1001L, "test-1@mail.com", "first", "last"));
        when(userService.getUserById(1002L)).thenReturn(UserModelFixture.create(1002L, "test-2@mail.com", "second", "last"));

        UserDetailDto one = createUserDetailDto(1001L, "new-test-1@mail.com", "new-first", "new-last");
        UserDetailDto two = createUserDetailDto(1002L, "new-test-2@mail.com", "new-second", "new-last");
        List<UserModel> models = Lists.newArrayList(Iterables.transform(Arrays.asList(one, two), converter));

        assertUserModel(models.get(0), 1001L, "new-test-1@mail.com", "new-first", "new-last");
        assertUserModel(models.get(1), 1002L, "new-test-2@mail.com", "new-second", "new-last");
        verify(userService).getUserById(1001L);
        verify(userService).getUserById(1002L);
    }

    @Test
    public void shouldTrimValuesToNull() throws Exception
    {
        when(userService.getUserById(1001L)).thenReturn(UserModelFixture.create(1001L, "test-1@mail.com", "first", "last"));

        UserDetailDto one = createUserDetailDto(1001L, "", "", "");
        List<UserModel> models = Lists.newArrayList(Iterables.transform(Arrays.asList(one), converter));

        assertUserModel(models.get(0), 1001L, null, null, null);
        verify(userService).getUserById(1001L);
    }

    private UserDetailDto createUserDetailDto(long id, String email, String first, String last)
    {
        UserDetailDto dto = new UserDetailDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setFirstName(first);
        dto.setLastName(last);
        return dto;
    }

    private void assertUserModel(UserModel model, long id, String email, String first, String last)
    {
        assertThat(model, notNullValue());
        assertThat(model.getId(), is(id));
        assertThat(model.getEmail(), is(email));
        assertThat(model.getFirstName(), is(first));
        assertThat(model.getLastName(), is(last));
    }
}
