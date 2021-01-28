package com.web.spirder.demo.dao.repository;

import com.idaoben.web.common.dao.BaseRepository;
import com.web.spirder.demo.dao.entity.Admin;
import com.web.spirder.demo.dao.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminRepository extends BaseRepository<Admin, Long>  {
    Admin findByUsername(String username);

    @EntityGraph(value = "Admin.detailed", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select admin from Admin admin where admin.username = ?1")
    Admin findByUsernameWithRole(String username);

    @Query("select count(admin.id) = 0 from Admin admin where admin.username = ?1")
    boolean checkIfUsernameIsUnique(String username);

    @Query("select count(admin.id) = 0 from Admin admin where admin.username = ?1 and admin.id <> ?2")
    boolean checkIfUsernameIsUniqueExcludingAdminId(String username, long id);

    @Query("select admin from Admin admin where ?1 member of admin.roles")
    List<Admin> findByRole(Role role);

}
