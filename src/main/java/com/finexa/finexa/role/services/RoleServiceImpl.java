package com.finexa.finexa.role.services;


import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.finexa.finexa.exceptions.BadRequestException;
import com.finexa.finexa.exceptions.NotFoundException;
import com.finexa.finexa.res.Response;
import com.finexa.finexa.role.entity.Role;
import com.finexa.finexa.role.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PresentationDirection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepo roleRepo;

    @Override
    public Response<Role> createRole(Role roleRequest) {
        if(roleRepo.findByName(roleRequest.getName()).isPresent()){
            throw new BadRequestException("Role already exists");
        }
        Role savedRole = roleRepo.save(roleRequest);
        return Response.<Role>builder().statusCode(HttpStatus.OK.value()).message("Role saved successfully").build();
    }

    @Override
    public Response<Role> updateRole(Role roleRequest) {
        Role role  = roleRepo.findById(roleRequest.getId())
                .orElseThrow(()->new NotFoundException("Role not found"));


        role.setName(roleRequest.getName());

        Role updateRole = roleRepo.save(role);


        return Response.<Role>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(updateRole)
                .build();
    }

    @Override
    public Response<List<Role>> getAllRoles() {

        List<Role> roles = roleRepo.findAll();
        return Response.<List<Role>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Roles retreived Successfully")
                .data(roles)
                .build();
    }


    @Override
    public Response<?> deleteRole(Long id) {
        if(!roleRepo.existsById(id)){
            throw new NotFoundException("Role not found");
        }
        roleRepo.deleteById(id);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role Deleted SuccessFully")
                .build();

    }
}
