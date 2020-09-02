import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './patient-reponse.reducer';
import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPatientReponseProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const PatientReponse = (props: IPatientReponseProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { patientReponseList, match, loading } = props;
  return (
    <div>
      <h2 id="patient-reponse-heading">
        <Translate contentKey="tnTsuiviApp.patientReponse.home.title">Patient Reponses</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.patientReponse.home.createLabel">Create new Patient Reponse</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {patientReponseList && patientReponseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientReponse.content">Content</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientReponse.patientQuestionnaire">Patient Questionnaire</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientReponse.question">Question</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patientReponse.questionAnswer">Question Answer</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {patientReponseList.map((patientReponse, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${patientReponse.id}`} color="link" size="sm">
                      {patientReponse.id}
                    </Button>
                  </td>
                  <td>{patientReponse.content}</td>
                  <td>
                    {patientReponse.patientQuestionnaire ? (
                      <Link to={`patient-questionnaire/${patientReponse.patientQuestionnaire.id}`}>
                        {patientReponse.patientQuestionnaire.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {patientReponse.question ? (
                      <Link to={`question/${patientReponse.question.id}`}>{patientReponse.question.label}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {patientReponse.questionAnswer ? (
                      <Link to={`question-answer/${patientReponse.questionAnswer.id}`}>{patientReponse.questionAnswer.label}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${patientReponse.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${patientReponse.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${patientReponse.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="tnTsuiviApp.patientReponse.home.notFound">No Patient Reponses found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ patientReponse }: IRootState) => ({
  patientReponseList: patientReponse.entities,
  loading: patientReponse.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientReponse);
