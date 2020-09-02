import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './patient-questionnaire.reducer';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPatientQuestionnaireDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PatientQuestionnaireDetail = (props: IPatientQuestionnaireDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { patientQuestionnaireEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.patientQuestionnaire.detail.title">PatientQuestionnaire</Translate> [
          <b>{patientQuestionnaireEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="doneTimeDate">
              <Translate contentKey="tnTsuiviApp.patientQuestionnaire.doneTimeDate">Done Time Date</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={patientQuestionnaireEntity.doneTimeDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="done">
              <Translate contentKey="tnTsuiviApp.patientQuestionnaire.done">Done</Translate>
            </span>
          </dt>
          <dd>{patientQuestionnaireEntity.done ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.patientQuestionnaire.patient">Patient</Translate>
          </dt>
          <dd>{patientQuestionnaireEntity.patient ? patientQuestionnaireEntity.patient.id : ''}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.patientQuestionnaire.questionnaire">Questionnaire</Translate>
          </dt>
          <dd>{patientQuestionnaireEntity.questionnaire ? patientQuestionnaireEntity.questionnaire.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient-questionnaire" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient-questionnaire/${patientQuestionnaireEntity.id}/reponse`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ patientQuestionnaire }: IRootState) => ({
  patientQuestionnaireEntity: patientQuestionnaire.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientQuestionnaireDetail);
