import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput ,AvField,AvRadioGroup,AvRadio,AvCheckboxGroup,AvCheckbox  } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, size } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { getEntities as getQuestionnaires } from 'app/entities/questionnaire/questionnaire.reducer';
import { getEntity, updateEntity, createEntity, reset } from './patient-questionnaire.reducer';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { getEntities as getGroupeDePatient } from 'app/entities/groupe-de-patient/groupe-de-patient.reducer';
import { text } from '@fortawesome/fontawesome-svg-core';
import { QuestionState } from '../question/question.reducer';
import { IQuestion } from 'app/shared/model/question.model';
import {RadioGroup, Radio} from 'react-radio-group'


export interface IPatientQuestionnaireReponseProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PatientQuestionnaireReponse = (props: IPatientQuestionnaireReponseProps) => {
  const [patientId, setPatientId] = useState('0');
  const [questionnaireId, setQuestionnaireId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  
 
  const { patientQuestionnaireEntity,groupeDePatient, patients, questionnaires, loading, updating } = props;

  const  [listReponse, setInputFields] = useState([

  ])


  const handleInputChangeText = (index, event) => {
    const valuess = [...listReponse];
     
      valuess[index].contenu = event.target.value; 
      
    setInputFields(valuess);
  };
 
  const handleInputChangeSelect = (index, event) => {
    const valuess = [...listReponse];
      valuess[index].contenu = '';
      valuess[index].choixId = [ event.target.value]; 
     
    setInputFields(valuess);
  };
 
  const handleInputChangeRadio = (index, event) => {
    const valuess = [...listReponse];
 
      valuess[index].choixId = [event.target.value]; 
 
    setInputFields(valuess);
  };
 

  const handleInputChangeCheckBox = (index, event) => {
    const valuess = [...listReponse];
    if (event.target.checked) {
     valuess[index].choixId.push (event.target.value);
     setInputFields(valuess);}
      else {
        valuess[index].choixId.splice(valuess[index].choixId.indexOf(event.target.value),1);
        setInputFields(valuess);
      }
   
  };
 

 


  const handleClose = () => {
    props.history.push('/patient-questionnaire');
  };
  const RadioButton = (variable) => (
    <label>
      <Radio
        name={variable.name}
        value={variable.value}
      />
      {' '+variable.name }
    </label>
  );
  const func = (ques: IQuestion , index : number) => {
 
   
        
      
     
      
    switch(ques.typeQuestion) {
      

      
      case "Text": 
        return  (
        <AvGroup>
         <AvField
        type="text"
        className="form-control"
        name="contenu"    onChange={event => handleInputChangeText(index, event)}
        validate={{
          maxLength: {
            value: 50,
           
          }
        }}
      /> </AvGroup> );
      break;
      case "Select": return ( 
      <AvGroup>
      <AvInput
        id="patient-questionnaire-questionnaire"
        type="select" 
        className="form-control" onChange={event => handleInputChangeSelect(index, event)}
        name={'question'+ques.id} value={ques.id}  
      >
      
        {ques
          ? ques.questionAnswers.map(otherEntity => (
              <option value={otherEntity.id} key={otherEntity.id}>
                 {otherEntity.label}
            
              </option>
            ))
          : null}
      </AvInput>
    </AvGroup> );

    break;
    case "RadioBox":   return  (
    <AvGroup>

<AvRadioGroup name="radio"  required errorMessage="Pick one!"  >
      
    {ques.questionAnswers
        ? ques.questionAnswers.map(choix => (
              <AvRadio label={choix.label} value={choix.id} key={choix.id }  onClick={event => handleInputChangeRadio(index, event )} />  
    ) ) 
    : null}
</AvRadioGroup>
</AvGroup>)
  ;
  break;
  case "CheckBox":   return  (
    <AvGroup>
     <AvCheckboxGroup inline name="checkbox"    >
 
      
    {ques.questionAnswers
        ? ques.questionAnswers.map(choix => (
              <AvCheckbox  label={choix.label} value={choix.id} key={choix.id } required onClick={event => handleInputChangeCheckBox(index, event)}   />  
    ) ) 
    : null}
 </AvCheckboxGroup>
</AvGroup>)
  ;
  break;
     default:null
       break;
    } }


  useEffect(() => {
  
  
    
    
        
        props.getEntity(props.match.params.id);
  
        const valuess = [...listReponse];
    
        patientQuestionnaireEntity.questionnaire? patientQuestionnaireEntity.questionnaire.questions.map((question) =>(
        question.typeQuestion==="CheckBox"? 
        valuess.push(  
         { questionId: question.id.toString(),
           choixId :  [],
           contenu:''
         }  )   : 
     
     
       
      valuess.push(  
       { questionId: question.id.toString(),
         choixId :  question.questionAnswers.length>0? [question.questionAnswers[0].id.toString()]:[] ,
         contenu:''
       }  ) ,
       setInputFields(valuess)
    
     )) : null ;
   

      
  },  [patientQuestionnaireEntity.id]);

  useEffect(() => {
    
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);
 
  const saveEntity = (event, errors, values) => {
   

    if (errors.length === 0) {
      const entity = {
        ...patientQuestionnaireEntity,
        ...values,
        listReponse: listReponse.map(rep => {
          return( rep)
           })
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };



  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tnTsuiviApp.patientQuestionnaire.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.patientQuestionnaire.home.createOrEditLabel">
              Create or edit a PatientQuestionnaire
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={ patientQuestionnaireEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="patient-questionnaire-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="patient-questionnaire-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
        {patientQuestionnaireEntity.questionnaire? patientQuestionnaireEntity.questionnaire.questions.map((question, i) =>  (
           <>
      <Label   key={`entity-${i}`} >    Question {i+1} : {question.label} </Label>
        
           
          {func(question , i)  }
          </> 
        )
       
        ):null}
             
             
              
              <Button tag={Link} id="cancel-save" to="/patient-questionnaire" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  
  patients: storeState.patient.entities,
  groupeDePatient: storeState.groupeDePatient.entities,
  questionnaires: storeState.questionnaire.entities,
  patientQuestionnaireEntity: storeState.patientQuestionnaire.entity,
  loading: storeState.patientQuestionnaire.loading,
  updating: storeState.patientQuestionnaire.updating,
  updateSuccess: storeState.patientQuestionnaire.updateSuccess
});

const mapDispatchToProps = {
  getGroupeDePatient,
  getPatients,
  getQuestionnaires,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientQuestionnaireReponse);
