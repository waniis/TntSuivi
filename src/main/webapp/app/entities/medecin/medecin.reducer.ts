import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction, ICrudGetAllActionByFullName, ICrudGetAllActionByCenter } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMedecin, defaultValue } from 'app/shared/model/medecin.model';

export const ACTION_TYPES = {
  FETCH_MEDECIN_LIST: 'medecin/FETCH_MEDECIN_LIST',
  FETCH_MEDECIN: 'medecin/FETCH_MEDECIN',
  CREATE_MEDECIN: 'medecin/CREATE_MEDECIN',
  UPDATE_MEDECIN: 'medecin/UPDATE_MEDECIN',
  DELETE_MEDECIN: 'medecin/DELETE_MEDECIN',
  RESET: 'medecin/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMedecin>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type MedecinState = Readonly<typeof initialState>;

// Reducer

export default (state: MedecinState = initialState, action): MedecinState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MEDECIN_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MEDECIN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MEDECIN):
    case REQUEST(ACTION_TYPES.UPDATE_MEDECIN):
    case REQUEST(ACTION_TYPES.DELETE_MEDECIN):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MEDECIN_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MEDECIN):
    case FAILURE(ACTION_TYPES.CREATE_MEDECIN):
    case FAILURE(ACTION_TYPES.UPDATE_MEDECIN):
    case FAILURE(ACTION_TYPES.DELETE_MEDECIN):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDECIN_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_MEDECIN):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MEDECIN):
    case SUCCESS(ACTION_TYPES.UPDATE_MEDECIN):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MEDECIN):
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

const apiUrl = 'api/medecins';

// Actions

export const getEntities: ICrudGetAllAction<IMedecin> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MEDECIN_LIST,
    payload: axios.get<IMedecin>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IMedecin> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MEDECIN,
    payload: axios.get<IMedecin>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMedecin> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MEDECIN,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMedecin> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MEDECIN,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMedecin> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MEDECIN,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const SearchMedecinByFullName: ICrudGetAllActionByFullName<IMedecin> = (fullName) => ({
  type: ACTION_TYPES.FETCH_MEDECIN_LIST,
  payload: axios.get<IMedecin>(`${apiUrl}?fullName.contains=${fullName}`)
});


export const getEntitiesWithCenter: ICrudGetAllActionByCenter<IMedecin> = (center) => {
  const requestUrl = `${apiUrl}${`?centreId.equals=${center}` }`;
  return {
    type: ACTION_TYPES.FETCH_MEDECIN_LIST,
    payload: axios.get<IMedecin>(`${requestUrl}`)
  };
};


export const reset = () => ({
  type: ACTION_TYPES.RESET
});
