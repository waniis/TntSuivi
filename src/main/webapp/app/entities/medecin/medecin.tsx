import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { MDBCol, MDBIcon } from "mdbreact";
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
 

import { IRootState } from 'app/shared/reducers';
import { getEntities , SearchMedecinByFullName} from './medecin.reducer';
import { IMedecin } from 'app/shared/model/medecin.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IMedecinProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Medecin = (props: IMedecinProps) => {
  const handleChange: React.ReactEventHandler<HTMLInputElement> = (ev) => { 
    props.SearchMedecinByFullName(ev.currentTarget.value);
   }

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

  const { medecinList, match, loading, totalItems } = props;
  return (
    <div>
      <h2 id="medecin-heading">
        <Translate contentKey="tnTsuiviApp.medecin.home.title">Medecins</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.medecin.home.createLabel">Create new Medecin</Translate>
        </Link>
      
      
      <div className="input-group md-form form-sm form-1 pl-0">
        <div className="input-group-prepend">
        <span className="input-group-text purple lighten-3" id="basic-text1">
            <MDBIcon className="text-white" icon="search" />
          </span>
        </div>
       
        <input className="form-control my-0 py-1" onChange={handleChange}   type="text" placeholder="Search" aria-label="Search" />
      </div>
    
         
      </h2>
    
      <div className="table-responsive">
        {medecinList && medecinList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('fullName')}>
                  <Translate contentKey="tnTsuiviApp.medecin.fullName">Full Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="tnTsuiviApp.medecin.phone">Phone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('phone2')}>
                  <Translate contentKey="tnTsuiviApp.medecin.phone2">Phone 2</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('adress')}>
                  <Translate contentKey="tnTsuiviApp.medecin.adress">Adress</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('sexe')}>
                  <Translate contentKey="tnTsuiviApp.medecin.sexe">Sexe</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.medecin.user">User</Translate>  
                </th>

                <th>
                  <Translate contentKey="tnTsuiviApp.centre.region">Region</Translate>  
                </th>

                <th>
                  <Translate contentKey="tnTsuiviApp.medecin.centre">Centre</Translate>   
                </th>

                
                <th>
                  <Translate contentKey="tnTsuiviApp.medecin.specialty">Specialty</Translate>  
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {medecinList.map((medecin, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${medecin.id}`} color="link" size="sm">
                      {medecin.id}
                    </Button>
                  </td>
                  <td>{medecin.fullName}</td>
                  <td>{medecin.phone}</td>
                  <td>{medecin.phone2}</td>
                  <td>{medecin.adress}</td>
                  <td>
                    <Translate contentKey={`tnTsuiviApp.Sexe.${medecin.sexe}`} />
                  </td>
                  <td>{medecin.user ? medecin.user.id : ''}</td>
                  <td>{medecin.centre.region ? <Link to={`region/${medecin.centre.region.id}`}>{medecin.centre.region.name}</Link> : ''}</td>
                  <td>{medecin.centre ? <Link to={`centre/${medecin.centre.id}`}>{medecin.centre.name}</Link> : ''}</td>
                  <td>{medecin.specialty ? <Link to={`specialty/${medecin.specialty.id}`}>{medecin.specialty.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${medecin.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${medecin.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`${match.url}/${medecin.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="tnTsuiviApp.medecin.home.notFound">No Medecins found</Translate>
            </div>
          )
        )}
      </div>
      <div className={medecinList && medecinList.length > 0 ? '' : 'd-none'}>
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

const mapStateToProps = ({ medecin }: IRootState) => ({
  medecinList: medecin.entities,
  loading: medecin.loading,
  totalItems: medecin.totalItems
});

const mapDispatchToProps = {
  getEntities,
  SearchMedecinByFullName,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Medecin);
