import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './question-answer.reducer';
import { IQuestionAnswer } from 'app/shared/model/question-answer.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQuestionAnswerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const QuestionAnswer = (props: IQuestionAnswerProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { questionAnswerList, match, loading } = props;
  return (
    <div>
      <h2 id="question-answer-heading">
        <Translate contentKey="tnTsuiviApp.questionAnswer.home.title">Question Answers</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="tnTsuiviApp.questionAnswer.home.createLabel">Create new Question Answer</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {questionAnswerList && questionAnswerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.questionAnswer.label">Label</Translate>
                </th>
                <th>
                  <Translate contentKey="tnTsuiviApp.questionAnswer.question">Question</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {questionAnswerList.map((questionAnswer, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${questionAnswer.id}`} color="link" size="sm">
                      {questionAnswer.id}
                    </Button>
                  </td>
                  <td>{questionAnswer.label}</td>
                  <td>
                    {questionAnswer.question ? (
                      <Link to={`question/${questionAnswer.question.id}`}>{questionAnswer.question.label}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${questionAnswer.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${questionAnswer.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${questionAnswer.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="tnTsuiviApp.questionAnswer.home.notFound">No Question Answers found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ questionAnswer }: IRootState) => ({
  questionAnswerList: questionAnswer.entities,
  loading: questionAnswer.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(QuestionAnswer);
