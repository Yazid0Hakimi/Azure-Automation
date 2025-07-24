package org.exemple.virementservice.feign;

import org.exemple.virementservice.dtos.EmployeeProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", path = "/api/employees")
public interface EmployeeProfileClient {

    @GetMapping("/{id}")
    EmployeeProfileDTO getEmployeeById(@PathVariable("id") Long id);

    @GetMapping("/{id}/exists")
    Boolean checkEmployeeExists(@PathVariable("id") Long id);
}