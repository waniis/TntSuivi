import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IQuestion } from 'app/shared/model/question.model';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { IMedecin } from 'app/shared/model/medecin.model';
import { getEntities as getMedecins } from 'app/entities/medecin/medecin.reducer';


import { getEntity, updateEntity, createEntity, reset } from './questionnaire.reducer';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IQuestionnaireUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const QuestionnaireUpdate = (props: IQuestionnaireUpdateProps) => {
  const [idsquestion, setIdsquestion] = useState([]);
  const [medecinId, setMedecinId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { questionnaireEntity, questions, medecins, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/questionnaire');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getQuestions();

  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const entity = {
        ...questionnaireEntity,
        ...values,
        questions: mapIdList(values.questions)
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
          <h2 id="tnTsuiviApp.questionnaire.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.questionnaire.home.createOrEditLabel">Create or edit a Questionnaire</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : questionnaireEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="questionnaire-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="questionnaire-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="subjectLabel" for="questionnaire-subject">
                  <Translate contentKey="tnTsuiviApp.questionnaire.subject">Subject</Translate>
                </Label>
                <AvField id="questionnaire-subject" type="text" name="subject" />
              </AvGroup>
              <AvGroup>
                <Label id="startDateLabel" for="questionnaire-startDate">
                  <Translate contentKey="tnTsuiviApp.questionnaire.startDate">Start Date</Translate>
                </Label>
                <AvInput
                  id="questionnaire-startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.questionnaireEntity.startDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="questionnaire-endDate">
                  <Translate contentKey="tnTsuiviApp.questionnaire.endDate">End Date</Translate>
                </Label>
                <AvInput
                  id="questionnaire-endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.questionnaireEntity.endDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="questionnaire-question">
                  <Translate contentKey="tnTsuiviApp.questionnaire.question">Question</Translate>
                </Label>
                <AvInput
                  id="questionnaire-question"
                  type="select"
                  multiple
                  className="form-control"
                  name="questions"
                  value={questionnaireEntity.questions && questionnaireEntity.questions.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {questions
                    ? questions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.label}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
          
              <Button tag={Link} id="cancel-save" to="/questionnaire" replace color="info">
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
  questions: storeState.question.entities,
  medecins: storeState.medecin.entities,
  questionnaireEntity: storeState.questionnaire.entity,
  loading: storeState.questionnaire.loading,
  updating: storeState.questionnaire.updating,
  updateSuccess: storeState.questionnaire.updateSuccess
});

const mapDispatchToProps = {
  
  getQuestions,
  getMedecins,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(QuestionnaireUpdate);
