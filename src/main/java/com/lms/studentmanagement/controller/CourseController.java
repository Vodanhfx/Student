package com.lms.studentmanagement.controller;


import com.lms.studentmanagement.dto.CourseDTO;
import com.lms.studentmanagement.mapper.CourseMapper;
import com.lms.studentmanagement.model.Course;
import com.lms.studentmanagement.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Course API", description = "Endpoints for course management")
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(
            summary = "Create a new course",
            description = "Create a new course. Only admins are allowed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Course createCourse(
            @RequestBody Course course,
            @Parameter(description = "ID of the admin creating the course") @RequestParam Long adminId
    ) {
        return courseService.createCourse(course, adminId);
    }

    @Operation(
            summary = "Update existing course",
            description = "Update an existing course by id. Only admins are allowed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Course updateCourse(
            @Parameter(description = "ID of the course to update") @PathVariable Long id,
            @RequestBody Course course,
            @Parameter(description = "ID of the admin updating the course") @RequestParam Long adminId
    ) {
        return courseService.updateCourse(id, course, adminId);
    }

    @Operation(
            summary = "Delete a course",
            description = "Delete a course by id. Only admins are allowed."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCourse(
            @Parameter(description = "ID of the course to delete") @PathVariable Long id,
            @Parameter(description = "ID of the admin deleting the course") @RequestParam Long adminId
    ) {
        courseService.deleteCourse(id, adminId);
    }

    @Operation(
            summary = "Get course by id",
            description = "Get course details by course id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the course"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public CourseDTO getCourseById(
            @Parameter(description = "ID of the course to retrieve") @PathVariable Long id
    ) {
        return CourseMapper.toCourseDTO(courseService.getCourseById(id));
    }

    @Operation(
            summary = "Get all courses",
            description = "Retrieve all available courses."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of all courses")
    })
    @GetMapping
    public List<CourseDTO> getAllCourses() {
        return courseService.getAllCourses().stream()
                .map(CourseMapper::toCourseDTO)
                .collect(Collectors.toList());
    }
}