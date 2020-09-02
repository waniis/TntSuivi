import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getEntities as getPatients } from 'app/entities/patient/patient.reducer';

import { IRootState } from 'app/shared/reducers';
import { getEntities ,createEntity} from './message.reducer';
import { IMessage } from 'app/shared/model/message.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import {Launcher} from 'react-chat-window';
import value from '*.json';
import { AccountMenu } from 'app/shared/layout/menus';
import { now } from 'moment';
import ChatBox, { ChatFrame } from 'react-chat-plugin';
import { once } from 'lodash';
import { url } from 'inspector';
import { URL } from 'url';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { mount } from 'enzyme';
export interface IMessageProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}


export const Message = (props: IMessageProps) => {
  
 
  const [other , setOther] = useState(0);
  const { messageEntity,messageList,patients, account, match, loading } = props;
 
  const [attr, setAttr] = useState({
    showChatbox: false,
    showIcon: true,
    messages: [
    
    ],
  });
  const handleClickIcon = () => {
    // toggle showChatbox and showIcon
    setAttr({
      ...attr,
      showChatbox: !attr.showChatbox,
      showIcon: !attr.showIcon,
    });
  };
  const saveEntity = (text) => {
    const entity = {
    contenu:text,
    receiver: {
     id :other,
    } 
    };

    props.createEntity(entity);
     
  
};
  const handleOnSendMessage = (message) => {
 
    saveEntity(message);
     
  };
  
 
   

  
  useEffect(() => {
   hasAnyAuthority(account.authorities ,[AUTHORITIES.MEDECIN]) ?  (
    props.getPatients()  ) : null

    props.getEntities() ;     


    attr.messages=[];
    const valuess = [...attr .messages];

   
     messageList.map((msg) =>(


      valuess.push( {   author: {
        username: msg.sender.firstName,
        id: msg.sender.id,
        avatarUrl: 'https://image.flaticon.com/icons/svg/2446/2446032.svg',
      },
      receiver :msg.receiver.id,
      text: msg.contenu,
      type: 'text',
      timestamp: msg.dateTime,

      } ) )) ;

    
      setAttr({
        ...attr,
        messages: valuess
       
      })
    
     
  }, [messageList.length ] , );

 
  
 
 


 

 
  return (
   
    <div>
      <h2 id="message-heading">
       
        <Translate contentKey="tnTsuiviApp.message.home.title">Messages</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.message.home.createLabel">Create new Message</Translate>
        </Link>
      </h2>
    
      <div className="table-responsive">
        {messageList && messageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.message.contenu">Contenu</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.message.dateTime">Date Time</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.message.sender">Sender</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.message.receiver">Receiver</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {messageList.map((message, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${message.id}`} color="link" size="sm">
                      {message.id}
                    </Button>
                  </td>
                  <td>{message.contenu}</td>
                  <td>{message.dateTime ? <TextFormat type="date" value={message.dateTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{message.sender ? message.sender.id : ''}</td>
                  <td>{message.receiver ? message.receiver.id : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${message.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${message.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${message.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="tnTsuiviApp.message.home.notFound">No Messages found</Translate>
            </div>
          )
        )}
      </div>
     
       <ChatFrame
      chatbox={

        <div>
        <ChatBox
          onSendMessage={handleOnSendMessage}
          userId={account.id}
          messages={ hasAnyAuthority(account.authorities ,[AUTHORITIES.MEDECIN]) ?  (attr.messages.filter(msg =>  msg.author? ((msg.author.id===other && msg.receiver===account.id) || ( msg.author.id === account.id && msg.receiver===other)): null) ):attr.messages}
          width={'300px'}
     
        />
          
        
      {patients
                    ? patients.map(otherEntity => (

               
                      <button key= {otherEntity.user.id} onClick={() => (setOther(otherEntity.user.id)   )}>
                       {otherEntity.fullName}
                    </button> )):null}
                     
      </div>
      }
      
      clickIcon={handleClickIcon}
      showChatbox={attr.showChatbox}
      showIcon={attr.showIcon}
      iconStyle={{ background: 'red', fill: 'white' }}
    >
      <div className="Greeting" style={{ width: '300px' }}>
        👋 Hey, I’m a ChatBot! Want to see what I can do?
      </div>
    </ChatFrame>
 
      
    </div>
    
  );
};

const mapStateToProps = ({ message,authentication,patient }: IRootState) => ({
  messageList: message.entities,
  account: authentication.account,
  loading: message.loading,
  messageEntity: message.entity,
  patients: patient.entities
});

const mapDispatchToProps = {
  getEntities,
  createEntity,
  getPatients,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Message);
