import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './local-transfer.reducer';

export const LocalTransferDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const localTransferEntity = useAppSelector(state => state.gateway.localTransfer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="localTransferDetailsHeading">
          <Translate contentKey="gatewayApp.accountsLocalTransfer.detail.title">LocalTransfer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.id}</dd>
          <dt>
            <span id="senderAccountNumber">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.senderAccountNumber">Sender Account Number</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.senderAccountNumber}</dd>
          <dt>
            <span id="recipientAccountNumber">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.recipientAccountNumber">Recipient Account Number</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.recipientAccountNumber}</dd>
          <dt>
            <span id="recipientBankName">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.recipientBankName">Recipient Bank Name</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.recipientBankName}</dd>
          <dt>
            <span id="recipientBankBranch">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.recipientBankBranch">Recipient Bank Branch</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.recipientBankBranch}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.amount}</dd>
          <dt>
            <span id="transactionDate">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.transactionDate">Transaction Date</Translate>
            </span>
          </dt>
          <dd>
            {localTransferEntity.transactionDate ? (
              <TextFormat value={localTransferEntity.transactionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.description">Description</Translate>
            </span>
          </dt>
          <dd>{localTransferEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.accountsLocalTransfer.bankAccount">Bank Account</Translate>
          </dt>
          <dd>{localTransferEntity.bankAccount ? localTransferEntity.bankAccount.accountNumber : ''}</dd>
        </dl>
        <Button tag={Link} to="/local-transfer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/local-transfer/${localTransferEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocalTransferDetail;
