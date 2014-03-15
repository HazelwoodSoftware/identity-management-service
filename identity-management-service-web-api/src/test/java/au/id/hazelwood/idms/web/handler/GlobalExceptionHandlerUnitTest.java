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

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
    public void shouldHandleEntityNotFound() throws Exception
    {
        ResponseEntity<Object> responseEntity = handler.handleEntityNotFound(new EntityNotFoundException("Entity not found"), webRequest);
        assertResponseEntity(responseEntity, HttpStatus.NOT_FOUND, "Entity not found", 0);
    }

    @Test
    public void shouldHandleIllegalArgument() throws Exception
    {
        ResponseEntity<Object> responseEntity = handler.handleIllegalArgument(new IllegalArgumentException("Illegal argument"), webRequest);
        assertResponseEntity(responseEntity, HttpStatus.BAD_REQUEST, "Illegal argument", 0);
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
        assertResponseEntity(responseEntity, HttpStatus.BAD_REQUEST, "Constraint violation.", 2);
        assertResponseBodyError(responseEntity, "path one", "message one");
        assertResponseBodyError(responseEntity, "path two", "message two");
    }

    @Test
    public void shouldHandleAllUnknownWithConstraintViolationExceptionCause() throws Exception
    {
        ConstraintViolation<?> one = mock(ConstraintViolation.class);
        when(one.getPropertyPath()).thenReturn(mock(Path.class, "path"));
        when(one.getMessage()).thenReturn("message");

        RuntimeException ex = new RuntimeException("Runtime exception due to cve", new ConstraintViolationException(Collections.singleton(one)));

        ResponseEntity<Object> responseEntity = handler.handleAllUnknown(ex, webRequest);
        assertResponseEntity(responseEntity, HttpStatus.BAD_REQUEST, "Constraint violation.", 1);
        assertResponseBodyError(responseEntity, "path", "message");
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

        assertResponseEntity(responseEntity, HttpStatus.BAD_REQUEST, "Validation error.", 2);
        assertResponseBodyError(responseEntity, "", "object error message");
        assertResponseBodyError(responseEntity, "field name", "field error message");
        verify(messageSource).getMessage(objectError, LocaleContextHolder.getLocale());
        verify(messageSource).getMessage(fieldError, LocaleContextHolder.getLocale());
    }

    @Test
    public void shouldHandleAllUnknown() throws Exception
    {
        RuntimeException ex = new RuntimeException("Runtime exception");

        ResponseEntity<Object> responseEntity = handler.handleAllUnknown(ex, webRequest);

        assertResponseEntity(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR, "Runtime exception", 0);
        verify(webRequest).setAttribute("javax.servlet.error.exception", ex, WebRequest.SCOPE_REQUEST);
    }

    private void assertResponseEntity(ResponseEntity<Object> responseEntity, HttpStatus status, String message, int errors)
    {
        assertThat(responseEntity.getStatusCode(), is(status));
        assertThat(responseEntity.getHeaders().size(), is(0));
        assertThat(responseEntity.getBody(), hasProperty("message", is(message)));
        assertThat(responseEntity.getBody(), hasProperty("errors", hasSize(errors)));
    }

    private void assertResponseBodyError(ResponseEntity<Object> responseEntity, String property, String message)
    {
        assertThat(responseEntity.getBody(), hasProperty("errors", hasItem(hasProperty("field", is(property)))));
        assertThat(responseEntity.getBody(), hasProperty("errors", hasItem(hasProperty("message", is(message)))));
    }
}
