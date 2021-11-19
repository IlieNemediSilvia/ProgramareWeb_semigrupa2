package demo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TaskModel implements BaseModel {

    public enum TaskStatus {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }
    public enum TaskSeverity {
        LOW,
        NORMAL,
        HIGH
    }
    private String id;
    @NotBlank(message = "Please provide a task title.")
    private String title;
    private String description;
    @NotBlank(message = "Please assign the task to someone.")
    private String assignedTo;
    @NotNull(message = "Please provide the task's status")
    private TaskStatus status;
    @NotNull(message = "Please provide the task's severity")
    private TaskSeverity severity;

    public TaskModel() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(TaskSeverity severity) {
        this.severity = severity;
    }

    public void update (TaskModel task) {
        if(task != null) {
            title = task.getTitle();
            description = task.getDescription();
            assignedTo = task.getAssignedTo();
            severity = task.getSeverity();
            status = task.getStatus();
        }
    }

    public void patch (TaskModel task) {
        if(task != null) {
            if (task.getTitle() != null) {
                title = task.getTitle();
            }
            if (task.getDescription() != null) {
                description = task.getDescription();
            }
            if (task.getAssignedTo() != null) {
                assignedTo = task.getAssignedTo();
            }
            if (task.getSeverity() != null) {
                severity = task.getSeverity();
            }
            if (task.getStatus() != null) {
                status = task.getStatus();
            }
        }
    }
}
