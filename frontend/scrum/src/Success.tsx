import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

const Success: React.FC = () => {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const accessToken = params.get('access');

  useEffect(() => {
    if (accessToken) {
      // 서버에 로그인 요청을 보낼 함수 호출
      sendLoginRequest(accessToken);
    }
  }, [accessToken]);

  const sendLoginRequest = async (token: string | null) => {
    const response = await fetch('http://localhost:8080/api/oauth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ access: token }),
    });

    const data = await response.json();
    console.log(data);
  };

  return <div>소셜로그인 성공</div>;
};

export default Success;