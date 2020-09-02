import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IQuestionAnswer, defaultValue } from 'app/shared/model/question-answer.model';

export const ACTION_TYPES = {
  FETCH_QUESTIONANSWER_LIST: 'questionAnswer/FETCH_QUESTIONANSWER_LIST',
  FETCH_QUESTIONANSWER: 'questionAnswer/FETCH_QUESTIONANSWER',
  CREATE_QUESTIONANSWER: 'questionAnswer/CREATE_QUESTIONANSWER',
  UPDATE_QUESTIONANSWER: 'questionAnswer/UPDATE_QUESTIONANSWER',
  DELETE_QUESTIONANSWER: 'questionAnswer/DELETE_QUESTIONANSWER',
  RESET: 'questionAnswer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IQuestionAnswer>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type QuestionAnswerState = Readonly<typeof initialState>;

// Reducer

export default (state: QuestionAnswerState = initialState, action): QuestionAnswerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_QUESTIONANSWER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_QUESTIONANSWER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_QUESTIONANSWER):
    case REQUEST(ACTION_TYPES.UPDATE_QUESTIONANSWER):
    case REQUEST(ACTION_TYPES.DELETE_QUESTIONANSWER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_QUESTIONANSWER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_QUESTIONANSWER):
    case FAILURE(ACTION_TYPES.CREATE_QUESTIONANSWER):
    case FAILURE(ACTION_TYPES.UPDATE_QUESTIONANSWER):
    case FAILURE(ACTION_TYPES.DELETE_QUESTIONANSWER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUESTIONANSWER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUESTIONANSWER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_QUESTIONANSWER):
    case SUCCESS(ACTION_TYPES.UPDATE_QUESTIONANSWER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_QUESTIONANSWER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/question-answers';

// Actions

export const getEntities: ICrudGetAllAction<IQuestionAnswer> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_QUESTIONANSWER_LIST,
  payload: axios.get<IQuestionAnswer>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IQuestionAnswer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_QUESTIONANSWER,
    payload: axios.get<IQuestionAnswer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IQuestionAnswer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_QUESTIONANSWER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IQuestionAnswer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_QUESTIONANSWER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IQuestionAnswer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_QUESTIONANSWER,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
