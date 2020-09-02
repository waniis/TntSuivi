package com.mycompany.myapp.domain;

import java.util.List;

import com.mycompany.myapp.domain.payload.ReponseRequest;

public class PatientQuestionnaireRequest {

    private Long id;

    private List<ReponseRequest> listReponse;

    public Long getId() {
        return id;
    }

    public List<ReponseRequest> getListReponse() {
        return listReponse;
    }

    public void setListReponse(List<ReponseRequest> listReponse) {
        this.listReponse = listReponse;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
}
