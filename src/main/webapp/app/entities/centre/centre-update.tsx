import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRegion } from 'app/shared/model/region.model';
import { getEntities as getRegions } from 'app/entities/region/region.reducer';
import { getEntity, updateEntity, createEntity, reset } from './centre.reducer';
import { ICentre } from 'app/shared/model/centre.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICentreUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CentreUpdate = (props: ICentreUpdateProps) => {
  const [regionId, setRegionId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { centreEntity, regions, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/centre' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getRegions();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...centreEntity,
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
          <h2 id="tnTsuiviApp.centre.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.centre.home.createOrEditLabel">Create or edit a Centre</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : centreEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="centre-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="centre-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="centre-name">
                  <Translate contentKey="tnTsuiviApp.centre.name">Name</Translate>
                </Label>
                <AvField
                  id="centre-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="addressLabel" for="centre-address">
                  <Translate contentKey="tnTsuiviApp.centre.address">Address</Translate>
                </Label>
                <AvField
                  id="centre-address"
                  type="text"
                  name="address"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="centre-phone">
                  <Translate contentKey="tnTsuiviApp.centre.phone">Phone</Translate>
                </Label>
                <AvField
                  id="centre-phone"
                  type="text"
                  name="phone"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="faxLabel" for="centre-fax">
                  <Translate contentKey="tnTsuiviApp.centre.fax">Fax</Translate>
                </Label>
                <AvField
                  id="centre-fax"
                  type="text"
                  name="fax"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="emergencyLabel" for="centre-emergency">
                  <Translate contentKey="tnTsuiviApp.centre.emergency">Emergency</Translate>
                </Label>
                <AvField id="centre-emergency" type="text" name="emergency" />
              </AvGroup>
              <AvGroup>
                <Label for="centre-region">
                  <Translate contentKey="tnTsuiviApp.centre.region">Region</Translate>
                </Label>
                <AvInput
                  id="centre-region"
                  type="select"
                  className="form-control"
                  name="region.id"
              
                  required
                >
                  {regions
                    ? regions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/centre" replace color="info">
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
  regions: storeState.region.entities,
  centreEntity: storeState.centre.entity,
  loading: storeState.centre.loading,
  updating: storeState.centre.updating,
  updateSuccess: storeState.centre.updateSuccess
});

const mapDispatchToProps = {
  getRegions,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CentreUpdate);
