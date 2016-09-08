package com.newnil.cas.oauth2.provider.controller;

import com.newnil.cas.oauth2.provider.dao.entity.RoleEntity;
import com.newnil.cas.oauth2.provider.dao.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RequestMapping("/roles")
@Controller
public class RoleAdminController {

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String listAllRoles(Model model, Pageable pageable) {

        model.addAttribute("roles", roleRepository.findAll(pageable));
        return "roles/roles";
    }

    private static final Pattern ROLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z_]+$");

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String createRole(@RequestParam("roleName") String roleName, RedirectAttributes attributes) {
        if (ROLE_NAME_PATTERN.matcher(roleName).matches()) {
            if (roleRepository.findOneByName(roleName.toUpperCase()).isPresent()) {
                // error message
                attributes.addFlashAttribute("dangerMessages", Collections.singletonList("角色名 " + roleName + " 已存在。"));
                attributes.addFlashAttribute("roleName", roleName);
            } else {
                roleRepository.save(RoleEntity.builder().name(roleName.toUpperCase()).build());
                // success message
                attributes.addFlashAttribute("successMessages", Collections.singletonList("已成功添加 " + roleName + " 角色。"));
            }

        } else {
            attributes.addFlashAttribute("dangerMessages", Collections.singletonList("角色名 " + roleName + " 含有非法字符。（只能使用[a-zA-Z_]）"));
            attributes.addFlashAttribute("roleName", roleName);
        }
        return "redirect:/roles";
    }

    private static final String[] INVINCIBLE_ROLES = {"ADMIN", "USER"};
    private static final List<String> INVINCIBLE_ROLES_LIST = Arrays.asList(INVINCIBLE_ROLES);

    @RequestMapping(path = "/_remove/{roleName}", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_XHTML_XML_VALUE})
    public String removeRole(@PathVariable("roleName") String roleName, RedirectAttributes attributes) {

        if (INVINCIBLE_ROLES_LIST.contains(roleName.toUpperCase())) {

            attributes.addFlashAttribute("dangerMessages", "该角色不可删除：" + roleName);

        } else {

            roleRepository.findOneByName(roleName.toUpperCase()).map(
                    roleEntity -> {
                        roleRepository.delete(roleEntity);
                        attributes.addFlashAttribute("successMessages", Collections.singletonList("已成功删除 " + roleName + " 角色。"));
                        return roleEntity;
                    }
            ).orElseGet(() -> {
                attributes.addFlashAttribute("warningMessages", Collections.singletonList("没有找到 " + roleName + " 角色。"));
                return null;
            });
        }

        return "redirect:/roles";
    }
}
