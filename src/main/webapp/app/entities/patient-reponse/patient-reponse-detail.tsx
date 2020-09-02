import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntitiesWithId } from './patient-reponse.reducer';
import { IPatientReponse } from 'app/shared/model/patient-reponse.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPatientReponseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PatientReponseDetail = (props: IPatientReponseDetailProps) => {
  useEffect(() => {
    props.getEntitiesWithId(props.match.params.id);
  }, []);

  const {   patientReponseList } = props;

  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.patientReponse.detail.title">PatientReponse</Translate> [<b> </b>]
        </h2>
        <dl className="jh-entity-details">
          

          {patientReponseList.map((patientReponse,index) => (
          
          <dt key={patientReponse.id}>
      
              {index+1 < patientReponseList.length && !(patientReponse.question.id === patientReponseList[index+1].question.id)  ? patientReponse.question.label +"?"  : ''}
            
             {patientReponse.question.typeQuestion==="Text"?  "text"+patientReponse.content : 
            
             patientReponse.questionAnswer? patientReponse.questionAnswer.label :null  }
             <br></br>
            
          </dt>
          ))}
        </dl>
        <Button tag={Link} to="/patient-questionnaire" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
      
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ patientReponse }: IRootState) => ({
  patientReponseList: patientReponse.entities,
});

const mapDispatchToProps = { getEntitiesWithId };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PatientReponseDetail);
