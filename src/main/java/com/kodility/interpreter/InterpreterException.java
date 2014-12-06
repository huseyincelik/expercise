package com.kodility.interpreter;

public class InterpreterException extends Exception {

    private InterpreterResult interpreterResult;

    public InterpreterException(InterpreterResult interpreterResult) {
        this.interpreterResult = interpreterResult;
    }

    public InterpreterResult getInterpreterResult() {
        return interpreterResult;
    }

}