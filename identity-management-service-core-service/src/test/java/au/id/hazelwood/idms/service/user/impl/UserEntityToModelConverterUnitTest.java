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

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@FixMethodOrder(MethodSorters.JVM)
public class UserEntityToModelConverterUnitTest
{
    private final UserEntityToModelConverter converter = new UserEntityToModelConverter();

    @Test
    public void testApplyWithNull() throws Exception
    {
        UserModel model = converter.apply(null);
        assertThat(model, nullValue());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testApplyWithUser() throws Exception
    {
        UserEntity entity = new UserEntity("test@mail.com", "first", "last");
        entity.setId(1001L);

        UserModel model = converter.apply(entity);

        assertThat(model, notNullValue());
        assertThat(model.getId(), is(1001L));
        assertThat(model.getEmail(), is("test@mail.com"));
        assertThat(model.getFirstName(), is("first"));
        assertThat(model.getLastName(), is("last"));
    }
}
