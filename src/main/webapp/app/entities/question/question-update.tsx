import React, { useState, useEffect, Fragment } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import {RadioGroup, Radio} from 'react-radio-group'

import { IMedecin } from 'app/shared/model/medecin.model';
import { getEntities as getMedecins } from 'app/entities/medecin/medecin.reducer';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { getEntities as getQuestionnaires } from 'app/entities/questionnaire/questionnaire.reducer';
import { getEntity, updateEntity, createEntity, reset } from './question.reducer';
import { IQuestion } from 'app/shared/model/question.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IQuestionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const QuestionUpdate = (props: IQuestionUpdateProps) => {
  const [medecinId, setMedecinId] = useState('0');
  const [questionnaireId, setQuestionnaireId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { questionEntity, medecins, questionnaires, loading, updating } = props;




  const  [questionAnswers, setInputFields] = useState([
    
    { choice: '' }   
  ])
  
 
  const handleAddFields = () => {
    const valuess = [...questionAnswers];
    valuess.push({ choice: '' });
    setInputFields(valuess);
  };

  const handleRemoveFields = index => {
    const valuess = [...questionAnswers];
    valuess.splice(index, 1);
    setInputFields(valuess);
  };

  const handleInputChange = (index, event) => {
    const valuess = [...questionAnswers];
    if (event.target.name === "choice") {
     
      valuess[index].choice = event.target.value; 
 
    } 
   
  
    setInputFields(valuess);
  };
  const handleClose = () => {
    props.history.push('/question');
  };

  
  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getQuestionnaires();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...questionEntity,
        ...values,
        questionAnswers: questionAnswers.map(answer => {
         return( answer.choice)
          })
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };
  const [typeQues, setTypeQues] = useState((!isNew && questionEntity.typeQuestion ) || 'Text');
  const handleChange = (event : React.FormEvent<HTMLInputElement>) => { 
  
 
   setTypeQues(event.currentTarget.value);
   if (event.currentTarget.value === 'Text')
   questionAnswers.splice(0);
   else  if (questionAnswers.length ===0)
   handleAddFields();
  }
  const RadioButton = (variable) => (
    <label>
      <Radio
        name={variable.value}
        value={variable.value}
        disabled
      />
      {' '+variable.name }
    </label>
  );
 
  const typeFun = (x: string) => {
    switch(x) {
  
     
      case "CheckBox":   return <h1> box </h1> ;

      case "Select": return      <AvGroup>
      <Label for="questionnaire-question">Question</Label>
      <AvInput
        id="questionnaire-question"
        type="select"
        multiple
        className="form-control"
        name="affichage"
       readonly="true"
       disabled
      >
      
        {questionAnswers
          ? questionAnswers.map(otherEntity => (
              <option value={otherEntity.choice} key={otherEntity.choice}>
                 {otherEntity.choice}
            
              </option>
            ))
          : null}
      </AvInput>
    </AvGroup> ;
      case "RadioBox":   return  <AvGroup>
      <Label for="questionnaire-question">Question</Label>

      <RadioGroup   readonly="true"  name="affichage" >
      {questionAnswers
          ? questionAnswers.map(choix => (
         <RadioButton name={choix.choice}  value={choix.choice} key={choix.choice} />    
            
      ) ) 
      : null}
  </RadioGroup>
  </AvGroup>
    ;
   

      default:      return null;

    }
  }
  

 

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tnTsuiviApp.question.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.question.home.createOrEditLabel">Create or edit a Question</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : questionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="question-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="question-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="labelLabel" for="question-label">
                  <Translate contentKey="tnTsuiviApp.question.label">Label</Translate>
                </Label>
                <AvField
                  id="question-label"
                  type="text"
                  name="label"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeQuestionLabel" for="question-typeQuestion">
                  <Translate contentKey="tnTsuiviApp.question.typeQuestion">Type Question</Translate>
                </Label>
                <AvInput onChange={handleChange}
                  id="question-typeQuestion"
                  type="select"
                  className="form-control"
                  name="typeQuestion"
                  value={(!isNew && questionEntity.typeQuestion) || 'Select'}
                >
              
                  <option value="CheckBox">{translate('tnTsuiviApp.TypeQuestion.CheckBox')}</option>
                  <option value="Text">{translate('tnTsuiviApp.TypeQuestion.Text')}</option>
                  <option value="RadioBox">{translate('tnTsuiviApp.TypeQuestion.RadioBox')}</option>
                  <option value="Select">{translate('tnTsuiviApp.TypeQuestion.Select')}</option>
                </AvInput>
                <div> { typeFun(typeQues)  }</div> 
             
             {questionAnswers.map((inputField, index) => (
           <Fragment   key={`${inputField}~${index}`}>
             <AvGroup >
               <Label id="labelLabel" for="question-answer-label">
                 Choix {index+1}
               </Label>
               <AvField id="question-answer-label" type="text" onChange={event => handleInputChange(index, event)}   value={inputField.choice} required name="choice" />
             </AvGroup> 
             <div className="form-group col-sm-2">
               <button
                 className="btn btn-link"
                 type="button"
                 onClick={() => handleRemoveFields(index)}
               >
                 -
               </button>
               <button
                 className="btn btn-link"
                 type="button"
                 onClick={() => handleAddFields()}
               >
                 +
               </button>
            
        
       
             </div>
             </Fragment> ))}
              </AvGroup>
         
              <Button tag={Link} id="cancel-save" to="/question" replace color="info">
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
  medecins: storeState.medecin.entities,
  questionnaires: storeState.questionnaire.entities,
  questionEntity: storeState.question.entity,
  loading: storeState.question.loading,
  updating: storeState.question.updating,
  updateSuccess: storeState.question.updateSuccess
});

const mapDispatchToProps = {
  getMedecins,
  getQuestionnaires,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(QuestionUpdate);


