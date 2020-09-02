import { IAdminDeCentre } from 'app/shared/model/admin-de-centre.model';
import { IMedecin } from 'app/shared/model/medecin.model';
import { IPatient } from 'app/shared/model/patient.model';
import { IRegion } from 'app/shared/model/region.model';

export interface ICentre {
  id?: number;
  name?: string;
  address?: string;
  phone?: string;
  fax?: string;
  emergency?: string;
  adminDeCentres?: IAdminDeCentre[];
  medecins?: IMedecin[];
  patients?: IPatient[];
  region?: IRegion;
}

export const defaultValue: Readonly<ICentre> = {};
