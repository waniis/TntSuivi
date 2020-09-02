import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './patient.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPatientDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PatientDetail = (props: IPatientDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { patientEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.patient.detail.title">Patient</Translate> [<b>{patientEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fullName">
              <Translate contentKey="tnTsuiviApp.patient.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{patientEntity.fullName}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="tnTsuiviApp.patient.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{patientEntity.phone}</dd>
          <dt>
            <span id="adress">
              <Translate contentKey="tnTsuiviApp.patient.adress">Adress</Translate>
            </span>
          </dt>
          <dd>{patientEntity.adress}</dd>
          <dt>
            <span id="sexe">
              <Translate contentKey="tnTsuiviApp.patient.sexe">Sexe</Translate>
            </span>
          </dt>
          <dd>{patientEntity.sexe}</dd>
          <dt>
            <span id="alcool">
              <Translate contentKey="tnTsuiviApp.patient.alcool">Alcool</Translate>
            </span>
          </dt>
          <dd>{patientEntity.alcool}</dd>
          <dt>
            <span id="startDateAlcool">
              <Translate contentKey="tnTsuiviApp.patient.startDateAlcool">Start Date Alcool</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={patientEntity.startDateAlcool} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="endDateAlcool">
              <Translate contentKey="tnTsuiviApp.patient.endDateAlcool">End Date Alcool</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={patientEntity.endDateAlcool} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="tobacoo">
              <Translate contentKey="tnTsuiviApp.patient.tobacoo">Tobacoo</Translate>
            </span>
          </dt>
          <dd>{patientEntity.tobacoo}</dd>
          <dt>
            <span id="startDateTobacco">
              <Translate contentKey="tnTsuiviApp.patient.startDateTobacco">Start Date Tobacco</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={patientEntity.startDateTobacco} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="endDateTobacco">
              <Translate contentKey="tnTsuiviApp.patient.endDateTobacco">End Date Tobacco</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={patientEntity.endDateTobacco} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.patient.user">User</Translate>
          </dt>
          <dd>{patientEntity.user ? patientEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.patient.medecin">Medecin</Translate>
          </dt>
          <dd>{patientEntity.medecin ? patientEntity.medecin.fullName : ''}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.patient.centre">Centre</Translate>
          </dt>
          <dd>{patientEntity.centre ? patientEntity.centre.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/patient" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ patient }: IRootState) => ({
  patientEntity: patient.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientDetail);
