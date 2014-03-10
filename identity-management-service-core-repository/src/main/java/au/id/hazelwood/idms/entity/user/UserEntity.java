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
package au.id.hazelwood.idms.entity.user;

import au.id.hazelwood.idms.entity.framework.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * This entity represents the "users" table.
 *
 * @author Ricky Hazelwood
 */
@Entity
@Table(name = "users")
@SuppressWarnings("UnusedDeclaration")
public class UserEntity extends BaseEntity
{
    @NotNull
    @Size(min = 1, max = 254)
    @Pattern(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE, message = "not a well-formed email address")
    @Column(name = "email", unique = true, nullable = false, length = 254)
    private String email;
    @Size(max = 20)
    @Pattern(regexp = NAME_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE, message = "not a well-formed name")
    @Column(name = "first_name", length = 20)
    private String firstName;
    @Size(max = 20)
    @Pattern(regexp = NAME_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE, message = "not a well-formed name")
    @Column(name = "last_name", length = 20)
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
