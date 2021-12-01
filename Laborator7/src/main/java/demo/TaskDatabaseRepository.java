package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("database")
public class TaskDatabaseRepository implements TaskRepository{

    private NamedParameterJdbcTemplate template;
    @Autowired
    public void setDataSource(final DataSource dataSource) {
       template = new NamedParameterJdbcTemplate(dataSource);
    }

    public final RowMapper<TaskModel> mapper = new RowMapper<TaskModel>() {
        @Override
        public TaskModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TaskModel(rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("assignedto"),
                    TaskModel.TaskStatus.valueOf(rs.getString("status")),
                    TaskModel.TaskSeverity.valueOf(rs.getString("severity"))
            );
        }
    };

    @Override
    public Collection<TaskModel> findAll() {
        return template.query("select id, title, description, assignedto, status, severity from task", mapper);
    }

    @Override
    public Optional<TaskModel> findById(String id) {
        List<TaskModel> tasks = template.query("select id, title, description, assignedto, status, severity from task where id=:id",
                new MapSqlParameterSource().addValue("id", id), mapper);
        return tasks != null && !tasks.isEmpty() ? Optional.of(tasks.get(0)) : Optional.empty();
    }

    @Override
    public void save(TaskModel task) throws IOException {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", task.getId())
                .addValue("title", task.getTitle())
                .addValue("description", task.getDescription())
                .addValue("assigned", task.getAssignedTo())
                .addValue("status", task.getStatus().name())
                .addValue("severity", task.getSeverity().name());
        int count = template.update("update task set title=:title, description=:description, assignedto=:assigned, " +
                "status=:status, severity=:severity where id=:id", parameters);
        if(count == 0) {
            template.update("insert into task (id, title, description, assignedto, status, severity) " +
                    "values (:id, :title, :description, :assigned, :status, :severity)", parameters);
        }
    }

    @Override
    public boolean deleteById(String id) throws IOException {
        return template.update("delete from task where id=:id", new MapSqlParameterSource().addValue("id", id)) > 0;
    }
}
