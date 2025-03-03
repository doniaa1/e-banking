import './home.scss';
import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row, Button } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import AOS from 'aos';
import 'aos/dist/aos.css';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  useEffect(() => {
    AOS.init({
      duration: 1200, // مدة الحركة
      once: true, // تشغيل الحركة مرة واحدة فقط
    });
  }, []);

  return (
    <div className="home-container">
      {/* قسم البطل */}
      <Row>
        <Col md="12" className="hero-section">
          <Slider
            dots={true}
            infinite={true}
            speed={500}
            slidesToShow={1}
            slidesToScroll={1}
            autoplay={true}
            autoplaySpeed={3000}
            lazyLoad="ondemand"
          >
            <div>
              <img src="/content/images/pexels-artempodrez-5716052.jpg" alt="Hero 1" className="carousel-image" />
            </div>
            <div>
              <img src="/content/images/pexels-tima-miroshnichenko-7567443.jpg" alt="Hero 2" className="carousel-image" />
            </div>
            <div>
              <img src="/content/images/pexels-shvetsa-4482900.jpg" alt="Hero 3" className="carousel-image" />
            </div>
          </Slider>
          <div className="hero-text" data-aos="fade-up">
            <h1 className="display-4">
              <Translate contentKey="global.title">Zenith</Translate>
            </h1>
            <p className="lead">
              <Translate contentKey="global.subtitle">
                Experience a new era of digital banking Secure, reliable, and tailored for you.
              </Translate>
            </p>
            {account?.login ? (
              <Alert color="success" data-aos="fade-up">
                <Translate contentKey="global.menu.logged.message" interpolate={{ username: account.login }}>
                  You are logged in as Operations Manager
                </Translate>
              </Alert>
            ) : (
              <div className="cta-buttons" data-aos="fade-up">
                <Button color="primary" size="lg" tag={Link} to="/login">
                  <Translate contentKey="global.messages.info.authenticated.link">Sign In</Translate>
                </Button>
                {/* <Button color="secondary" size="lg" tag={Link} to="/account/register" className="ml-3">
                  <Translate contentKey="global.messages.info.register.link">Register Now</Translate>
                </Button>*/}
              </div>
            )}
          </div>
        </Col>
      </Row>

      {/* قسم الخدمات */}
      <div className="services-section" data-aos="fade-up">
        <h2 className="section-title">
          <Translate contentKey="global.menu.services.title">Our Core Services</Translate>
        </h2>
        <Row>
          <Col md="4">
            <div className="service-item" data-aos="zoom-in">
              <div className="service-icon">
                <img src="/content/images/account.jpg" alt="Account Management Icon" />
              </div>
              <h3>
                <Translate contentKey="global.menu.services.accountManagement">Account Management</Translate>
              </h3>
              <p>
                <Translate contentKey="global.menu.services.accountManagementDesc">
                  Easily manage your bank accounts, view transaction history, and check balances anytime.
                </Translate>
              </p>
            </div>
          </Col>
          <Col md="4">
            <div className="service-item" data-aos="zoom-in" data-aos-delay="100">
              <div className="service-icon">
                <img src="/content/images/account(5).jpg" alt="Secure Payments Icon" />
              </div>
              <h3>
                <Translate contentKey="global.menu.services.securePayments">Secure Payments</Translate>
              </h3>
              <p>
                <Translate contentKey="global.menu.services.securePaymentsDesc">
                  Perform quick and secure payments to vendors, bills, or friends with just a few clicks.
                </Translate>
              </p>
            </div>
          </Col>
          <Col md="4">
            <div className="service-item" data-aos="zoom-in" data-aos-delay="200">
              <div className="service-icon">
                <img src="/content/images/account(2).jpg" alt="Global Transfers Icon" />
              </div>
              <h3>
                <Translate contentKey="global.menu.services.globalTransfers">Global Transfers</Translate>
              </h3>
              <p>
                <Translate contentKey="global.menu.services.globalTransfersDesc">
                  Send and receive money globally with competitive exchange rates and no hidden fees.
                </Translate>
              </p>
            </div>
          </Col>
        </Row>
        <Row className="mt-4">
          <Col md="4">
            <div className="service-item" data-aos="zoom-in" data-aos-delay="300">
              <div className="service-icon">
                <img src="/content/images/account(3).jpg" alt="Credit Card Management Icon" />
              </div>
              <h3>
                <Translate contentKey="global.menu.services.creditCardManagement">Credit Card Management</Translate>
              </h3>
              <p>
                <Translate contentKey="global.menu.services.creditCardManagementDesc">
                  Monitor your credit card usage, pay bills, and request new cards with ease.
                </Translate>
              </p>
            </div>
          </Col>
          <Col md="4">
            <div className="service-item" data-aos="zoom-in" data-aos-delay="400">
              <div className="service-icon">
                <img src="/content/images/account(4).jpg" alt="Investment Management Icon" />
              </div>
              <h3>
                <Translate contentKey="global.menu.services.investmentManagement">Investment Management</Translate>
              </h3>
              <p>
                <Translate contentKey="global.menu.services.investmentManagementDesc">
                  Plan and manage your investments with expert tools and insights tailored for your financial growth.
                </Translate>
              </p>
            </div>
          </Col>
          <Col md="4">
            <div className="service-item" data-aos="zoom-in" data-aos-delay="500">
              <div className="service-icon">
                <img src="/content/images/account(6).jpg" alt="Financial Analysis Icon" />
              </div>
              <h3>
                <Translate contentKey="global.menu.services.financialAnalysis">Financial Analysis</Translate>
              </h3>
              <p>
                <Translate contentKey="global.menu.services.financialAnalysisDesc">
                  Analyze your financial data to make informed decisions about your investments.
                </Translate>
              </p>
            </div>
          </Col>
        </Row>
      </div>
      {/* قسم الرؤية */}
      <div className="vision-section" data-aos="fade-up">
        <Row className="align-items-center">
          <Col md="6">
            <img
              src="/content/images/28208.jpg"
              alt="Vision"
              data-aos="zoom-in"
              style={{
                maxWidth: '100%',
                height: 'auto',
                borderRadius: '25px', // تعديل الحواف الدائرية
                boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)', // تحسين الظل
                border: '4px solid #ffffff', // إضافة إطار أبيض
                transition: 'transform 0.3s ease, box-shadow 0.3s ease', // تأثيرات الحركة
              }}
              onMouseOver={e => {
                e.currentTarget.style.transform = 'scale(1.05)'; // تكبير الصورة عند التمرير
                e.currentTarget.style.boxShadow = '0 12px 24px rgba(0, 0, 0, 0.3)'; // تحسين الظل عند التمرير
              }}
              onMouseOut={e => {
                e.currentTarget.style.transform = 'scale(1)'; // العودة للحجم الطبيعي
                e.currentTarget.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.2)'; // العودة للظل الطبيعي
              }}
            />
          </Col>

          <Col md="6">
            <div className="vision-text" data-aos="fade-left">
              <h2>
                <Translate contentKey="global.menu.vision.title">Our Vision</Translate>
              </h2>
              <p>
                <Translate contentKey="global.menu.vision.text">
                  To revolutionize digital banking with innovative solutions, ensuring seamless and secure experiences for our customers
                  worldwide.
                </Translate>
              </p>
            </div>
          </Col>
        </Row>
      </div>
      <div className="contact-us-section" data-aos="fade-up">
        <h2 className="section-title" data-aos="fade-down">
          <Translate contentKey="global.menu.contact.title">GET IN TOUCH</Translate>
        </h2>
        <p className="section-description" data-aos="fade-down">
          <Translate contentKey="global.menu.contact.description">
            Have questions or need help? Reach out to us through any of the following ways or fill out the form below.
          </Translate>
        </p>
        <div className="contact-container">
          <div className="left-column" data-aos="fade-right">
            <div className="image-container">
              <img
                src="/content/images/contact-us-communication-support-service-assistance-concept.jpg"
                alt="Contact Us"
                className="contact-image"
              />
            </div>
            <div className="contact-info">
              <h3>
                <Translate contentKey="global.menu.contact.infoTitle">Contact Information</Translate>
              </h3>
              <p>
                <i className="material-icons">email</i> <Translate contentKey="global.menu.contact.email">Email:</Translate>{' '}
                support@zenithbank.com
              </p>
              <p>
                <i className="material-icons">phone</i> <Translate contentKey="global.menu.contact.phone">Phone:</Translate> +218-1115555
              </p>
              <p>
                <i className="material-icons">location_on</i> <Translate contentKey="global.menu.contact.address">Address:</Translate> 1234
                Street, Tripoli, Libya
              </p>
            </div>
          </div>
          <div className="right-column" data-aos="fade-left">
            <div className="contact-form">
              <h3>
                <Translate contentKey="global.menu.contact.formTitle">Send Us a Message</Translate>
              </h3>
              <form>
                <div className="form-group">
                  <label htmlFor="name">
                    <Translate contentKey="global.menu.contact.formName">Name</Translate>
                  </label>
                  <input type="text" id="name" className="form-control" placeholder="Your Name" />
                </div>
                <div className="form-group">
                  <label htmlFor="email">
                    <Translate contentKey="global.menu.contact.formEmail">Email</Translate>
                  </label>
                  <input type="email" id="email" className="form-control" placeholder="Your Email" />
                </div>
                <div className="form-group">
                  <label htmlFor="message">
                    <Translate contentKey="global.menu.contact.formMessage">Message</Translate>
                  </label>
                  <textarea id="message" className="form-control" rows={3} placeholder="Your Message"></textarea>
                </div>
                <button type="submit" className="btn-primary">
                  <Translate contentKey="global.menu.contact.formButton">Send Message</Translate>
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
