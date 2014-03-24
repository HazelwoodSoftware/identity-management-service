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

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static au.id.hazelwood.idms.web.handler.ErrorResponseEntityAssert.assertResponseBody;
import static au.id.hazelwood.idms.web.handler.ErrorResponseEntityAssert.assertResponseErrors;
import static au.id.hazelwood.idms.web.handler.ErrorResponseEntityAssert.assertResponseHeader;
import static au.id.hazelwood.idms.web.handler.ErrorResponseEntityAssert.assertResponseStatus;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.JVM)
@RunWith(MockitoJUnitRunner.class)
public class GlobalExceptionHandlerUnitTest
{
    @Mock
    private MessageSource messageSource;
    @Mock
    private WebRequest webRequest;
    private GlobalExceptionHandler handler;

    @Before
    public void before() throws Exception
    {
        handler = new GlobalExceptionHandler(messageSource);
    }

    @After
    public void after() throws Exception
    {
        verifyNoMoreInteractions(messageSource, webRequest);
    }

    @Test
    public void shouldHandleConstraintViolationException() throws Exception
    {
        ConstraintViolation<?> one = mock(ConstraintViolation.class);
        when(one.getPropertyPath()).thenReturn(mock(Path.class, "path one"));
        when(one.getMessage()).thenReturn("message one");
        ConstraintViolation<?> two = mock(ConstraintViolation.class);
        when(two.getPropertyPath()).thenReturn(mock(Path.class, "path two"));
        when(two.getMessage()).thenReturn("message two");

        ConstraintViolationException cve = new ConstraintViolationException(new HashSet<>(Arrays.asList(one, two)));

        ResponseEntity<Object> responseEntity = handler.handleConstraintViolationException(cve, webRequest);
        assertResponseStatus(responseEntity, HttpStatus.BAD_REQUEST);
        assertResponseHeader(responseEntity, 0);
        assertResponseBody(responseEntity, "Constraint violation.", 2);
        assertResponseErrors(responseEntity, "path one", "message one");
        assertResponseErrors(responseEntity, "path two", "message two");
    }

    @Test
    public void shouldHandleAllUnknownWithConstraintViolationExceptionCause() throws Exception
    {
        ConstraintViolation<?> one = mock(ConstraintViolation.class);
        when(one.getPropertyPath()).thenReturn(mock(Path.class, "path"));
        when(one.getMessage()).thenReturn("message");

        RuntimeException ex = new RuntimeException("Runtime exception due to cve", new ConstraintViolationException(Collections.singleton(one)));

        ResponseEntity<Object> responseEntity = handler.handleAllUnknown(ex, webRequest);
        assertResponseStatus(responseEntity, HttpStatus.BAD_REQUEST);
        assertResponseHeader(responseEntity, 0);
        assertResponseBody(responseEntity, "Constraint violation.", 1);
        assertResponseErrors(responseEntity, "path", "message");
    }

    @Test
    public void shouldHandleMethodArgumentNotValid() throws Exception
    {
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError objectError = mock(ObjectError.class);
        FieldError fieldError = mock(FieldError.class);
        when(bindingResult.getGlobalErrors()).thenReturn(Arrays.asList(objectError));
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError));
        when(messageSource.getMessage(objectError, LocaleContextHolder.getLocale())).thenReturn("object error message");
        when(fieldError.getField()).thenReturn("field name");
        when(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale())).thenReturn("field error message");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> responseEntity = handler.handleException(ex, webRequest);

        assertResponseStatus(responseEntity, HttpStatus.BAD_REQUEST);
        assertResponseHeader(responseEntity, 0);
        assertResponseBody(responseEntity, "Validation error.", 2);
        assertResponseErrors(responseEntity, null, "object error message");
        assertResponseErrors(responseEntity, "field name", "field error message");
        verify(messageSource).getMessage(objectError, LocaleContextHolder.getLocale());
        verify(messageSource).getMessage(fieldError, LocaleContextHolder.getLocale());
    }

    @Test
    public void shouldHandleAllUnknown() throws Exception
    {
        RuntimeException ex = new RuntimeException("Runtime exception");

        ResponseEntity<Object> responseEntity = handler.handleAllUnknown(ex, webRequest);

        assertResponseStatus(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
        assertResponseHeader(responseEntity, 0);
        assertResponseBody(responseEntity, "Unexpected system error.", 0);
        verify(webRequest).setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
    }
}
