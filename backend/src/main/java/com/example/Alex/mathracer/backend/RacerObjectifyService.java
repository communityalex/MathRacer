package com.example.Alex.mathracer.backend;

import com.example.Alex.mathracer.backend.models.Question;
import com.example.Alex.mathracer.backend.models.Race;
import com.example.Alex.mathracer.backend.models.Student;
import com.example.Alex.mathracer.backend.models.Teacher;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by Alex Connolly on 19/01/2016.
 */
public class RacerObjectifyService<T> {

    public RacerObjectifyService(){

    }

    static {
        factory().register(Student.class);
        factory().register(Teacher.class);
        factory().register(Race.class);
        factory().register(Question.class);
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }
}
