import React from 'react';
import { Translate } from 'react-jhipster';
import './security-policy.scss';

const SecurityPolicy = () => {
  return (
    <div className="security-policy">
      <h1>
        <Translate contentKey="global.securityPolicy.title">Bank Security Policy</Translate>
      </h1>
      <p>
        <Translate contentKey="global.securityPolicy.intro">
          In our electronic banking project, we are committed to the highest security standards to protect customer data and ensure the
          safety of financial operations. This policy is designed to outline the measures we take to maintain a secure and reliable banking
          environment.
        </Translate>
      </p>

      <h2>
        <Translate contentKey="global.securityPolicy.dataEncryption.title">1. Data Encryption</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.dataEncryption.content">
          We use the latest encryption technologies to secure data, including:
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.dataEncryption.bullet1">Encrypting all customer data during storage.</Translate>
        </li>
        <li>
          <Translate contentKey="global.securityPolicy.dataEncryption.bullet2">
            Protecting passwords with advanced encryption techniques.
          </Translate>
        </li>
        <li>
          <Translate contentKey="global.securityPolicy.dataEncryption.bullet3">
            Ensuring the safety of sensitive details related to payments and transactions.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.twoFactorAuth.title">2. Two-Factor Authentication (2FA)</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.twoFactorAuth.content">
          We provide two-factor authentication for all customers to enhance login security.
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.twoFactorAuth.bullet1">
            A verification code is sent to the customer&apos;s mobile device or email to confirm identity.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.fraudDetection.title">3. Fraud Detection and Analysis</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.fraudDetection.content">
          Our advanced systems monitor financial activity continuously to detect suspicious activities.
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.fraudDetection.bullet1">
            Immediate alerts are issued in case of suspicious transactions.
          </Translate>
        </li>
        <li>
          <Translate contentKey="global.securityPolicy.fraudDetection.bullet2">
            Advanced analytics identify suspicious patterns proactively to prevent fraud.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.jwt.title">4. Account and Transaction Protection Using JWT</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.jwt.content">
          Banking operations are secured using JSON Web Tokens (JWT) for session authentication.
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.jwt.bullet1">
            JWT prevents unauthorized access to customer data or banking services.
          </Translate>
        </li>
        <li>
          <Translate contentKey="global.securityPolicy.jwt.bullet2">
            Authentication tokens are renewed periodically to maintain top security levels.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.digitalPayments.title">5. Security in Digital Payments</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.digitalPayments.content">
          All digital transactions are conducted through secure payment interfaces designed to verify payment validity before processing.
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.digitalPayments.bullet1">
            Fraud protection in payments through user identity verification.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.securityAudits.title">6. Continuous Security Audits</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.securityAudits.content">
          We conduct regular audits on our system to ensure compliance with the latest security standards.
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.securityAudits.bullet1">
            Banking platforms are continually updated to counter new cyber threats.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.customerDataProtection.title">7. Protecting Customer Personal Data</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.customerDataProtection.content">
          Our systems are committed to protecting personal customer data such as addresses, phone numbers, and emails.
        </Translate>
      </p>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.customerDataProtection.bullet1">
            User access and permissions are determined based on their roles for additional protection.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.tips.title">8. Tips to Protect Your Accounts</Translate>
      </h2>
      <ul>
        <li>
          <Translate contentKey="global.securityPolicy.tips.bullet1">Use strong and unique passwords for each account.</Translate>
        </li>
        <li>
          <Translate contentKey="global.securityPolicy.tips.bullet2">Avoid sharing your login credentials with others.</Translate>
        </li>
        <li>
          <Translate contentKey="global.securityPolicy.tips.bullet3">
            Regularly review your transactions and report any suspicious activity.
          </Translate>
        </li>
      </ul>

      <h2>
        <Translate contentKey="global.securityPolicy.support.title">9. Support and Assistance</Translate>
      </h2>
      <p>
        <Translate contentKey="global.securityPolicy.support.content">
          If you have any inquiries about your account security or suspect unauthorized activity, please contact us via:
        </Translate>
      </p>
      <ul>
        <li>
          <strong>
            <Translate contentKey="global.securityPolicy.support.email">Email:</Translate>
          </strong>{' '}
          <a href="mailto:support@ebank.com">support@ebank.com</a>
        </li>
        <li>
          <strong>
            <Translate contentKey="global.securityPolicy.support.phone">Phone:</Translate>
          </strong>{' '}
          +218-91-8880077
        </li>
        <li>
          <strong>
            <Translate contentKey="global.securityPolicy.support.available">24/7 Customer Support</Translate>
          </strong>
        </li>
      </ul>
    </div>
  );
};

export default SecurityPolicy;
