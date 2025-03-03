import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { InvestmentType } from 'app/shared/model/enumerations/investment-type.model';
import { ActivityType } from 'app/shared/model/enumerations/activity-type.model';
import { StatusType } from 'app/shared/model/enumerations/status-type.model';
import { RiskLevel } from 'app/shared/model/enumerations/risk-level.model';
import { createEntity, getEntity, reset, updateEntity } from './investment-activity.reducer';

export const InvestmentActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const investmentActivityEntity = useAppSelector(state => state.gateway.investmentActivity.entity);
  const loading = useAppSelector(state => state.gateway.investmentActivity.loading);
  const updating = useAppSelector(state => state.gateway.investmentActivity.updating);
  const updateSuccess = useAppSelector(state => state.gateway.investmentActivity.updateSuccess);
  const investmentTypeValues = Object.keys(InvestmentType);
  const activityTypeValues = Object.keys(ActivityType);
  const statusTypeValues = Object.keys(StatusType);
  const riskLevelValues = Object.keys(RiskLevel);

  const handleClose = () => {
    navigate(`/investment-activity${location.search}`);
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
    if (values.targetAmount !== undefined && typeof values.targetAmount !== 'number') {
      values.targetAmount = Number(values.targetAmount);
    }
    if (values.currentAmount !== undefined && typeof values.currentAmount !== 'number') {
      values.currentAmount = Number(values.currentAmount);
    }
    values.activityDate = convertDateTimeToServer(values.activityDate);
    if (values.activityAmount !== undefined && typeof values.activityAmount !== 'number') {
      values.activityAmount = Number(values.activityAmount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...investmentActivityEntity,
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
          activityDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          investmentType: 'REAL_ESTATE',
          activityType: 'PROJECT',
          status: 'ACTIVE',
          riskLevel: 'LOW',
          ...investmentActivityEntity,
          activityDate: convertDateTimeFromServer(investmentActivityEntity.activityDate),
          createdDate: convertDateTimeFromServer(investmentActivityEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(investmentActivityEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.investmentsInvestmentActivity.home.createOrEditLabel" data-cy="InvestmentActivityCreateUpdateHeading">
            <Translate contentKey="gatewayApp.investmentsInvestmentActivity.home.createOrEditLabel">
              Create or edit a InvestmentActivity
            </Translate>
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
                  id="investment-activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.investmentType')}
                id="investment-activity-investmentType"
                name="investmentType"
                data-cy="investmentType"
                type="select"
              >
                {investmentTypeValues.map(investmentType => (
                  <option value={investmentType} key={investmentType}>
                    {translate(`gatewayApp.InvestmentType.${investmentType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.activityType')}
                id="investment-activity-activityType"
                name="activityType"
                data-cy="activityType"
                type="select"
              >
                {activityTypeValues.map(activityType => (
                  <option value={activityType} key={activityType}>
                    {translate(`gatewayApp.ActivityType.${activityType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.projectName')}
                id="investment-activity-projectName"
                name="projectName"
                data-cy="projectName"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.description')}
                id="investment-activity-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.location')}
                id="investment-activity-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.targetAmount')}
                id="investment-activity-targetAmount"
                name="targetAmount"
                data-cy="targetAmount"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.currentAmount')}
                id="investment-activity-currentAmount"
                name="currentAmount"
                data-cy="currentAmount"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.bondIssuer')}
                id="investment-activity-bondIssuer"
                name="bondIssuer"
                data-cy="bondIssuer"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.activityDate')}
                id="investment-activity-activityDate"
                name="activityDate"
                data-cy="activityDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.activityAmount')}
                id="investment-activity-activityAmount"
                name="activityAmount"
                data-cy="activityAmount"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.status')}
                id="investment-activity-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusTypeValues.map(statusType => (
                  <option value={statusType} key={statusType}>
                    {translate(`gatewayApp.StatusType.${statusType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.riskLevel')}
                id="investment-activity-riskLevel"
                name="riskLevel"
                data-cy="riskLevel"
                type="select"
              >
                {riskLevelValues.map(riskLevel => (
                  <option value={riskLevel} key={riskLevel}>
                    {translate(`gatewayApp.RiskLevel.${riskLevel}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.login')}
                id="investment-activity-login"
                name="login"
                data-cy="login"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.createdBy')}
                id="investment-activity-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.createdDate')}
                id="investment-activity-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.lastModifiedBy')}
                id="investment-activity-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.investmentsInvestmentActivity.lastModifiedDate')}
                id="investment-activity-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/investment-activity" replace color="info">
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

export default InvestmentActivityUpdate;
