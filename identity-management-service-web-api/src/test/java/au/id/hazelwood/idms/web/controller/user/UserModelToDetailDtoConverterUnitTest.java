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
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class UserModelToDetailDtoConverterUnitTest
{
    private final UserModelToDetailDtoConverter converter = new UserModelToDetailDtoConverter();

    @Test
    public void shouldConvertNull() throws Exception
    {
        assertThat(converter.apply(null), nullValue());
    }

    @Test
    public void shouldConvertAll() throws Exception
    {
        UserModel one = createUserModel(1001L, "test-1@mail.com", "first", "last");
        UserModel two = createUserModel(1002L, "test-2@mail.com", "second", "last");
        List<UserDetailDto> dtos = Lists.newArrayList(Iterables.transform(Arrays.asList(one, two), converter));
        assertUserDetailDto(dtos.get(0), 1001L, "test-1@mail.com", "first", "last");
        assertUserDetailDto(dtos.get(1), 1002L, "test-2@mail.com", "second", "last");
    }

    private UserModel createUserModel(long id, String email, String first, String last)
    {
        UserModel model = new UserModel();
        model.setId(id);
        model.setEmail(email);
        model.setFirstName(first);
        model.setLastName(last);
        return model;
    }

    private void assertUserDetailDto(UserDetailDto dto, long id, String email, String first, String last)
    {
        assertThat(dto, notNullValue());
        assertThat(dto.getId(), is(id));
        assertThat(dto.getEmail(), is(email));
        assertThat(dto.getFirstName(), is(first));
        assertThat(dto.getLastName(), is(last));
    }
}
