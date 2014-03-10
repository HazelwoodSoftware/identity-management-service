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
import au.id.hazelwood.idms.service.user.UserService;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Implementation of {@link UserService} that uses {@link UserRepository}.
 *
 * @author Ricky Hazelwood
 */
@Service
@Transactional
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final UserEntityToModelConverter userEntityToModelConverter;
    private final UserModelToEntityConverter userModelToEntityConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserEntityToModelConverter userEntityToModelConverter,
                           UserModelToEntityConverter userModelToEntityConverter1)
    {
        this.userRepository = userRepository;
        this.userEntityToModelConverter = userEntityToModelConverter;
        userModelToEntityConverter = userModelToEntityConverter1;
    }

    @Override
    public List<UserModel> findAllUsers()
    {
        return Lists.newArrayList(Iterables.transform(userRepository.findAll(), userEntityToModelConverter));
    }

    @Override
    public UserModel findUserById(Long id)
    {
        return transform(userRepository.findOne(id));
    }

    @Override
    public UserModel findUserByEmail(String email)
    {
        return transform(userRepository.findOneByEmail(email));
    }

    @Override
    public UserModel saveUser(UserModel model)
    {
        Assert.notNull(model, "The given model must not be null!");
        return transform(userRepository.save(transform(model)));
    }

    private UserEntity transform(UserModel model)
    {
        return userModelToEntityConverter.apply(model);
    }

    private UserModel transform(UserEntity entity)
    {
        return userEntityToModelConverter.apply(entity);
    }
}
