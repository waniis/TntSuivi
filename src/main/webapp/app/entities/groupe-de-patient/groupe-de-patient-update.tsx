import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPatient } from 'app/shared/model/patient.model';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';
import { IMedecin } from 'app/shared/model/medecin.model';
import { getEntities as getMedecins } from 'app/entities/medecin/medecin.reducer';
import { getEntity, updateEntity, createEntity, reset } from './groupe-de-patient.reducer';
import { IGroupeDePatient } from 'app/shared/model/groupe-de-patient.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export interface IGroupeDePatientUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GroupeDePatientUpdate = (props: IGroupeDePatientUpdateProps) => {
  const [idspatient, setIdspatient] = useState([]);
  const [medecinId, setMedecinId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { account,groupeDePatientEntity, patients, medecins, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/groupe-de-patient');
  };

  useEffect(() => {

    if ( hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN_CENTRE]) ){
      props.getMedecins()
    }
    
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getPatients();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...groupeDePatientEntity,
        ...values,
        patients: mapIdList(values.patients)
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
          <h2 id="tnTsuiviApp.groupeDePatient.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.groupeDePatient.home.createOrEditLabel">Create or edit a GroupeDePatient</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : groupeDePatientEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="groupe-de-patient-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="groupe-de-patient-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="groupe-de-patient-name">
                  <Translate contentKey="tnTsuiviApp.groupeDePatient.name">Name</Translate>
                </Label>
                <AvField id="groupe-de-patient-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label for="groupe-de-patient-patient">
                  <Translate contentKey="tnTsuiviApp.groupeDePatient.patient">Patient</Translate>
                </Label>
                <AvInput
                  id="groupe-de-patient-patient"
                  type="select"
                  multiple
                  className="form-control"
                  name="patients"
                  value={groupeDePatientEntity.patients && groupeDePatientEntity.patients.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {patients
                    ? patients.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.fullName}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>

              { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN_CENTRE]) ?  (
              <AvGroup>
                <Label for="groupe-de-patient-medecin">
                  <Translate contentKey="tnTsuiviApp.groupeDePatient.medecin">Medecin</Translate>
                </Label>
                <AvInput id="groupe-de-patient-medecin" type="select" className="form-control" name="medecin.id">
                  <option value="" key="0" />
                  {medecins
                    ? medecins.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.fullName}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup> ):null}
              <Button tag={Link} id="cancel-save" to="/groupe-de-patient" replace color="info">
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
  account: storeState.authentication.account,
  patients: storeState.patient.entities,
  medecins: storeState.medecin.entities,
  groupeDePatientEntity: storeState.groupeDePatient.entity,
  loading: storeState.groupeDePatient.loading,
  updating: storeState.groupeDePatient.updating,
  updateSuccess: storeState.groupeDePatient.updateSuccess
});

const mapDispatchToProps = {
  getPatients,
  getMedecins,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GroupeDePatientUpdate);
