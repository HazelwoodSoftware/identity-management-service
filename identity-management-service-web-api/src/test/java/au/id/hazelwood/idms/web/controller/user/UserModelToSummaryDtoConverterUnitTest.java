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
import au.id.hazelwood.idms.web.dto.user.UserSummaryDto;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@FixMethodOrder(MethodSorters.JVM)
public class UserModelToSummaryDtoConverterUnitTest
{
    private final UserModelToSummaryDtoConverter converter = new UserModelToSummaryDtoConverter();

    @Test
    public void shouldConvertNull() throws Exception
    {
        assertThat(converter.apply(null), nullValue());
    }

    @Test
    public void shouldConvertAll() throws Exception
    {
        UserModel one = UserModelFixture.create(1001L, "test-1@mail.com", "first", "last");
        UserModel two = UserModelFixture.create(1002L, "test-2@mail.com", null, null);
        UserModel three = UserModelFixture.create(1003L, "test-3@mail.com", null, "Admin");
        List<UserSummaryDto> dtos = Lists.newArrayList(Iterables.transform(Arrays.asList(one, two, three), converter));
        assertUserSummaryDto(dtos.get(0), 1001L, "test-1@mail.com", "first last");
        assertUserSummaryDto(dtos.get(1), 1002L, "test-2@mail.com", "");
        assertUserSummaryDto(dtos.get(2), 1003L, "test-3@mail.com", "Admin");
    }

    private void assertUserSummaryDto(UserSummaryDto dto, long id, String email, String name)
    {
        assertThat(dto, notNullValue());
        assertThat(dto.getId(), is(id));
        assertThat(dto.getEmail(), is(email));
        assertThat(dto.getName(), is(name));
    }
}
