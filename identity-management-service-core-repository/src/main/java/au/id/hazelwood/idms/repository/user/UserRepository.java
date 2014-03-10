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
package au.id.hazelwood.idms.repository.user;

import au.id.hazelwood.idms.entity.user.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link UserEntity}.
 *
 * @author Ricky Hazelwood
 */
public interface UserRepository extends JpaRepository<UserEntity, Long>
{
    /**
     * Retrieves {@link UserEntity} by email address.
     *
     * @param email {@link UserEntity}'s email address
     * @return {@link UserEntity} with the given email address or {@literal null} if none found.
     */
    UserEntity findOneByEmail(String email);
}
