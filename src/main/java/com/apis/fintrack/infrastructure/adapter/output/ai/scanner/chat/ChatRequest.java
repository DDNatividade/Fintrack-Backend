package com.apis.fintrack.application.ai.scannging.model;

public class ChatRequest {

    private final String message;
    private final Byte[] file;

    private ChatRequest(Byte[] file, String message) {
        this.message = message;
        this.file = file;
    }

    public String createMessage() {
        return this.message + "\n" +
               "Attached file size: " + (file != null ? file.length : 0) + " bytes."+
               "Please analyze the file and provide the necessary information.";
    }

    public static ChatRequest of(Byte[] file, String message) {
        return new ChatRequest(file, message);
    }



}
