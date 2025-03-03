import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InvestmentActivity from './investment-activity';
import InvestmentActivityDetail from './investment-activity-detail';
import InvestmentActivityUpdate from './investment-activity-update';
import InvestmentActivityDeleteDialog from './investment-activity-delete-dialog';

const InvestmentActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InvestmentActivity />} />
    <Route path="new" element={<InvestmentActivityUpdate />} />
    <Route path=":id">
      <Route index element={<InvestmentActivityDetail />} />
      <Route path="edit" element={<InvestmentActivityUpdate />} />
      <Route path="delete" element={<InvestmentActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InvestmentActivityRoutes;
