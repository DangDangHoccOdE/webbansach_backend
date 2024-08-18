package vn.spring.webbansach_backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.spring.webbansach_backend.dao.RoleRepository;
import vn.spring.webbansach_backend.entity.Role;
import vn.spring.webbansach_backend.service.inter.IRoleService;

@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
