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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@FixMethodOrder(MethodSorters.JVM)
public class UserEntityToModelConverterUnitTest
{
    private final UserEntityToModelConverter converter = new UserEntityToModelConverter();

    @Test
    public void shouldConvertNull() throws Exception
    {
        assertThat(converter.apply(null), nullValue());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldConvertAll() throws Exception
    {
        UserEntity one = createUserEntity(1001L, "test-1@mail.com", "first", "last");
        UserEntity two = createUserEntity(1002L, "test-2@mail.com", "second", "last");


        List<UserModel> models = Lists.newArrayList(Iterables.transform(Arrays.asList(one, two), converter));

        assertUserModel(models.get(0), 1001L, "test-1@mail.com", "first", "last");
        assertUserModel(models.get(1), 1002L, "test-2@mail.com", "second", "last");
    }

    private void assertUserModel(UserModel model, Long id, String email, String first, String last)
    {
        assertThat(model.getId(), is(id));
        assertThat(model.getEmail(), is(email));
        assertThat(model.getFirstName(), is(first));
        assertThat(model.getLastName(), is(last));
    }

    private UserEntity createUserEntity(Long id, String email, String first, String last)
    {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail(email);
        entity.setFirstName(first);
        entity.setLastName(last);
        return entity;
    }
}
