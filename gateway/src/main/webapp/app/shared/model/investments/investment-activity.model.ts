import dayjs from 'dayjs';
import { InvestmentType } from 'app/shared/model/enumerations/investment-type.model';
import { ActivityType } from 'app/shared/model/enumerations/activity-type.model';
import { StatusType } from 'app/shared/model/enumerations/status-type.model';
import { RiskLevel } from 'app/shared/model/enumerations/risk-level.model';

export interface IInvestmentActivity {
  id?: number;
  investmentType?: keyof typeof InvestmentType;
  activityType?: keyof typeof ActivityType;
  projectName?: string | null;
  description?: string | null;
  location?: string | null;
  targetAmount?: number | null;
  currentAmount?: number | null;
  bondIssuer?: string | null;
  activityDate?: dayjs.Dayjs;
  activityAmount?: number | null;
  status?: keyof typeof StatusType | null;
  riskLevel?: keyof typeof RiskLevel;
  login?: string;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IInvestmentActivity> = {};
