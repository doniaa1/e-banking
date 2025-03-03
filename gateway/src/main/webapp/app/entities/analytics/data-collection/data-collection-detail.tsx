import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './data-collection.reducer';

export const DataCollectionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const dataCollectionEntity = useAppSelector(state => state.gateway.dataCollection.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="dataCollectionDetailsHeading">
          <Translate contentKey="gatewayApp.analyticsDataCollection.detail.title">DataCollection</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.id}</dd>
          <dt>
            <span id="login">
              <Translate contentKey="gatewayApp.analyticsDataCollection.login">Login</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.login}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="gatewayApp.analyticsDataCollection.name">Name</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.name}</dd>
          <dt>
            <span id="source">
              <Translate contentKey="gatewayApp.analyticsDataCollection.source">Source</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.source}</dd>
          <dt>
            <span id="collectedAt">
              <Translate contentKey="gatewayApp.analyticsDataCollection.collectedAt">Collected At</Translate>
            </span>
          </dt>
          <dd>
            {dataCollectionEntity.collectedAt ? (
              <TextFormat value={dataCollectionEntity.collectedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dataType">
              <Translate contentKey="gatewayApp.analyticsDataCollection.dataType">Data Type</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.dataType}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="gatewayApp.analyticsDataCollection.status">Status</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.status}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="gatewayApp.analyticsDataCollection.description">Description</Translate>
            </span>
          </dt>
          <dd>{dataCollectionEntity.description}</dd>
        </dl>
        <Button tag={Link} to="/data-collection" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/data-collection/${dataCollectionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DataCollectionDetail;
