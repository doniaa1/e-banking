import React from 'react';
import { useAppSelector } from 'app/config/store'; // لاستدعاء حالة المصادقة من Redux
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

// تعريف واجهة الخصائص (Props Interface) لتوضيح نوع البيانات
interface AccountMenuProps {
  isAuthenticated: boolean;
}

const accountMenuItemsAuthenticated = () => (
  <>
    <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
      <Translate contentKey="global.menu.account.settings">Settings</Translate>
    </MenuItem>
    <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
      <Translate contentKey="global.menu.account.password">Password</Translate>
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
      <Translate contentKey="global.menu.account.logout">Sign out</Translate>
    </MenuItem>
  </>
);

export const AccountMenu = ({ isAuthenticated }: AccountMenuProps) => {
  // إخفاء القائمة بالكامل إذا لم يكن المستخدم مسجل الدخول
  if (!isAuthenticated) {
    return null; // لا يتم عرض أي شيء
  }

  // عرض القائمة إذا كان المستخدم مسجل الدخول
  return (
    <NavDropdown icon="user" name={translate('global.menu.account.main')} id="account-menu" data-cy="accountMenu">
      {accountMenuItemsAuthenticated()}
    </NavDropdown>
  );
};

export default AccountMenu;
