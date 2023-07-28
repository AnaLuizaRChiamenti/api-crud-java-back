package br.com.apiNotes.apinotes.controllers;

import br.com.apiNotes.apinotes.dataBase.DataBase;
import br.com.apiNotes.apinotes.dtos.*;
import br.com.apiNotes.apinotes.models.Task;
import br.com.apiNotes.apinotes.repositories.UserRepository;
import br.com.apiNotes.apinotes.repositories.UserTaskRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tasks")
@NoArgsConstructor
public class TaskController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTaskRepository userTaskRepository;

    @PostMapping("/{email}")
    @Transactional
    public ResponseEntity addTask(@PathVariable String email, @RequestBody @Valid AddTask newTask, String emailuser){
//        var user = userRepository.getReferenceByEmail(email);
//
//        if(user == null) {
//            return ResponseEntity.badRequest().body(new ErrorData("Usuário não localizado."));
//        }
//         user.getTasks().add(new Task(newTask));
//        userTaskRepository.save(new Task(newTask));
//        return ResponseEntity.ok().body(newTask);

        var optionalUser = userRepository.findById(newTask.emailuser());

        if(optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorData("Usuário não localizado."));
        }

        var user = optionalUser.get();

        var task = new Task(newTask, user.getEmail());
        userTaskRepository.save(task);
        return ResponseEntity.ok().body(newTask);
    }

    @GetMapping("/{email}")
    public ResponseEntity getTasks(@PathVariable String email, @RequestParam(required = false) String title, @RequestParam(required = false) boolean archived) {
        var checkUser = userRepository.getReferenceByEmail(email);
//        if(checkUser.isEmpty()){
//            return ResponseEntity.badRequest().body(new ErrorData("Usuário não encontrado!"));
//        }


        var tasks = checkUser.getTasks();

        return  ResponseEntity.ok().body(tasks.stream().map(TasksDetail::new).toList());
    }


    @DeleteMapping ("/{email}/{idTask}")
    public ResponseEntity deleteTask(@PathVariable String email, @PathVariable UUID idTask){
        var user = userRepository.getReferenceByEmail(email);

        var task = userTaskRepository.findById(idTask);

        if(task == null){
            return ResponseEntity.badRequest().body(new ErrorData("Recado não encontrado!"));
        }

        userTaskRepository.delete(task.get());

        return ResponseEntity.ok().body(user.getTasks());
    }

    @PutMapping ("/{email}/{idTask}")
    public ResponseEntity updateTask(@PathVariable String email, @PathVariable UUID idTask, @RequestBody UpdateTask taskUpdated ){
        var optionalUser = userRepository.findById(email);
        var user = optionalUser.get();

    var taskOptional = user.getTasks().stream().filter(t -> t.getId().equals(idTask)).findAny();



        var task = taskOptional.get();
        task.UpdateTask(taskUpdated);
        userTaskRepository.save(task);


//        if(task.isEmpty()){
//            return ResponseEntity.badRequest().body(new ErrorData("Recado não encontrado!"));
//        }


        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}/{idTask}/archived")
    public ResponseEntity archivedTask(@PathVariable String email, @PathVariable UUID idTask) {
        try {
            var findtask = userTaskRepository.findById(idTask);

            var task = findtask.get();

            var archived = task.getArchived();
            task.setArchived(!archived);
            userTaskRepository.save(task);

            return ResponseEntity.ok().body(task.getArchived());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorData("Task não encontrada"));
        }
    }
}
