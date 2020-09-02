import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './medecin.reducer';
import { IMedecin } from 'app/shared/model/medecin.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMedecinDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MedecinDetail = (props: IMedecinDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { medecinEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.medecin.detail.title">Medecin</Translate> [<b>{medecinEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fullName">
              <Translate contentKey="tnTsuiviApp.medecin.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{medecinEntity.fullName}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="tnTsuiviApp.medecin.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{medecinEntity.phone}</dd>
          <dt>
            <span id="phone2">
              <Translate contentKey="tnTsuiviApp.medecin.phone2">Phone 2</Translate>
            </span>
          </dt>
          <dd>{medecinEntity.phone2}</dd>
          <dt>
            <span id="adress">
              <Translate contentKey="tnTsuiviApp.medecin.adress">Adress</Translate>
            </span>
          </dt>
          <dd>{medecinEntity.adress}</dd>
          <dt>
            <span id="sexe">
              <Translate contentKey="tnTsuiviApp.medecin.sexe">Sexe</Translate>
            </span>
          </dt>
          <dd>{medecinEntity.sexe}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.medecin.centre">Centre</Translate>
          </dt>
          <dd>{medecinEntity.centre ? medecinEntity.centre.name : ''}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.medecin.specialty">Specialty</Translate>
          </dt>
          <dd>{medecinEntity.specialty ? medecinEntity.specialty.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/medecin" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medecin/${medecinEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ medecin }: IRootState) => ({
  medecinEntity: medecin.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MedecinDetail);
