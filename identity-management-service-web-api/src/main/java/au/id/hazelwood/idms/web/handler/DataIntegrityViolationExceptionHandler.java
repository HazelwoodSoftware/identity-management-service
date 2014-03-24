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

import au.id.hazelwood.idms.web.dto.error.ErrorDto;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler for {@link DataIntegrityViolationException} thrown during the processing of a request.
 *
 * @author Ricky Hazelwood
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataIntegrityViolationExceptionHandler
{
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handle()
    {
        return new ResponseEntity<Object>(new ErrorDto("Data integrity violation."), HttpStatus.CONFLICT);
    }
}
