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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Handler for all exceptions thrown during the processing of a request.
 *
 * @author Ricky Hazelwood
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request)
    {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, WebRequest request)
    {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request)
    {
        return handleExceptionInternal(ex, createResponseBody(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleAllUnknown(Exception ex, WebRequest request)
    {
        ResponseEntity<Object> entity;
        int cveIndex = ExceptionUtils.indexOfType(ex, ConstraintViolationException.class);
        if (cveIndex != -1)
        {
            ConstraintViolationException cve = ConstraintViolationException.class.cast(ExceptionUtils.getThrowableList(ex).get(cveIndex));
            entity = handleExceptionInternal(ex, createResponseBody(cve), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
        }
        else
        {
            entity = handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
        return entity;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException nve, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request)
    {
        return handleExceptionInternal(nve, createResponseBody(nve), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return super.handleExceptionInternal(ex, body == null ? createResponseBody(ex) : body, headers, status, request);
    }

    private ErrorResponseBody createResponseBody(Exception ex)
    {
        return new ErrorResponseBody(ex.getMessage());
    }

    private ErrorResponseBody createResponseBody(ConstraintViolationException cve)
    {
        List<ErrorDetail> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : cve.getConstraintViolations())
        {
            errors.add(new ErrorDetail(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        Collections.sort(errors, new ErrorDetailComparator());
        return new ErrorResponseBody("Constraint violation.", errors);
    }

    private ErrorResponseBody createResponseBody(MethodArgumentNotValidException nve)
    {
        List<ErrorDetail> errors = new ArrayList<>();
        for (ObjectError error : nve.getBindingResult().getGlobalErrors())
        {
            errors.add(new ErrorDetail("", resolveErrorMessage(error)));
        }
        for (FieldError error : nve.getBindingResult().getFieldErrors())
        {
            errors.add(new ErrorDetail(error.getField(), resolveErrorMessage(error)));
        }
        Collections.sort(errors, new ErrorDetailComparator());
        return new ErrorResponseBody("Validation error.", errors);
    }

    private String resolveErrorMessage(MessageSourceResolvable resolvable)
    {
        return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
    }

    private static class ErrorDetailComparator implements Serializable, Comparator<ErrorDetail>
    {
        @Override
        public int compare(ErrorDetail o1, ErrorDetail o2)
        {
            return o1.getField().compareTo(o2.getField());
        }
    }
}