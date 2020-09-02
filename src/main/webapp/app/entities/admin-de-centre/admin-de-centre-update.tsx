import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICentre } from 'app/shared/model/centre.model';
import { getEntities as getCentres } from 'app/entities/centre/centre.reducer';
import { getEntities2 as getCentres2 } from 'app/entities/centre/centre.reducer';
import { getEntity, updateEntity, createEntity, reset } from './admin-de-centre.reducer';
import { getEntities as getRegions } from 'app/entities/region/region.reducer';
import { IAdminDeCentre } from 'app/shared/model/admin-de-centre.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { locales, languages } from 'app/config/translation';

export interface IAdminDeCentreUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AdminDeCentreUpdate = (props: IAdminDeCentreUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [centreId, setCentreId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const {regions, adminDeCentreEntity, user, users, centres, loading, updating } = props;
  const handleChange: React.ReactEventHandler<HTMLInputElement> = (ev) => { 
    props.getCentres2(ev.currentTarget.value);
  
   }
  const handleClose = () => {
    props.history.push('/admin-de-centre');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
      
    } else {
      props.getEntity(props.match.params.id);
    }
    props.getRegions();
    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...adminDeCentreEntity,
        ...values
      };
      entity.user = values.user;

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
          <h2 id="tnTsuiviApp.adminDeCentre.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.adminDeCentre.home.createOrEditLabel">Create or edit a AdminDeCentre</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : adminDeCentreEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="admin-de-centre-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="admin-de-centre-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              

              {user.id ? (
                <AvGroup>
                  <Label for="id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvField type="text" className="form-control" id="medecin-user-id" name="user.id" required readOnly value={user.id} />
                </AvGroup>
              ) : null}

               <AvGroup>
                <Label for="login">
                  <Translate contentKey="userManagement.login">Login</Translate>
                </Label>
                <AvField
                  type="text"
                  className="form-control"
                  name="user.login"
                  validate={{
                    required: {
                      value: true,
                      errorMessage: translate('register.messages.validate.login.required')
                    },
                    pattern: {
                      value: '^[_.@A-Za-z0-9-]*$',
                      errorMessage: translate('register.messages.validate.login.pattern')
                    },
                    minLength: {
                      value: 1,
                      errorMessage: translate('register.messages.validate.login.minlength')
                    },
                    maxLength: {
                      value: 50,
                      errorMessage: translate('register.messages.validate.login.maxlength')
                    }
                  }}
                  value={user.login}
                />
              </AvGroup>
              <AvGroup>
                <Label for="firstName">
                  <Translate contentKey="userManagement.firstName">First Name</Translate>
                </Label>
                <AvField
                  type="text"
                  className="form-control"
                  name="user.firstName"
                  validate={{
                    maxLength: {
                      value: 50,
                      errorMessage: translate('entity.validation.maxlength', { max: 50 })
                    }
                  }}
                  value={user.firstName}
                />

              </AvGroup>
              <AvGroup>
                <Label for="lastName">
                  <Translate contentKey="userManagement.lastName">Last Name</Translate>
                </Label>
                <AvField
                  type="text"
                  className="form-control"
                  name="user.lastName"
                  validate={{
                    maxLength: {
                      value: 50,
                      errorMessage: translate('entity.validation.maxlength', { max: 50 })
                    }
                  }}
                  value={user.lastName}
                />
                <AvFeedback>This field cannot be longer than 50 characters.</AvFeedback>
              </AvGroup>
              <AvGroup>
                <AvField
                  name="user.email"
                  label={translate('global.form.email.label')}
                  placeholder={translate('global.form.email.placeholder')}
                  type="email"
                  validate={{
                    required: {
                      value: true,
                      errorMessage: translate('global.messages.validate.email.required')
                    },
                    email: {
                      errorMessage: translate('global.messages.validate.email.invalid')
                    },
                    minLength: {
                      value: 5,
                      errorMessage: translate('global.messages.validate.email.minlength')
                    },
                    maxLength: {
                      value: 254,
                      errorMessage: translate('global.messages.validate.email.maxlength')
                    }
                  }}
                  value={user.email}
                />
              </AvGroup>
              <AvGroup check>
                <Label>
                  <AvInput type="checkbox" name="user.activated" value={user.activated} checked={user.activated} disabled={!user.id} />{' '}
                  <Translate contentKey="userManagement.activated">Activated</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="langKey">
                  <Translate contentKey="userManagement.langKey">Language Key</Translate>
                </Label>
                <AvField type="select" className="form-control" name="user.langKey" value={user.langKey || locales[0]}>
                  {locales.map(locale => (
                    <option value={locale} key={locale}>
                      {languages[locale].name}
                    </option>
                  ))}
                </AvField>
              </AvGroup>
              
              <AvGroup>
                <Label id="phoneLabel" for="admin-de-centre-phone">
                  <Translate contentKey="tnTsuiviApp.adminDeCentre.phone">Phone</Translate>
                </Label>
                <AvField
                  id="admin-de-centre-phone"
                  type="text"
                  name="phone"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
            

              <AvGroup>
                <Label for="centre-region">
                  <Translate contentKey="tnTsuiviApp.centre.region">Region</Translate>
                </Label>
                <AvInput
                  id="centre-region"
                  type="select"  onChange={handleChange}  
                  className="form-control"
                  name="centre.region.id"
                  required>
                       <option value="" key="0" />
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

              <AvGroup>
                <Label for="admin-de-centre-centre">
                  <Translate contentKey="tnTsuiviApp.adminDeCentre.centre">Centre</Translate>
                </Label>
                <AvInput id="admin-de-centre-centre" type="select" className="form-control"  name="centre.id">
              
                  {centres
                    ? centres.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/admin-de-centre" replace color="info">
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
  user: storeState.userManagement.user,
  users: storeState.userManagement.users,
  centres: storeState.centre.entities,
  adminDeCentreEntity: storeState.adminDeCentre.entity,
  loading: storeState.adminDeCentre.loading,
  updating: storeState.adminDeCentre.updating,
  updateSuccess: storeState.adminDeCentre.updateSuccess
});

const mapDispatchToProps = {
  getRegions,
  getUsers,
  getCentres2,
  getCentres,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AdminDeCentreUpdate);
