package com.example.finalproject.items;

import java.util.List;

public class OpenAIResponse {

    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }

        public class Message {
            private String role;
            private String content;

            public String getContent() {
                return content;
            }
        }
    }
}
