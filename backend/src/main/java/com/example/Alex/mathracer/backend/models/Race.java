package com.example.Alex.mathracer.backend.models;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alex on 20/10/2016.
 */
@Entity
@NoArgsConstructor
public class Race {

    @Getter @Setter @Id Long ID;
    @Parent Key<Student> student;
    @Getter @Setter Integer numQuestions;
    @Getter @Setter Integer numCorrect;
    @Getter @Setter String startTime;

    public void setStudent(Student s){
        this.student = Key.create(s);
    }

    public void addCorrectQuestion() {
        numCorrect = numCorrect + 1;
    }
}
