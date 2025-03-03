import dayjs from 'dayjs';
import { IDataCollection } from 'app/shared/model/analytics/data-collection.model';
import { AnalysisType } from 'app/shared/model/enumerations/analysis-type.model';
import { ReportType } from 'app/shared/model/enumerations/report-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IAnalysisReport {
  id?: number;
  title?: string;
  createdAt?: dayjs.Dayjs;
  analysisType?: keyof typeof AnalysisType;
  reportType?: keyof typeof ReportType;
  generatedBy?: string | null;
  content?: string;
  status?: keyof typeof Status;
  dataCollection?: IDataCollection;
}

export const defaultValue: Readonly<IAnalysisReport> = {};
