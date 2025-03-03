import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBankAccounts } from 'app/entities/accounts/bank-account/bank-account.reducer';
import { CurrencyType } from 'app/shared/model/enumerations/currency-type.model';
import { createEntity, getEntity, reset, updateEntity } from './international-transfer.reducer';

export const InternationalTransferUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bankAccounts = useAppSelector(state => state.gateway.bankAccount.entities);
  const internationalTransferEntity = useAppSelector(state => state.gateway.internationalTransfer.entity);
  const loading = useAppSelector(state => state.gateway.internationalTransfer.loading);
  const updating = useAppSelector(state => state.gateway.internationalTransfer.updating);
  const updateSuccess = useAppSelector(state => state.gateway.internationalTransfer.updateSuccess);
  const currencyTypeValues = Object.keys(CurrencyType);

  const handleClose = () => {
    navigate(`/international-transfer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBankAccounts({}));
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.transactionDate = convertDateTimeToServer(values.transactionDate);

    const entity = {
      ...internationalTransferEntity,
      ...values,
      bankAccount: bankAccounts.find(it => it.id.toString() === values.bankAccount?.toString()),
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
          transactionDate: displayDefaultDateTime(),
        }
      : {
          currency: 'USD',
          ...internationalTransferEntity,
          transactionDate: convertDateTimeFromServer(internationalTransferEntity.transactionDate),
          bankAccount: internationalTransferEntity?.bankAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.accountsInternationalTransfer.home.createOrEditLabel" data-cy="InternationalTransferCreateUpdateHeading">
            <Translate contentKey="gatewayApp.accountsInternationalTransfer.home.createOrEditLabel">
              Create or edit a InternationalTransfer
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
                  id="international-transfer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.senderAccountNumber')}
                id="international-transfer-senderAccountNumber"
                name="senderAccountNumber"
                data-cy="senderAccountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.recipientIban')}
                id="international-transfer-recipientIban"
                name="recipientIban"
                data-cy="recipientIban"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 15, message: translate('entity.validation.minlength', { min: 15 }) },
                  maxLength: { value: 34, message: translate('entity.validation.maxlength', { max: 34 }) },
                  pattern: { value: /^[A-Z0-9]+$/, message: translate('entity.validation.pattern', { pattern: '^[A-Z0-9]+$' }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.swiftCode')}
                id="international-transfer-swiftCode"
                name="swiftCode"
                data-cy="swiftCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 11, message: translate('entity.validation.maxlength', { max: 11 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.recipientName')}
                id="international-transfer-recipientName"
                name="recipientName"
                data-cy="recipientName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.amount')}
                id="international-transfer-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.currency')}
                id="international-transfer-currency"
                name="currency"
                data-cy="currency"
                type="select"
              >
                {currencyTypeValues.map(currencyType => (
                  <option value={currencyType} key={currencyType}>
                    {translate(`gatewayApp.CurrencyType.${currencyType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.transactionDate')}
                id="international-transfer-transactionDate"
                name="transactionDate"
                data-cy="transactionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsInternationalTransfer.description')}
                id="international-transfer-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="international-transfer-bankAccount"
                name="bankAccount"
                data-cy="bankAccount"
                label={translate('gatewayApp.accountsInternationalTransfer.bankAccount')}
                type="select"
                required
              >
                <option value="" key="0" />
                {bankAccounts
                  ? bankAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.iban}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/international-transfer" replace color="info">
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

export default InternationalTransferUpdate;
