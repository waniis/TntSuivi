import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './groupe-de-patient.reducer';
import { IGroupeDePatient } from 'app/shared/model/groupe-de-patient.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export interface IGroupeDePatientDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const GroupeDePatientDetail = (props: IGroupeDePatientDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { groupeDePatientEntity ,account } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.groupeDePatient.detail.title">GroupeDePatient</Translate> [<b>{groupeDePatientEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="tnTsuiviApp.groupeDePatient.name">Name</Translate>
            </span>
          </dt>
          <dd>{groupeDePatientEntity.name}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.groupeDePatient.patient">Patient</Translate>
          </dt>
          <dd>
            {groupeDePatientEntity.patients
              ? groupeDePatientEntity.patients.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.fullName}</a>
                    {i === groupeDePatientEntity.patients.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN_CENTRE]) ?  (
          <dt>
            <Translate contentKey="tnTsuiviApp.groupeDePatient.medecin">Medecin</Translate>
          </dt>   ):null}
          { hasAnyAuthority(account.authorities ,[AUTHORITIES.ADMIN_CENTRE]) ?  (
          <dd>{groupeDePatientEntity.medecin ? groupeDePatientEntity.medecin.fullName : ''}</dd>  ):null}
        </dl>
        <Button tag={Link} to="/groupe-de-patient" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/groupe-de-patient/${groupeDePatientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ groupeDePatient,authentication }: IRootState) => ({
  account:authentication.account,
  groupeDePatientEntity: groupeDePatient.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(GroupeDePatientDetail);
