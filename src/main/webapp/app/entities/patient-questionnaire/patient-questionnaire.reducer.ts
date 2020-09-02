import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPatientQuestionnaire, defaultValue } from 'app/shared/model/patient-questionnaire.model';

export const ACTION_TYPES = {
  FETCH_PATIENTQUESTIONNAIRE_LIST: 'patientQuestionnaire/FETCH_PATIENTQUESTIONNAIRE_LIST',
  FETCH_PATIENTQUESTIONNAIRE: 'patientQuestionnaire/FETCH_PATIENTQUESTIONNAIRE',
  CREATE_PATIENTQUESTIONNAIRE: 'patientQuestionnaire/CREATE_PATIENTQUESTIONNAIRE',
  UPDATE_PATIENTQUESTIONNAIRE: 'patientQuestionnaire/UPDATE_PATIENTQUESTIONNAIRE',
  DELETE_PATIENTQUESTIONNAIRE: 'patientQuestionnaire/DELETE_PATIENTQUESTIONNAIRE',
  RESET: 'patientQuestionnaire/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPatientQuestionnaire>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PatientQuestionnaireState = Readonly<typeof initialState>;

// Reducer

export default (state: PatientQuestionnaireState = initialState, action): PatientQuestionnaireState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PATIENTQUESTIONNAIRE):
    case REQUEST(ACTION_TYPES.UPDATE_PATIENTQUESTIONNAIRE):
    case REQUEST(ACTION_TYPES.DELETE_PATIENTQUESTIONNAIRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE):
    case FAILURE(ACTION_TYPES.CREATE_PATIENTQUESTIONNAIRE):
    case FAILURE(ACTION_TYPES.UPDATE_PATIENTQUESTIONNAIRE):
    case FAILURE(ACTION_TYPES.DELETE_PATIENTQUESTIONNAIRE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PATIENTQUESTIONNAIRE):
    case SUCCESS(ACTION_TYPES.UPDATE_PATIENTQUESTIONNAIRE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PATIENTQUESTIONNAIRE):
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

const apiUrl = 'api/patient-questionnaires';

// Actions

export const getEntities: ICrudGetAllAction<IPatientQuestionnaire> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE_LIST,
  payload: axios.get<IPatientQuestionnaire>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPatientQuestionnaire> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PATIENTQUESTIONNAIRE,
    payload: axios.get<IPatientQuestionnaire>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPatientQuestionnaire> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PATIENTQUESTIONNAIRE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPatientQuestionnaire> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PATIENTQUESTIONNAIRE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPatientQuestionnaire> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PATIENTQUESTIONNAIRE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
