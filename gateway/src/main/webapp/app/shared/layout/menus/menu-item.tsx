import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
export interface IMenuItem {
  children: React.ReactNode;
  to: string;
  icon: IconProp;
  id?: string;
  'data-cy'?: string;
}

const MenuItem = (props: IMenuItem) => {
  const { to, id, children } = props;

  return (
    <DropdownItem
      tag={Link}
      to={to}
      id={id}
      data-cy={props['data-cy']}
      className="custom-menu-item"
      style={{
        fontSize: '16px',
        padding: '12px 20px',
        color: '#333',
        display: 'flex',
        alignItems: 'center',
        transition: 'all 0.3s ease-in-out',
        borderBottom: '1px solid #e9ecef',
      }}
    >
      {children}
    </DropdownItem>
  );

  {
    /* تضمين الأنماط */
  }
  <style>
    {`
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
        background-color: #f8f9fa;
        color: #007bff;
        border-radius: 5px;
      }
    `}
  </style>;
};

export default MenuItem;
