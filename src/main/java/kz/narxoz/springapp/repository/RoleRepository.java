package kz.narxoz.springapp.repository;

import kz.narxoz.springapp.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository <Roles, Long>{
    Roles findByRole(String role);
}
