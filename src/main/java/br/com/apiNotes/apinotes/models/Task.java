package br.com.apiNotes.apinotes.models;

import br.com.apiNotes.apinotes.dtos.AddTask;
import br.com.apiNotes.apinotes.dtos.UpdateTask;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String description;
    private Boolean archived;
    private String emailuser;

    public Task(AddTask newTask, String emailUser) {
        title = newTask.title();
        description = newTask.description();
        archived = false;
        emailuser = newTask.emailUser();
    }

    public void UpdateTask(UpdateTask taskUpdated) {
            if(taskUpdated.title() != null) {
                title = taskUpdated.title();
            }
            if(taskUpdated.description() != null) {
                description = taskUpdated.description();
            }
    }
}

