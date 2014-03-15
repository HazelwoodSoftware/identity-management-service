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

import au.id.hazelwood.idms.validation.Email;
import au.id.hazelwood.idms.validation.Name;
import au.id.hazelwood.idms.web.dto.framework.BaseDto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Detail dto for {@link au.id.hazelwood.idms.model.user.UserModel}.
 *
 * @author Ricky Hazelwood
 */
@ApiModel(value = "Existing user", description = "Detail dto for an user")
public class UserDetailDto extends BaseDto
{
    @NotNull
    @ApiModelProperty(value = "ID of the user", notes = "The system identifier of this user", required = true, position = 0)
    private Long id;
    @Email
    @NotNull
    @Size(min = 1, max = 254)
    @ApiModelProperty(value = "Email address of the user", notes = "The email address of this user", required = true, position = 1)
    private String email;
    @Name
    @Size(max = 20)
    @ApiModelProperty(value = "Fist name of the user", notes = "The first name of this user", required = false, position = 2)
    private String firstName;
    @Name
    @Size(max = 20)
    @ApiModelProperty(value = "Last name of the user", notes = "The last name of this user", required = false, position = 3)
    private String lastName;

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

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
