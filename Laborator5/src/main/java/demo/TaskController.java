package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskService service;

    @GetMapping
    public ResponseEntity<List<TaskModel>> getTasks (@RequestParam(required = false) String title,
                                                     @RequestParam(required = false) String description,
                                                     @RequestParam(required = false) String assignedTo,
                                                     @RequestParam(required = false) TaskModel.TaskStatus status,
                                                     @RequestParam(required = false) TaskModel.TaskSeverity severity) {
        List<TaskModel> tasks = service.getTasks(title, description, assignedTo, status, severity);
        return tasks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tasks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable String id) {
        Optional<TaskModel> task = service.getTask(id);
        return task.isPresent() ? ResponseEntity.ok(task.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Void> addTask(@RequestBody TaskModel task) {
        try {
            service.addTask(task);
            URI uri = WebMvcLinkBuilder.linkTo(getClass()).slash(task.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTask(@PathVariable String id, @RequestBody TaskModel task) {
        try {
            if (service.updateTask(id, task)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchTask(@PathVariable String id, @RequestBody TaskModel task) {
        try {
            if (service.patchTask(id, task)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        try {
            if (service.deleteTask(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
