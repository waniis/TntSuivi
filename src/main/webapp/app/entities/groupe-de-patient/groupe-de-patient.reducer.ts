import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGroupeDePatient, defaultValue } from 'app/shared/model/groupe-de-patient.model';

export const ACTION_TYPES = {
  FETCH_GROUPEDEPATIENT_LIST: 'groupeDePatient/FETCH_GROUPEDEPATIENT_LIST',
  FETCH_GROUPEDEPATIENT: 'groupeDePatient/FETCH_GROUPEDEPATIENT',
  CREATE_GROUPEDEPATIENT: 'groupeDePatient/CREATE_GROUPEDEPATIENT',
  UPDATE_GROUPEDEPATIENT: 'groupeDePatient/UPDATE_GROUPEDEPATIENT',
  DELETE_GROUPEDEPATIENT: 'groupeDePatient/DELETE_GROUPEDEPATIENT',
  RESET: 'groupeDePatient/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGroupeDePatient>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type GroupeDePatientState = Readonly<typeof initialState>;

// Reducer

export default (state: GroupeDePatientState = initialState, action): GroupeDePatientState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GROUPEDEPATIENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GROUPEDEPATIENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GROUPEDEPATIENT):
    case REQUEST(ACTION_TYPES.UPDATE_GROUPEDEPATIENT):
    case REQUEST(ACTION_TYPES.DELETE_GROUPEDEPATIENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GROUPEDEPATIENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GROUPEDEPATIENT):
    case FAILURE(ACTION_TYPES.CREATE_GROUPEDEPATIENT):
    case FAILURE(ACTION_TYPES.UPDATE_GROUPEDEPATIENT):
    case FAILURE(ACTION_TYPES.DELETE_GROUPEDEPATIENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GROUPEDEPATIENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GROUPEDEPATIENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GROUPEDEPATIENT):
    case SUCCESS(ACTION_TYPES.UPDATE_GROUPEDEPATIENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GROUPEDEPATIENT):
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

const apiUrl = 'api/groupe-de-patients';

// Actions

export const getEntities: ICrudGetAllAction<IGroupeDePatient> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GROUPEDEPATIENT_LIST,
  payload: axios.get<IGroupeDePatient>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IGroupeDePatient> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GROUPEDEPATIENT,
    payload: axios.get<IGroupeDePatient>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGroupeDePatient> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GROUPEDEPATIENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGroupeDePatient> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GROUPEDEPATIENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGroupeDePatient> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GROUPEDEPATIENT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
