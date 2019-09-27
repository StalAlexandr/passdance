package ru.maximumdance.passcontrol;

public class Action {

    public enum Code {
        CREATEPERSON, SEARCHPERSON, STAT
    }

    @FunctionalInterface
    public interface Call {
        void call();
    }


    private Code code;
    private String name;
    private Integer imageId;
    private Integer buttonId;
    private Call call;


    public Action(Action.Code code, String name, Integer imageId, Integer buttonId, Call call) {
        code = code;
        this.name = name;
        this.imageId = imageId;
        this.buttonId = buttonId;
        this.call = call;
    }

    public Action.Code getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getImageId() {
        return imageId;
    }

    public Integer getButtonId() {
        return buttonId;
    }

    public void setButtonId(Integer buttonId) {
        this.buttonId = buttonId;
    }

    public Call getCall() {
        return call;
    }

}
