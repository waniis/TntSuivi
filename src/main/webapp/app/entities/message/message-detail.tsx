import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './message.reducer';
import { IMessage } from 'app/shared/model/message.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMessageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MessageDetail = (props: IMessageDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { messageEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.message.detail.title">Message</Translate> [<b>{messageEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="contenu">
              <Translate contentKey="tnTsuiviApp.message.contenu">Contenu</Translate>
            </span>
          </dt>
          <dd>{messageEntity.contenu}</dd>
          <dt>
            <span id="dateTime">
              <Translate contentKey="tnTsuiviApp.message.dateTime">Date Time</Translate>
            </span>
          </dt>
          <dd>{messageEntity.dateTime ? <TextFormat value={messageEntity.dateTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.message.sender">Sender</Translate>
          </dt>
          <dd>{messageEntity.sender ? messageEntity.sender.id : ''}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.message.receiver">Receiver</Translate>
          </dt>
          <dd>{messageEntity.receiver ? messageEntity.receiver.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/message" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/message/${messageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ message }: IRootState) => ({
  messageEntity: message.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MessageDetail);
