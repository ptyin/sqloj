package net.ptyin.sqloj.user.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;


@RequestMapping("/user/{userUuid}")
@Controller
public class UserController
{

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    @GetMapping("/course-list")
    public List<?> getCourseList(@PathVariable UUID userUuid)
    {
        // TODO
        return null;
    }
}
