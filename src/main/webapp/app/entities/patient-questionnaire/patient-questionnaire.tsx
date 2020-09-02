import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { MDBCol, MDBIcon } from 'mdbreact';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUser, faTimes } from "@fortawesome/free-solid-svg-icons";

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './patient-questionnaire.reducer';
import { IPatientQuestionnaire } from 'app/shared/model/patient-questionnaire.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import moment from 'moment';
import {faCheck } from '@fortawesome/free-solid-svg-icons'
 


export interface IPatientQuestionnaireProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}
const now =moment();
export const PatientQuestionnaire = (props: IPatientQuestionnaireProps) => {
  useEffect(() => {
 
    props.getEntities();
  }, []);

  const { patientQuestionnaireList, account ,match, loading } = props;
  return (
    <div>
      <h2 id="patient-questionnaire-heading">
        <Translate contentKey="tnTsuiviApp.patientQuestionnaire.home.title">Patient Questionnaires</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.patientQuestionnaire.home.createLabel">Create new Patient Questionnaire</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {patientQuestionnaireList && patientQuestionnaireList.length > 0 ? (
          <Table responsive>
     
            { hasAnyAuthority(account.authorities ,[AUTHORITIES.MEDECIN]) ? (
                <thead>
              <tr>
              
                <th>
                  <Translate contentKey="tnTsuiviApp.patientQuestionnaire.doneTimeDate">Done Time Date</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientQuestionnaire.done">Done</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientQuestionnaire.patient">Patient</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientQuestionnaire.questionnaire">Questionnaire</Translate>
                </th>
                <th />
              </tr>
              
            </thead>): (
                  <thead>
                  <tr>
                  
               
                    <th>
                      <Translate contentKey="tnTsuiviApp.patientQuestionnaire.done">Done</Translate>
                    </th>
                    <th>
                      <Translate contentKey="tnTsuiviApp.patientQuestionnaire.questionnaire">Questionnaire</Translate>
                    </th>
                    

                    <th>
                      Questionnaire date end
                    </th>
                  </tr>
                  
                </thead>

            ) }
            <tbody>
              {patientQuestionnaireList.map((patientQuestionnaire, i) => (

                 hasAnyAuthority(account.authorities ,[AUTHORITIES.MEDECIN]) ? (
                <tr key={`entity-${i}`}>
                  
                  <td>
                    <TextFormat type="date" value={patientQuestionnaire.doneTimeDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{patientQuestionnaire.done ?   <FontAwesomeIcon icon={faCheck} /> : <FontAwesomeIcon icon={faTimes} />} </td>
                  <td>
                    {patientQuestionnaire.patient ? (
                      <Link to={`patient/${patientQuestionnaire.patient.id}`}>{patientQuestionnaire.patient.fullName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {patientQuestionnaire.questionnaire ? (
                      <Link to={`questionnaire/${patientQuestionnaire.questionnaire.id}`}>{patientQuestionnaire.questionnaire.subject}</Link>
                    ) : (
                      ''
                    )}
                  </td>

                
             
                  <td className="text-right">
                  {patientQuestionnaire.done? 
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`patient-reponse/${patientQuestionnaire.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                 
                    </div> :null}
                  </td>
                </tr>
              ):
              
              <tr key={`entity-${i}`}>
              <td>{patientQuestionnaire.done ?   <FontAwesomeIcon icon="dailymotion" /> : 'false'}</td>
           
              <td>
                {patientQuestionnaire.questionnaire ? (
                  <Link to={`questionnaire/${patientQuestionnaire.questionnaire.id}`}>{patientQuestionnaire.questionnaire.subject}</Link>
                ) : (
                  ''
                )}
              </td>
              <td>
               
              <TextFormat type="date" value={patientQuestionnaire.questionnaire.endDate} format={APP_DATE_FORMAT} />
              {patientQuestionnaire.id}
              </td>

              <td className="text-right">

              {now.isBefore(patientQuestionnaire.questionnaire.endDate) && !patientQuestionnaire.done && now.isAfter(patientQuestionnaire.questionnaire.startDate)?
                <div className="btn-group flex-btn-group-container">
                  <Button tag={Link} to={`${match.url}/${patientQuestionnaire.id}/reponse`} color="info" size="sm">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span className="d-none d-md-inline">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
             
                </div> :null}
              </td>
            </tr>
              
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="tnTsuiviApp.patientQuestionnaire.home.notFound">No Patient Questionnaires found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ patientQuestionnaire,authentication }: IRootState) => ({
  patientQuestionnaireList: patientQuestionnaire.entities,
  loading: patientQuestionnaire.loading,
  account: authentication.account,
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientQuestionnaire);
