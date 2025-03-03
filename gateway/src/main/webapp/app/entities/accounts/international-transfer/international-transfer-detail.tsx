import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './international-transfer.reducer';

export const InternationalTransferDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const internationalTransferEntity = useAppSelector(state => state.gateway.internationalTransfer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="internationalTransferDetailsHeading">
          <Translate contentKey="gatewayApp.accountsInternationalTransfer.detail.title">InternationalTransfer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.id}</dd>
          <dt>
            <span id="senderAccountNumber">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.senderAccountNumber">Sender Account Number</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.senderAccountNumber}</dd>
          <dt>
            <span id="recipientIban">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.recipientIban">Recipient Iban</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.recipientIban}</dd>
          <dt>
            <span id="swiftCode">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.swiftCode">Swift Code</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.swiftCode}</dd>
          <dt>
            <span id="recipientName">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.recipientName">Recipient Name</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.recipientName}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.currency}</dd>
          <dt>
            <span id="transactionDate">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.transactionDate">Transaction Date</Translate>
            </span>
          </dt>
          <dd>
            {internationalTransferEntity.transactionDate ? (
              <TextFormat value={internationalTransferEntity.transactionDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.accountsInternationalTransfer.description">Description</Translate>
            </span>
          </dt>
          <dd>{internationalTransferEntity.description}</dd>
          <dt>
            <Translate contentKey="gatewayApp.accountsInternationalTransfer.bankAccount">Bank Account</Translate>
          </dt>
          <dd>{internationalTransferEntity.bankAccount ? internationalTransferEntity.bankAccount.iban : ''}</dd>
        </dl>
        <Button tag={Link} to="/international-transfer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/international-transfer/${internationalTransferEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InternationalTransferDetail;
