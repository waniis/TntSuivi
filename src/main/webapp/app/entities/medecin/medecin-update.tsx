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
import { getEntitiesWithIdMedecin as getCentersWithIdMedecin } from 'app/entities/centre/centre.reducer';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { getEntities as getSpecialties } from 'app/entities/specialty/specialty.reducer';
import { getEntity, updateEntity, createEntity, reset } from './medecin.reducer';
import { IMedecin } from 'app/shared/model/medecin.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { locales, languages } from 'app/config/translation';
import { getEntities as getRegions } from 'app/entities/region/region.reducer';
import region from '../region/region';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import authentication from 'app/shared/reducers/authentication';
import { AUTHORITIES } from 'app/config/constants';

export interface IMedecinUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
}


export const MedecinUpdate = (props: IMedecinUpdateProps) => {

  
  const handleChange: React.ReactEventHandler<HTMLInputElement> = (ev) => { 
    props.getCentres2(ev.currentTarget.value);
  
   }

  const [userId, setUserId] = useState('0');
  const [centreId, setCentreId] = useState('0');
  const [specialtyId, setSpecialtyId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { account, regions, medecinEntity, users,user,roles, centres, specialties, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/medecin' + props.location.search);
  };

  useEffect(() => {

    props.getRegions();
    props.getUsers();
    props.getSpecialties();
   
   

    if (isNew) {
      props.reset();
    
       
     
   
    } else {
      props.getCentersWithIdMedecin(props.match.params.id);
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
        ...medecinEntity,
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
          <h2 id="tnTsuiviApp.medecin.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.medecin.home.createOrEditLabel">Create or edit a Medecin</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : medecinEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="medecin-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="medecin-id" type="text" className="form-control" name="id" required readOnly />
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
                <Label id="phoneLabel" for="medecin-phone">
                  <Translate contentKey="tnTsuiviApp.medecin.phone">Phone</Translate>
                </Label>
                <AvField
                  id="medecin-phone"
                  type="text"
                  name="phone"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="phone2Label" for="medecin-phone2">
                  <Translate contentKey="tnTsuiviApp.medecin.phone2">Phone 2</Translate>
                </Label>
                <AvField id="medecin-phone2" type="text" name="phone2" />
              </AvGroup>
              <AvGroup>
                <Label id="adressLabel" for="medecin-adress">
                  <Translate contentKey="tnTsuiviApp.medecin.adress">Adress</Translate>
                </Label>
                <AvField
                  id="medecin-adress"
                  type="text"
                  name="adress"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="sexeLabel" for="medecin-sexe">
                  <Translate contentKey="tnTsuiviApp.medecin.sexe">Sexe</Translate>
                </Label>
                <AvInput
                  id="medecin-sexe"
                  type="select"
                  className="form-control"
                  name="sexe"
                  value={(!isNew && medecinEntity.sexe) || 'Feminin'}
                >
                  <option value="Feminin">{translate('tnTsuiviApp.Sexe.Feminin')}</option>
                  <option value="Masculin">{translate('tnTsuiviApp.Sexe.Masculin')}</option>
                </AvInput>
              </AvGroup>

              { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN]) ?  (
              <AvGroup>
                <Label for="centre-region">
                  <Translate contentKey="tnTsuiviApp.centre.region">Region</Translate>
                </Label>
                <AvInput
                  id="centre-region"
                  type="select"  onChange={handleChange}  
                  className="form-control"
                  name="centre.region.id"
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
                    </AvGroup> ):null}
                    { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN]) ?  (
              <AvGroup>
                <Label for="medecin-centre">
                  <Translate contentKey="tnTsuiviApp.medecin.centre">Centre</Translate>
                </Label>
                <AvInput
                  id="medecin-centre"
                  type="select"
                  className="form-control"
                  name="centre.id">
                  {centres
                    ? centres.map(otherEntity => (
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
             ): null } 
              <AvGroup>
                <Label for="medecin-specialty">
                  <Translate contentKey="tnTsuiviApp.medecin.specialty">Specialty</Translate>
                </Label>
                <AvInput
                  id="medecin-specialty"
                  type="select"
                  className="form-control"
                  name="specialty.id">
                 
                
                  {specialties
                    ? specialties.map(otherEntity => (
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
              <Button tag={Link} id="cancel-save" to="/medecin" replace color="info">
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
  isAuthenticated: storeState.authentication.isAuthenticated,
  users: storeState.userManagement.users,
  centres: storeState.centre.entities,
  specialties: storeState.specialty.entities,
  medecinEntity: storeState.medecin.entity,
  loading: storeState.medecin.loading,
  updating: storeState.medecin.updating,
  updateSuccess: storeState.medecin.updateSuccess,
  user: storeState.userManagement.user,
  roles: storeState.userManagement.authorities,
  regions: storeState.region.entities,
  centreEntity: storeState.centre.entity,
});




const mapDispatchToProps = {
  getCentersWithIdMedecin,
  getUsers,
  getCentres,
  getCentres2,
  getSpecialties,
  getEntity,
  updateEntity,
  createEntity,
  getRegions,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MedecinUpdate);
