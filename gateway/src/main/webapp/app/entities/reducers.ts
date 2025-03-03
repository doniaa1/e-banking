import customer from 'app/entities/customers/customer/customer.reducer';
import document from 'app/entities/customers/document/document.reducer';
import payment from 'app/entities/payment/payment/payment.reducer';
import qRPayment from 'app/entities/payment/qr-payment/qr-payment.reducer';
import billPayment from 'app/entities/payment/bill-payment/bill-payment.reducer';
import cardPayment from 'app/entities/payment/card-payment/card-payment.reducer';
import investmentActivity from 'app/entities/investments/investment-activity/investment-activity.reducer';
import dataCollection from 'app/entities/analytics/data-collection/data-collection.reducer';
import analysisReport from 'app/entities/analytics/analysis-report/analysis-report.reducer';
import notification from 'app/entities/notifications/notification/notification.reducer';
import bankAccount from 'app/entities/accounts/bank-account/bank-account.reducer';
import localTransfer from 'app/entities/accounts/local-transfer/local-transfer.reducer';
import internationalTransfer from 'app/entities/accounts/international-transfer/international-transfer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  customer,
  document,
  bankAccount,
  localTransfer,
  internationalTransfer,
  payment,
  qRPayment,
  billPayment,
  cardPayment,
  investmentActivity,
  dataCollection,
  analysisReport,
  notification,

  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
