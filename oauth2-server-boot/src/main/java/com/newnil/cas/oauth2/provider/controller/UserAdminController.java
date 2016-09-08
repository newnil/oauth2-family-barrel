package com.newnil.cas.oauth2.provider.controller;

import com.newnil.cas.oauth2.provider.dao.entity.UserEntity;
import com.newnil.cas.oauth2.provider.dao.entity.UserRoleXRef;
import com.newnil.cas.oauth2.provider.dao.repository.RoleRepository;
import com.newnil.cas.oauth2.provider.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserAdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String listAllUsers(Model model, Pageable pageable) {

        model.addAttribute("users", userRepository.findAll(pageable));
        model.addAttribute("roles", roleRepository.findAll());
        return "users/users";
    }

    private static final Pattern USER_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final Pattern PASSWORD_WORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,}$");

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String createUser(@RequestParam("username") String username, @RequestParam("password") String password,
                             @RequestParam("password-confirmation") String passwordConfirmation, @RequestParam(name = "roles", defaultValue = "") List<String> roles, RedirectAttributes attributes) {

        if (userRepository.findOneByUsername(username).isPresent()) {
            attributes.addFlashAttribute("dangerMessages", Collections.singletonList("用户名 " + username + " 已被注册"));
        }

        if (!USER_NAME_PATTERN.matcher(username).matches()) {
            attributes.addFlashAttribute("dangerMessages", Collections.singletonList("用户名 " + username + " 含有非法字符。（只能使用[a-zA-Z0-9_]）"));
        }

        if (!PASSWORD_WORD_PATTERN.matcher(password).matches()) {
            attributes.addFlashAttribute("dangerMessages", Collections.singletonList("密码含有非法字符。（只能使用[a-zA-Z0-9]，至少6位）"));
        }

        if (!password.equals(passwordConfirmation)) {
            attributes.addFlashAttribute("dangerMessages", Collections.singletonList("重复密码不正确"));
        }

        if (roles.isEmpty()) {
            attributes.addFlashAttribute("dangerMessages", Collections.singletonList("至少选择一个角色。"));
        } else {
            roles.forEach(role -> roleRepository.findOneByName(role).orElseGet(() -> {
                attributes.addFlashAttribute("dangerMessages", Collections.singletonList("角色 " + role + " 不存在。"));
                return null;
            }));
        }


        if (!attributes.getFlashAttributes().isEmpty()) {
            attributes.addFlashAttribute("username", username);

        } else {

            UserEntity userEntity = UserEntity.builder().username(username).password(passwordEncoder.encode(password)).build();
            userEntity.setRoles(roles.stream().map(
                    role -> UserRoleXRef.builder().user(userEntity).role(roleRepository.findOneByName(role).<RuntimeException>orElseThrow(() -> new RuntimeException("角色 " + role + " 不存在。"))).build()
            ).collect(Collectors.toList()));

            userRepository.save(userEntity);
            attributes.addFlashAttribute("successMessages", Collections.singletonList("用户 " + username + " 创建成功。"));

        }

        return "redirect:/users";
    }

}
