import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISpecialty, defaultValue } from 'app/shared/model/specialty.model';

export const ACTION_TYPES = {
  FETCH_SPECIALTY_LIST: 'specialty/FETCH_SPECIALTY_LIST',
  FETCH_SPECIALTY: 'specialty/FETCH_SPECIALTY',
  CREATE_SPECIALTY: 'specialty/CREATE_SPECIALTY',
  UPDATE_SPECIALTY: 'specialty/UPDATE_SPECIALTY',
  DELETE_SPECIALTY: 'specialty/DELETE_SPECIALTY',
  RESET: 'specialty/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISpecialty>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SpecialtyState = Readonly<typeof initialState>;

// Reducer

export default (state: SpecialtyState = initialState, action): SpecialtyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SPECIALTY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SPECIALTY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SPECIALTY):
    case REQUEST(ACTION_TYPES.UPDATE_SPECIALTY):
    case REQUEST(ACTION_TYPES.DELETE_SPECIALTY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SPECIALTY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SPECIALTY):
    case FAILURE(ACTION_TYPES.CREATE_SPECIALTY):
    case FAILURE(ACTION_TYPES.UPDATE_SPECIALTY):
    case FAILURE(ACTION_TYPES.DELETE_SPECIALTY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SPECIALTY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SPECIALTY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SPECIALTY):
    case SUCCESS(ACTION_TYPES.UPDATE_SPECIALTY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SPECIALTY):
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

const apiUrl = 'api/specialties';

// Actions

export const getEntities: ICrudGetAllAction<ISpecialty> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SPECIALTY_LIST,
  payload: axios.get<ISpecialty>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISpecialty> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SPECIALTY,
    payload: axios.get<ISpecialty>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISpecialty> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SPECIALTY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISpecialty> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SPECIALTY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISpecialty> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SPECIALTY,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
