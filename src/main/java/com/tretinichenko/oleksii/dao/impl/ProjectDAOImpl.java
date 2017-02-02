package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.ProjectDAO;
import com.tretinichenko.oleksii.entity.Project;

@Service
@Transactional
public class ProjectDAOImpl extends JdbcDaoSupport implements ProjectDAO {
	
	private static final String SELECT_ALL_PROJECTS = 
			"SELECT * FROM Project";
	
	private static final String SELECT_PROJECT_BY_ID = 
			"SELECT * FROM Project WHERE id = ?";
	
	private static final String INSERT_PROJECT = 
			"INSERT INTO Project(id, name, startDate, endDate, "
			+ "projectManagerId, company, customer) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String UPDATE_PROJECT = 
			"UPDATE Project SET name = ?, startDate = ?, endDate = ?, "
			+ "projectManagerId = ?, company = ?, customer = ? "
			+ "WHERE id = ?";
	
	private static final String DELETE_PROJECT_BY_ID = 
			"DELETE FROM Project WHERE id = ?";
	
	private static final String SELECT_ALL_PROJECTS_BY_MANAGER_ID = 
			"SELECT * FROM Project WHERE projectManagerId = ?";
	
	private static final String DELETE_ALL_PROJECTS_BY_MANAGER_ID = 
			"DELETE FROM Project WHERE projectManagerId = ?";

	@Autowired
	public ProjectDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}
	
	@Override
	public Project findProjectById(int id) {
		try {
			return this.getJdbcTemplate().queryForObject(SELECT_PROJECT_BY_ID,
					new ProjectRowMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void saveProject(Project project) {
		this.getJdbcTemplate().update(INSERT_PROJECT, project.getId(),
				project.getName(), project.getStartDate(), project.getEndDate(),
				project.getProjectManagerId(), project.getCompany(),
				project.getCustomer()); 
	}

	@Override
	public void updateProject(Project project) {
		this.getJdbcTemplate().update(UPDATE_PROJECT, project.getName(),
				project.getStartDate(), project.getEndDate(),
				project.getProjectManagerId(), project.getCompany(), 
				project.getCustomer(), project.getId());
	}

	@Override
	public void deleteProjectById(int projectId) {
		this.getJdbcTemplate().update(DELETE_PROJECT_BY_ID, projectId);
	}

	@Override
	public List<Project> listAllProjects() {
		return this.getJdbcTemplate().query(SELECT_ALL_PROJECTS,
				new ProjectRowMapper());
	}

	private static final class ProjectRowMapper implements RowMapper<Project> {
		public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int projectManagerId = rs.getInt("projectManagerId");
			Date startDate = rs.getTimestamp("startDate");
			Date endDate = rs.getTimestamp("endDate");
			String name = rs.getString("name");
			String company = rs.getString("company");
			String customer = rs.getString("customer");
			return new Project(id, name, startDate, endDate,
					projectManagerId, company, customer);
		}
	}
}
