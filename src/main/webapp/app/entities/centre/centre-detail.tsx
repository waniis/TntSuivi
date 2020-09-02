import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './centre.reducer';
import { ICentre } from 'app/shared/model/centre.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICentreDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CentreDetail = (props: ICentreDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { centreEntity  } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.centre.detail.title">Centre</Translate> [<b>{centreEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="tnTsuiviApp.centre.name">Name</Translate>
            </span>
          </dt>
          <dd>{centreEntity.name}</dd>
          <dt>
            <span id="address">
              <Translate contentKey="tnTsuiviApp.centre.address">Address</Translate>
            </span>
          </dt>
          <dd>{centreEntity.address}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="tnTsuiviApp.centre.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{centreEntity.phone}</dd>
          <dt>
            <span id="fax">
              <Translate contentKey="tnTsuiviApp.centre.fax">Fax</Translate>
            </span>
          </dt>
          <dd>{centreEntity.fax}</dd>
          <dt>
            <span id="emergency">
              <Translate contentKey="tnTsuiviApp.centre.emergency">Emergency</Translate>
            </span>
          </dt>
          <dd>{centreEntity.emergency}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.centre.region">Region</Translate>
          </dt>
          <dd>{centreEntity.region ? centreEntity.region.name : ''}</dd>
          
        
          <dt>
  <span id="typeQuestion"> patients  ({ centreEntity.patients ? centreEntity.patients.length : 0}) </span>
          </dt>
          <dd>
          {centreEntity.patients
          ? centreEntity.patients.map(p => (  
             
                 <li key={p.id} >
                 {p.fullName}  
                 </li>
                
            
      ) ) 
      : null}
        
        </dd> 
      
       
        <dt>
            <span id="typeQuestion"> Medecins  ({ centreEntity.medecins ? centreEntity.medecins.length : 0}) </span>
          </dt>
          <dd>
          {centreEntity.patients
          ? centreEntity.patients.map(p => (  
            
                 <li key={p.id} >
                 {p.fullName}  
                 </li>
                
            
      ) ) 
      : null}
        
        </dd> 
        

        </dl>
        <Button tag={Link} to="/centre" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/centre/${centreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ centre }: IRootState) => ({
  centreEntity: centre.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CentreDetail);
