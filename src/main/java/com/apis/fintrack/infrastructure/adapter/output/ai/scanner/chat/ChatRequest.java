package com.apis.fintrack.infrastructure.adapter.output.ai.scanner.chat;

public record ChatRequest(Byte[] file, String message) {

    public String createMessage() {
        return this.message + "\n" +
                "Attached file size: " + (file != null ? file.length : 0) + " bytes." +
                "Please analyze the file and provide the necessary information.";
    }

    public static ChatRequest of(Byte[] file, String message) {
        return new ChatRequest(file, message);
    }


}
