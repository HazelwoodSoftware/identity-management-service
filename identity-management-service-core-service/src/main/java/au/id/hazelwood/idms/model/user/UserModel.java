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
package au.id.hazelwood.idms.model.user;

import au.id.hazelwood.idms.model.framework.BaseModel;
import au.id.hazelwood.idms.validation.Email;
import au.id.hazelwood.idms.validation.Name;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This model is a representation of an user.
 *
 * @author Ricky Hazelwood
 * @see au.id.hazelwood.idms.entity.user.UserEntity
 */
public class UserModel extends BaseModel
{
    @Email
    @NotNull
    @Size(min = 1, max = 254)
    private String email;
    @Name
    @Size(max = 20)
    private String firstName;
    @Name
    @Size(max = 20)
    private String lastName;

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
