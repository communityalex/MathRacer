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
public class Student {

    @Id @Getter @Setter Long ID;
    @Getter @Setter String identifier;
    @Parent Key<Teacher> teacher;
    @Getter @Setter String firstName;
    @Getter @Setter String lastName;
    @Getter @Setter String lastRaceTime;

    public void setTeacher(Teacher t){
        this.teacher = Key.create(t);
    }


}
