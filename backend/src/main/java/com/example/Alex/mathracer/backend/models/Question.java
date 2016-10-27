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
public class Question {

    @Getter @Setter @Id Long ID;
    @Parent Key<Race> race;
    @Getter @Setter String question;
    @Getter @Setter int correct;
    @Getter @Setter int submitted;

    public void setRace(Race r){
        this.race = Key.create(r);
    }

}
