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
package au.id.hazelwood.idms.web.dto.user;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

@FixMethodOrder(MethodSorters.JVM)
public class UserSummaryDtoUnitTest
{
    @Test
    public void shouldHaveValidConstructor()
    {
        assertThat(UserSummaryDto.class, hasValidBeanConstructor());
    }

    @Test
    public void shouldHaveValidGettersAndSetters()
    {
        assertThat(UserSummaryDto.class, hasValidGettersAndSetters());
    }

    @Test
    public void shouldHaveValidEqualsHashCodeAndToString()
    {
        assertThat(UserSummaryDto.class, allOf(hasValidBeanHashCode(), hasValidBeanEquals(), hasValidBeanToString()));
    }
}
