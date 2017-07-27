/**
 * Copyright © 2017 Sonicity (info@sonicity.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.sonicity.sha2017.cms.cmshabackend.api.exceptions;

import nl.sonicity.sha2017.cms.cmshabackend.api.models.ErrorDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by hugo on 02/07/2017.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    protected static final String LOGMSG_MAPPING_EXCEPTION = "Mapping Exception {}: {}";

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFound(RuntimeException ex, WebRequest request) {
        ErrorDetail bodyOfResponse = new ErrorDetail(ex.getMessage());
        LOG.debug(LOGMSG_MAPPING_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {ZoneAlreadyClaimedException.class})
    protected ResponseEntity<Object> handleClaimedZone(RuntimeException ex, WebRequest request) {
        ErrorDetail bodyOfResponse = new ErrorDetail(ex.getMessage());
        LOG.debug(LOGMSG_MAPPING_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {ValidationFailedException.class})
    protected ResponseEntity<Object> handleInvalidArgument(RuntimeException ex, WebRequest request) {
        ErrorDetail bodyOfResponse = new ErrorDetail(ex.getMessage());
        LOG.debug(LOGMSG_MAPPING_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Object> handleIAccessDenied(RuntimeException ex, WebRequest request) {
        ErrorDetail bodyOfResponse = new ErrorDetail("Access is denied");
        LOG.debug(LOGMSG_MAPPING_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleDataIntegrityException(RuntimeException ex, WebRequest request) {
        ErrorDetail bodyOfResponse = new ErrorDetail("Unable to persist");
        LOG.debug(LOGMSG_MAPPING_EXCEPTION, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleOther(Exception ex, WebRequest request) {
        ErrorDetail bodyOfResponse = new ErrorDetail("Unhandled exception, please contact operator");
        LOG.error("Unhandled Exception: {}", ex.getMessage(), ex);
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
