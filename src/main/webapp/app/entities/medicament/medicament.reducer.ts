import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMedicament, defaultValue } from 'app/shared/model/medicament.model';

export const ACTION_TYPES = {
  FETCH_MEDICAMENT_LIST: 'medicament/FETCH_MEDICAMENT_LIST',
  FETCH_MEDICAMENT: 'medicament/FETCH_MEDICAMENT',
  CREATE_MEDICAMENT: 'medicament/CREATE_MEDICAMENT',
  UPDATE_MEDICAMENT: 'medicament/UPDATE_MEDICAMENT',
  DELETE_MEDICAMENT: 'medicament/DELETE_MEDICAMENT',
  RESET: 'medicament/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMedicament>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MedicamentState = Readonly<typeof initialState>;

// Reducer

export default (state: MedicamentState = initialState, action): MedicamentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEDICAMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEDICAMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEDICAMENT):
    case REQUEST(ACTION_TYPES.UPDATE_MEDICAMENT):
    case REQUEST(ACTION_TYPES.DELETE_MEDICAMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MEDICAMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEDICAMENT):
    case FAILURE(ACTION_TYPES.CREATE_MEDICAMENT):
    case FAILURE(ACTION_TYPES.UPDATE_MEDICAMENT):
    case FAILURE(ACTION_TYPES.DELETE_MEDICAMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDICAMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDICAMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEDICAMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_MEDICAMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEDICAMENT):
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

const apiUrl = 'api/medicaments';

// Actions

export const getEntities: ICrudGetAllAction<IMedicament> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MEDICAMENT_LIST,
  payload: axios.get<IMedicament>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMedicament> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEDICAMENT,
    payload: axios.get<IMedicament>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMedicament> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEDICAMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMedicament> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEDICAMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMedicament> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEDICAMENT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
