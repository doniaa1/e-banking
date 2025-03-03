import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { DataType } from 'app/shared/model/enumerations/data-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { createEntity, getEntity, reset, updateEntity } from './data-collection.reducer';

export const DataCollectionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dataCollectionEntity = useAppSelector(state => state.gateway.dataCollection.entity);
  const loading = useAppSelector(state => state.gateway.dataCollection.loading);
  const updating = useAppSelector(state => state.gateway.dataCollection.updating);
  const updateSuccess = useAppSelector(state => state.gateway.dataCollection.updateSuccess);
  const dataTypeValues = Object.keys(DataType);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate(`/data-collection${location.search}`);
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
    values.collectedAt = convertDateTimeToServer(values.collectedAt);

    const entity = {
      ...dataCollectionEntity,
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
          collectedAt: displayDefaultDateTime(),
        }
      : {
          dataType: 'TEXT',
          status: 'ACTIVE',
          ...dataCollectionEntity,
          collectedAt: convertDateTimeFromServer(dataCollectionEntity.collectedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.analyticsDataCollection.home.createOrEditLabel" data-cy="DataCollectionCreateUpdateHeading">
            <Translate contentKey="gatewayApp.analyticsDataCollection.home.createOrEditLabel">Create or edit a DataCollection</Translate>
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
                  id="data-collection-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.login')}
                id="data-collection-login"
                name="login"
                data-cy="login"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.name')}
                id="data-collection-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.source')}
                id="data-collection-source"
                name="source"
                data-cy="source"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.collectedAt')}
                id="data-collection-collectedAt"
                name="collectedAt"
                data-cy="collectedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.dataType')}
                id="data-collection-dataType"
                name="dataType"
                data-cy="dataType"
                type="select"
              >
                {dataTypeValues.map(dataType => (
                  <option value={dataType} key={dataType}>
                    {translate(`gatewayApp.DataType.${dataType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.status')}
                id="data-collection-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate(`gatewayApp.Status.${status}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.analyticsDataCollection.description')}
                id="data-collection-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/data-collection" replace color="info">
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

export default DataCollectionUpdate;
