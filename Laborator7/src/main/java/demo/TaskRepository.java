package demo;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Collection<TaskModel> findAll();
    Optional<TaskModel> findById (String id);
    void save(TaskModel task) throws IOException;
    boolean deleteById (String id) throws IOException;
}
