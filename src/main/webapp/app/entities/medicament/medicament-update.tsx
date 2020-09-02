import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './medicament.reducer';
import { IMedicament } from 'app/shared/model/medicament.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMedicamentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MedicamentUpdate = (props: IMedicamentUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { medicamentEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/medicament');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...medicamentEntity,
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
          <h2 id="tnTsuiviApp.medicament.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.medicament.home.createOrEditLabel">Create or edit a Medicament</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : medicamentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="medicament-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="medicament-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="medicament-name">
                  <Translate contentKey="tnTsuiviApp.medicament.name">Name</Translate>
                </Label>
                <AvField id="medicament-name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="formeLabel" for="medicament-forme">
                  <Translate contentKey="tnTsuiviApp.medicament.forme">Forme</Translate>
                </Label>
                <AvInput
                  id="medicament-forme"
                  type="select"
                  className="form-control"
                  name="forme"
                  value={(!isNew && medicamentEntity.forme) || 'Orales'}
                >
                  <option value="Orales">{translate('tnTsuiviApp.Forme.Orales')}</option>
                  <option value="Injectables">{translate('tnTsuiviApp.Forme.Injectables')}</option>
                  <option value="Dermiques">{translate('tnTsuiviApp.Forme.Dermiques')}</option>
                  <option value="Inhalees">{translate('tnTsuiviApp.Forme.Inhalees')}</option>
                  <option value="Rectales">{translate('tnTsuiviApp.Forme.Rectales')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="descrpitionLabel" for="medicament-descrpition">
                  <Translate contentKey="tnTsuiviApp.medicament.descrpition">Descrpition</Translate>
                </Label>
                <AvField id="medicament-descrpition" type="text" name="descrpition" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/medicament" replace color="info">
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
  medicamentEntity: storeState.medicament.entity,
  loading: storeState.medicament.loading,
  updating: storeState.medicament.updating,
  updateSuccess: storeState.medicament.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MedicamentUpdate);
