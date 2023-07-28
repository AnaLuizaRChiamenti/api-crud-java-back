package br.com.apiNotes.apinotes.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String email;
    private String password;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "emailuser")
    private List<Task> tasks;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        tasks = new ArrayList<Task>();
    }
}

