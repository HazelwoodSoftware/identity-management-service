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
package au.id.hazelwood.idms.web.handler;

import au.id.hazelwood.idms.web.exception.BadRequestException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * Handler for {@link EntityNotFoundException} thrown during the processing of a request.
 *
 * @author Ricky Hazelwood
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BadRequestExceptionHandler
{
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handle(BadRequestException ex)
    {
        return new ResponseEntity<Object>(ex.getErrorDto(), HttpStatus.BAD_REQUEST);
    }
}
