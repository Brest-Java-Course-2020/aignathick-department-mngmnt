package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
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

    @Captor
    private ArgumentCaptor<RowMapper<Department>> mapper; // перехватчик

//    @BeforeEach
//    void before(){
//        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
//        departmentDao = new DepartmentJdbcDaoImpl(namedParameterJdbcTemplate);
//    }

    @Test
    public void getDepartments() throws SQLException {
        //не пустая коллекция
        int id = 5;
        String name = "name";
        Department department = new Department();
        ResultSet rs = mock(ResultSet.class); // заглушка
        String sql = "Select";
        ReflectionTestUtils.setField(departmentDao, "selectSql", sql);

        //Arrays.asList(department) 1й вариань
        //Collections.singletonList(department) 2й ва
        when(namedParameterJdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(Collections.singletonList(department));
        when(rs.getInt(anyString())).thenReturn(5);
        when(rs.getString(anyString())).thenReturn("name");

        List<Department> departments = departmentDao.getDepartments();
        assertNotNull(departments);
        assertEquals(1, departments.size());//проверка на пустую коллекцию
        Department dep = departments.get(0);
        assertSame(department, dep);

        verify(namedParameterJdbcTemplate).query(eq(sql), mapper.capture());

        RowMapper<Department> rowMapper = mapper.getValue();
        assertNotNull(rowMapper);
        Department result = rowMapper.mapRow(rs, 0);
        assertNotNull(result);
        assertEquals(id, result.getDepartmentId().intValue());
        assertEquals(name, result.getDepartmentName());
    }

    @Test
    public void getDepartmentById() {

    }

    @Test
    public void addDepartment() {

    }

    @Test
    public void updateDepartment() {

    }

    @Test
    public void deleteDepartment() {

    }

}