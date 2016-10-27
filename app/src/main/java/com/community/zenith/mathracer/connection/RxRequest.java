package com.community.zenith.mathracer.connection;

/**
 * Created by Alex on 30/09/2016.
 */
public class RxRequest{

    public RxType type;
    private Long [] longs;
    private Double [] doubles;
    private String [] strings;
    private Integer [] ints;
    private Boolean [] booleans;
    private Object object;

    public RxRequest(RxType type) {
        this.type = type;
    }

    //builder methods for setting properties
    public RxRequest longs(Long...IDs){this.longs = IDs; return this; }
    public RxRequest object(Object object){this.object = object; return this; }
    public RxRequest doubles(Double...texts){this.doubles = texts; return this; }
    public RxRequest strings(String...texts){this.strings = texts; return this; }
    public RxRequest ints(Integer...numbers){this.ints = numbers; return this; }
    public RxRequest booleans(Boolean...bools){this.booleans = bools; return this; }

    public RxType getType(){
        return type;
    }

    public Long getLongs(int index) {
        return longs[index];
    }

    public Double getDoubles(int index) {
        return doubles[index];
    }

    public String getStrings(int index) {
        return strings[index];
    }

    public Integer getInts(int index) {
        return ints[index];
    }

    public Boolean getBooleans(int index) {
        return booleans[index];
    }

    public Object getObject() {
        return object;
    }

}
