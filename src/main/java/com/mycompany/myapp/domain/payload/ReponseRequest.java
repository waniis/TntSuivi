package com.mycompany.myapp.domain.payload;

import java.util.List;

public class ReponseRequest {

    private List<String> choixId;
    private String questionId;
    private String contenu;

    public List<String> getChoixId() {
        return choixId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setChoixId(List<String> choixId) {
        this.choixId = choixId;
    }


    
}