/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Alex.mathracer.backend.endpoints;

import com.example.Alex.mathracer.backend.RacerObjectifyService;
import com.example.Alex.mathracer.backend.models.Question;
import com.example.Alex.mathracer.backend.models.Race;
import com.example.Alex.mathracer.backend.models.Student;
import com.example.Alex.mathracer.backend.models.Teacher;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Work;

import java.util.List;
import java.util.logging.Logger;

import static com.example.Alex.mathracer.backend.RacerObjectifyService.ofy;

import javax.annotation.Nullable;
import javax.inject.Named;

@Api(
  name = "mathRacerApi",
  version = "v1",
  namespace = @ApiNamespace(
    ownerDomain = "backend.mathracer.Alex.example.com",
    ownerName = "backend.mathracer.Alex.example.com",
    packagePath=""
  )
)
public class StandardEndpoint {

    protected static final Logger log = Logger.getLogger(StandardEndpoint.class.getName());

    @ApiMethod(name = "getStudents", path = "teacher/{teacherID}/students")
    public List<Student> getStudents(@Named("teacherID") Long teacherID) {
        List<Student> students = ofy().load().type(Student.class).ancestor(Key.create(Teacher.class, teacherID)).list();
        log.severe(students.toString());
        return students;
    }

    @ApiMethod(name = "getRaces", path = "teacher/{teacherID}/student/{studentID}/races")
    public List<Race> getRaces(@Named("teacherID") Long teacherID, @Named("studentID") Long studentID) {
        return ofy().load().type(Race.class).ancestor(Key.create(Key.create(Teacher.class, teacherID), Student.class, studentID)).list();
    }

    @ApiMethod(name = "getQuestions", path = "teacher/{teacherID}/student/{studentID}/race/{raceID}/questions")
    public List<Question> getQuestions(@Named("teacherID") Long teacherID, @Named("studentID") Long studentID, @Named("raceID") Long raceID) {
        return ofy().load().type(Question.class).ancestor(Key.create(Key.create(Key.create(Teacher.class, teacherID), Student.class, studentID), Race.class, raceID)).list();
    }

    @ApiMethod(name = "insertTeacher", path = "teacher")
    public Teacher insertTeacher(Teacher teacher){
        return ofy().load().key(ofy().save().entity(teacher).now()).now();
    }

    @ApiMethod(name = "insertStudent", path = "teacher/{teacherID}/student")
    public Student insertStudent(@Named("teacherID") Long teacherID, Student s){
        RacerObjectifyService service = new RacerObjectifyService();
        s.setTeacher(ofy().load().key(Key.create(Teacher.class, teacherID)).now());
        return ofy().load().key(ofy().save().entity(s).now()).now();
    }

    @ApiMethod(name = "loginTeacher", path = "loginTeacher")
    public Teacher loginTeacher(@Named("id") String id, @Named("password") String password){
        @Nullable final Teacher teacher = ofy().load().type(Teacher.class).filter("identifier", id).first().now();
        if (teacher != null && teacher.getPassword().equals(password)){
            return teacher;
        }
        return null;
    }

    @ApiMethod(name = "addRaceToStudent", path = "teacher/{teacherID}/student/{studentID}/race")
    public Race addRaceToStudent(@Named("teacherID") Long teacherID, @Named("studentID") Long studentID, Race race){
        RacerObjectifyService service = new RacerObjectifyService();
        race.setStudent(ofy().load().key(Key.create(Key.create(Teacher.class, teacherID), Student.class, studentID)).now());
        return ofy().load().key(ofy().save().entity(race).now()).now();
    }

    @ApiMethod(name = "addQuestionToRace", path = "teacher/{teacherID}/student/{studentID}/race/{raceID}/questioon")
    public Question addQuestionToRace(@Named("teacherID") Long teacherID, @Named("studentID") Long studentID,
                                      @Named("raceID") Long raceID, final Question question){
        RacerObjectifyService service = new RacerObjectifyService();
        final Race race = ofy().load().key(Key.create(Key.create(Key.create(Teacher.class, teacherID), Student.class, studentID), Race.class, raceID)).now();
        ofy().transact(new Work<Race>() {
            @Override
            public Race run() {
                if (question.getCorrect() == question.getSubmitted()){
                    race.addCorrectQuestion();
                }
                ofy().save().entity(race).now();
                return race;
            }
        });
        question.setRace(race);
        return ofy().load().key(ofy().save().entity(question).now()).now();
    }


}
