package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public List<TaskModel> getTasks(String title, String description, String assignedTo, TaskModel.TaskStatus status, TaskModel.TaskSeverity severity) {
        return repository.findAll().stream()
                .filter(task -> isMatch(task, title, description, assignedTo, status, severity))
                .collect(Collectors.toList());
    }
    private boolean isMatch (TaskModel task, String title, String description, String assignedTo, TaskModel.TaskStatus status, TaskModel.TaskSeverity severity) {
        return (title == null || task.getTitle().toLowerCase().startsWith(title.toLowerCase()))
                && (description == null || task.getDescription().toLowerCase().startsWith(description.toLowerCase()))
                && (assignedTo == null || task.getAssignedTo().toLowerCase().startsWith(assignedTo.toLowerCase()))
                && (status == null || task.getStatus().equals(status))
                && (severity == null || task.getSeverity().equals(severity));
    }

    public Optional<TaskModel> getTask(String id) {
        return repository.findById(id);
    }

    public TaskModel addTask (TaskModel task) throws IOException {
        repository.save(task);
        return task;
    }

    public boolean updateTask (String id, TaskModel task) throws IOException {
        if(repository.findById(id).isPresent()) {
            task.setId(id);
            repository.save(task);
            return true;
        } else {
            return false;
        }
    }

    public boolean patchTask (String id, TaskModel task) throws IOException {
        Optional<TaskModel> existingTask = repository.findById(id);
        if(existingTask.isPresent()) {
            existingTask.get().patch(task);
            repository.save(existingTask.get());
            return true;
        } else {
            return false;
        }
    }
    public boolean deleteTask (String id) throws IOException {
        return repository.deleteById(id);
    }
}
