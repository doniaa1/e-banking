import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AnalysisReport from './analysis-report';
import AnalysisReportDetail from './analysis-report-detail';
import AnalysisReportUpdate from './analysis-report-update';
import AnalysisReportDeleteDialog from './analysis-report-delete-dialog';

const AnalysisReportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AnalysisReport />} />
    <Route path="new" element={<AnalysisReportUpdate />} />
    <Route path=":id">
      <Route index element={<AnalysisReportDetail />} />
      <Route path="edit" element={<AnalysisReportUpdate />} />
      <Route path="delete" element={<AnalysisReportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AnalysisReportRoutes;
