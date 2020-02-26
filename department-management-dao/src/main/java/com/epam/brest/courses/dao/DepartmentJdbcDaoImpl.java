package com.epam.brest.courses.dao;

import com.epam.brest.courses.model.Department;
import com.epam.brest.courses.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DepartmentJdbcDaoImpl implements DepartmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentJdbcDaoImpl.class);
    @Value("${department.select}")
    private String selectSql;
    private final String SELECT_BY_ID_QUERY = "SELECT departmentId, departmentName FROM department WHERE departmentId = :id";
    private final String INSERT_QUERY = "insert into department (departmentId, departmentName) values (:departmentId, :departmentName)";
    private final String UPDATE_QUERY = "update department set departmentName = :departmentName where departmentId = :id";
    private final String DELETE_QUERY = "delete from department where departmentId = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DepartmentJdbcDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Department> getDepartments() {
        LOGGER.info("Get all deparments");
        List<Department> departments = namedParameterJdbcTemplate
                .query(selectSql, new DepartmentRowMapper());
        return departments;
    }

    @Override
    public Department getDepartmentById(Integer departmentId) {
        LOGGER.info("Get deparment by id");
        try {
            Department department = namedParameterJdbcTemplate
                    .queryForObject(SELECT_BY_ID_QUERY, new MapSqlParameterSource(
                            "id", departmentId), new DepartmentRowMapper());
            return department;
        } catch (Exception ex) {
            LOGGER.error("Error during getting department with id: " + departmentId);
            return null;
        }
    }

    @Override
    public Department addDepartment(Department department) {
        LOGGER.info("Add deparment");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("departmentId", department.getDepartmentId());
        namedParameters.addValue("departmentName", department.getDepartmentName());
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        int status  = namedParameterJdbcTemplate.update(INSERT_QUERY, namedParameters, generatedKeyHolder);
        department.setDepartmentId(generatedKeyHolder.getKey().intValue());
        if (status!=0){
            LOGGER.info("Department was add with id: " + department.getDepartmentId());
        } else {
            LOGGER.error("Error during add department to db: " + department.getDepartmentName());
        }
        return department;
    }

    @Override
    public void updateDepartment(Department department) {
        LOGGER.info("Update deparment");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", department.getDepartmentId());
        namedParameters.addValue("departmentName", department.getDepartmentName());

        SqlParameterSource sqlParameterSource = namedParameters;
        int status = namedParameterJdbcTemplate.update(UPDATE_QUERY, sqlParameterSource);
        if (status!=0){
            LOGGER.info("Department data was update for id: " + department.getDepartmentId());
        } else {
            LOGGER.error("No department was found for update id: " + department.getDepartmentId());
        }

    }

    @Override
    public void deleteDepartment(Integer departmentId) {
        LOGGER.info("Delete department");
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("id", departmentId);
        int status = namedParameterJdbcTemplate.update(DELETE_QUERY, namedParameters);
        if (status!=0){
            LOGGER.info("Department data was delete for id: " + departmentId);
        } else {
            LOGGER.error("No department was found for update with id: " + departmentId);
        }
    }

    private class DepartmentRowMapper implements RowMapper<Department> {
        @Override
        public Department mapRow(ResultSet resultSet, int i) throws SQLException {
            Department department = new Department();
            department.setDepartmentId(resultSet.getInt("DepartmentId"));
            department.setDepartmentName(resultSet.getString("DepartmentName"));
            return department;
        }
    }

    private class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet resultSet, int i) throws SQLException {
            Employee employee = new Employee();
            employee.setEmployeeId(resultSet.getInt("employeeId"));
            employee.setFirstname(resultSet.getString("firstName"));
            employee.setLastname(resultSet.getString("lastName"));
            employee.setLastname(resultSet.getString("salary"));
            employee.setDepartmentId(resultSet.getInt("departmentId"));
            return employee;
        }
    }
}
