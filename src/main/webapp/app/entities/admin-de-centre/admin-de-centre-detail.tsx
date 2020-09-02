import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './admin-de-centre.reducer';
import { IAdminDeCentre } from 'app/shared/model/admin-de-centre.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAdminDeCentreDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AdminDeCentreDetail = (props: IAdminDeCentreDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { adminDeCentreEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.adminDeCentre.detail.title">AdminDeCentre</Translate> [<b>{adminDeCentreEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fullName">
              <Translate contentKey="tnTsuiviApp.adminDeCentre.fullName">Full Name</Translate>
            </span>
          </dt>
          <dd>{adminDeCentreEntity.fullName}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="tnTsuiviApp.adminDeCentre.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{adminDeCentreEntity.phone}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.adminDeCentre.user">User</Translate>
          </dt>
          <dd>{adminDeCentreEntity.user ? adminDeCentreEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.adminDeCentre.centre">Centre</Translate>
          </dt>
          <dd>{adminDeCentreEntity.centre ? adminDeCentreEntity.centre.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/admin-de-centre" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/admin-de-centre/${adminDeCentreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ adminDeCentre }: IRootState) => ({
  adminDeCentreEntity: adminDeCentre.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AdminDeCentreDetail);
