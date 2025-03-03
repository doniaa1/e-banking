import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import InternationalTransfer from './international-transfer';
import InternationalTransferDetail from './international-transfer-detail';
import InternationalTransferUpdate from './international-transfer-update';
import InternationalTransferDeleteDialog from './international-transfer-delete-dialog';

const InternationalTransferRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<InternationalTransfer />} />
    <Route path="new" element={<InternationalTransferUpdate />} />
    <Route path=":id">
      <Route index element={<InternationalTransferDetail />} />
      <Route path="edit" element={<InternationalTransferUpdate />} />
      <Route path="delete" element={<InternationalTransferDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default InternationalTransferRoutes;
