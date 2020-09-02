package com.mycompany.myapp.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class QuestionnaireGroupeDepatientRequest  implements Serializable {
  private static final long serialVersionUID = 1L; 

    @NotBlank
    private String groupeDePatient;

    @NotBlank
    private String questionnaire;

    public QuestionnaireGroupeDepatientRequest(){

    }
    public QuestionnaireGroupeDepatientRequest(String groupeDePatient ,  String questionnaire) {

        this.groupeDePatient = groupeDePatient;
        this.questionnaire = questionnaire;
    }

    public String getQuestionnaire() {
        return questionnaire;
    }
    
    public void setQuestionnaire(String questionnaire) {
        this.questionnaire = questionnaire;
    }


    
    public String getGroupeDePatient() {
        return groupeDePatient;
    }
    
    public void setGroupeDePatient(String groupeDePatient) {
        this.groupeDePatient = groupeDePatient;
    }


  

    
}

