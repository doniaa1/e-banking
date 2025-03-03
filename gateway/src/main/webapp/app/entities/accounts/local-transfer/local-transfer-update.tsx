import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getBankAccounts } from 'app/entities/accounts/bank-account/bank-account.reducer';
import { createEntity, getEntity, reset, updateEntity } from './local-transfer.reducer';

export const LocalTransferUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const bankAccounts = useAppSelector(state => state.gateway.bankAccount.entities);
  const localTransferEntity = useAppSelector(state => state.gateway.localTransfer.entity);
  const loading = useAppSelector(state => state.gateway.localTransfer.loading);
  const updating = useAppSelector(state => state.gateway.localTransfer.updating);
  const updateSuccess = useAppSelector(state => state.gateway.localTransfer.updateSuccess);

  const handleClose = () => {
    navigate(`/local-transfer${location.search}`);
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
      ...localTransferEntity,
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
          ...localTransferEntity,
          transactionDate: convertDateTimeFromServer(localTransferEntity.transactionDate),
          bankAccount: localTransferEntity?.bankAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.accountsLocalTransfer.home.createOrEditLabel" data-cy="LocalTransferCreateUpdateHeading">
            <Translate contentKey="gatewayApp.accountsLocalTransfer.home.createOrEditLabel">Create or edit a LocalTransfer</Translate>
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
                  id="local-transfer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.senderAccountNumber')}
                id="local-transfer-senderAccountNumber"
                name="senderAccountNumber"
                data-cy="senderAccountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.recipientAccountNumber')}
                id="local-transfer-recipientAccountNumber"
                name="recipientAccountNumber"
                data-cy="recipientAccountNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.recipientBankName')}
                id="local-transfer-recipientBankName"
                name="recipientBankName"
                data-cy="recipientBankName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.recipientBankBranch')}
                id="local-transfer-recipientBankBranch"
                name="recipientBankBranch"
                data-cy="recipientBankBranch"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.amount')}
                id="local-transfer-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.transactionDate')}
                id="local-transfer-transactionDate"
                name="transactionDate"
                data-cy="transactionDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.accountsLocalTransfer.description')}
                id="local-transfer-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="local-transfer-bankAccount"
                name="bankAccount"
                data-cy="bankAccount"
                label={translate('gatewayApp.accountsLocalTransfer.bankAccount')}
                type="select"
                required
              >
                <option value="" key="0" />
                {bankAccounts
                  ? bankAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.accountNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/local-transfer" replace color="info">
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

export default LocalTransferUpdate;
