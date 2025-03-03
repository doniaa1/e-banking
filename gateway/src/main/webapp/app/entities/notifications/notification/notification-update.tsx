import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { NotificationType } from 'app/shared/model/enumerations/notification-type.model';
import { NotificationChannel } from 'app/shared/model/enumerations/notification-channel.model';
import { createEntity, getEntity, reset, updateEntity } from './notification.reducer';

export const NotificationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const notificationEntity = useAppSelector(state => state.gateway.notification.entity);
  const loading = useAppSelector(state => state.gateway.notification.loading);
  const updating = useAppSelector(state => state.gateway.notification.updating);
  const updateSuccess = useAppSelector(state => state.gateway.notification.updateSuccess);
  const notificationTypeValues = Object.keys(NotificationType);
  const notificationChannelValues = Object.keys(NotificationChannel);

  const handleClose = () => {
    navigate(`/notification${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.sentAt = convertDateTimeToServer(values.sentAt);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.readAt = convertDateTimeToServer(values.readAt);

    const entity = {
      ...notificationEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          sentAt: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          readAt: displayDefaultDateTime(),
        }
      : {
          type: 'ALERT',
          channel: 'EMAIL',
          ...notificationEntity,
          sentAt: convertDateTimeFromServer(notificationEntity.sentAt),
          createdAt: convertDateTimeFromServer(notificationEntity.createdAt),
          readAt: convertDateTimeFromServer(notificationEntity.readAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.notificationsNotification.home.createOrEditLabel" data-cy="NotificationCreateUpdateHeading">
            <Translate contentKey="gatewayApp.notificationsNotification.home.createOrEditLabel">Create or edit a Notification</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="notification-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.login')}
                id="notification-login"
                name="login"
                data-cy="login"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.title')}
                id="notification-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.message')}
                id="notification-message"
                name="message"
                data-cy="message"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.type')}
                id="notification-type"
                name="type"
                data-cy="type"
                type="select"
              >
                {notificationTypeValues.map(notificationType => (
                  <option value={notificationType} key={notificationType}>
                    {translate(`gatewayApp.NotificationType.${notificationType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.channel')}
                id="notification-channel"
                name="channel"
                data-cy="channel"
                type="select"
              >
                {notificationChannelValues.map(notificationChannel => (
                  <option value={notificationChannel} key={notificationChannel}>
                    {translate(`gatewayApp.NotificationChannel.${notificationChannel}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.sentAt')}
                id="notification-sentAt"
                name="sentAt"
                data-cy="sentAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.isRead')}
                id="notification-isRead"
                name="isRead"
                data-cy="isRead"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.createdAt')}
                id="notification-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.notificationsNotification.readAt')}
                id="notification-readAt"
                name="readAt"
                data-cy="readAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notification" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NotificationUpdate;
