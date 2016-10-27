package com.community.zenith.mathracer.connection;

import com.example.alex.mathracer.backend.mathRacerApi.MathRacerApi;
import com.example.alex.mathracer.backend.mathRacerApi.model.Question;
import com.example.alex.mathracer.backend.mathRacerApi.model.Race;
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by Alex Connolly on 25/06/2016.
 */
public class RxMaker<T> {

    private static MathRacerApi racerApiService;

    public Observable<T> make(final RxRequest request){
        // the default threading options are also appended
        return Observable.defer(new Func0<Observable<T>>() {
            @Override
            public Observable<T> call() {
                try{
                    return Observable.just(handleRequest(request));
                } catch (IOException e){
                    return null;
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @SuppressWarnings("unchecked")
    private T handleRequest(RxRequest request) throws IOException {
        if (racerApiService == null) {
            racerApiService = new MathRacerApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://mathracer-147011.appspot.com/_ah/api/").build();
        }

        switch (request.getType()) {
            case GET_STUDENTS:
                return (T) racerApiService.getStudents(request.getLongs(0)).execute().getItems();
            case ADD_STUDENT:
                return (T) racerApiService.insertStudent(request.getLongs(0), (Student) request.getObject()).execute();
            case ADD_QUESTION:
                return (T) racerApiService.addQuestionToRace(request.getLongs(0), request.getLongs(1), request.getLongs(2), (Question) request.getObject()).execute();
            case LOGIN_TEACHER:
                return (T) racerApiService.loginTeacher(request.getStrings(0), request.getStrings(1)).execute();
            case ADD_RACE:
                return (T) racerApiService.addRaceToStudent(request.getLongs(0), request.getLongs(1), (Race) request.getObject()).execute();
            case INSERT_TEACHER:
                return (T) racerApiService.insertTeacher((Teacher) request.getObject()).execute();
            case GET_RACES:
                return (T) racerApiService.getRaces(request.getLongs(0), request.getLongs(1)).execute().getItems();
            case GET_QUESTIONS:
                return (T) racerApiService.getQuestions(request.getLongs(0), request.getLongs(1), request.getLongs(2)).execute().getItems();
            case INSERT_STUDENT:
                return (T) racerApiService.insertStudent(request.getLongs(0), (Student) request.getObject()).execute();
            default:
                return null;
        }
    }
}
