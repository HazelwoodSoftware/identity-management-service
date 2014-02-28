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

import com.google.common.base.Function;
import org.springframework.stereotype.Component;

/**
 * Function to convert {@link UserEntity} into {@link UserModel}.
 *
 * @author Ricky Hazelwood
 */
@Component
public class UserEntityToModelConverter implements Function<UserEntity, UserModel>
{
    @Override
    public UserModel apply(UserEntity entity)
    {
        return entity == null ? null : convert(entity);
    }

    private UserModel convert(UserEntity entity)
    {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        return model;
    }
}
