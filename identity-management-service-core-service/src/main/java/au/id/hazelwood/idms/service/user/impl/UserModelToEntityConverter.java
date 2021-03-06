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
import au.id.hazelwood.idms.repository.user.UserRepository;

import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Function to convert {@link UserModel} into {@link UserEntity}.
 *
 * @author Ricky Hazelwood
 */
@Component
public class UserModelToEntityConverter implements Function<UserModel, UserEntity>
{
    private final UserRepository userRepository;

    @Autowired
    public UserModelToEntityConverter(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity apply(UserModel model)
    {
        return model == null ? null : convert(model);
    }

    private UserEntity convert(UserModel model)
    {
        UserEntity entity = model.getId() == null ? new UserEntity() : userRepository.getOne(model.getId());
        entity.setEmail(model.getEmail());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        return entity;
    }
}
