import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction, ICrudGetAllActionWithRegion, ICrudGetAllActionWithMedecin } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICentre, defaultValue } from 'app/shared/model/centre.model';

export const ACTION_TYPES = {
  FETCH_CENTRE_LIST: 'centre/FETCH_CENTRE_LIST',
  FETCH_CENTRE: 'centre/FETCH_CENTRE',
  CREATE_CENTRE: 'centre/CREATE_CENTRE',
  UPDATE_CENTRE: 'centre/UPDATE_CENTRE',
  DELETE_CENTRE: 'centre/DELETE_CENTRE',
  RESET: 'centre/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICentre>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CentreState = Readonly<typeof initialState>;

// Reducer

export default (state: CentreState = initialState, action): CentreState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CENTRE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CENTRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CENTRE):
    case REQUEST(ACTION_TYPES.UPDATE_CENTRE):
    case REQUEST(ACTION_TYPES.DELETE_CENTRE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CENTRE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CENTRE):
    case FAILURE(ACTION_TYPES.CREATE_CENTRE):
    case FAILURE(ACTION_TYPES.UPDATE_CENTRE):
    case FAILURE(ACTION_TYPES.DELETE_CENTRE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CENTRE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CENTRE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CENTRE):
    case SUCCESS(ACTION_TYPES.UPDATE_CENTRE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CENTRE):
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

const apiUrl = 'api/centres';

// Actions

export const getEntities: ICrudGetAllAction<ICentre> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CENTRE_LIST,
    payload: axios.get<ICentre>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICentre> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CENTRE,
    payload: axios.get<ICentre>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICentre> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CENTRE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICentre> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CENTRE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICentre> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CENTRE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const getEntities2: ICrudGetAllActionWithRegion<ICentre> = (region) => {
  const requestUrl = `${apiUrl}${`?regionId.equals=${region}` }`;
  return {
    type: ACTION_TYPES.FETCH_CENTRE_LIST,
    payload: axios.get<ICentre>(`${requestUrl}`)
  };
};

export const getEntitiesWithIdMedecin: ICrudGetAllActionWithMedecin<ICentre> = (medecin) => {
  const requestUrl = `${apiUrl}${`?medecinId.equals=${medecin}` }`;
  return {
    type: ACTION_TYPES.FETCH_CENTRE_LIST,
    payload: axios.get<ICentre>(`${requestUrl}`)
  };
};
