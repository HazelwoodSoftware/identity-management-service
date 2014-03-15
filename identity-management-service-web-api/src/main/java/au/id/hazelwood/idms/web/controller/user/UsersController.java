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

import au.id.hazelwood.idms.service.user.UserService;
import au.id.hazelwood.idms.web.dto.user.UserCreateDto;
import au.id.hazelwood.idms.web.dto.user.UserDetailDto;
import au.id.hazelwood.idms.web.dto.user.UserSummaryDto;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for all user management operations.
 *
 * @author Ricky Hazelwood
 */
@Controller
@RequestMapping(value = "/api/users")
@Api(value = "/api/users", description = "All operations for users")
public class UsersController
{
    private final UserService userService;
    private final UserModelToSummaryDtoConverter userModelToSummaryDtoConverter;
    private final UserModelToDetailDtoConverter userModelToDetailDtoConverter;
    private final UserDetailDtoToModelConverter userDetailDtoToModelConverter;

    @Autowired
    public UsersController(UserService userService,
                           UserModelToSummaryDtoConverter userModelToSummaryDtoConverter,
                           UserModelToDetailDtoConverter userModelToDetailDtoConverter,
                           UserDetailDtoToModelConverter userDetailDtoToModelConverter)
    {
        this.userService = userService;
        this.userModelToSummaryDtoConverter = userModelToSummaryDtoConverter;
        this.userModelToDetailDtoConverter = userModelToDetailDtoConverter;
        this.userDetailDtoToModelConverter = userDetailDtoToModelConverter;
    }

    @ApiOperation(value = "Get all users", notes = "Get all users currently available", position = 1,
                  response = UserSummaryDto.class, responseContainer = "List", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(@ApiResponse(code = 500, message = "Processing error"))
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<UserSummaryDto> findAllUsers()
    {
        return Lists.newArrayList(Iterables.transform(userService.findAllUsers(), userModelToSummaryDtoConverter));
    }

    @ApiOperation(value = "Creates a new user", notes = "Create user with specified detail", position = 2,
                  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 500, message = "Processing error")})
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createUser(@ApiParam(name = "body", required = true, value = "User Detail") @RequestBody UserCreateDto dto)
    {
    }

    @ApiOperation(value = "Find specific users", notes = "Get user by specified ID", position = 3,
                  response = UserDetailDto.class, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 500, message = "Processing error"), @ApiResponse(code = 404, message = "User not found")})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public UserDetailDto findUser(@ApiParam(name = "id", required = true, value = "User ID") @PathVariable("id") Long id)
    {
        return userModelToDetailDtoConverter.apply(userService.getUserById(id));
    }

    @ApiOperation(value = "Save specific user", notes = "Save user by specified ID", position = 4,
                  consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 500, message = "Processing error"), @ApiResponse(code = 404, message = "User not found")})
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@ApiParam(name = "id", required = true, value = "User ID") @PathVariable("id") Long id,
                           @Valid @ApiParam(name = "body", required = true, value = "User Detail") @RequestBody UserDetailDto dto)
    {
        Assert.isTrue(id.equals(dto.getId()), "Id in the url doesn't match id in the body.");
        userService.saveUser(userDetailDtoToModelConverter.apply(dto));
    }
}
