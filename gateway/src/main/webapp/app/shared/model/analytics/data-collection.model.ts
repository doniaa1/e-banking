import dayjs from 'dayjs';
import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IDataCollection {
  id?: number;
  login?: string;
  name?: string;
  source?: string;
  collectedAt?: dayjs.Dayjs;
  dataType?: keyof typeof DataType | null;
  status?: keyof typeof Status;
  description?: string | null;
}

export const defaultValue: Readonly<IDataCollection> = {};
