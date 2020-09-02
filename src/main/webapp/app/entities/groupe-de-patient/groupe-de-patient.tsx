import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './groupe-de-patient.reducer';
import { IGroupeDePatient } from 'app/shared/model/groupe-de-patient.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IGroupeDePatientProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const GroupeDePatient = (props: IGroupeDePatientProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { account,groupeDePatientList, match, loading } = props;
  return (
    <div>
      <h2 id="groupe-de-patient-heading">
        <Translate contentKey="tnTsuiviApp.groupeDePatient.home.title">Groupe De Patients</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.groupeDePatient.home.createLabel">Create new Groupe De Patient</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {groupeDePatientList && groupeDePatientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.groupeDePatient.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.groupeDePatient.patient">Patient</Translate>
                </th>
                { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN_CENTRE]) ?  (
                <th>
                  <Translate contentKey="tnTsuiviApp.groupeDePatient.medecin">Medecin</Translate>
                </th>):null}
                <th />
              </tr>
            </thead>
            <tbody>
              {groupeDePatientList.map((groupeDePatient, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${groupeDePatient.id}`} color="link" size="sm">
                      {groupeDePatient.id}
                    </Button>
                  </td>
                  <td>{groupeDePatient.name}</td>
                  <td>
                    {groupeDePatient.patients
                      ? groupeDePatient.patients.map((val, j) => (
                          <span key={j}>
                            <Link to={`patient/${val.id}`}>{val.fullName}</Link>
                            {j === groupeDePatient.patients.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN_CENTRE]) ?  (
                  <td>
                    {groupeDePatient.medecin.id ? <Link to={`medecin/${groupeDePatient.medecin.id}`}>{groupeDePatient.medecin.fullName}</Link> : ''}
                  </td> ):null}
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${groupeDePatient.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${groupeDePatient.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${groupeDePatient.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="tnTsuiviApp.groupeDePatient.home.notFound">No Groupe De Patients found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ groupeDePatient , authentication  }: IRootState) => ({
  account:authentication.account,
  groupeDePatientList: groupeDePatient.entities,
  loading: groupeDePatient.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GroupeDePatient);
