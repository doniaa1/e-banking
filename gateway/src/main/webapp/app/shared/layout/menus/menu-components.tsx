import React from 'react';
import { DropdownMenu, DropdownToggle, UncontrolledDropdown } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id} data-cy={props['data-cy']}>
    <DropdownToggle
      nav
      caret
      className="modern-dropdown-toggle"
      style={{
        fontSize: '16px',
        fontWeight: 'bold',
        color: '#007bff',
        transition: 'color 0.3s ease',
      }}
    >
      <FontAwesomeIcon icon={props.icon} />
      <span style={{ marginLeft: '8px' }}>{props.name}</span>
    </DropdownToggle>
    <DropdownMenu
      end
      className="custom-dropdown-menu"
      style={{
        maxHeight: '90vh', // الحد الأقصى للارتفاع
        overflowY: 'auto', // إضافة شريط تمرير عند الحاجة
        zIndex: 1050, // التحكم في مستوى العرض
        width: '300px', // عرض القائمة
        backgroundColor: '#ffffff',
        borderRadius: '12px',
        boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)',
        padding: '10px 0',
      }}
    >
      {props.children}
    </DropdownMenu>

    {/* تضمين الأنماط */}
    <style>
      {`
        .custom-dropdown-menu {
  max-height: 90vh; /* يضمن أن القائمة لا تتجاوز ارتفاع الشاشة */
  overflow-y: auto; /* يضيف شريط تمرير إذا كانت العناصر كثيرة */
  z-index: 1050; /* يضمن ظهورها فوق جميع العناصر الأخرى */
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  padding: 10px 0;
  width: 300px;
}


        .modern-dropdown-toggle {
          font-size: 16px;
          font-weight: bold;
          color: #007bff;
          transition: color 0.3s ease;
        }

        .modern-dropdown-toggle:hover {
          color: #0056b3;
        }

        .custom-menu-item {
          font-size: 16px;
          padding: 12px 20px;
          color: #333;
          display: flex;
          align-items: center;
          transition: all 0.3s ease-in-out;
          border-bottom: 1px solid #e9ecef;
        }

        .custom-menu-item:hover {
          background-color: #f8f9fa; /* تغيير لون الخلفية عند التحويم */
          color: #007bff; /* تغيير لون النص عند التحويم */
          border-radius: 5px;
        }
      `}
    </style>
  </UncontrolledDropdown>
);
