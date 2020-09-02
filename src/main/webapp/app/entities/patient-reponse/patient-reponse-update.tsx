import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { getEntities as getPatientQuestionnaires } from 'app/entities/patient-questionnaire/patient-questionnaire.reducer';
import { IQuestion } from 'app/shared/model/question.model';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { IQuestionAnswer } from 'app/shared/model/question-answer.model';
import { getEntities as getQuestionAnswers } from 'app/entities/question-answer/question-answer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './patient-reponse.reducer';
import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPatientReponseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PatientReponseUpdate = (props: IPatientReponseUpdateProps) => {
  const [patientQuestionnaireId, setPatientQuestionnaireId] = useState('0');
  const [questionId, setQuestionId] = useState('0');
  const [questionAnswerId, setQuestionAnswerId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { patientReponseEntity, patientQuestionnaires, questions, questionAnswers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/patient-reponse');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getPatientQuestionnaires();
    props.getQuestions();
    props.getQuestionAnswers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...patientReponseEntity,
        ...values
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
          <h2 id="tnTsuiviApp.patientReponse.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.patientReponse.home.createOrEditLabel">Create or edit a PatientReponse</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : patientReponseEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="patient-reponse-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="patient-reponse-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="contentLabel" for="patient-reponse-content">
                  <Translate contentKey="tnTsuiviApp.patientReponse.content">Content</Translate>
                </Label>
                <AvField id="patient-reponse-content" type="text" name="content" />
              </AvGroup>
              <AvGroup>
                <Label for="patient-reponse-patientQuestionnaire">
                  <Translate contentKey="tnTsuiviApp.patientReponse.patientQuestionnaire">Patient Questionnaire</Translate>
                </Label>
                <AvInput id="patient-reponse-patientQuestionnaire" type="select" className="form-control" name="patientQuestionnaire.id">
                  <option value="" key="0" />
                  {patientQuestionnaires
                    ? patientQuestionnaires.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="patient-reponse-question">
                  <Translate contentKey="tnTsuiviApp.patientReponse.question">Question</Translate>
                </Label>
                <AvInput
                  id="patient-reponse-question"
                  type="select"
                  className="form-control"
                  name="question.id"
                  value={isNew ? questions[0] && questions[0].id : patientReponseEntity.question.id}
                  required
                >
                  {questions
                    ? questions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.label}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="patient-reponse-questionAnswer">
                  <Translate contentKey="tnTsuiviApp.patientReponse.questionAnswer">Question Answer</Translate>
                </Label>
                <AvInput id="patient-reponse-questionAnswer" type="select" className="form-control" name="questionAnswer.id">
                  <option value="" key="0" />
                  {questionAnswers
                    ? questionAnswers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.label}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/patient-reponse" replace color="info">
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
  patientQuestionnaires: storeState.patientQuestionnaire.entities,
  questions: storeState.question.entities,
  questionAnswers: storeState.questionAnswer.entities,
  patientReponseEntity: storeState.patientReponse.entity,
  loading: storeState.patientReponse.loading,
  updating: storeState.patientReponse.updating,
  updateSuccess: storeState.patientReponse.updateSuccess
});

const mapDispatchToProps = {
  getPatientQuestionnaires,
  getQuestions,
  getQuestionAnswers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientReponseUpdate);
