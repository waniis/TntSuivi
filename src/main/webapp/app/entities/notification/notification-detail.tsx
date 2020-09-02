import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './notification.reducer';
import { INotification } from 'app/shared/model/notification.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INotificationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const NotificationDetail = (props: INotificationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { notificationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.notification.detail.title">Notification</Translate> [<b>{notificationEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="description">
              <Translate contentKey="tnTsuiviApp.notification.description">Description</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.description}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="tnTsuiviApp.notification.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={notificationEntity.dateTime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="link">
              <Translate contentKey="tnTsuiviApp.notification.link">Link</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.link}</dd>
          <dt>
            <span id="seen">
              <Translate contentKey="tnTsuiviApp.notification.seen">Seen</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.seen ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.notification.user">User</Translate>
          </dt>
          <dd>{notificationEntity.user ? notificationEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/notification" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification/${notificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ notification }: IRootState) => ({
  notificationEntity: notification.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(NotificationDetail);
