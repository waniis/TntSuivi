import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAdminDeCentre, defaultValue } from 'app/shared/model/admin-de-centre.model';

export const ACTION_TYPES = {
  FETCH_ADMINDECENTRE_LIST: 'adminDeCentre/FETCH_ADMINDECENTRE_LIST',
  FETCH_ADMINDECENTRE: 'adminDeCentre/FETCH_ADMINDECENTRE',
  CREATE_ADMINDECENTRE: 'adminDeCentre/CREATE_ADMINDECENTRE',
  UPDATE_ADMINDECENTRE: 'adminDeCentre/UPDATE_ADMINDECENTRE',
  DELETE_ADMINDECENTRE: 'adminDeCentre/DELETE_ADMINDECENTRE',
  RESET: 'adminDeCentre/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAdminDeCentre>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AdminDeCentreState = Readonly<typeof initialState>;

// Reducer

export default (state: AdminDeCentreState = initialState, action): AdminDeCentreState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ADMINDECENTRE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ADMINDECENTRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ADMINDECENTRE):
    case REQUEST(ACTION_TYPES.UPDATE_ADMINDECENTRE):
    case REQUEST(ACTION_TYPES.DELETE_ADMINDECENTRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ADMINDECENTRE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ADMINDECENTRE):
    case FAILURE(ACTION_TYPES.CREATE_ADMINDECENTRE):
    case FAILURE(ACTION_TYPES.UPDATE_ADMINDECENTRE):
    case FAILURE(ACTION_TYPES.DELETE_ADMINDECENTRE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADMINDECENTRE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADMINDECENTRE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ADMINDECENTRE):
    case SUCCESS(ACTION_TYPES.UPDATE_ADMINDECENTRE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ADMINDECENTRE):
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

const apiUrl = 'api/admin-de-centres';

// Actions

export const getEntities: ICrudGetAllAction<IAdminDeCentre> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ADMINDECENTRE_LIST,
  payload: axios.get<IAdminDeCentre>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAdminDeCentre> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ADMINDECENTRE,
    payload: axios.get<IAdminDeCentre>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAdminDeCentre> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ADMINDECENTRE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAdminDeCentre> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ADMINDECENTRE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAdminDeCentre> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ADMINDECENTRE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
