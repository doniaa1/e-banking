import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DataCollection from './data-collection';
import DataCollectionDetail from './data-collection-detail';
import DataCollectionUpdate from './data-collection-update';
import DataCollectionDeleteDialog from './data-collection-delete-dialog';

const DataCollectionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DataCollection />} />
    <Route path="new" element={<DataCollectionUpdate />} />
    <Route path=":id">
      <Route index element={<DataCollectionDetail />} />
      <Route path="edit" element={<DataCollectionUpdate />} />
      <Route path="delete" element={<DataCollectionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DataCollectionRoutes;
