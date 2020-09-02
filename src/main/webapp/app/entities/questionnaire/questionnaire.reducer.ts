import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IQuestionnaire, defaultValue } from 'app/shared/model/questionnaire.model';

export const ACTION_TYPES = {
  FETCH_QUESTIONNAIRE_LIST: 'questionnaire/FETCH_QUESTIONNAIRE_LIST',
  FETCH_QUESTIONNAIRE: 'questionnaire/FETCH_QUESTIONNAIRE',
  CREATE_QUESTIONNAIRE: 'questionnaire/CREATE_QUESTIONNAIRE',
  UPDATE_QUESTIONNAIRE: 'questionnaire/UPDATE_QUESTIONNAIRE',
  DELETE_QUESTIONNAIRE: 'questionnaire/DELETE_QUESTIONNAIRE',
  RESET: 'questionnaire/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IQuestionnaire>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type QuestionnaireState = Readonly<typeof initialState>;

// Reducer

export default (state: QuestionnaireState = initialState, action): QuestionnaireState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_QUESTIONNAIRE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_QUESTIONNAIRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_QUESTIONNAIRE):
    case REQUEST(ACTION_TYPES.UPDATE_QUESTIONNAIRE):
    case REQUEST(ACTION_TYPES.DELETE_QUESTIONNAIRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_QUESTIONNAIRE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_QUESTIONNAIRE):
    case FAILURE(ACTION_TYPES.CREATE_QUESTIONNAIRE):
    case FAILURE(ACTION_TYPES.UPDATE_QUESTIONNAIRE):
    case FAILURE(ACTION_TYPES.DELETE_QUESTIONNAIRE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUESTIONNAIRE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUESTIONNAIRE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_QUESTIONNAIRE):
    case SUCCESS(ACTION_TYPES.UPDATE_QUESTIONNAIRE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_QUESTIONNAIRE):
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

const apiUrl = 'api/questionnaires';

// Actions

export const getEntities: ICrudGetAllAction<IQuestionnaire> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_QUESTIONNAIRE_LIST,
  payload: axios.get<IQuestionnaire>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IQuestionnaire> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_QUESTIONNAIRE,
    payload: axios.get<IQuestionnaire>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IQuestionnaire> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_QUESTIONNAIRE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IQuestionnaire> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_QUESTIONNAIRE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IQuestionnaire> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_QUESTIONNAIRE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
