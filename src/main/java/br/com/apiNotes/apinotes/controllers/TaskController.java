package br.com.apiNotes.apinotes.controllers;

import br.com.apiNotes.apinotes.dataBase.DataBase;
import br.com.apiNotes.apinotes.dtos.*;
import br.com.apiNotes.apinotes.models.Task;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tasks")
public class TaskController {

    @PostMapping("/{email}")
    public ResponseEntity addTask(@PathVariable String email, @RequestBody @Valid AddTask newTask){
        var user = DataBase.getUserByEmail(email);

        if(user == null) {
            return ResponseEntity.badRequest().body(new ErrorData("Usuário não localizado."));
        }
        user.getTasks().add(new Task(newTask));
        return ResponseEntity.ok().body(newTask);
    }

    @GetMapping("/{email}")
    public ResponseEntity getTasks(@PathVariable String email, @RequestParam(required = false) String title, @RequestParam(required = false) boolean archived){
        var userTasks = DataBase.getEmail(email).getTasks();

        if(userTasks == null) {
            return ResponseEntity.badRequest().body(new ErrorData("Nenhum recado adicionado."));
        }

        if(userTasks == null) {
            return ResponseEntity.badRequest().body(new ErrorData("Task não localizada."));
        }
        if(title != null) {
            userTasks = userTasks.stream().filter(t -> t.getTitle().contains((title))).toList();
            return ResponseEntity.ok().body(userTasks);
        }

        if(archived) {
            userTasks = userTasks.stream().filter(a -> a.getArchived().equals(true)).toList();
            return ResponseEntity.ok().body(userTasks);
        }

        return ResponseEntity.ok().body(userTasks);
    }

    @DeleteMapping ("/{email}/{idTask}")
    public ResponseEntity deleteTask(@PathVariable String email, @PathVariable UUID idTask){
        var user = DataBase.getUserByEmail(email);

        var taskId = DataBase.getTaskByID(idTask, email);

        if(taskId == null){
            return ResponseEntity.badRequest().body(new ErrorData("Recado não encontrado!"));
        }

        user.getTasks().remove(taskId);

        return ResponseEntity.ok().body(user.getTasks());
    }

    @PutMapping ("/{email}/{idTask}")
    public ResponseEntity updateTask(@PathVariable String email, @PathVariable UUID idTask, @RequestBody UpdateTask taskUpdated ){
        var user = DataBase.getUserByEmail(email);

        var taskUpdateFilter = DataBase.getTaskByID(idTask, email);

        if(taskUpdateFilter == null) {
            return ResponseEntity.badRequest().body(new ErrorData("Recado não encontrado."));
        }

        taskUpdateFilter.UpdateTask(taskUpdated);

        return ResponseEntity.ok().body(user.getTasks());
    }

    @PutMapping("/{email}/{idTask}/archived")
    public ResponseEntity archivedTask(@PathVariable String email, @PathVariable UUID idTask) {
        try {
            var task = DataBase.getTaskByID(idTask, email);

            var archived = task.getArchived();
            task.setArchived(!archived);

            return ResponseEntity.ok().body(task.getArchived());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorData("Task não encontrada"));
        }
    }
}
