import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction, ICrudGetAllActionWithIdPatientQuestionnaire } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPatientReponse, defaultValue } from 'app/shared/model/patient-reponse.model';

export const ACTION_TYPES = {
  FETCH_PATIENTREPONSE_LIST: 'patientReponse/FETCH_PATIENTREPONSE_LIST',
  FETCH_PATIENTREPONSE: 'patientReponse/FETCH_PATIENTREPONSE',
  CREATE_PATIENTREPONSE: 'patientReponse/CREATE_PATIENTREPONSE',
  UPDATE_PATIENTREPONSE: 'patientReponse/UPDATE_PATIENTREPONSE',
  DELETE_PATIENTREPONSE: 'patientReponse/DELETE_PATIENTREPONSE',
  RESET: 'patientReponse/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPatientReponse>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PatientReponseState = Readonly<typeof initialState>;

// Reducer

export default (state: PatientReponseState = initialState, action): PatientReponseState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PATIENTREPONSE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PATIENTREPONSE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PATIENTREPONSE):
    case REQUEST(ACTION_TYPES.UPDATE_PATIENTREPONSE):
    case REQUEST(ACTION_TYPES.DELETE_PATIENTREPONSE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PATIENTREPONSE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PATIENTREPONSE):
    case FAILURE(ACTION_TYPES.CREATE_PATIENTREPONSE):
    case FAILURE(ACTION_TYPES.UPDATE_PATIENTREPONSE):
    case FAILURE(ACTION_TYPES.DELETE_PATIENTREPONSE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATIENTREPONSE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATIENTREPONSE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PATIENTREPONSE):
    case SUCCESS(ACTION_TYPES.UPDATE_PATIENTREPONSE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PATIENTREPONSE):
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

const apiUrl = 'api/patient-reponses';

// Actions

export const getEntities: ICrudGetAllAction<IPatientReponse> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PATIENTREPONSE_LIST,
  payload: axios.get<IPatientReponse>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntitiesWithId: ICrudGetAllActionWithIdPatientQuestionnaire<IPatientReponse> = (id) => ({
  type: ACTION_TYPES.FETCH_PATIENTREPONSE_LIST,
  payload: axios.get<IPatientReponse>(`${apiUrl}/${id}`)
});


export const getEntity: ICrudGetAction<IPatientReponse> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PATIENTREPONSE,
    payload: axios.get<IPatientReponse>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPatientReponse> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PATIENTREPONSE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPatientReponse> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PATIENTREPONSE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPatientReponse> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PATIENTREPONSE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
