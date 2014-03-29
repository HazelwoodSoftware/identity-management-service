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
package au.id.hazelwood.idms.web.dto.error;

public enum ErrorType
{
    UNEXPECTED_ERROR(1000, "Unexpected system error."),
    INVALID_REQUEST(1010, "Invalid request."),
    VALIDATION_ERROR(1020, "Validation error."),
    CONSTRAINT_VIOLATION(1030, "Constraint violation."),
    INTEGRITY_VIOLATION(1040, "Data integrity violation."),
    ENTITY_MISSING(1050, "Entity not found.");
    private final Integer code;
    private final String message;

    private ErrorType(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
