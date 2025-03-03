import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getDataCollections } from 'app/entities/analytics/data-collection/data-collection.reducer';
import { AnalysisType } from 'app/shared/model/enumerations/analysis-type.model';
import { ReportType } from 'app/shared/model/enumerations/report-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { createEntity, getEntity, reset, updateEntity } from './analysis-report.reducer';

export const AnalysisReportUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dataCollections = useAppSelector(state => state.gateway.dataCollection.entities);
  const analysisReportEntity = useAppSelector(state => state.gateway.analysisReport.entity);
  const loading = useAppSelector(state => state.gateway.analysisReport.loading);
  const updating = useAppSelector(state => state.gateway.analysisReport.updating);
  const updateSuccess = useAppSelector(state => state.gateway.analysisReport.updateSuccess);
  const analysisTypeValues = Object.keys(AnalysisType);
  const reportTypeValues = Object.keys(ReportType);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate(`/analysis-report${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDataCollections({}));
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
    values.createdAt = convertDateTimeToServer(values.createdAt);

    const entity = {
      ...analysisReportEntity,
      ...values,
      dataCollection: dataCollections.find(it => it.id.toString() === values.dataCollection?.toString()),
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
          createdAt: displayDefaultDateTime(),
        }
      : {
          analysisType: 'STATISTICAL',
          reportType: 'PAYMENT',
          status: 'ACTIVE',
          ...analysisReportEntity,
          createdAt: convertDateTimeFromServer(analysisReportEntity.createdAt),
          dataCollection: analysisReportEntity?.dataCollection?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.analyticsAnalysisReport.home.createOrEditLabel" data-cy="AnalysisReportCreateUpdateHeading">
            <Translate contentKey="gatewayApp.analyticsAnalysisReport.home.createOrEditLabel">Create or edit a AnalysisReport</Translate>
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
                  id="analysis-report-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.title')}
                id="analysis-report-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.createdAt')}
                id="analysis-report-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.analysisType')}
                id="analysis-report-analysisType"
                name="analysisType"
                data-cy="analysisType"
                type="select"
              >
                {analysisTypeValues.map(analysisType => (
                  <option value={analysisType} key={analysisType}>
                    {translate(`gatewayApp.AnalysisType.${analysisType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.reportType')}
                id="analysis-report-reportType"
                name="reportType"
                data-cy="reportType"
                type="select"
              >
                {reportTypeValues.map(reportType => (
                  <option value={reportType} key={reportType}>
                    {translate(`gatewayApp.ReportType.${reportType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.generatedBy')}
                id="analysis-report-generatedBy"
                name="generatedBy"
                data-cy="generatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.content')}
                id="analysis-report-content"
                name="content"
                data-cy="content"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('gatewayApp.analyticsAnalysisReport.status')}
                id="analysis-report-status"
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
                id="analysis-report-dataCollection"
                name="dataCollection"
                data-cy="dataCollection"
                label={translate('gatewayApp.analyticsAnalysisReport.dataCollection')}
                type="select"
                required
              >
                <option value="" key="0" />
                {dataCollections
                  ? dataCollections.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/analysis-report" replace color="info">
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

export default AnalysisReportUpdate;
