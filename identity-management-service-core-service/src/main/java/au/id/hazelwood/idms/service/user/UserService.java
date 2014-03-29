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
package au.id.hazelwood.idms.service.user;

import au.id.hazelwood.idms.exception.EmailAddressInUseException;
import au.id.hazelwood.idms.model.user.UserModel;

import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

/**
 * Service to find, create, update users.
 *
 * @author Ricky Hazelwood
 */
@Validated
public interface UserService
{
    /**
     * Returns all {@link UserModel}.
     *
     * @return all {@link UserModel}
     */
    List<UserModel> findAllUsers();

    /**
     * Retrieves {@link UserModel} by id.
     *
     * @param id {@link UserModel}'s id
     * @return {@link UserModel} with the given id.
     * @throws IllegalArgumentException if {@code id} is {@literal null}
     * @throws EntityNotFoundException  {@code UserModel} with {@code id} does not exist.
     */
    UserModel getUserById(Long id);

    /**
     * Retrieves {@link UserModel} by email address.
     *
     * @param email {@link UserModel}'s email address
     * @return {@link UserModel} with the given email address or {@literal null} if none found.
     */
    UserModel findUserByEmail(String email);

    /**
     * Create or update {@link UserModel}.
     * <p/>
     * Use the returned instance for further operations as the save operation might have changed the entity instance completely.
     *
     * @param model {@link UserModel} to save.
     * @return saved {@link UserModel}
     * @throws IllegalArgumentException   if {@code UserModel} is {@literal null}
     * @throws EntityNotFoundException    for update operation if {@code UserModel} with {@code id} does not exist.
     * @throws EmailAddressInUseException if email address is used by another user.
     */
    UserModel saveUser(@Valid UserModel model) throws EmailAddressInUseException;
}
