import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './admin-de-centre.reducer';
import { IAdminDeCentre } from 'app/shared/model/admin-de-centre.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAdminDeCentreProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const AdminDeCentre = (props: IAdminDeCentreProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);
  

  const { adminDeCentreList, match, loading } = props;
  return (
    <div>
      <h2 id="admin-de-centre-heading">
        <Translate contentKey="tnTsuiviApp.adminDeCentre.home.title">Admin De Centres</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.adminDeCentre.home.createLabel">Create new Admin De Centre</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {adminDeCentreList && adminDeCentreList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.adminDeCentre.fullName">Full Name</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.adminDeCentre.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.adminDeCentre.user">User</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.adminDeCentre.centre">Centre</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {adminDeCentreList.map((adminDeCentre, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${adminDeCentre.id}`} color="link" size="sm">
                      {adminDeCentre.id}
                    </Button>
                  </td>
                  <td>{adminDeCentre.fullName}</td>
                  <td>{adminDeCentre.phone}</td>
                  <td>{adminDeCentre.user ? adminDeCentre.user.id : ''}</td>
                  <td>{adminDeCentre.centre ? <Link to={`centre/${adminDeCentre.centre.id}`}>{adminDeCentre.centre.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${adminDeCentre.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${adminDeCentre.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${adminDeCentre.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="tnTsuiviApp.adminDeCentre.home.notFound">No Admin De Centres found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ adminDeCentre }: IRootState) => ({
  adminDeCentreList: adminDeCentre.entities,
  loading: adminDeCentre.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AdminDeCentre);
