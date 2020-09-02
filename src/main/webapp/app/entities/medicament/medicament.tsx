import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './medicament.reducer';
import { IMedicament } from 'app/shared/model/medicament.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMedicamentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Medicament = (props: IMedicamentProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { medicamentList, match, loading } = props;
  return (
    <div>
      <h2 id="medicament-heading">
        <Translate contentKey="tnTsuiviApp.medicament.home.title">Medicaments</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.medicament.home.createLabel">Create new Medicament</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {medicamentList && medicamentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.medicament.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.medicament.forme">Forme</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.medicament.descrpition">Descrpition</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {medicamentList.map((medicament, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${medicament.id}`} color="link" size="sm">
                      {medicament.id}
                    </Button>
                  </td>
                  <td>{medicament.name}</td>
                  <td>
                    <Translate contentKey={`tnTsuiviApp.Forme.${medicament.forme}`} />
                  </td>
                  <td>{medicament.descrpition}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${medicament.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${medicament.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${medicament.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="tnTsuiviApp.medicament.home.notFound">No Medicaments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ medicament }: IRootState) => ({
  medicamentList: medicament.entities,
  loading: medicament.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Medicament);
