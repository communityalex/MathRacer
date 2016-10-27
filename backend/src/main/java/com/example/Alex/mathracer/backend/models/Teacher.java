package com.example.Alex.mathracer.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Alex on 19/10/2016.
 */
@Entity
@NoArgsConstructor
public class Teacher {

    @Id @Getter @Setter Long ID;
    @Index @Getter @Setter String identifier;
    @Getter @Setter String password;

}
