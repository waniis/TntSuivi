import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './patient.reducer';
import { IPatient } from 'app/shared/model/patient.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IPatientProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Patient = (props: IPatientProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  
  const sortEntities = () => {
    getAllEntities();
    props.history.push(
      `${props.location.pathname}?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
    );
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage
    });

  const { patientList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="patient-heading">
        <Translate contentKey="tnTsuiviApp.patient.home.title">Patients</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.patient.home.createLabel">Create new Patient</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {patientList && patientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fullName')}>
                  <Translate contentKey="tnTsuiviApp.patient.fullName">Full Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="tnTsuiviApp.patient.phone">Phone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('adress')}>
                  <Translate contentKey="tnTsuiviApp.patient.adress">Adress</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sexe')}>
                  <Translate contentKey="tnTsuiviApp.patient.sexe">Sexe</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('alcool')}>
                  <Translate contentKey="tnTsuiviApp.patient.alcool">Alcool</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startDateAlcool')}>
                  <Translate contentKey="tnTsuiviApp.patient.startDateAlcool">Start Date Alcool</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endDateAlcool')}>
                  <Translate contentKey="tnTsuiviApp.patient.endDateAlcool">End Date Alcool</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('tobacoo')}>
                  <Translate contentKey="tnTsuiviApp.patient.tobacoo">Tobacoo</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('startDateTobacco')}>
                  <Translate contentKey="tnTsuiviApp.patient.startDateTobacco">Start Date Tobacco</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('endDateTobacco')}>
                  <Translate contentKey="tnTsuiviApp.patient.endDateTobacco">End Date Tobacco</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patient.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patient.medecin">Medecin</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.patient.centre">Centre</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {patientList.map((patient, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${patient.id}`} color="link" size="sm">
                      {patient.id}
                    </Button>
                  </td>
                  <td>{patient.fullName}</td>
                  <td>{patient.phone}</td>
                  <td>{patient.adress}</td>
                  <td>
                    <Translate contentKey={`tnTsuiviApp.Sexe.${patient.sexe}`} />
                  </td>
                  <td>
                    <Translate contentKey={`tnTsuiviApp.Alcool.${patient.alcool}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={patient.startDateAlcool} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={patient.endDateAlcool} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <Translate contentKey={`tnTsuiviApp.Tobacco.${patient.tobacoo}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={patient.startDateTobacco} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={patient.endDateTobacco} format={APP_LOCAL_DATE_FORMAT} />
                  </td>
                  <td>{patient.user ? patient.user.id : ''}</td>
                  <td>{patient.medecin ? <Link to={`medecin/${patient.medecin.id}`}>{patient.medecin.fullName}</Link> : ''}</td>
                  <td>{patient.centre ? <Link to={`centre/${patient.centre.id}`}>{patient.centre.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${patient.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${patient.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${patient.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                      >
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
              <Translate contentKey="tnTsuiviApp.patient.home.notFound">No Patients found</Translate>
            </div>
          )
        )}
      </div>
      <div className={patientList && patientList.length > 0 ? '' : 'd-none'}>
        <Row className="justify-content-center">
          <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
        </Row>
        <Row className="justify-content-center">
          <JhiPagination
            activePage={paginationState.activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={paginationState.itemsPerPage}
            totalItems={props.totalItems}
          />
        </Row>
      </div>
    </div>
  );
};

const mapStateToProps = ({ patient }: IRootState) => ({
  patientList: patient.entities,
  loading: patient.loading,
  totalItems: patient.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Patient);
