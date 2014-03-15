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
package au.id.hazelwood.idms.repository.user;

import au.id.hazelwood.idms.entity.user.UserEntity;

import org.junit.Before;
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
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration("classpath:au/id/hazelwood/idms/application-context-test.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration
@Transactional
public class UserRepositoryIntegrationTest
{
    @Resource
    private UserRepository userRepository;

    @Before
    public void verifyTestDataLoaded() throws Exception
    {
        assertThat(userRepository.count(), is(10L));
    }

    @Test
    public void shouldDeleteAll() throws Exception
    {
        userRepository.deleteAll();
        assertThat(userRepository.count(), is(0L));
    }

    @Test
    public void shouldFindOneByEmail() throws Exception
    {
        assertThat(userRepository.findOneByEmail(""), nullValue());
        assertThat(userRepository.findOneByEmail(null), nullValue());
        assertThat(userRepository.findOneByEmail("test-01@hazelwood.id.au"), hasProperty("id", equalTo(1L)));
        assertThat(userRepository.findOneByEmail("test-10@hazelwood.id.au"), hasProperty("id", equalTo(10L)));
    }

    @Test
    public void shouldCreateAndDelete() throws Exception
    {
        long initialCount = userRepository.count();

        String email = "one@hazelwood.id.au";
        String firstName = "User";
        String lastName = "One";
        UserEntity one = createUserEntity(email, firstName, lastName);
        userRepository.save(one);
        assertThat(userRepository.count(), is(initialCount + 1L));
        assertThat(userRepository.exists(one.getId()), is(true));

        UserEntity two = createUserEntity("two@hazelwood.id.au", "User", "Two");
        UserEntity three = createUserEntity("three@hazelwood.id.au", "User", "Three");
        userRepository.save(Arrays.asList(two, three));
        assertThat(userRepository.count(), is(initialCount + 3L));

        userRepository.delete(one);
        assertThat(userRepository.count(), is(initialCount + 2L));
        assertThat(userRepository.exists(one.getId()), is(false));

        userRepository.delete(Arrays.asList(two, three));
        assertThat(userRepository.count(), is(initialCount));
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldGetValidationError() throws Exception
    {
        userRepository.save(createUserEntity("", "", ""));
    }

    private UserEntity createUserEntity(String email, String first, String last)
    {
        UserEntity entity = new UserEntity();
        entity.setEmail(email);
        entity.setFirstName(first);
        entity.setLastName(last);
        return entity;
    }
}
