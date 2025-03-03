import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <footer className="app-footer">
          <div className="footer-container">
            <p className="footer-text">
              &copy; 2024 Zenith EBank. <Translate contentKey="footer.rights">All Rights Reserved.</Translate>
            </p>
          </div>
        </footer>
      </Col>
    </Row>
  </div>
);

export default Footer;
