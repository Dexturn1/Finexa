package com.finexa.finexa.role.services;

import com.finexa.finexa.res.Response;
import com.finexa.finexa.role.entity.Role;

import java.util.List;

public interface RoleService {

    Response<Role> createRole(Role roleRequest);

    Response<Role> updateRole(Role roleRequest);

    Response<List<Role>> getAllRoles();

    Response<?> deleteRole(Long id);


}
