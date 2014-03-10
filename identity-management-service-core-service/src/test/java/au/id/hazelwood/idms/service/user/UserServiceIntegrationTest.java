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
package au.id.hazelwood.idms.service.user;

import au.id.hazelwood.idms.model.user.UserModel;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(locations = "classpath:au/id/hazelwood/idms/application-context-test.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration
@Transactional
public class UserServiceIntegrationTest
{
    @Resource
    private UserService userService;

    @Test
    public void shouldFindAllUsers() throws Exception
    {
        assertThat(userService.findAllUsers().size(), is(2));
    }

    @Test
    public void shouldFindUserById() throws Exception
    {
        assertThat(userService.findUserById(0L), nullValue());
        assertThat(userService.findUserById(1001L), hasProperty("email", equalTo("admin@hazelwood.id.au")));
    }

    @Test
    public void shouldFindUserByEmail() throws Exception
    {
        assertThat(userService.findUserByEmail(""), nullValue());
        assertThat(userService.findUserByEmail(null), nullValue());
        assertThat(userService.findUserByEmail("admin@hazelwood.id.au"), hasProperty("id", equalTo(1001L)));
    }

    @Test
    public void shouldSaveNewUser() throws Exception
    {
        String email = randomString(10) + "@hazelwood.id.au";
        assertThat(userService.findUserByEmail(email), nullValue());

        UserModel model = userService.saveUser(createUserModel(null, email, randomString(5), randomString(5)));

        assertThat(model, is(userService.findUserById(model.getId())));
        assertThat(model, is(userService.findUserByEmail(email)));
        assertThat(model, hasProperty("email", equalTo(email)));
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
        String email = randomString(10) + "@hazelwood.id.au";

        UserModel model = userService.saveUser(createUserModel(1001L, email, randomString(5), randomString(5)));

        assertThat(model, is(userService.findUserById(model.getId())));
        assertThat(model, is(userService.findUserByEmail(email)));
        assertThat(model, hasProperty("email", equalTo(email)));
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

    private String randomString(int length)
    {
        return RandomStringUtils.randomAlphabetic(length);
    }
}
