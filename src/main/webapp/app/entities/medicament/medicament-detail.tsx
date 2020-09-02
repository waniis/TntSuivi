import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './medicament.reducer';
import { IMedicament } from 'app/shared/model/medicament.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMedicamentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MedicamentDetail = (props: IMedicamentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { medicamentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.medicament.detail.title">Medicament</Translate> [<b>{medicamentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="tnTsuiviApp.medicament.name">Name</Translate>
            </span>
          </dt>
          <dd>{medicamentEntity.name}</dd>
          <dt>
            <span id="forme">
              <Translate contentKey="tnTsuiviApp.medicament.forme">Forme</Translate>
            </span>
          </dt>
          <dd>{medicamentEntity.forme}</dd>
          <dt>
            <span id="descrpition">
              <Translate contentKey="tnTsuiviApp.medicament.descrpition">Descrpition</Translate>
            </span>
          </dt>
          <dd>{medicamentEntity.descrpition}</dd>
        </dl>
        <Button tag={Link} to="/medicament" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/medicament/${medicamentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ medicament }: IRootState) => ({
  medicamentEntity: medicament.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MedicamentDetail);
