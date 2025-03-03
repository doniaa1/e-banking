import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './local-transfer.reducer';

export const LocalTransfer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const localTransferList = useAppSelector(state => state.gateway.localTransfer.entities);
  const loading = useAppSelector(state => state.gateway.localTransfer.loading);
  const totalItems = useAppSelector(state => state.gateway.localTransfer.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="local-transfer-heading" data-cy="LocalTransferHeading">
        <Translate contentKey="gatewayApp.accountsLocalTransfer.home.title">Local Transfers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.accountsLocalTransfer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/local-transfer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.accountsLocalTransfer.home.createLabel">Create new Local Transfer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {localTransferList && localTransferList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('senderAccountNumber')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.senderAccountNumber">Sender Account Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('senderAccountNumber')} />
                </th>
                <th className="hand" onClick={sort('recipientAccountNumber')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.recipientAccountNumber">Recipient Account Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientAccountNumber')} />
                </th>
                <th className="hand" onClick={sort('recipientBankName')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.recipientBankName">Recipient Bank Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientBankName')} />
                </th>
                <th className="hand" onClick={sort('recipientBankBranch')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.recipientBankBranch">Recipient Bank Branch</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recipientBankBranch')} />
                </th>
                <th className="hand" onClick={sort('amount')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.amount">Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('amount')} />
                </th>
                <th className="hand" onClick={sort('transactionDate')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.transactionDate">Transaction Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transactionDate')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th>
                  <Translate contentKey="gatewayApp.accountsLocalTransfer.bankAccount">Bank Account</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {localTransferList.map((localTransfer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/local-transfer/${localTransfer.id}`} color="link" size="sm">
                      {localTransfer.id}
                    </Button>
                  </td>
                  <td>{localTransfer.senderAccountNumber}</td>
                  <td>{localTransfer.recipientAccountNumber}</td>
                  <td>{localTransfer.recipientBankName}</td>
                  <td>{localTransfer.recipientBankBranch}</td>
                  <td>{localTransfer.amount}</td>
                  <td>
                    {localTransfer.transactionDate ? (
                      <TextFormat type="date" value={localTransfer.transactionDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{localTransfer.description}</td>
                  <td>
                    {localTransfer.bankAccount ? (
                      <Link to={`/bank-account/${localTransfer.bankAccount.id}`}>{localTransfer.bankAccount.accountNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/local-transfer/${localTransfer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/local-transfer/${localTransfer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/local-transfer/${localTransfer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="gatewayApp.accountsLocalTransfer.home.notFound">No Local Transfers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={localTransferList && localTransferList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default LocalTransfer;
