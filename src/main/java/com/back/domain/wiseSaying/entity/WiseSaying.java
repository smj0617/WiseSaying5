package com.back.domain.wiseSaying.entity;

public class WiseSaying {

    private int id;
    private String sentence;
    private String author;

    public WiseSaying(String sentence, String author) {
        this.sentence = sentence;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // 아직 저장소에 저장되지 않아 번호가 없는 상태
    public boolean isNew() {
        return id == 0;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
