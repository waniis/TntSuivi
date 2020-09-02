import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';
import NotificationSystem from 'react-notification-system';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IQuestionnaire } from 'app/shared/model/questionnaire.model';
import { getEntities as getQuestionnaires } from 'app/entities/questionnaire/questionnaire.reducer';
import { getEntity, updateEntity, createEntity, reset } from './patient-questionnaire.reducer';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { getEntities as getGroupeDePatient } from 'app/entities/groupe-de-patient/groupe-de-patient.reducer';

export interface IPatientQuestionnaireUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}


export const PatientQuestionnaireUpdate = (props: IPatientQuestionnaireUpdateProps) => {
  const [patientId, setPatientId] = useState('0');
  const [questionnaireId, setQuestionnaireId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { patientQuestionnaireEntity,groupeDePatient, patients, questionnaires, loading, updating } = props;
 
  const notificationSystem = React.createRef<HTMLElement>();

  /*const addNotification = event => {
    event.preventDefault();
    const notification = notificationSystem.current;
     notification.addNotification({
      message: 'Notification message',  
      level: 'info'
    });
  };*/

  const handleClose = () => {
    props.history.push('/patient-questionnaire');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

 
    props.getQuestionnaires();
    props.getGroupeDePatient();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
   

    if (errors.length === 0) {
      const entity = {
        ...patientQuestionnaireEntity,
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
            <AvForm model={isNew ? {} : patientQuestionnaireEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="patient-questionnaire-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="patient-questionnaire-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
        
              <AvGroup>
                <Label for="patient-questionnaire-patient">
                <Translate contentKey="tnTsuiviApp.groupeDePatient.detail.title">Groupe de patient</Translate>
                </Label>
                <AvInput id="patient-questionnaire-patient" type="select" className="form-control" name="groupeDePatient">
                  <option value="" key="0" />
                  {groupeDePatient
                    ? groupeDePatient.map(otherEntity => (
                     
                       <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="patient-questionnaire-questionnaire">
                  <Translate contentKey="tnTsuiviApp.patientQuestionnaire.questionnaire">Questionnaire</Translate>
                </Label>
                <AvInput id="patient-questionnaire-questionnaire" type="select" className="form-control" name="questionnaire">
                  <option value="" key="0" />
                  {questionnaires
                    ? questionnaires.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.subject}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
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

export default connect(mapStateToProps, mapDispatchToProps)(PatientQuestionnaireUpdate);
