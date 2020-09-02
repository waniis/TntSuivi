import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './notification.reducer';
import { INotification } from 'app/shared/model/notification.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface INotificationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Notification = (props: INotificationProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { notificationList, match, loading } = props;
  return (
    <div>
      <h2 id="notification-heading">
        <Translate contentKey="tnTsuiviApp.notification.home.title">Notifications</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.notification.home.createLabel">Create new Notification</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {notificationList && notificationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.notification.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.notification.dateTime">Date Time</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.notification.link">Link</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.notification.seen">Seen</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.notification.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {notificationList.map((notification, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${notification.id}`} color="link" size="sm">
                      {notification.id}
                    </Button>
                  </td>
                  <td>{notification.description}</td>
                  <td>
                    <TextFormat type="date" value={notification.dateTime} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    
                  <Button tag={Link} to={notification.link} color="link" size="sm">
                      Ici
                    </Button>
                     </td>
                  <td>{notification.seen ? 'true' : 'false'}</td>
                  <td>{notification.user ? notification.user.id : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${notification.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${notification.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${notification.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="tnTsuiviApp.notification.home.notFound">No Notifications found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ notification }: IRootState) => ({
  notificationList: notification.entities,
  loading: notification.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Notification);
