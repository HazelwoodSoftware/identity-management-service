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
import au.id.hazelwood.idms.service.user.UserService;
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;

import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Function to convert {@link UserDetailDto} into {@link UserModel}.
 *
 * @author Ricky Hazelwood
 */
@Component
public class UserDetailDtoToModelConverter implements Function<UserDetailDto, UserModel>
{
    private final UserService userService;

    @Autowired
    public UserDetailDtoToModelConverter(UserService userService)
    {
        this.userService = userService;
    }

    @Override
    public UserModel apply(UserDetailDto dto)
    {
        return dto == null ? null : convert(dto);
    }

    private UserModel convert(UserDetailDto dto)
    {
        UserModel model = userService.getUserById(dto.getId());
        model.setEmail(dto.getEmail());
        model.setFirstName(dto.getFirstName());
        model.setLastName(dto.getLastName());
        return model;
    }
}
