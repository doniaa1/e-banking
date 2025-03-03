import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Grid, Card, CardContent, Typography, Chip, Button, Box, Badge } from '@mui/material';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from 'recharts';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from './investment-activity.reducer';
import { APP_DATE_FORMAT } from 'app/config/constants';

const COLORS = ['#00C49F', '#FFBB28', '#FF8042'];

const data = [
  { name: 'Active', value: 400 },
  { name: 'Closed', value: 300 },
  { name: 'Pending', value: 200 },
];

const InvestmentActivityDetail = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams<'id'>();

  useEffect(() => {
    if (id) {
      dispatch(getEntity(id));
    }
  }, [id]);

  const investmentActivityEntity = useAppSelector(state => state.gateway.investmentActivity.entity);

  const statusChipColor = status => {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'CLOSED':
        return 'error';
      case 'COMPLETED':
        return 'warning';
      default:
        return 'default';
    }
  };

  return (
    <Grid container spacing={3} sx={{ padding: 3 }}>
      {/* Page Header */}
      <Grid item xs={12}>
        <Typography variant="h4" gutterBottom>
          <Translate contentKey="gatewayApp.investmentsInvestmentActivity.detail.title">Investment Activity</Translate>
        </Typography>
      </Grid>

      {/* Status and Pie Chart */}
      <Grid item xs={12} md={6}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.status">Status</Translate>
            </Typography>
            <Chip
              label={investmentActivityEntity.status}
              color={statusChipColor(investmentActivityEntity.status)}
              sx={{ fontSize: '1rem', padding: '0 10px' }}
            />
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={6}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              Activity Overview
            </Typography>
            <ResponsiveContainer width="100%" height={200}>
              <PieChart>
                <Pie data={data} dataKey="value" cx="50%" cy="50%" outerRadius={60} fill="#8884d8" label>
                  {data.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </Grid>

      {/* User Details */}
      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.login">Login</Translate>
            </Typography>
            <Typography>
              <Badge badgeContent={investmentActivityEntity.login} color="secondary" />
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.createdBy">Created By</Translate>
            </Typography>
            <Typography>
              <Badge badgeContent={investmentActivityEntity.createdBy} color="info" />
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.createdDate">Created Date</Translate>
            </Typography>
            <Typography>
              {investmentActivityEntity.createdDate ? (
                <TextFormat value={investmentActivityEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
              ) : (
                '-'
              )}
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      {/* Last Modified */}
      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.lastModifiedBy">Last Modified By</Translate>
            </Typography>
            <Typography>{investmentActivityEntity.lastModifiedBy}</Typography>
          </CardContent>
        </Card>
      </Grid>

      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.lastModifiedDate">Last Modified Date</Translate>
            </Typography>
            <Typography>
              {investmentActivityEntity.lastModifiedDate ? (
                <TextFormat value={investmentActivityEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
              ) : (
                '-'
              )}
            </Typography>
          </CardContent>
        </Card>
      </Grid>

      {/* Risk Level Section */}
      <Grid item xs={12} md={4}>
        <Card>
          <CardContent>
            <Typography variant="h6" color="primary" gutterBottom>
              <Translate contentKey="gatewayApp.investmentsInvestmentActivity.riskLevel">Risk Level</Translate>
            </Typography>
            <Chip label={investmentActivityEntity.riskLevel} color="warning" />
          </CardContent>
        </Card>
      </Grid>

      {/* Action Buttons */}
      <Grid item xs={12}>
        <Box display="flex" justifyContent="space-between">
          <Button variant="outlined" color="primary" component={Link} to="/investment-activity" sx={{ marginRight: 2 }}>
            <FontAwesomeIcon icon="arrow-left" /> <Translate contentKey="entity.action.back">Back</Translate>
          </Button>
          <Button variant="contained" color="primary" component={Link} to={`/investment-activity/${investmentActivityEntity.id}/edit`}>
            <FontAwesomeIcon icon="pencil-alt" /> <Translate contentKey="entity.action.edit">Edit</Translate>
          </Button>
        </Box>
      </Grid>
    </Grid>
  );
};

export default InvestmentActivityDetail;
