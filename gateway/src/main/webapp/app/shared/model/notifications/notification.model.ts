import dayjs from 'dayjs';
import { NotificationType } from 'app/shared/model/enumerations/notification-type.model';
import { NotificationChannel } from 'app/shared/model/enumerations/notification-channel.model';

export interface INotification {
  id?: number;
  login?: string;
  title?: string;
  message?: string;
  type?: keyof typeof NotificationType;
  channel?: keyof typeof NotificationChannel;
  sentAt?: dayjs.Dayjs;
  isRead?: boolean;
  createdAt?: dayjs.Dayjs;
  readAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<INotification> = {
  isRead: false,
};
