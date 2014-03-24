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

import au.id.hazelwood.idms.web.dto.framework.BaseDto;

public final class ErrorInfoDto extends BaseDto
{
    private final String field;
    private final String message;

    public ErrorInfoDto(String message)
    {
        this(null, message);
    }

    public ErrorInfoDto(String field, String message)
    {
        this.field = field;
        this.message = message;
    }

    public String getField()
    {
        return field;
    }

    public String getMessage()
    {
        return message;
    }
}
