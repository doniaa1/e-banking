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
import { getEntities } from './data-collection.reducer';
import { CSVLink } from 'react-csv'; // لإضافة زر التصدير إلى CSV

export const DataCollection = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [statusFilter, setStatusFilter] = useState(''); // حقل لتصفية الحالة
  const [sourceFilter, setSourceFilter] = useState(''); // حقل لتصفية المصدر
  const [filteredData, setFilteredData] = useState([]); // البيانات المصفاة

  const dataCollectionList = useAppSelector(state => state.gateway.dataCollection.entities);
  const loading = useAppSelector(state => state.gateway.dataCollection.loading);
  const totalItems = useAppSelector(state => state.gateway.dataCollection.totalItems);

  // إعداد أعمدة ملف CSV
  const headers = [
    { label: 'ID', key: 'id' },
    { label: 'Login', key: 'login' },
    { label: 'Name', key: 'name' },
    { label: 'Source', key: 'source' },
    { label: 'Collected At', key: 'collectedAt' },
    { label: 'Data Type', key: 'dataType' },
    { label: 'Status', key: 'status' },
    { label: 'Description', key: 'description' },
  ];

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

  // تحديث البيانات المصفاة بناءً على التصفية
  useEffect(() => {
    let filtered = dataCollectionList;
    if (statusFilter) {
      filtered = filtered.filter(item => item.status && item.status.includes(statusFilter));
    }
    if (sourceFilter) {
      filtered = filtered.filter(item => item.source && item.source.includes(sourceFilter));
    }
    setFilteredData(filtered);
  }, [dataCollectionList, statusFilter, sourceFilter]);

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
      <h2 id="data-collection-heading" data-cy="DataCollectionHeading">
        <Translate contentKey="gatewayApp.analyticsDataCollection.home.title">Data Collections</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="gatewayApp.analyticsDataCollection.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <CSVLink data={filteredData} headers={headers} filename="data-collections.csv" className="btn btn-success">
            <FontAwesomeIcon icon="file-export" /> Export to CSV
          </CSVLink>
          <Link to="/data-collection/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="gatewayApp.analyticsDataCollection.home.createLabel">Create new Data Collection</Translate>
          </Link>
        </div>
      </h2>

      {/* واجهة التصفية */}
      <div className="mb-3">
        <input
          type="text"
          placeholder="Filter by Status"
          value={statusFilter}
          onChange={e => setStatusFilter(e.target.value)}
          className="form-control me-2"
          style={{ display: 'inline', width: '200px' }}
        />
        <input
          type="text"
          placeholder="Filter by Source"
          value={sourceFilter}
          onChange={e => setSourceFilter(e.target.value)}
          className="form-control"
          style={{ display: 'inline', width: '200px' }}
        />
      </div>

      <div className="table-responsive">
        {filteredData && filteredData.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('login')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.login">Login</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('login')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('source')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.source">Source</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('source')} />
                </th>
                <th className="hand" onClick={sort('collectedAt')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.collectedAt">Collected At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('collectedAt')} />
                </th>
                <th className="hand" onClick={sort('dataType')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.dataType">Data Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataType')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="gatewayApp.analyticsDataCollection.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filteredData.map((dataCollection, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/data-collection/${dataCollection.id}`} color="link" size="sm">
                      {dataCollection.id}
                    </Button>
                  </td>
                  <td>{dataCollection.login}</td>
                  <td>{dataCollection.name}</td>
                  <td>{dataCollection.source}</td>
                  <td>
                    {dataCollection.collectedAt ? (
                      <TextFormat type="date" value={dataCollection.collectedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.DataType.${dataCollection.dataType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`gatewayApp.Status.${dataCollection.status}`} />
                  </td>
                  <td>{dataCollection.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/data-collection/${dataCollection.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/data-collection/${dataCollection.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/data-collection/${dataCollection.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="gatewayApp.analyticsDataCollection.home.notFound">No Data Collections found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={dataCollectionList && dataCollectionList.length > 0 ? '' : 'd-none'}>
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

export default DataCollection;
