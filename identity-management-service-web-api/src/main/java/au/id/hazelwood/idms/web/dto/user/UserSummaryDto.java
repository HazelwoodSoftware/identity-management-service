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

import au.id.hazelwood.idms.web.dto.framework.BaseDto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Summary dto for {@link au.id.hazelwood.idms.model.user.UserModel}.
 *
 * @author Ricky Hazelwood
 */
@ApiModel(value = "A user", description = "Summary dto for an user")
public class UserSummaryDto extends BaseDto
{
    @ApiModelProperty(value = "ID of the user", notes = "The system identifier of this user", required = true, position = 0)
    private Long id;
    @ApiModelProperty(value = "Email address of the user", notes = "The email address of this user", required = true, position = 1)
    private String email;
    @ApiModelProperty(value = "Name of the user", notes = "The display name of this user", required = false, position = 2)
    private String name;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
