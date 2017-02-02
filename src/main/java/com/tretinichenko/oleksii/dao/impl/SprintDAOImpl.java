package com.tretinichenko.oleksii.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tretinichenko.oleksii.dao.SprintDAO;
import com.tretinichenko.oleksii.entity.Sprint;

@Service
@Transactional
public class SprintDAOImpl extends JdbcDaoSupport implements SprintDAO {
	
	private static final String SELECT_ALL_SPRINTS = 
			"SELECT * FROM Sprint";
	
	private static final String SELECT_SPRINT_BY_ID = 
			"SELECT * FROM Sprint WHERE id = ?";
	
	private static final String DELETE_SPRINT_BY_ID = 
			"DELETE FROM Sprint WHERE id = ?";
	
	private static final String INSERT_SPRINT = 
			"INSERT INTO Sprint "
			+ "(id, projectId, name, dependsOn) "
			+ "VALUES ( ?, ?, ?, ? )";
	
	private static final String UPDATE_SPRINT = 
			"UPDATE Sprint SET "
			+ "projectId = ?, name = ?, dependsOn = ? WHERE id = ?";
	
	private static final String SELECT_ALL_SPRINTS_BY_PROJECT_ID =
			"SELECT * FROM Sprint WHERE projectId = ?";
	
	private static final String DELETE_ALL_SPRINTS_BY_PROJECT_ID = 
			"DELETE FROM Sprint WHERE projectId = ?";
	
	@Autowired
	public SprintDAOImpl(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Override
	public Sprint findSprintById(int sprintId) {
		try {
			return this.getJdbcTemplate().queryForObject(SELECT_SPRINT_BY_ID,
					new SprintRowMapper(), sprintId);
		} catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	@Override
	public void saveSprint(Sprint sprint) {
		int dependsOn = sprint.getDependsOn();
		this.getJdbcTemplate().update(INSERT_SPRINT, sprint.getId(), 
				sprint.getProjectId(), sprint.getName(),
				(dependsOn == 0 ? null : dependsOn)); 
	}

	@Override
	public void updateSprint(Sprint sprint) {
		int dependsOn = sprint.getDependsOn();
		this.getJdbcTemplate().update(UPDATE_SPRINT, 
				sprint.getProjectId(), sprint.getName(), 
				(dependsOn == 0 ? null : dependsOn), sprint.getId());
	}

	@Override
	public void deleteSprintById(int sprintId) {
		this.getJdbcTemplate().update(DELETE_SPRINT_BY_ID, sprintId);
	}

	@Override
	public List<Sprint> listAllSprints() {
		return this.getJdbcTemplate().query(SELECT_ALL_SPRINTS, 
				new SprintRowMapper());
	}

	private static final class SprintRowMapper implements RowMapper<Sprint> {
		public Sprint mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			int projectId = rs.getInt("projectId");
			String name = rs.getString("name");
			int dependsOn = rs.getInt("dependsOn");
			return new Sprint(id, projectId, name, dependsOn);
		}
	}
}
