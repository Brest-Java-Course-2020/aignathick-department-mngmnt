package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class DepartmentJdbcDaoImplMockTest {

    @InjectMocks
    private DepartmentJdbcDaoImpl departmentDao;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @AfterEach
    void after(){
        verifyNoMoreInteractions(namedParameterJdbcTemplate);
    }

    @Test
    public void getDepartments() {

        List<Department> departments = departmentDao.getDepartments();
        assertNotNull(departments);
        verify(namedParameterJdbcTemplate).query(anyString(), any(RowMapper.class));

    }

    @Test
    public void getDepartmentById() {
        Department department = departmentDao.getDepartmentById(1);
        assertNotNull(department);
    }

    @Test
    public void addDepartment() {
        Department department = new Department();
        department.setDepartmentName("IT");
        departmentDao.addDepartment(department);
        assertNotNull(department.getDepartmentId());
    }

    @Test
    public void updateDepartment() {
        Department department = new Department();
        department.setDepartmentName("UPDATE");
        departmentDao.addDepartment(department);
        department.setDepartmentName("UPDATE2");
        departmentDao.updateDepartment(department);
        departmentDao.getDepartmentById(department.getDepartmentId());
        assertTrue(department.getDepartmentName().equals("UPDATE2"));
    }

    @Test
    public void deleteDepartment() {
        Department department = new Department();
        department.setDepartmentName("Delete");
        departmentDao.addDepartment(department);
        Integer id = department.getDepartmentId();
        departmentDao.deleteDepartment(department.getDepartmentId());
        Department department2 = departmentDao.getDepartmentById(id);
        assertNull(department2);
    }

}