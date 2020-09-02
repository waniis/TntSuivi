import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './question-answer.reducer';
import { IQuestionAnswer } from 'app/shared/model/question-answer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQuestionAnswerDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const QuestionAnswerDetail = (props: IQuestionAnswerDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { questionAnswerEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="tnTsuiviApp.questionAnswer.detail.title">QuestionAnswer</Translate> [<b>{questionAnswerEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="label">
              <Translate contentKey="tnTsuiviApp.questionAnswer.label">Label</Translate>
            </span>
          </dt>
          <dd>{questionAnswerEntity.label}</dd>
          <dt>
            <Translate contentKey="tnTsuiviApp.questionAnswer.question">Question</Translate>
          </dt>
          <dd>{questionAnswerEntity.question ? questionAnswerEntity.question.label : ''}</dd>
        </dl>
        <Button tag={Link} to="/question-answer" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question-answer/${questionAnswerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ questionAnswer }: IRootState) => ({
  questionAnswerEntity: questionAnswer.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(QuestionAnswerDetail);
