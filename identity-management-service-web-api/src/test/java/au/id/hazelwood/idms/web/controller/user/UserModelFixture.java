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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public final class UserModelFixture
{
    private UserModelFixture()
    {
    }

    public static UserModel admin()
    {
        return UserModelFixture.create(1L, "admin@hazelwood.id.au", "", "Admin");
    }

    public static UserModel standard()
    {
        return UserModelFixture.create(2L, "ricky@hazelwood.id.au", "Ricky", "Hazelwood");
    }

    public static UserModel random()
    {
        return UserModelFixture.create(randomNumber(1000, 5000), randomEmail(), randomString(5), randomString(8));
    }

    public static UserModel create(long id, String email, String first, String last)
    {
        UserModel model = new UserModel();
        model.setId(id);
        model.setEmail(email);
        model.setFirstName(first);
        model.setLastName(last);
        return model;
    }

    private static String randomEmail()
    {
        return MessageFormat.format("{0}@{1}.com", StringUtils.lowerCase(randomString(5)), StringUtils.lowerCase(randomString(10)));
    }

    private static long randomNumber(int start, int end)
    {
        return (long) (Math.random() * (start - end) + start);
    }

    private static String randomString(int length)
    {
        return RandomStringUtils.randomAlphabetic(length);
    }
}
