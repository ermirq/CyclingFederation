package com.zerogravitysolutions.core.exceptions;

import com.zerogravitysolutions.core.audit.exceptions.InvalidAuditDocumentException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryAgeException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryDeletionException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryDuplicateException;
import com.zerogravitysolutions.core.categories.exceptions.CategoryNotFoundException;
import com.zerogravitysolutions.core.clubs.exceptions.*;
import com.zerogravitysolutions.core.cyclists.exceptions.*;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineDeletionException;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineDuplicateException;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineNotFoundException;
import com.zerogravitysolutions.core.disciplines.exceptions.DisciplineUpdateException;
import com.zerogravitysolutions.core.exceptions.database.RelationshipNotFoundException;
import com.zerogravitysolutions.core.members.exceptions.*;
import com.zerogravitysolutions.core.races.exceptions.*;
import com.zerogravitysolutions.core.results.exceptions.ResultNotFoundException;
import com.zerogravitysolutions.core.users.exceptions.UserDeletionException;
import com.zerogravitysolutions.core.users.exceptions.UserNotFoundException;
import com.zerogravitysolutions.core.users.exceptions.UserUpdateException;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersDeletionException;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersDuplicateException;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersNotFoundException;
import com.zerogravitysolutions.core.volunteers.exceptions.VolunteersUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Clubs
    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClubNotFoundException(
        final ClubNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DuplicateClubException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateClubException(
        final DuplicateClubException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidClubDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidClubDataException(
        final InvalidClubDataException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ClubDeletionException.class)
    public ResponseEntity<ErrorResponse> handleClubDeletionException(
        final ClubDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ClubUpdateException.class)
    public ResponseEntity<ErrorResponse> handleClubUpdateException(
        final ClubUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ClubMemberLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleClubMemberLimitExceededException(
        final ClubMemberLimitExceededException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    // Cyclists
    @ExceptionHandler(CyclistNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCyclistNotFoundException(
        final CyclistNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CyclistDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleCyclistDuplicateException(
        final CyclistDuplicateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(CyclistAgeRestrictionException.class)
    public ResponseEntity<ErrorResponse> handleCyclistAgeRestrictionException(
        final CyclistAgeRestrictionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(CyclistDeletionException.class)
    public ResponseEntity<ErrorResponse> handleCyclistDeletionException(
        final CyclistDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.GONE, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.GONE
        );
    }

    @ExceptionHandler(CyclistUpdateException.class)
    public ResponseEntity<ErrorResponse> handleCyclistUpdateException(
        final CyclistUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    // Members
    @ExceptionHandler(MemberUpdateException.class)
    public ResponseEntity<ErrorResponse> handleMemberUpdateException(
        final MemberUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MemberDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleMemberDuplicateException(
        final MemberDuplicateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MemberDeletionException.class)
    public ResponseEntity<ErrorResponse> handleMemberDeletionException(
        final MemberDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.GONE, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.GONE
        );
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(
        final MemberNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MemberAgeRestrictionException.class)
    public ResponseEntity<ErrorResponse> handleMemberAgeRestrictionException(
        final MemberAgeRestrictionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    // Disciplines
    @ExceptionHandler(DisciplineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDisciplineNotFoundException(
        final DisciplineNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DisciplineDeletionException.class)
    public ResponseEntity<ErrorResponse> handleDisciplineDeletionException(
        final DisciplineDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.GONE, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.GONE
        );
    }

    @ExceptionHandler(DisciplineUpdateException.class)
    public ResponseEntity<ErrorResponse> handleDisciplineUpdateException(
        final DisciplineUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DisciplineDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDisciplineDuplicateException(
        final DisciplineDuplicateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    // Races
    @ExceptionHandler(RaceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRaceNotFoundException (
            final RaceNotFoundException ex,
            final WebRequest request
    ){
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request,
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RaceDeletionException.class)
    public ResponseEntity<ErrorResponse> handleRaceDeletionException(
        final RaceDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RaceUpdateException.class)
    public ResponseEntity<ErrorResponse> handleRaceUpdateException(
        final RaceUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(SponsorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSponsorNotFoundException (
            final SponsorNotFoundException ex,
            final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request,
                null
            ),
            HttpStatus.NOT_FOUND
    );
    }

    @ExceptionHandler(LogisticsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLogisticsNotFoundException (
            final LogisticsNotFoundException ex,
             final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request,
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(LogoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLogoNotFoundException (
            final LogoNotFoundException ex,
            final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request,
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvalidLogoException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLogoException (
            final InvalidLogoException ex,
            final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request,
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    // Category
    @ExceptionHandler(CategoryDeletionException.class)
    public ResponseEntity<ErrorResponse> handleCategoryDeletionException(
        final CategoryDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(
        final CategoryNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(CategoryDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleCategoryDuplicateException(
        final CategoryDuplicateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(CategoryAgeException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAgeException(
        final CategoryAgeException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    // Volunteers
    @ExceptionHandler(VolunteersNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVolunteersNotFoundException(
        final VolunteersNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(VolunteersUpdateException.class)
    public ResponseEntity<ErrorResponse> handleVolunteersUpdateException(
        final VolunteersUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(VolunteersDeletionException.class)
    public ResponseEntity<ErrorResponse> handleVolunteersDeletionException(
        final VolunteersDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(VolunteersDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleVolunteersDuplicateException(
        final VolunteersDuplicateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.CONFLICT, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.CONFLICT
        );
    }

    // Relationships
    @ExceptionHandler(RelationshipNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRelationshipNotFoundException(
        final RelationshipNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    // Users
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
        final UserNotFoundException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.NOT_FOUND, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserUpdateException.class)
    public ResponseEntity<ErrorResponse> handleUserUpdateException(
        final UserUpdateException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserDeletionException.class)
    public ResponseEntity<ErrorResponse> handleUserDeletionException(
        final UserDeletionException ex, 
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST, 
                ex.getMessage(), 
                request, 
                null
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResultNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResultNotFoundException(
            final ResultNotFoundException ex,
            final WebRequest request
    ){
        return new ResponseEntity<>(
                ErrorResponse.from(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request,
                        null
                ),
                HttpStatus.NOT_FOUND
        );
    }

    // Audits
    @ExceptionHandler(InvalidAuditDocumentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAuditDocumentException(
            final InvalidAuditDocumentException ex,
            final WebRequest request
    ) {
        return new ResponseEntity<>(
                ErrorResponse.from(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getMessage(),
                        request,
                        null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    // Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        final MethodArgumentNotValidException ex,
        final WebRequest request
    ) {
        final Map<String, String> fieldErrors = new HashMap<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                request,
                Map.of("errors", fieldErrors)
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    // Global Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
        final Exception ex,
        final WebRequest request
    ) {
        return new ResponseEntity<>(
            ErrorResponse.from(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request,
                null
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}