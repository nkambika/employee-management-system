package com.myproject.controllers;

import com.myproject.dtos.LoginRequestDto;
import com.myproject.dtos.UserRegistrationDto;
import com.myproject.models.Role;
import com.myproject.services.RoleService;
import com.myproject.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    public UserController(UserService userService, RoleService roleService){
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String showUserRegistrationForm(Model model){
        model.addAttribute("user", new UserRegistrationDto());
        model.addAttribute("roles", roleService.getAllRoles().stream().map(Role::getName));
        model.addAttribute("loginRequest", new LoginRequestDto());
        return "user-registration-form";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserRegistrationDto userRegistrationDto, Model model){
        userService.save(userRegistrationDto);
        model.addAttribute("message","You have successfully registered to our awesome app");
        return "redirect:/?success";
    }
    @GetMapping("/login-with-google")
    public String oauthGoogleLogin(){
        return "redirect:/dashboard";
    }
    @GetMapping("/login-with-facebook")
    public String oauthFacebookLogin(){
        return "redirect:/dashboard";
    }
}
