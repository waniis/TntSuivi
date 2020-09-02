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
import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { getEntities as getPatientReponses } from 'app/entities/patient-reponse/patient-reponse.reducer';
import { IMedecin } from 'app/shared/model/medecin.model';
import { getEntities as getMedecins } from 'app/entities/medecin/medecin.reducer';
import { ICentre } from 'app/shared/model/centre.model';
import { getEntities as getCentres } from 'app/entities/centre/centre.reducer';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { getEntities as getPatientQuestionnaires } from 'app/entities/patient-questionnaire/patient-questionnaire.reducer';
import { IGroupeDePatient } from 'app/shared/model/groupe-de-patient.model';
import { getEntities as getGroupeDePatients } from 'app/entities/groupe-de-patient/groupe-de-patient.reducer';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { locales, languages } from 'app/config/translation';
import { getEntities2 as getCentres2 } from 'app/entities/centre/centre.reducer';
import { getEntitiesWithCenter as getMedecinwithCentre } from 'app/entities/medecin/medecin.reducer';

import { getEntities as getRegions } from 'app/entities/region/region.reducer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

export interface IPatientUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PatientUpdate = (props: IPatientUpdateProps) => {


    
     

    
  const [userId, setUserId] = useState('0');
  const [patientReponseId, setPatientReponseId] = useState('0');
  const [medecinId, setMedecinId] = useState('0');
  const [centreId, setCentreId] = useState('0');
  const [questionnaireId, setQuestionnaireId] = useState('0');
  const [groupeDePatientId, setGroupeDePatientId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const {regions,account, patientEntity, users,user, patientReponses, medecins, centres, patientQuestionnaires, groupeDePatients, loading, updating } = props;


  const handleClose = () => {
    props.history.push('/patient' + props.location.search);
  };

  useEffect(() => {

    
  
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
    props.getRegions();
    props.getUsers();
    props.getPatientReponses();
    props.getPatientQuestionnaires();
    props.getGroupeDePatients();
    props.getMedecins()
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...patientEntity,
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
  const handleChangeMedecin = (event: React.ChangeEvent<HTMLInputElement>) => {
    props.getMedecinwithCentre(event.currentTarget.value); }

const handleChange: React.ReactEventHandler<HTMLInputElement> = (ev) => { 

 props.getCentres2(ev.currentTarget.value);
 //this.props.forceUpdate();
 //props.getMedecinwithCentre(props.centres[0].id.toString()) ; 
 
}

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="tnTsuiviApp.patient.home.createOrEditLabel">
            <Translate contentKey="tnTsuiviApp.patient.home.createOrEditLabel">Create or edit a Patient</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : patientEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="patient-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="patient-id" type="text" className="form-control" name="id" required readOnly />
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
                <Label id="phoneLabel" for="patient-phone">
                  <Translate contentKey="tnTsuiviApp.patient.phone">Phone</Translate>
                </Label>
                <AvField
                  id="patient-phone"
                  type="text"
                  name="phone"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="adressLabel" for="patient-adress">
                  <Translate contentKey="tnTsuiviApp.patient.adress">Adress</Translate>
                </Label>
                <AvField
                  id="patient-adress"
                  type="text"
                  name="adress"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="sexeLabel" for="patient-sexe">
                  <Translate contentKey="tnTsuiviApp.patient.sexe">Sexe</Translate>
                </Label>
                <AvInput
                  id="patient-sexe"
                  type="select"
                  className="form-control"
                  name="sexe"
                  value={(!isNew && patientEntity.sexe) || 'Feminin'}
                >
                  <option value="Feminin">{translate('tnTsuiviApp.Sexe.Feminin')}</option>
                  <option value="Masculin">{translate('tnTsuiviApp.Sexe.Masculin')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="alcoolLabel" for="patient-alcool">
                  <Translate contentKey="tnTsuiviApp.patient.alcool">Alcool</Translate>
                </Label>
                <AvInput
                  id="patient-alcool"
                  type="select"
                  className="form-control"
                  name="alcool"
                  value={(!isNew && patientEntity.alcool) || 'No'}
                >
                  <option value="No">{translate('tnTsuiviApp.Alcool.No')}</option>
                  <option value="Yes">{translate('tnTsuiviApp.Alcool.Yes')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="startDateAlcoolLabel" for="patient-startDateAlcool">
                  <Translate contentKey="tnTsuiviApp.patient.startDateAlcool">Start Date Alcool</Translate>
                </Label>
                <AvField id="patient-startDateAlcool" type="date" className="form-control" name="startDateAlcool" />
              </AvGroup>
              <AvGroup>
                <Label id="endDateAlcoolLabel" for="patient-endDateAlcool">
                  <Translate contentKey="tnTsuiviApp.patient.endDateAlcool">End Date Alcool</Translate>
                </Label>
                <AvField id="patient-endDateAlcool" type="date" className="form-control" name="endDateAlcool" />
              </AvGroup>
              <AvGroup>
                <Label id="tobacooLabel" for="patient-tobacoo">
                  <Translate contentKey="tnTsuiviApp.patient.tobacoo">Tobacoo</Translate>
                </Label>
                <AvInput
                  id="patient-tobacoo"
                  type="select"
                  className="form-control"
                  name="tobacoo"
                  value={(!isNew && patientEntity.tobacoo) || 'No'}
                >
                  <option value="No">{translate('tnTsuiviApp.Tobacco.No')}</option>
                  <option value="Yes">{translate('tnTsuiviApp.Tobacco.Yes')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="startDateTobaccoLabel" for="patient-startDateTobacco">
                  <Translate contentKey="tnTsuiviApp.patient.startDateTobacco">Start Date Tobacco</Translate>
                </Label>
                <AvField id="patient-startDateTobacco" type="date" className="form-control" name="startDateTobacco" />
              </AvGroup>
              <AvGroup>
                <Label id="endDateTobaccoLabel" for="patient-endDateTobacco">
                  <Translate contentKey="tnTsuiviApp.patient.endDateTobacco">End Date Tobacco</Translate>
                </Label>
                <AvField id="patient-endDateTobacco" type="date" className="form-control" name="endDateTobacco" />
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
                    </AvGroup> ):null}
             

                    { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN]) ?  (
              <AvGroup>
                <Label for="patient-centre">
                  <Translate contentKey="tnTsuiviApp.patient.centre">Centre</Translate>
                </Label>
                <AvInput
                  id="patient-centre"
                  type="select"  onChange={handleChangeMedecin}  
                  className="form-control"
                  name="centre.id"
                  value={isNew ? centres[0] && centres[0].id : patientEntity.centre.id}
                  required
                >
                           <option value="" key="0" />
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
              ):null}
             
              <AvGroup>
                <Label for="patient-medecin">
                  <Translate contentKey="tnTsuiviApp.patient.medecin">Medecin</Translate>
                </Label>
                <AvInput id="patient-medecin" type="select" className="form-control" name="medecin.id">
                  {medecins
                    ? medecins.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.fullName}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>

              <Button tag={Link} id="cancel-save" to="/patient" replace color="info">
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
  regions: storeState.region.entities,
  user: storeState.userManagement.user,
  users: storeState.userManagement.users,
  patientReponses: storeState.patientReponse.entities,
  medecins: storeState.medecin.entities,
  centres: storeState.centre.entities,
  patientQuestionnaires: storeState.patientQuestionnaire.entities,
  groupeDePatients: storeState.groupeDePatient.entities,
  patientEntity: storeState.patient.entity,
  loading: storeState.patient.loading,
  updating: storeState.patient.updating,
  updateSuccess: storeState.patient.updateSuccess
});

const mapDispatchToProps = {
  getRegions,
  getCentres2,
  getMedecinwithCentre,
  getUsers,
  getPatientReponses,
  getMedecins,
  getCentres,
  getPatientQuestionnaires,
  getGroupeDePatients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientUpdate);
