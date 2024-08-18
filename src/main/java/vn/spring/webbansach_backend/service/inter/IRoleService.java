package vn.spring.webbansach_backend.service.inter;

import vn.spring.webbansach_backend.entity.Role;

public interface IRoleService {
    Role findRoleByName(String roleName);
}
