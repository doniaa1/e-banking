import React, { useEffect } from 'react';
import { useAppDispatch } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';

export const Logout = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(logout()); // تنفيذ تسجيل الخروج
    window.location.href = '/login'; // إعادة التوجيه الفوري
  }, [dispatch]);

  return null; // لا نعرض أي محتوى أثناء تسجيل الخروج
};

export default Logout;
