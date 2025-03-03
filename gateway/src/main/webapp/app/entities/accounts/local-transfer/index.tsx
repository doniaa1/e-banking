import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LocalTransfer from './local-transfer';
import LocalTransferDetail from './local-transfer-detail';
import LocalTransferUpdate from './local-transfer-update';
import LocalTransferDeleteDialog from './local-transfer-delete-dialog';

const LocalTransferRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LocalTransfer />} />
    <Route path="new" element={<LocalTransferUpdate />} />
    <Route path=":id">
      <Route index element={<LocalTransferDetail />} />
      <Route path="edit" element={<LocalTransferUpdate />} />
      <Route path="delete" element={<LocalTransferDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LocalTransferRoutes;
